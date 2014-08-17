package wechat7.agent

import scala.xml.Elem
import scala.xml.Node

import wechat7.logic.ActionRouter
class TextAgent extends Agent with ActionRouter {

  override def go(openId: String, appUserId: String, msgType: String, requestXml: Option[Elem], requestContent: String): Option[Node] = {
    val nickname = getNickname(openId) match {
      case Some(s) => s
      case None => ""
    }
    response(openId, nickname, appUserId, msgType, requestContent, requestContent)
  }

}
