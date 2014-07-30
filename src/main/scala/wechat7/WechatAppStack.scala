package wechat7

import org.scalatra._

import org.scalatra.scalate.ScalateSupport
import org.fusesource.scalate.TemplateEngine
import org.fusesource.scalate.layout.DefaultLayoutStrategy
import javax.servlet.http.HttpServletRequest
import collection.mutable
//(override val profile: JdbcProfile = SlickDBDriver.getDriver) extends DomainComponent with Profile {
trait WechatAppStack extends ScalatraServlet with ScalateSupport{

  /* wire up the precompiled templates */
  override protected def defaultTemplatePath: List[String] = List("/WEB-INF/templates/views")
  override protected def createTemplateEngine(config: ConfigT) = {
    val engine = super.createTemplateEngine(config)
    engine.layoutStrategy = new DefaultLayoutStrategy(engine,
      TemplateEngine.templateTypes.map("/WEB-INF/templates/layouts/default." + _): _*)
    engine.packagePrefix = "templates"
    engine
  }
  /* end wiring up the precompiled templates */
  
  override protected def templateAttributes(implicit request: HttpServletRequest): mutable.Map[String, Any] = {
    super.templateAttributes ++ mutable.Map.empty // Add extra attributes here, they need bindings in the build file
  }
  
  def write(content:String) {
    val writer = response.getWriter()
    try {
      writer.write(content)
    } catch {
      case e: Exception =>
    } finally {
      writer.close()
    }
  }
  
  def write(content:Option[String]) {
    content match  {
      case Some(msg) => write(msg)
      case _ =>
    }
  }
  
  def write(seq:Seq[String]){
    val writer = response.getWriter()
    try {
      for (content <- seq) {
        writer.write(content)
        //println(content)}
      }
    } catch {
      case e: Exception =>
    } finally {
      writer.close()
    }
  }
  
  error {
    case t: Throwable => t.printStackTrace()
  }

  notFound {
    // remove content type in case it was set through an action
    contentType = null
    // Try to render a ScalateTemplate if no route matched
    findTemplate(requestPath) map { path =>
      contentType = "text/html"
      layoutTemplate(path)
    } orElse serveStaticResource() getOrElse resourceNotFound()
  }
}
