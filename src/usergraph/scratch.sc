import usergraph.User
import scala.util.Random

object scratch {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  val joe = new User("joe", "v1")                 //> joe  : usergraph.User = joe(v1)
  val john = new User("john", "v2")               //> john  : usergraph.User = john(v2)

  joe.addCoach(john)
  joe.toString                                    //> res0: String = joe(v1)
  joe.total_infection("whee")
  joe.toString                                    //> res1: String = joe(whee)
  john.toString                                   //> res2: String = john(whee)
  (1 to 100)                                      //> res3: scala.collection.immutable.Range.Inclusive = Range(1, 2, 3, 4, 5, 6, 7
                                                  //| , 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 
                                                  //| 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 
                                                  //| 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 
                                                  //| 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 
                                                  //| 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100)
  val random = new Random(150)                    //> random  : scala.util.Random = scala.util.Random@1c93d6bc
  val numUsers = 100                              //> numUsers  : Int = 100
  val userList = (1 to numUsers).map(x => new User(x.toString, ""))
                                                  //> userList  : scala.collection.immutable.IndexedSeq[usergraph.User] = Vector(1
                                                  //| (), 2(), 3(), 4(), 5(), 6(), 7(), 8(), 9(), 10(), 11(), 12(), 13(), 14(), 15
                                                  //| (), 16(), 17(), 18(), 19(), 20(), 21(), 22(), 23(), 24(), 25(), 26(), 27(), 
                                                  //| 28(), 29(), 30(), 31(), 32(), 33(), 34(), 35(), 36(), 37(), 38(), 39(), 40()
                                                  //| , 41(), 42(), 43(), 44(), 45(), 46(), 47(), 48(), 49(), 50(), 51(), 52(), 53
                                                  //| (), 54(), 55(), 56(), 57(), 58(), 59(), 60(), 61(), 62(), 63(), 64(), 65(), 
                                                  //| 66(), 67(), 68(), 69(), 70(), 71(), 72(), 73(), 74(), 75(), 76(), 77(), 78()
                                                  //| , 79(), 80(), 81(), 82(), 83(), 84(), 85(), 86(), 87(), 88(), 89(), 90(), 91
                                                  //| (), 92(), 93(), 94(), 95(), 96(), 97(), 98(), 99(), 100())
  userList(5)                                     //> res4: usergraph.User = 6()
  userList.foreach(x => x.addCoach(userList(random.nextInt(numUsers))))
  userList.foreach(x => x.addCoach(userList(random.nextInt(numUsers))))
  userList.foreach(x => x.addCoach(userList(random.nextInt(numUsers))))

  userList(5)                                     //> res5: usergraph.User = 6()
  userList(5).getCoaches                          //> res6: scala.collection.mutable.HashSet[usergraph.User] = Set(6(), 54(), 15()
                                                  //| )

	userList(5).getCoachees                   //> res7: scala.collection.mutable.HashSet[usergraph.User] = Set(6())

	userList(5).limited_infection("limited", 2)
	userList(5).getCoaches                    //> res8: scala.collection.mutable.HashSet[usergraph.User] = Set(6(limited), 54(
                                                  //| limited), 15(limited))
	userList(5).getCoachees                   //> res9: scala.collection.mutable.HashSet[usergraph.User] = Set(6(limited))
  userList(5).getCoaches.map(x => x.getCoaches).flatten
                                                  //> res10: scala.collection.mutable.HashSet[usergraph.User] = Set(64(), 6(limite
                                                  //| d), 54(limited), 45(), 29(), 15(limited), 62(), 53(), 97())
	userList(6).toDotNode                     //> res11: String = 7 [label="7()"]
	userList(6).toDotEdges                    //> res12: String = 7->{ 59 }
}