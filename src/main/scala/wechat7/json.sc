package wechat7
import org.json4s._
import org.json4s.jackson.JsonMethods._
import java.net._
import wechat7.util._
object json {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  case class tokenClass(access_token:String,expires_in:String)
   implicit val formats = DefaultFormats          //> formats  : org.json4s.DefaultFormats.type = org.json4s.DefaultFormats$@604ed
                                                  //| 9f0
  val appId = "wx97c2cba93843a8e6"                //> appId  : String = wx97c2cba93843a8e6
  val appSecret = "4f488c849ddf23c83ef97e5b8482b8a1"
                                                  //> appSecret  : String = 4f488c849ddf23c83ef97e5b8482b8a1
  val lotto = """
{
  "lotto":{
    "lotto-id":5,
    "winning-numbers":[2,45,34,23,7,5,3],
    "winners":[ {
      "winner-id":23,
      "numbers":[2,45,34,23,3, 5]
    },{
      "winner-id" : 54 ,
      "numbers":[ 52,3, 12,11,18,22 ]
    }]
  }
}
"""                                               //> lotto  : String = "
                                                  //| {
                                                  //|   "lotto":{
                                                  //|     "lotto-id":5,
                                                  //|     "winning-numbers":[2,45,34,23,7,5,3],
                                                  //|     "winners":[ {
                                                  //|       "winner-id":23,
                                                  //|       "numbers":[2,45,34,23,3, 5]
                                                  //|     },{
                                                  //|       "winner-id" : 54 ,
                                                  //|       "numbers":[ 52,3, 12,11,18,22 ]
                                                  //|     }]
                                                  //|   }
                                                  //| }
                                                  //| "

  val json = parse(lotto)                         //> json  : org.json4s.JValue = JObject(List((lotto,JObject(List((lotto-id,JInt(
                                                  //| 5)), (winning-numbers,JArray(List(JInt(2), JInt(45), JInt(34), JInt(23), JIn
                                                  //| t(7), JInt(5), JInt(3)))), (winners,JArray(List(JObject(List((winner-id,JInt
                                                  //| (23)), (numbers,JArray(List(JInt(2), JInt(45), JInt(34), JInt(23), JInt(3), 
                                                  //| JInt(5)))))), JObject(List((winner-id,JInt(54)), (numbers,JArray(List(JInt(5
                                                  //| 2), JInt(3), JInt(12), JInt(11), JInt(18), JInt(22))))))))))))))

  val jValue = json \\ "lotto-id"                 //> jValue  : org.json4s.JValue = JInt(5)
  compact(render(jValue))                         //> res0: String = 5

  //  val token = WechatUtils.getAccess_token

  // println(" token -> " + token)

  val j = """{"access_token":"XmFuAHgmpy5jPiIF8a_zoEJvj5kkNXkDvqKxkq53F1rxvUAKAl6r5R-p3KSZAS5q5Ev3cL1a2y-YWALb18JRZRSbGj898M97zKkUVaDSINE-QOPTxiaPyryw1xRJf3PVNkbOg4scayzd30iMPdMnLQ","expires_in":7200}"""
                                                  //> j  : String = {"access_token":"XmFuAHgmpy5jPiIF8a_zoEJvj5kkNXkDvqKxkq53F1rx
                                                  //| vUAKAl6r5R-p3KSZAS5q5Ev3cL1a2y-YWALb18JRZRSbGj898M97zKkUVaDSINE-QOPTxiaPyry
                                                  //| w1xRJf3PVNkbOg4scayzd30iMPdMnLQ","expires_in":7200}
      

                                           
                                                  val j2 = parse(j)
                                                  //> j2  : org.json4s.JValue = JObject(List((access_token,JString(XmFuAHgmpy5jPi
                                                  //| IF8a_zoEJvj5kkNXkDvqKxkq53F1rxvUAKAl6r5R-p3KSZAS5q5Ev3cL1a2y-YWALb18JRZRSbG
                                                  //| j898M97zKkUVaDSINE-QOPTxiaPyryw1xRJf3PVNkbOg4scayzd30iMPdMnLQ)), (expires_i
                                                  //| n,JInt(7200))))
                                                  
                                                 val j3 =j2 \\"access_token"
                                                  //> j3  : org.json4s.JValue = JString(XmFuAHgmpy5jPiIF8a_zoEJvj5kkNXkDvqKxkq53F
                                                  //| 1rxvUAKAl6r5R-p3KSZAS5q5Ev3cL1a2y-YWALb18JRZRSbGj898M97zKkUVaDSINE-QOPTxiaP
                                                  //| yryw1xRJf3PVNkbOg4scayzd30iMPdMnLQ)
                                                  
                                                  
                                                val t=  compact(render(j3))
                                                  //> t  : String = "XmFuAHgmpy5jPiIF8a_zoEJvj5kkNXkDvqKxkq53F1rxvUAKAl6r5R-p3KSZ
                                                  //| AS5q5Ev3cL1a2y-YWALb18JRZRSbGj898M97zKkUVaDSINE-QOPTxiaPyryw1xRJf3PVNkbOg4s
                                                  //| cayzd30iMPdMnLQ"
                                                  println(" token -> " + t)
                                                  //>  token -> "XmFuAHgmpy5jPiIF8a_zoEJvj5kkNXkDvqKxkq53F1rxvUAKAl6r5R-p3KSZAS5q
                                                  //| 5Ev3cL1a2y-YWALb18JRZRSbGj898M97zKkUVaDSINE-QOPTxiaPyryw1xRJf3PVNkbOg4scayz
                                                  //| d30iMPdMnLQ"
                                                  
                                                  val t2= j3.extract[String]
                                                  //> t2  : String = XmFuAHgmpy5jPiIF8a_zoEJvj5kkNXkDvqKxkq53F1rxvUAKAl6r5R-p3KSZ
                                                  //| AS5q5Ev3cL1a2y-YWALb18JRZRSbGj898M97zKkUVaDSINE-QOPTxiaPyryw1xRJf3PVNkbOg4s
                                                  //| cayzd30iMPdMnLQ
                                                  println(" token2 -> " + t2)
                                                  //>  token2 -> XmFuAHgmpy5jPiIF8a_zoEJvj5kkNXkDvqKxkq53F1rxvUAKAl6r5R-p3KSZAS5q
                                                  //| 5Ev3cL1a2y-YWALb18JRZRSbGj898M97zKkUVaDSINE-QOPTxiaPyryw1xRJf3PVNkbOg4scayz
                                                  //| d30iMPdMnLQ
                                                  
                                                 
}