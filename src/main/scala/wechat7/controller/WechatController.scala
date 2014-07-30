package wechat7.controller

import scala.xml.Elem
import scala.xml.XML

import wechat7.WechatAppStack
import wechat7.agent._
import wechat7.util.WechatUtils

trait WechatController extends WechatAppStack with AgentProxy  {
  get("/wechatauth") {
    contentType = "text/html"
    val result = WechatUtils.checkSignature(params)
    println(result)
    result
  }

  get("/wechat") {
    redirect("/wechatauth")
  }

  post("/wechatauth") {
    println("request body is -> " + request.body)
    val requestXml = XML.loadString(request.body)
    wechat(Some(requestXml))
  }

  post("/wechat") {
    redirect("/wechatauth")
  }

  get("/test/text/:slug") {
    val slug = params("slug")

    val requestXml = <xml>
                       <ToUserName><![CDATA[gh_c2bb951675bb]]></ToUserName>
                       <Content>{ slug }</Content>
                       <FromUserName><![CDATA[oIySzjrizSaAyqnlB57ggb0j2WNc]]></FromUserName>
                       <MsgType><![CDATA[text]]></MsgType>
                     </xml>
    contentType = "xml;charset=utf-8"
    wechat(Some(requestXml))
  }

  get("/test/news/:slug") {
    val slug = params("slug")

    val requestXml = <xml>
                       <ToUserName><![CDATA[gh_c2bb951675bb]]></ToUserName>
                       <Content>news{ slug }</Content>
                       <FromUserName><![CDATA[oIySzjrizSaAyqnlB57ggb0j2WNc]]></FromUserName>
                       <MsgType><![CDATA[text]]></MsgType>
                     </xml>
    contentType = "xml;charset=utf-8"
    wechat(Some(requestXml))
  }

  def wechat(requestXml: Option[Elem]) {
    contentType = "xml;charset=utf-8"
     // for( s <- Router.response(requestXml)) write(s)
    write(response(requestXml))
  }

}

