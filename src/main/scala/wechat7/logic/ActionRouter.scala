package wechat7.logic

import scala.xml.Node
import wechat7.repo.ActionRepo
import wechat7.util.CacheMgr



trait ActionRouter extends ActionRepo with Plugin with VotePlugin with ArticlePlugin {
  import system.dispatcher

  override def process(openId: String, nickname: String, appUserId: String, msgType: String, currentAction: Option[String], requestContent: String): Option[Node] = {
    val votePattern = "vote(\\d+)".r
    val votingPattern = "voting(\\d+)".r
    val articlePattern = "(\\d+)".r
    currentAction match {
      case Some(action) => {
        action match {
          case "ignore" => { None }
          case "" => { None }
          case votePattern(voteId) => {
            vote(openId, nickname, appUserId, voteId.toInt, "")
          }
          case votingPattern(voteId) => {
            voting(openId, nickname, appUserId, voteId.toInt, requestContent)
          }
          case "history" => {
            getArticleTopics(openId, nickname, appUserId)
          }
          
          case "vote" =>{
            getVoteTopics(openId, nickname, appUserId)
          }
            

          case _ => {
            getArticle(openId, nickname, appUserId, action, requestContent)
          }
        }
      }

      case _ => {
        println(" process from ActionRouter - no  action")
        getArticle(openId, nickname, appUserId, requestContent, requestContent)
      }
    }

  }

  def response(openId: String, nickname: String, appUserId: String, msgType: String, actionKey: String, requestContent: String): Option[Node] = {
    println(" process from ActionRouter actionkey " + actionKey)
    val userAction = getUserAction(openId)
    println(" The user action is " + userAction)

    val content = userAction match {
      case Some(action) => {
        println(" process from ActionRouter User action " + action)
        val s = process(openId, nickname, appUserId, msgType,userAction, requestContent)
        CacheMgr.userActionCache.remove(openId)
        s
      }
      case _ => {
        val current = getCurrentAction(actionKey)
        println(" process from ActionRouter currrent action " + current + " of action key " + actionKey)
        val t = process(openId, nickname, appUserId, msgType,current, requestContent)
        updateUserAction(openId, actionKey)
        t
      }
    }

    content

  }

}
