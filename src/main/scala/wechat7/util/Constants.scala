package wechat7.util
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

object Constants {
  val env = scala.util.Properties.envOrElse("runMode", "prod")
  val config = ConfigFactory.load(env)
  val appId = config.getString("wechat.appId")
  val appSecret =  config.getString("wechat.appSecret")
  val TOKEN =  config.getString("wechat.TOKEN")
  val USE_ADVANCED_VERSION = config.getBoolean("wechat.USE_ADVANCED_VERSION")

  val REQ_MSG_TYP_TEXT = "text"
  val REQ_MSG_TYP_IMAGE = "image"
  val REQ_MSG_TYP_VOICE = "voice"
  val REQ_MSG_TYP_VIDEO = "video"
  val REQ_MSG_TYP_LOCATION = "location"
  val REQ_MSG_TYP_LINK = "link"

  val REQ_MSG_TYP_EVENT = "event"
  val EVT_TYP_SUBSCRIBE = "subscribe"
  val EVT_TYP_UNSUBSCRIBE = "unsubscribe"
  val EVT_TYP_CLICK = "CLICK"
  val EVT_TYP_SCAN = "SCAN"
  val EVT_TYP_VIEW = "VIEW"
  val EVT_TYP_LOCATION = "LOCATION"

  val MENU = "menu"

  val REDWINE_PIC = "http://www.cnyangjiu.com/html/UploadFiles/201051975110330.jpg"
  val SHOP_AT_DIANPING = "http://www.dianping.com/shop/17180808/photos"

  val VOTE_METHOD_LIMIT = 1
  val VOTE_METHOD_ALL = 2

}
