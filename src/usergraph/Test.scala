package usergraph

import java.io._

import scala.collection.mutable.HashMap
import scala.util.Random

object Test extends App {


  var testNumber = 0
  def runTests(seed: Int, numUsers: Int): Unit = {
    val random = new Random(seed)
    println("Running tests with seed " + seed.toString + " numUsers: " + numUsers.toString)

    def time(f: => Unit) = {
      val s = System.currentTimeMillis
      f
      System.currentTimeMillis - s
    }
    
    val userList = (1 to numUsers).map(x => new User(x.toString, "0")).toSet

    def testTotal(graph: Graph[User]): Unit = {
      testNumber += 1
      println("Running total_infection test " + testNumber.toString)

      val randomUser = random.nextInt(userList.size)
      println("root user: " + (randomUser + 1))

      println("time: " + (time { userList.toVector(randomUser).totalInfection(testNumber.toString, graph) }) + "ms")

      val pw = new PrintWriter(testNumber.toString + ".dot")
      pw.write(graph.toDot(testNumber.toString, userList.toVector(randomUser)))
      pw.close

      // All coaches are infected
      graph.reverse(userList.toVector(randomUser)).foreach(x => assert(x.getVersion == testNumber.toString))

      // Coaches' coaches are infected
      graph.reverse(userList.toVector(randomUser)).map(x => graph.reverse(x)).flatten.foreach(x => assert(x.getVersion == testNumber.toString))

      // Coaches' coachees are infected
      graph.reverse(userList.toVector(randomUser)).map(x => graph.forward(x)).flatten.foreach(x => assert(x.getVersion == testNumber.toString))

      // Coachees are infected
      graph.forward(userList.toVector(randomUser)).foreach(x => assert(x.getVersion == testNumber.toString))

      // Coachees' coaches are infected
      graph.forward(userList.toVector(randomUser)).map(x => graph.reverse(x)).flatten.foreach(x => assert(x.getVersion == testNumber.toString))

      // Coachees' coachees are infected
      graph.forward(userList.toVector(randomUser)).map(x => graph.forward(x)).flatten.foreach(x => assert(x.getVersion == testNumber.toString))

    }

    def testLimited(graph: Graph[User], n: Int): Unit = {
      testNumber += 1
      println("Running limited_infection test " + testNumber.toString + " to infect " + n.toString)
      val randomUser = random.nextInt(userList.size)
      println("root user: " + (randomUser + 1))
      println("Time: " + (time { userList.toVector(randomUser).limitedInfection(testNumber.toString, n, graph) }) + "ms")

      val pw = new PrintWriter(testNumber.toString + ".dot")
      pw.write(graph.toDot(testNumber.toString, userList.toVector(randomUser)))
      pw.close

      val c = userList.count(x => x.getVersion == testNumber.toString)
      println("number infected: " + c)
    }


    val tenPercent: Int = numUsers / 10
    val fivePercent: Int = numUsers / 20
    val twoPercent: Int = numUsers / 50
    
    

    /* The first 10% can coach the last 90%, each one coaches 5% */
    val simpleEdges = (1 to fivePercent).flatMap(x => userList.take(tenPercent).map(x => (x, userList.toVector(random.nextInt(numUsers - tenPercent) + tenPercent)))).toSet ++
                      (1 to twoPercent).flatMap(x => userList.takeRight(tenPercent).map(x => (x, userList.toVector(random.nextInt(numUsers))))).toSet

    val simpleGraph = new Graph(simpleEdges)
    testTotal(simpleGraph) 
    testLimited(simpleGraph, numUsers / 10) 
    testLimited(simpleGraph, numUsers / 3) 
    testLimited(simpleGraph, numUsers / 2) 
    testLimited(simpleGraph, numUsers) 
    testLimited(simpleGraph, numUsers * 2) 


    val complexEdges = 
    /* In the complex graph, each user will coach any 3 other users */
    (1 to 3).flatMap(x => userList.map(x => (x, userList.toVector(random.nextInt(numUsers))))).toSet
    val complexGraph = new Graph(complexEdges)
    
    testTotal(complexGraph) 
    testLimited(complexGraph, numUsers / 10) 
    testLimited(complexGraph, numUsers / 3) 
    testLimited(complexGraph, numUsers / 2) 
    testLimited(complexGraph, numUsers) 
    testLimited(complexGraph, numUsers * 2)     
  }
  //(1 to 100).foreach(x => runTests(x, 100))
  //(1 to 100).foreach(x => runTests(x, 500))
  //(1 to 100).foreach(x => runTests(x, 1000))
  runTests(1, 100)

}