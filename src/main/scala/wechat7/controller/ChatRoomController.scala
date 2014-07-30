package wechat7.controller

import java.util.Date

import scala.concurrent.ExecutionContext.Implicits.global

import org.json4s.JValue
import org.json4s.JsonDSL.jobject2assoc
import org.json4s.JsonDSL.pair2Assoc
import org.json4s.JsonDSL.pair2jvalue
import org.json4s.JsonDSL.string2jvalue
import org.json4s.jvalue2extractable
import org.json4s.jvalue2monadic
import org.scalatra.atmosphere.AtmoReceive
import org.scalatra.atmosphere.AtmosphereClient
import org.scalatra.atmosphere.ClientDisconnected
import org.scalatra.atmosphere.Connected
import org.scalatra.atmosphere.Disconnected
import org.scalatra.atmosphere.JsonMessage
import org.scalatra.atmosphere.ServerDisconnected
import org.scalatra.atmosphere.TextMessage
import scala.concurrent._
import ExecutionContext.Implicits.global

import wechat7.AtmosphereStack
import wechat7.WechatAppStack
 
trait ChatRoomController extends WechatAppStack with AtmosphereStack {
  //implicit protected val jsonFormats: Formats = DefaultFormats
  get("/chat") {
    contentType = "text/html"
    ssp("/pages/chat")
  }
 

  atmosphere("/the-chat") {
    new AtmosphereClient {
      def receive: AtmoReceive = {
        case Connected =>
          println("Client %s is connected" format uuid)
          broadcast(("author" -> "Someone") ~ ("message" -> "joined the room") ~ ("time" -> (new Date().getTime.toString)), Everyone)

        case Disconnected(ClientDisconnected, _) =>
          broadcast(("author" -> "Someone") ~ ("message" -> "has left the room") ~ ("time" -> (new Date().getTime.toString)), Everyone)

        case Disconnected(ServerDisconnected, _) =>
          println("Server disconnected the client %s" format uuid)
        case _: TextMessage =>
          send(("author" -> "system") ~ ("message" -> "Only json is allowed") ~ ("time" -> (new Date().getTime.toString)))

        case JsonMessage(json) =>
          println("Got message %s from %s".format((json \ "message").extract[String], (json \ "author").extract[String]))
          val msg = json merge (("time" -> (new Date().getTime().toString)): JValue)
          broadcast(msg) // by default a broadcast is to everyone but self
        //  send(msg) // also send to the sender
      }
    }
  }
}