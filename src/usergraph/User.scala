package usergraph
/** Provides classes to implement the graph of KA users */

/**
 * A user of the system
 *
 *  @constructor creates a new person with a name and version
 *  @param name the user's name
 *  @param version the starting version of the software for this user
 *
 */
class User(name: String, var version: String) extends GraphNode {
  override def toString(): String = name + "(" + version + ")"
  val colorMap = Map(0 -> "orange", 1 -> "red", 2 -> "green")
  
  def color = {
    colorMap(version.toInt % colorMap.size)
  }

  /** Sets the version for this user */
  def setVersion(v: String): Unit = {
    version = v
  }

  /** Gets the version */
  def getVersion: String = version

  /**
   * Infects everyone connected to this user, and this user, with the new
   * version, given a graph
   */
  def totalInfection(new_version: String, graph: Graph[User]): Unit = {
    val toInfect = graph.connected(this)
    println(toInfect)
    toInfect.foreach(x => x.setVersion(new_version))
  }
  
  def limitedInfection(new_version: String, n: Int, graph: Graph[User]): Unit = {
    val toInfect = graph.limitedConnected(this, n)
    toInfect.foreach(x => x.setVersion(new_version))
  }
}
  