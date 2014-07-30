package wechat7.agent

import scala.xml.Elem

import wechat7.util.Constants


trait AgentProxy {
 
  
  def response(requestXml: Option[Elem]): Option[String] = {

    val msgType = (requestXml.get \ "MsgType").text

    val agent: Agent =
      msgType match {
        case Constants.REQ_MSG_TYP_TEXT => new TextAgent
        case Constants.REQ_MSG_TYP_IMAGE => new DefaultAgent
        case Constants.REQ_MSG_TYP_VOICE => new DefaultAgent
        case Constants.REQ_MSG_TYP_VIDEO => new DefaultAgent
        case Constants.REQ_MSG_TYP_LOCATION => new DefaultAgent
        case Constants.REQ_MSG_TYP_LINK => new DefaultAgent
        case Constants.REQ_MSG_TYP_EVENT => new EventAgent
        case _ => new DefaultAgent
      }

    agent.go(requestXml) match {
      case Some(node) => Some(node.toString())
      case _ => None
    }

  }
}
