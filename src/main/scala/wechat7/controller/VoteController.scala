package wechat7.controller

import wechat7.WechatAppStack
import wechat7.repo._
import wechat7.util._
import javax.servlet.annotation.MultipartConfig
import org.scalatra.servlet.FileUploadSupport
import org.scalatra.ScalatraServlet
import org.scalatra.servlet.FileItem

trait VoteController extends WechatAppStack {
  self: ScalatraServlet =>
  class VoteRepoImpl extends VoteRepo {

  }
  val voteRepo = new VoteRepoImpl

  get("/vote/view/:slug") {
    val slug = params("slug")
    val vote = voteRepo.getVoteThread(slug.toInt)
    val voteOptions = voteRepo.getVoteOptionsToTuples(slug.toInt)
    vote match {
      case Some(v) => {
        ssp("/pages/vote/view", "title" -> "Show Vote detail ", "voteName" -> v.name, "description" -> v.description, "voteId" -> v.voteId.toString(), "voteMethod" -> v.voteMethod, "voteOptions" -> voteOptions)
      }
      case _ => { "file not found" }
    }

  }

  get("/vote/edit/:slug") {
    val slug = params("slug")
    val vote = voteRepo.getVoteThread(slug.toInt)
    val voteOptions = voteRepo.getVoteOptionsToTuples(slug.toInt)
    vote match {
      case Some(v) => {
        ssp("/pages/vote/edit", "title" -> "Edit Vote detail ", "voteName" -> v.name, "description" -> v.description, "voteId" -> v.voteId.toString(), "voteMethod" -> v.voteMethod, "voteOptions" -> voteOptions, "action" -> "update")
      }
      case _ => { "file not found" }
    }

  }

  get("/vote/delete/:slug") {
    val voteId = params("slug").toInt
    voteRepo.deleteVoteThread(voteId)
    CacheMgr.voteThreadCache.remove(voteId)
    redirect("/vote/list")
  }

  get("/vote/result/:slug") {
    val voteId = params.getAs[Int]("slug").get
    val vote = voteRepo.getVoteThread(voteId)
    val voteOptions = voteRepo.getVoteOptionsToTuples(voteId)
    val voteResult = voteRepo.getVoteGrpResult(voteId)
    vote match {
      case Some(v) => {
        ssp("/pages/vote/viewresult", "title" -> "Show Vote Result ", "voteName" -> v.name, "description" -> v.description, "voteId" -> v.voteId.toString(), "voteMethod" -> v.voteMethod, "voteOptions" -> voteOptions, "voteResult" -> voteResult)
      }
      case _ => { "file not found" }
    }

  }

  post("/vote/save") {

    val voteName = params("voteName")
    val description = params("description")
    val voteMethod = params("voteMethod").toInt
    val (saveNew, qVoteId) = params("action") match {
      case "new" => (true, 0)
      case _ => (false, params("qVoteId").toInt)
    }

    val sizeOfVoteOptions = params("sizeOfVoteOptions").toInt

    params.getAs[Int]("voteId") match {
      case Some(voteId) => {
        try {
          voteRepo.updateVoteThread(voteName, description, voteMethod, voteId, qVoteId, saveNew)
          for (seq <- (1 to sizeOfVoteOptions)) {
            params.getAs[Int]("optionId-" + seq) match {
              case Some(optionId) => {

                val option = params("option-" + seq)
                val optionDesc = params("optionDesc-" + seq)
                val id = optionId.toInt

                voteRepo.updateVoteOpton(voteId, option, optionDesc, id, qVoteId, saveNew)
              }

              case _ => {

                " Can not get a valid option id"
              }
            }
          }
          CacheMgr.voteThreadCache.remove(qVoteId)
          val message = "Successful Update " + voteId + " !"
          val voteOptions = voteRepo.getVoteOptionsToTuples(voteId)
          ssp("/pages/vote/view", "title" -> "Show Vote detail ", "voteName" -> voteName, "description" -> description, "voteId" -> voteId.toString(), "voteMethod" -> voteMethod, "voteOptions" -> voteOptions, "message" -> message)
        } catch {
          case e: Throwable =>
            "Have exception to update -> " + e.getMessage()
        }
      }
      case _ => {
        " Can not get a valid vote Id"
      }
    }

  }

  get("/vote/list") {
    val list = voteRepo.getVoteTopics(20)
    ssp("/pages/vote/votes", "title" -> "List Votes", "list" -> list)
  }
  get("/vote/new") {
    var voteOptions: List[(Int, String, String)] = List[(Int, String, String)]()
    for (i <- 1 to 5) {
      //voteOptions += (i,"","")
      voteOptions = voteOptions.:+((i, i.toString(), ""))
    }

    ssp("/pages/vote/edit", "title" -> "Create Vote detail ", "voteName" -> "", "description" -> "", "voteId" -> "", "voteMethod" -> 1, "voteOptions" -> voteOptions, "action" -> "new")

  }

}