package wechat7.controller

import org.scalatra.test.specs2._
import wechat7.MyController
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import wechat7.MyControllerSpec

@RunWith(classOf[JUnitRunner])
class WechatControllerSpec extends MyControllerSpec {
  def is =
    "GET /wechatauth on WechatController" ^
      "wechatauth should return the echostr" ! testAuthFunction ^
      "/ should return status 200" ! root200 ^
      "/unknown should return status 400" ! unknown404 ^
      "/test/text/vote21 should return the vote result" ! testVote21 ^
      end
  def testAuthFunction = verifyResult("/wechatauth?signature=439bde0d5af260f241a327b44eef7e531c20c02a&echostr=2821088496660249143&timestamp=1398698533&nonce=1773830803", "2821088496660249143")
  def root200 = statusResult("/",200)
  def unknown404 = statusResult("/unknown",404)
  def testVote21 =verifyResult("/test/text/vote21","<Title>欢迎参加红酒调查(地点) </Title>")
}