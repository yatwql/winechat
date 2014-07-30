package wechat7.agent

import scala.xml.Elem
import scala.xml.Node

import wechat7.logic.Plugin
import wechat7.repo.ActionRepo
import wechat7.repo.AdminRepo
import wechat7.repo.SlickRepo
import wechat7.repo.UserRepo
import wechat7.repo.VoteRepo
import wechat7.util.WechatUtils
class Agent extends SlickRepo with AdminRepo with UserRepo with VoteRepo with ActionRepo with Plugin {
  import profile.simple._
  import system.dispatcher

  def go(requestXml: Option[Elem]): Option[Node] = {
    val appUserId = (requestXml.get \ "ToUserName").text
    val openId = (requestXml.get \ "FromUserName").text
    val requestContent = (requestXml.get \ "Content").text
    val msgType = (requestXml.get \ "MsgType").text
    val requestXmlContent = requestXml.toString
    println("Get Message Type  " + msgType + " from user " + openId)
    audit(openId, appUserId, msgType, requestXmlContent)
    val responseXml = go(openId, appUserId, msgType, requestXml, requestContent)
    responseXml match {
      case Some(s) => {
        val responseContent = responseXml.get.toString()
        val responseMsgType = (responseXml.get \\ "MsgType").text
        audit(appUserId, openId, responseMsgType, responseContent)
      }
      case _ =>
    }

    responseXml
  }

  def go(openId: String, appUserId: String, msgType: String, requestXml: Option[Elem], requestContent: String): Option[Node] = {
    val responseContent = " Thanks for your information '" + requestContent + "' with msg type " + msgType
    Some(WechatUtils.getTextMsg(appUserId, openId, responseContent));
  }

}
class DefaultAgent extends Agent {

}
