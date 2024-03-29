package wechat7.logic

import scala.xml.Node
import wechat7.util._
import akka.actor.ActorSystem
import spray.caching.ValueMagnet.fromAny
import spray.util.pimpFuture
import wechat7.repo.ActionRepo
import wechat7.repo.UserRepo
import wechat7.util.WechatUtils
import wechat7.util.CacheMgr

trait Plugin extends ActionRepo with UserRepo {
  import profile.simple._
  val system = ActorSystem()
  import system.dispatcher
  def getNextAction(actionKey: String): Option[String] = {
    CacheMgr.nextActionCache(actionKey) {
      val action = getAction(actionKey)
      action match {
        case Some(action1) => {
          action1.currentAction match {
            case "" => None
            case _ => Some(action1.nextAction)
          }
        }
        case _ => None
      }

    }.await
  }

  def getCurrentAction(actionKey: String): Option[String] = {
    CacheMgr.currentActionCache(actionKey) {
      val action = getAction(actionKey)
      action match {
        case Some(action1) => {
          action1.currentAction match {
            case "" => None
            case _ => Some(action1.currentAction)
          }
        }
        case _ => None
      }

    }.await
  }

  def getUserAction(openId: String): Option[String] = {
    val action = CacheMgr.userActionCache.get(openId)

    action match {
      case None => None
      case _ => action.get.await
    }
  }

  def updateUserAction(openId: String, actionKey: String) = {
    println(" prepare to update the next action of " + openId)
    CacheMgr.userActionCache(openId) {
      val action = getNextAction(actionKey)
      action match {
        case Some("ignore") => {
          println(" Ignore the save action of " + openId)
          None
        }
        case Some("") => {
          println(" Ignore the save action of " + openId)
          None
        }
        case Some(action1) => {
          println(" Update the next action of " + openId + " to " + action)
          action
        }
        case _ => {
          println(" Ignore the save action of " + openId)
          None
        }
      }

    }
  }

  def getNicknameFromDB(openId: String): Option[String] = {
    println(" Visit DB to get nickname for openid " + openId)
    val s = super.getNickname(openId)
    val nickname = s match {
      case Some(t) => t
      case None => {
        println(" Get user info from wechat site")
        WechatUtils.getUserInfo(openId) match {
          case Some(json) => addUser(json)
          case _ => {
            println("Not able to load user information from wechat site")
            ""
          }
        }

      }
      case _ => {
        println("Not able to load user information")
        ""
      }
    }
    Some(nickname)
  }

  override def getNickname(openId: String): Option[String] = {
    Constants.USE_ADVANCED_VERSION match {
      case true => {
        println(" Get nickname for openid " + openId)
        CacheMgr.nicknameCache(openId) {
          getNicknameFromDB(openId)
        }.await()
      }
      case _ => None
    }

  }
  def process(openId: String, nickname: String, appUserId: String, msgType: String, currentAction: Option[String], requestContent: String): Option[Node] = {
    None
  }

  def dontknow(openId: String, appUserId: String, nickname: String, requestContent: String): Option[Node] = {
    val responseContent = nickname + " 没能理解您的意思 '" + requestContent + "', 输入 help 可获取帮助  "
    Some(WechatUtils.getTextMsg(appUserId, openId, responseContent))
  }

  def splitListIntoDesc(list: List[(String, Int, String)]): Option[String] = {
    list match {
      case data :: rest => {
        val (name, id, remark) = data
        splitListIntoDesc(rest) match {
          case Some((restDesc)) => Some((name + "," + remark + "; " + restDesc))
          case _ => Some((name + " , " + remark))
        }
      }
      case _ => None
    }
  }
}