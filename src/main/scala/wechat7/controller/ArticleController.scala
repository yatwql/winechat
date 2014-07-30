package wechat7.controller

import wechat7.WechatAppStack
import wechat7.repo._
import wechat7.util._
import javax.servlet.annotation.MultipartConfig
import org.scalatra.servlet.FileUploadSupport
import org.scalatra.ScalatraServlet
import org.scalatra.servlet.FileItem

trait ArticleController extends WechatAppStack {
  self: ScalatraServlet =>
  class ActionRepoImpl extends ActionRepo {

  }
  val articleRepo = new ActionRepoImpl

  get("/article/list") {
    val list = articleRepo.getArticleList(20)
    ssp("/pages/articles", "title" -> "List articles", "list" -> list)
  }

  get("/article/view/:slug") {
    val slug = params("slug")
    val article = articleRepo.getArticle(slug.toInt)
    ssp("/pages/article/view", "title" -> "Show article", "article" -> article)
  }

}