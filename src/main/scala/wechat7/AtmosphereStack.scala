package wechat7

import org.scalatra._
import scalate.ScalateSupport
import org.scalatra.atmosphere._
import org.scalatra.servlet.AsyncSupport
import org.scalatra.json.{JValueResult, JacksonJsonSupport}
import org.json4s._
import JsonDSL._
import java.util.Date
import java.text.SimpleDateFormat
import org.fusesource.scalate.Template

import scala.concurrent._
import ExecutionContext.Implicits.global


trait AtmosphereStack extends ScalatraServlet with ScalateSupport with JValueResult 
  with JacksonJsonSupport with SessionSupport 
  with AtmosphereSupport {
  implicit protected val jsonFormats: Formats = DefaultFormats
}