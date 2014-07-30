package wechat7.agent

import scala.xml.Elem
import scala.xml.Node
import wechat7.logic.ActionRouter
import wechat7.util.Constants
import wechat7.util.WechatUtils
import wechat7.util.CacheMgr
class EventAgent extends Agent with ActionRouter {

  override def go(openId: String, appUserId: String, msgType: String, requestXml: Option[Elem], requestContent: String): Option[Node] = {
    import wechat7.util.Constants
    val event = (requestXml.get \ "Event").text
    val nickname = getNickname(openId).get
    val responseContent = nickname + "  love redwine for event " + event

    event match {
      case Constants.EVT_TYP_SUBSCRIBE => {
        updateSubscriptStatus(openId, 1)
        val item1 = <item>
                      <Title>Welcome you - { nickname }</Title>
                      <Description>I love redwine</Description>
                      <PicUrl>{ Constants.REDWINE_PIC }</PicUrl>
                      <Url>{ Constants.SHOP_AT_DIANPING }</Url>
                    </item>

        val items = Seq(item1)
        val node = WechatUtils.getNewsMsg(appUserId, openId, items)
        Some(node)
      }
      case Constants.EVT_TYP_UNSUBSCRIBE => {
        CacheMgr.nicknameCache.remove(openId)
        updateSubscriptStatus(openId, 0)
        None
      }
      case Constants.EVT_TYP_SCAN => Some(WechatUtils.getNewsMsg(appUserId, openId, "Hello world ", responseContent, Constants.REDWINE_PIC, Constants.SHOP_AT_DIANPING));
      case Constants.EVT_TYP_LOCATION => Some(WechatUtils.getNewsMsg(appUserId, openId, "Hello world ", responseContent, Constants.REDWINE_PIC, Constants.SHOP_AT_DIANPING));
      case Constants.EVT_TYP_CLICK => {
        val eventKey = (requestXml.get \ "EventKey").text
        response(openId, nickname, appUserId, msgType, eventKey,eventKey)
      }
      case Constants.EVT_TYP_VIEW => None
      case _ => None;
    }
  }
}
