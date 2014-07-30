import wechat7._


import org.slf4j.LoggerFactory

import org.scalatra._
import javax.servlet.ServletContext
import wechat7.MyController


class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    val logger = LoggerFactory.getLogger(getClass)
    context.mount(new MyController, "/*")
    context.addServlet("/*",classOf[wechat7.MyController] )
  }
}
