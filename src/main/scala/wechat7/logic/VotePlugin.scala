package wechat7.logic

import scala.concurrent.ExecutionContext.Implicits.global
import scala.xml.Node
import spray.caching.ValueMagnet.fromAny
import spray.util.pimpFuture
import wechat7.repo.VoteRepo
import wechat7.util.Constants
import wechat7.util.WechatUtils
import wechat7.util.CacheMgr

trait VotePlugin extends VoteRepo with Plugin {

  def splitVoteOptions(list: List[VoteOptions#TableElementType]): Option[(String, Seq[String])] = {
    list match {
      case a :: rest => {
        val option = a.option
        val optionDesc = a.optionDesc
        splitVoteOptions(rest) match {
          case Some((restDesc, restSeq)) => Some((option + ":" + optionDesc + "; " + restDesc, restSeq :+ option))
          case _ => Some((option + ":" + optionDesc, Seq(option)))
        }
      }
      case _ => None
    }
  }

  def getVoteTopics(openId: String, nickname: String, appUserId: String): Option[Node] = {
    val list = getVoteTopics(20)
    val desc = splitListIntoDesc(list)

    val responseContent = splitListIntoDesc(list) match {
      case Some(desc) =>  nickname + ". 您可参加以下 " + list.size + " 类调查 -> " + desc
      case _ => nickname + ". 最近的没有调查呀 " 
    }
    Some(WechatUtils.getTextMsg(appUserId, openId, responseContent))
  }

  def getVoteThreadFromCache(voteId: Int): Option[(String, String, Int, Seq[String])] = {
    CacheMgr.voteThreadCache(voteId) {
      val responseContent = getVoteThread(voteId)
      getVoteThread(voteId) match {
        case Some(voteThread) => {
       
          val (voteId, name, threadDesc, voteMethod) = (voteThread.voteId, voteThread.name, voteThread.description, voteThread.voteMethod)
          val list: List[VoteOptions#TableElementType] = this.getVoteOptions(voteId)
          val (description, voteOptions) = splitVoteOptions(list) match {
            case Some((optionDesc, t)) => { 
              (threadDesc + " -> " + optionDesc, t) 
              }
            case _ => {
              (threadDesc, Seq()) }
          }
           
          Some((name, description, voteMethod, voteOptions))
        }
        case _ => {
          None}
      }
    }.await
  }
  def vote(openId: String, nickname: String, appUserId: String, voteId: Int, requestContent: String): Option[Node] = {

    val voteResultOption = getVoteResult(openId, voteId) match {
      case Some(voteResult) => Some(voteResult.option)
      case _ => {
        addVoteResult(openId, voteId, requestContent)
        None
      }
    }
    val responseContent = getVoteThreadFromCache(voteId) match {
      case Some((voteName, description, voteMethod, voteOptions)) => {
        val desc1 = nickname + ", 欢迎参加  '" + voteName + "'. "
        val desc2 = voteResultOption match {
          case Some(s) => " 您已经投票 (" + s + "). "
          case _ => ""
        }
        desc1 + desc2 + description
      }
      case _ => nickname + " ,您投了 '" + requestContent + "' 给了一个无效的调查  " + voteId
    }
    Some(WechatUtils.getTextMsg(appUserId, openId, responseContent))
  }

  def voting(openId: String, nickname: String, appUserId: String, voteId: Int, requestContent: String): Option[Node] = {

    val responseContent = getVoteThreadFromCache(voteId) match {
      case Some((voteName, description, voteMethod, voteOptions)) =>
        {
          if (voteMethod == Constants.VOTE_METHOD_ALL || voteOptions.contains(requestContent.trim())) {
            updateVoteResult(openId, voteId, requestContent)
            nickname + ", 您投了  '" + requestContent + "' 给 '" + voteName + "'"
          } else {
            nickname + ", 您投了无效选项 '" + requestContent + "' 给 '" + voteName + "'"
          }
        }

      case _ => nickname + " ,您投了 '" + requestContent + "' 给了一个无效的调查 " + voteId
    }
    Some(WechatUtils.getTextMsg(appUserId, openId, responseContent))
  }
}