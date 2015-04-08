package usergraph
/** Provides classes to implement the graph of KA users */

import scala.collection.mutable.{ HashSet, HashMap }

/**
 * A user of the system
 *
 *  @constructor creates a new person with a name and version
 *  @param name the user's name
 *  @param version the starting version of the software for this user
 *
 */
class User(name: String, var version: String) {
  private val coachees = new HashSet[User]()
  private val coaches = new HashSet[User]()

  override def toString(): String = name + "(" + version + ")"

  def toDotNode(colorMap: HashMap[String, String], root: Boolean): String = {
    if (root)
      if (colorMap contains version)
        name + " [shape=polygon, sides=5, peripheries=3, style=filled, color=" + colorMap(version) + ", label=\"" + this.toString + "\"]"
      else
        name + " [shape=polygon, sides=5, peripheries=3,l abel=\"" + this.toString + "\"]"
    else if (colorMap contains version)
      name + " [label=\"" + this.toString + "\", style=filled, color=" + colorMap(version) + "]"
    else
      name + " [label=\"" + this.toString + "\"]"
  }
  def toDotEdges(): String = name + "->{ " + this.coachees.map(x => x.getName).mkString(" ") + " }"

  def isCoachee(u: User) = coachees contains u
  def isCoach(u: User) = coaches contains u

  /** Adds a coachee to the user */
  def addCoachee(u: User): Unit = {
    coachees.add(u)
    if (!u.isCoach(this))
      u.addCoach(this)
  }

  /** Adds a coach to the user */
  def addCoach(u: User): Unit = {
    coaches.add(u)
    if (!u.isCoachee(u))
      u.addCoachee(this)
  }

  /** Gets the coach list */
  def getCoaches = coaches

  /** Gets the coachee list */
  def getCoachees = coachees

  /** Get the name */
  def getName = name

  /** Sets the version for this user */
  def setVersion(v: String): Unit = {
    version = v
  }

  /** Gets the version */
  def getVersion: String = version

  /**
   * Infects everyone connected to this user, and this user, with the new
   *  version, uses DFS
   */
  def total_infection(new_version: String): Unit = {
    val infected = new HashSet[User]()

    def infect(u: User): Unit = {
      if (!(infected contains u)) {
        u.setVersion(new_version)
        infected add u
        u.getCoachees.foreach(x => infect(x))
        u.getCoaches.foreach(x => infect(x))
      }
    }
    infect(this)
  }

  /**
   * Only infects approximately n people connected to this user, including
   *  this user, with the new version
   *
   *  It tries to keep a coach and all of his coachees on the same version
   *  So first we go 'down' the chain, given a coach to start with
   *  first infect his coachees, if that will not exceed 'n'.
   *
   *  Then infect each coachee's coachees, if that will not exceed 'n'
   *
   *  Keep going until we have run out of slots or coachees
   *
   *  Then go 'up' the chain, and start infecting the coaches of the
   *  people we've already infected, one by one, infecting their coachees,
   *  if necessary, using the same algo as above
   *
   */
  def limitedInfection(new_version: String, n: Int): Unit = {
    val infected = new HashSet[User]()
    val coachesToInfect = new HashSet[User]()
    var infectedCount: Int = 0

    /**
     * Infects the user, if the user hasn't already been infected,
     *  removed the user from the list of coaches to infect, and
     *  adds all of the user's coaches which haven't been infected
     *  to the list of coaches to infect
     */
    def infect(u: User): User = {
      if (!(infected contains u)) {
        u.setVersion(new_version)
        infected add u
        infectedCount += 1
        if (coachesToInfect contains u) {
          coachesToInfect remove u
        }
        val coachesToAdd = u.getCoaches.filter(x => !(infected contains x))
        coachesToAdd.foreach(coachesToInfect.add)
      }
      u
    }

    /** Infect all the coachees of the user, recursively */
    def infectDown(u: User): Unit = {
      if (u.getCoachees.count(x => !(infected contains x)) + infectedCount <= n) {
        val toInfectList = u.getCoachees.filter(x => !(infected contains x))
        toInfectList.map(infect).foreach(infectDown)
      }
    }

    /* Actually infect this user and start infecting down */
    infect(this)
    infectDown(this)

    /**
     * Infect up until we are done, once coach at a time, only infect a coach if we
     *  can infect his entire set of coachees
     */
    def infectUp: Unit = {
      if (infectedCount < n && coachesToInfect.count(x => !(infected contains x)) > 0) {
        def infectCoach(u: User) {
          /* See if we can infect the coach and his whole team */
          if (u.getCoachees.count(x => !(infected contains x)) + infectedCount + 1 <= n) {
            infect(u)
            infectDown(u)
          } else
            /* If not remove him from the list of coaches to infect */
            coachesToInfect remove u
        }
        coachesToInfect.filter(x => !(infected contains x)).foreach(infectCoach)
        infectUp
      }
    }

    /* Now that we're done infecting down, start infecting up */
    infectUp
  }
}
  