package wechat7

import org.scalatra.test.specs2._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
//import org.specs.runner.JUnitSuiteRunner
//@RunWith(classOf[JUnitRunner])

// For more on Specs2, see http://etorreborre.github.com/specs2/guide/org.specs2.guide.QuickStart.html
@RunWith(classOf[JUnitRunner])
trait MyControllerSpec extends ScalatraSpec {
  

  addServlet(classOf[MyController], "/*")

 // def root200 = statusResult("/",200)
  
  def statusResult(url: String, code: Int) =
    get(url) {
      //println(" Access URl -> " + url)
      //println(" body -> " + body)
      //println(" status -> " + status)
      status must_== code
    }

  def bodyResult(url: String, expectedBody: String) =
    get(url) {
     // println(" Access URl -> " + url)
     // println(" body -> " + body)
     // println(" status -> " + status)
      body must contain(expectedBody)
    }
  
   def verifyResult(url: String, expectedBody:String,code: Int=200) =
    get(url) {
      println(" Access URl -> " + url)
      //println(" body -> " + body)
      println(" status -> " + status)
      (status must_== code) and ( body must contain(expectedBody))
    }
}
