package usergraph
import scala.util.Random
import scala.sys.process._
import scala.math.abs
import java.io._
import scala.collection.mutable.HashMap

object Test extends App {
  def toDot(users: Iterable[User], colorMap: HashMap[String, String], rootUser: User): String =
    "digraph users {\n" + users.map(x => x.toDotNode(colorMap, x == rootUser)).mkString("\n") + "\n\n" +
      users.map(x => x.toDotEdges).mkString("\n") + "\n}"

  var testNumber = 0
  def runTests(seed: Int, numUsers: Int): Unit = {
    val random = new Random(seed)
    println("Running tests with seed " + seed.toString + " numUsers: " + numUsers.toString)

    def time(f: => Unit) = {
      val s = System.currentTimeMillis
      f
      System.currentTimeMillis - s
    }
    
    def testTotal(userList: IndexedSeq[User]): Unit = {
      testNumber += 1
      println("Running total_infection test " + testNumber.toString)

      val randomUser = random.nextInt(userList.length)
      println("root user: " + (randomUser + 1))

      println("time: " + (time { userList(randomUser).total_infection(testNumber.toString) }) + "ms")

      val pw = new PrintWriter(testNumber.toString + ".dot")
      val colorMap = HashMap(testNumber.toString -> "red")

      pw.write(toDot(userList, colorMap, userList(randomUser)))
      pw.close

      // All coaches are infected
      userList(randomUser).getCoaches.foreach(x => assert(x.getVersion == testNumber.toString))

      // Coaches' coaches are infected
      userList(randomUser).getCoaches.map(x => x.getCoaches).flatten.foreach(x => assert(x.getVersion == testNumber.toString))

      // Coaches' coachees are infected
      userList(randomUser).getCoaches.map(x => x.getCoachees).flatten.foreach(x => assert(x.getVersion == testNumber.toString))

      // Coachees are infected
      userList(randomUser).getCoachees.foreach(x => assert(x.getVersion == testNumber.toString))

      // Coachees' coaches are infected
      userList(randomUser).getCoachees.map(x => x.getCoaches).flatten.foreach(x => assert(x.getVersion == testNumber.toString))

      // Coachees' coachees are infected
      userList(randomUser).getCoachees.map(x => x.getCoachees).flatten.foreach(x => assert(x.getVersion == testNumber.toString))

    }

    def testLimited(userList: IndexedSeq[User], n: Int): Unit = {
      testNumber += 1
      println("Running limited_infection test " + testNumber.toString + " to infect " + n.toString)
      val randomUser = random.nextInt(userList.length)
      println("root user: " + (randomUser + 1))
      println("Time: " + (time { userList(randomUser).limitedInfection(testNumber.toString, n) }) + "ms")

      val pw = new PrintWriter(testNumber.toString + ".dot")
      val colorMap = HashMap(testNumber.toString -> "red")

      pw.write(toDot(userList, colorMap, userList(randomUser)))
      pw.close

      val c = userList.count(x => x.getVersion == testNumber.toString)
      println("number infected: " + c)
    }

    val simpleList = (1 to numUsers).map(x => new User(x.toString, ""))

    val tenPercent: Int = numUsers / 10
    val fivePercent: Int = numUsers / 20
    val twoPercent: Int = numUsers / 50

    /* The first 10% can coach the last 90%, each one coaches 5% */
    (1 to fivePercent).foreach(x => simpleList.take(tenPercent).foreach(x => x.addCoachee(simpleList(random.nextInt(numUsers - tenPercent) + tenPercent))))

    /* The last 10% can coach anyone, each one coaches 1% */
    (1 to twoPercent).foreach(x => simpleList.takeRight(tenPercent).foreach(x => x.addCoachee(simpleList(random.nextInt(numUsers)))))

    testTotal(simpleList) 
    testLimited(simpleList, numUsers / 10) 
    testLimited(simpleList, numUsers / 3) 
    testLimited(simpleList, numUsers / 2) 
    testLimited(simpleList, numUsers) 
    testLimited(simpleList, numUsers * 2) 


    val complexList = (1 to numUsers).map(x => new User(x.toString, ""))

    /* In the complex graph, each user will coach any 3 other users */
    (1 to 3).foreach(x => complexList.foreach(x => x.addCoachee(complexList(random.nextInt(numUsers)))))

    testTotal(complexList) 
    testLimited(complexList, numUsers / 10) 
    testLimited(complexList, numUsers / 3) 
    testLimited(complexList, numUsers / 2) 
    testLimited(complexList, numUsers) 
    testLimited(complexList, numUsers * 2)     
  }
  (1 to 100).foreach(x => runTests(x, 100))
  (1 to 100).foreach(x => runTests(x, 500))
  (1 to 100).foreach(x => runTests(x, 1000))

}