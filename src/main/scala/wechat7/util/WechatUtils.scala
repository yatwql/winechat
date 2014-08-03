package wechat7.util

import java.io.InputStream

import wechat7.repo._
import java.util.Date

import scala.xml._

import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse
import org.json4s.jvalue2extractable
import org.json4s.jvalue2monadic
import org.json4s._
object WechatUtils {
  implicit val formats = DefaultFormats

  def checkSignature(params: org.scalatra.Params): String =
    {
      val signature = params.getOrElse("signature", "")
      println("signature = " + signature)
      val timestamp = params.getOrElse("timestamp", "")
      println("timestamp = " + timestamp)
      val nonce = params.getOrElse("nonce", "")
      println("nonce = " + nonce)
      val echostr = params.getOrElse("echostr", "")
      println("echostr = " + echostr)

      val token = Constants.TOKEN
      println("token = " + token)
      val tmpStr = Array(token, timestamp, nonce).sortWith(_ < _).mkString

      println("tmpStr = " + tmpStr)

      val md = java.security.MessageDigest.getInstance("SHA1")

      val ha = md.digest(tmpStr.getBytes("UTF-8")).map("%02x".format(_)).mkString

      println("ha = " + ha)

      println("signature = " + signature)

      if (ha == signature) {
        echostr
      } else {
        "InvalidSigatureResult"
      }

    }

  def getAccess_token(): String = { // 获得ACCESS_TOKEN

    val url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + Constants.appId + "&secret=" + Constants.appSecret;

    try {
      val message = HttpUtils.get(url)

      val json = parse(message)

      println("The return message -> " + message)
      //val accessToken = compact(render(json \\ "access_token"))

      val accessToken = (json \ "access_token").extract[String]
      println("The accessToken -> " + accessToken)
      accessToken
    } catch {
      case e: Exception => e.printStackTrace(); "";
    }

  }

  def loadMenuFromFile: String = {
    val in: InputStream = getClass().getResourceAsStream("/menu.json")
    val size = in.available();
    val data = new Array[Byte](size)
    in.read(data);
    val message = new String(data, "UTF-8");
    message
  }

  def createMenu(menu: String): String = {
    try {
      val access_token = WechatUtils.getAccess_token
      //val access_token = "Testing"
      println(" access_token -> " + access_token)
      val menu_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + access_token;
      println(" Will post to " + menu_url)
      val message = HttpUtils.post(menu_url, menu);
      val json = parse(message)
      val errcode = (json \\ "errcode").extract[Int]
      val errmsg = (json \\ "errmsg").extract[String]

      println(" errcode -> " + errcode)

      println(" create menu -> " + menu)

      val responseMsg = "URL -> " + menu_url + " </br></br>  Menu -> " + menu

      if (errcode != 0) {
        responseMsg + " </br></br> Failed to create menu due to " + errmsg
      } else {
        responseMsg
      }

    } catch {
      case e: Exception => e.printStackTrace(); "";
    }
  }

  def getMenu(): String = {
    val menu_url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token="
    val message = getPageByToken("Get menu", menu_url)
    val responseMsg = "URL -> " + menu_url + " </br></br>  Menu -> " + message

    responseMsg
  }

  def deleteMenu(): String = {
    val menu_url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token="
    val message = getPageByToken("Delete menu", menu_url)
    val responseMsg = "URL -> " + menu_url + " </br></br>  Menu -> " + message

    responseMsg
  }

  def getUserInfo(openId: String): Option[JValue] = {

    Constants.USE_ADVANCED_VERSION match {
      case true => {
        val url = "https://api.weixin.qq.com/cgi-bin/user/info?openid=" + openId + "&lang=zh_CN&access_token="
        val message = getPageByToken("Get User Info for open user id " + openId, url)
        val json = parse(message)
        val nickname = (json \ "nickname").extract[String]
        val sex = (json \ "sex").extract[String]
        val language = (json \ "language").extract[String]
        val city = (json \ "city").extract[String]
        val province = (json \ "province").extract[String]
        val country = (json \ "country").extract[String]
        val headimgurl = (json \ "headimgurl").extract[String]
        val subscribeTime = (json \ "subscribe_time").extract[String]
        Some(json)
      }
      case _ => None
    }

  }

  def getPageByToken(action: String, url: String): String = {
    try {
      val access_token = WechatUtils.getAccess_token

      println(" access_token -> " + access_token)
      val menu_url = url + access_token;
      println(" menu ul -> " + menu_url)
      val message = HttpUtils.get(menu_url)

      println(action + " -> " + message)
      message

    } catch {
      case e: Exception => e.printStackTrace(); "";
    }
  }

  def getTextMsg(fromUser: String, toUser: String, content: String): Node = {
    val now = new Date().getTime()
    val message =
      <xml>
        <ToUserName>{ toUser }</ToUserName>
        <FromUserName>{ fromUser }</FromUserName>
        <Content> { content }</Content>
        <CreateTime>{ now }</CreateTime>
        <MsgType><![CDATA[text]]></MsgType>
        <FuncFlag>0</FuncFlag>
      </xml>
    message

  }

  def getNewsMsg(fromUser: String, toUser: String, title: String, content: String, picUrl: String, url: String): Node = {
    val item = <item>
                 <Title>{ title }</Title>
                 <Description>{ content }</Description>
                 <PicUrl>{ picUrl }</PicUrl>
                 <Url>{ url }</Url>
               </item>
    val items = Seq(item)
    getNewsMsg(fromUser, toUser, items)

  }

  def getNewsMsg(fromUser: String, toUser: String, items: Seq[Node]): Node = {

    val now = new Date().getTime()
    val oldXML =
      <xml>
        <ToUserName>{ toUser }</ToUserName>
        <FromUserName>{ fromUser }</FromUserName>
        <CreateTime>{ now }</CreateTime>
        <MsgType><![CDATA[news]]></MsgType>
        <ArticleCount>{ items.size }</ArticleCount>
        <Articles>
        </Articles>
        <FuncFlag>0</FuncFlag>
      </xml>

    val message = XmlUtils.addChildren(oldXML, "Articles", items)
    //println(message.toString().length())
    message
  }

}