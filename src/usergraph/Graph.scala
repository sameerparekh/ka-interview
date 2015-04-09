package usergraph

class Graph[T](edges: Set[(T, T)]) {
  private def edgesFromElem(elem: T) = (elem, edges.filter(x => x._1 == elem).map(x => x._2))
  private def edgesToElem(elem: T) = (elem, edges.filter(x => x._2 == elem).map(x => x._1))

  lazy val forward = edges.map(x => x._1).map(edgesFromElem).toMap
  lazy val reverse = edges.map(x => x._2).map(edgesToElem).toMap
  
  def connected(startElem: T) = {
    def connectedAux(fromElem: T, foundElems: Set[T]): Set[T] = {
      val localElems = forward.getOrElse(fromElem, Set[T]()) ++ reverse.getOrElse(fromElem, Set[T]()) 
      val newFoundElems = localElems ++ foundElems + fromElem
      val localElemsLessFound = localElems -- foundElems
      localElemsLessFound.foldRight(newFoundElems)((x, y) => connectedAux(x, y))
    }
    connectedAux(startElem, Set[T]())
  }
}