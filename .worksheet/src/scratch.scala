import usergraph.User
import scala.util.Random

object scratch {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(108); 
  println("Welcome to the Scala worksheet");$skip(34); 
  val joe = new User("joe", "v1");System.out.println("""joe  : usergraph.User = """ + $show(joe ));$skip(36); 
  val john = new User("john", "v2");System.out.println("""john  : usergraph.User = """ + $show(john ));$skip(22); 

  joe.addCoach(john);$skip(15); val res$0 = 
  joe.toString;System.out.println("""res0: String = """ + $show(res$0));$skip(30); 
  joe.total_infection("whee");$skip(15); val res$1 = 
  joe.toString;System.out.println("""res1: String = """ + $show(res$1));$skip(16); val res$2 = 
  john.toString;System.out.println("""res2: String = """ + $show(res$2));$skip(13); val res$3 = 
  (1 to 100);System.out.println("""res3: scala.collection.immutable.Range.Inclusive = """ + $show(res$3));$skip(31); 
  val random = new Random(150);System.out.println("""random  : scala.util.Random = """ + $show(random ));$skip(21); 
  val numUsers = 100;System.out.println("""numUsers  : Int = """ + $show(numUsers ));$skip(68); 
  val userList = (1 to numUsers).map(x => new User(x.toString, ""));System.out.println("""userList  : scala.collection.immutable.IndexedSeq[usergraph.User] = """ + $show(userList ));$skip(14); val res$4 = 
  userList(5);System.out.println("""res4: usergraph.User = """ + $show(res$4));$skip(72); 
  userList.foreach(x => x.addCoach(userList(random.nextInt(numUsers))));$skip(72); 
  userList.foreach(x => x.addCoach(userList(random.nextInt(numUsers))));$skip(72); 
  userList.foreach(x => x.addCoach(userList(random.nextInt(numUsers))));$skip(15); val res$5 = 

  userList(5);System.out.println("""res5: usergraph.User = """ + $show(res$5));$skip(25); val res$6 = 
  userList(5).getCoaches;System.out.println("""res6: scala.collection.mutable.HashSet[usergraph.User] = """ + $show(res$6));$skip(26); val res$7 = 

	userList(5).getCoachees;System.out.println("""res7: scala.collection.mutable.HashSet[usergraph.User] = """ + $show(res$7));$skip(46); 

	userList(5).limited_infection("limited", 2);$skip(24); val res$8 = 
	userList(5).getCoaches;System.out.println("""res8: scala.collection.mutable.HashSet[usergraph.User] = """ + $show(res$8));$skip(25); val res$9 = 
	userList(5).getCoachees;System.out.println("""res9: scala.collection.mutable.HashSet[usergraph.User] = """ + $show(res$9));$skip(56); val res$10 = 
  userList(5).getCoaches.map(x => x.getCoaches).flatten;System.out.println("""res10: scala.collection.mutable.HashSet[usergraph.User] = """ + $show(res$10));$skip(23); val res$11 = 
	userList(6).toDotNode;System.out.println("""res11: String = """ + $show(res$11));$skip(24); val res$12 = 
	userList(6).toDotEdges;System.out.println("""res12: String = """ + $show(res$12))}
}
