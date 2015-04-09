package usergraph

import java.util.HashMap

trait GraphNode {
  def color: String
}

class Graph[T <: GraphNode](edges: Set[(T, T)]) {
  private def edgesFromNode(node: T) = (node, edges.filter(x => x._1 == node).map(x => x._2))
  private def edgesToNode(node: T) = (node, edges.filter(x => x._2 == node).map(x => x._1))

  lazy val forward = edges.map(x => x._1).map(edgesFromNode).toMap.withDefaultValue(Set[T]())
  lazy val reverse = edges.map(x => x._2).map(edgesToNode).toMap.withDefaultValue(Set[T]())
  lazy val nodes = edges.map(x => x._1) ++ edges.map(x => x._2)

  def connected(startNode: T) = {
    def connectedAux(fromNode: T, foundNodes: Set[T]): Set[T] = {
      val localNodes = forward(fromNode) ++ reverse(fromNode)
      val newFoundNodes = localNodes ++ foundNodes + fromNode
      val localNodesLessFound = localNodes -- foundNodes
      localNodesLessFound.foldRight(newFoundNodes)((x, y) => connectedAux(x, y))
    }
    connectedAux(startNode, Set[T]())
  }

  /**
   *  Finds only approximately 'n' nodes that are connected to the initial node,
   *  using a policy:
   *
   *  It tries to keep a node and all of its children together
   *  So first we go forward, find the connected graph only forward
   *  that does not exceed n, while accumulating a list of nodes that
   *  are backward from the nodes we've found
   *
   *  Then revisit backward nodes, and only 'find' them if finding
   *  that node and all of its immediate children will not exceed n. At that
   *  point go forward again as above
   *
   */
  def limitedConnected(startNode: T, n: Int): Set[T] = {
    type pairSets = (Set[T], Set[T])

    /**
     * Finds nodes forward, accumulating the set of nodes we've
     *  already found and the set of nodes of the reverse nodes of
     *  the ones we've found
     */
    def connectedForward(fromNode: T, accum: pairSets): pairSets = {
      val (foundNodes, reverseNodeLog) = accum

      /* Nodes forward from the fromNode */
      val forwardNodes = forward(fromNode)

      /* Now our new found nodes might be the combination of these forward nodes
       * and what we've found before
       */
      val newFoundNodesMaybe = foundNodes ++ forwardNodes

      /* But only if it doesn't exceed n */
      val newFoundNodes = if (newFoundNodesMaybe.size > n) foundNodes else newFoundNodesMaybe

      /* The nodes we are going to continue traversing are the new nodes minus the ones we've
       * already found. Could be an empty set if newFoundNodes is just foundNodes as above                   
       */
      val nodesToTraverse = newFoundNodes -- foundNodes

      /* Find the nodes behind the ones we about to traverse into */
      val forwardReverseNodes = nodesToTraverse.flatMap(x => reverse(x))

      /* Add them to the reverseLog, while also removing the nodes already found */
      val newReverseNodeLog = reverseNodeLog ++ forwardReverseNodes -- newFoundNodes

      /* Recurse! */
      nodesToTraverse.foldRight((newFoundNodes, newReverseNodeLog))((x, y) => connectedForward(x, y))
    }

    /* Initialize the found nodes to the node we are starting on, 
     * and the reverse log to the nodes behind the starting node
     */
    val (foundNodes, reverseNodeLog) = connectedForward(startNode, (Set[T](startNode), reverse(startNode)))

    /**
     * Now foundNodes is the set of all nodes we've found so far, and reverseNodeLog are the nodes
     *  that are behind the nodes we've found so far
     */
    def connectedReverse(startElem: T, accum: pairSets): pairSets = {
      val (foundNodes, reverseNodeLog) = accum
      
      /* What will new found nodes look like if startElem and its forward elements are found */
      val newFoundNodesMaybe = forward(startElem) ++ foundNodes + startElem
      
      /* If it's greater than n, then let's stick with the old one and we won't have found these */
      val newFoundNodes = if (newFoundNodesMaybe.size > n) foundNodes else newFoundNodesMaybe
      
      /* Remove this node from the reverseNodeLog */
      val newReverseNodeLog = reverseNodeLog - startElem
      
      /* Now we want to find forward on the new nodes we've found */
      val nodesToTraverse = newFoundNodes -- foundNodes
      nodesToTraverse.foldRight((newFoundNodes, newReverseNodeLog))((x, y) => connectedForward(x, y))
    }
    val (finalFoundNodes, finalReverseNodeLog) = reverseNodeLog.foldRight((foundNodes, reverseNodeLog))((x, y) => connectedReverse(x, y))
    finalFoundNodes
  }

  def toDot(label: String, rootNode: T): String = {
    def toDotNode(node: T): String =
      if (node == rootNode)
        node.hashCode + " [shape=polygon,edges=5,peripheries=5,color=" + node.color + ",style=filled,label=\"" + node.toString + "\"]"        
      else
        node.hashCode + " [color=" + node.color + ",style=filled,label=\"" + node.toString + "\"]"

    def toDotNodes: String =
      nodes.map(toDotNode).mkString("\n")

    def toDotEdge(nodeA: T, nodeB: T): String =
      nodeA.hashCode + "->" + nodeB.hashCode

    def toDotEdges =
      edges.map(x => toDotEdge(x._1, x._2)).mkString("\n")

    "digraph " + label + " {\n" + toDotNodes + "\n\n" + toDotEdges + "\n}"
  }
}