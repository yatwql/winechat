package wechat7.repo
import wechat7.repo._
object my {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  class VoteRepoImpl extends VoteRepo {

  }
  val voteRepo = new VoteRepoImpl                 //> SLF4J: Class path contains multiple SLF4J bindings.
                                                  //| SLF4J: Found binding in [jar:file:/Users/wang/.ivy2/cache/org.slf4j/slf4j-no
                                                  //| p/jars/slf4j-nop-1.6.4.jar!/org/slf4j/impl/StaticLoggerBinder.class]
                                                  //| SLF4J: Found binding in [jar:file:/Users/wang/.ivy2/cache/ch.qos.logback/log
                                                  //| back-classic/jars/logback-classic-1.0.6.jar!/org/slf4j/impl/StaticLoggerBind
                                                  //| er.class]
                                                  //| SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanat
                                                  //| ion.
                                                  //| SLF4J: Actual binding is of type [org.slf4j.helpers.NOPLoggerFactory]
                                                  //| voteRepo  : wechat7.repo.my.VoteRepoImpl = wechat7.repo.my$$anonfun$main$1$V
                                                  //| oteRepoImpl$1@cd3fee8
  
  
  val vote = voteRepo.getVoteThread(21)           //> Connection info =>Run mode: dev, db url: jdbc:postgresql://localhost:5432/we
                                                  //| chatdb, driver: org.postgresql.Driver
                                                  //| vote  : Option[wechat7.repo.my.voteRepo.VoteThread] = Some(VoteThread(21,红
                                                  //| 酒调查(地点),您喜欢以下哪个产地的红酒: ,1))
                                                  
                                                  val c=voteRepo.getVoteGrpResult(22)
                                                  //> java.lang.NoSuchMethodError: wechat7.repo.VoteRepo$class.getVoteGrpResult(Lw
                                                  //| echat7/repo/VoteRepo;I)Lscala/slick/lifted/Query;
                                                  //| 	at wechat7.repo.my$$anonfun$main$1$VoteRepoImpl$1.getVoteGrpResult(wecha
                                                  //| t7.repo.my.scala:6)
                                                  //| 	at wechat7.repo.my$$anonfun$main$1.apply$mcV$sp(wechat7.repo.my.scala:14
                                                  //| )
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$$anonfun$$exe
                                                  //| cute$1.apply$mcV$sp(WorksheetSupport.scala:76)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$.redirected(W
                                                  //| orksheetSupport.scala:65)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$.$execute(Wor
                                                  //| ksheetSupport.scala:75)
                                                  //| 	at wechat7.repo.my$.main(wechat7.repo.my.scala:3)
                                                  //| 	at wechat7.repo.my.main(wechat7.repo.my.scala)
                                                  
                                                  
               
                                                  
  
}