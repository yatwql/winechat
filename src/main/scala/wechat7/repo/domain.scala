package wechat7.repo

import java.sql.Timestamp
import wechat7.util._
import java.util.Date

import scala.slick.driver.JdbcProfile
import scala.slick.lifted.ProvenShape.proveShapeOf
//define driver
trait Profile {
  val profile: JdbcProfile
}

trait DomainComponent { this: Profile =>
  import profile.simple._

  case class Account(name: String, email: String, fullName: String, password: String, role: String, id: Int = 0)
  class Accounts(tag: Tag) extends Table[Account](tag, "accounts") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name", O.NotNull, O.DBType("VARCHAR(100)"))
    def fullName = column[String]("fullname", O.NotNull, O.DBType("VARCHAR(100)"))
    def email = column[String]("email", O.NotNull, O.DBType("VARCHAR(100)"))
    def password = column[String]("password", O.NotNull, O DBType ("VARCHAR(100)"))
    def role = column[String]("role", O.NotNull, O.DBType("VARCHAR(100)"))
    def * = (name, email, fullName, password, role, id) <> (Account.tupled, Account.unapply)
  }
  val accounts = TableQuery[Accounts]

  case class Article(title: String, description: String, actionKey: String, msgTyp: String = "text", picUrl: String = "", url: String = "", id: Int = 0)
  class Articles(tag: Tag) extends Table[Article](tag, "articles") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def actionKey = column[String]("action_key", O.NotNull, O.DBType("VARCHAR(30)"))
    def msgTyp = column[String]("msg_type", O.NotNull, O.DBType("VARCHAR(10)"))
    def title = column[String]("title", O.NotNull, O.DBType("VARCHAR(100)"))
    def description = column[String]("description", O.NotNull, O.DBType("VARCHAR(5000)"))
    def picUrl = column[String]("picUrl", O.NotNull, O DBType ("VARCHAR(300)"))
    def url = column[String]("url", O.NotNull, O DBType ("VARCHAR(300)"))
    def * = (title, description, actionKey, msgTyp, picUrl, url, id) <> (Article.tupled, Article.unapply)
  }
  val articles = TableQuery[Articles]

  case class Action(actionKey: String, currentAction: String, nextAction: String, id: Int = 0)
  class Actions(tag: Tag) extends Table[Action](tag, "actions") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def actionKey = column[String]("action_key", O.NotNull, O.DBType("VARCHAR(30)"))
    def currentAction = column[String]("currentAction", O.NotNull, O.DBType("VARCHAR(200)"))
    def nextAction = column[String]("nextAction", O.NotNull, O.DBType("VARCHAR(200)"))
   
    
    def * = (actionKey, currentAction, nextAction, id) <> (Action.tupled, Action.unapply)
  }
  val actions = TableQuery[Actions]

  case class VoteThread(voteId:Int,name: String, description: String, voteMethod:Int=1)
  class VoteThreads(tag: Tag) extends Table[VoteThread](tag, "vote_threads") {
    def voteId = column[Int]("vote_id", O.PrimaryKey)
    def name = column[String]("name", O.NotNull, O.DBType("VARCHAR(100)"))
    def description = column[String]("description", O.NotNull, O.DBType("VARCHAR(2000)"))
    def voteMethod = column[Int]("voteMethod", O.NotNull,O.Default(Constants.VOTE_METHOD_LIMIT))
    def * = (voteId,name,description,voteMethod) <> (VoteThread.tupled, VoteThread.unapply)
  }
  val voteThreads = TableQuery[VoteThreads]
  
  case class VoteOption(voteId:Int,option:String,optionDesc:String,id:Int=0)
  class VoteOptions(tag: Tag) extends Table[VoteOption](tag, "vote_options") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def option = column[String]("option", O.NotNull, O.DBType("VARCHAR(100)"))
    def optionDesc = column[String]("optionDesc", O.NotNull, O.DBType("VARCHAR(1000)"))
    def voteId = column[Int]("voteId", O.NotNull)
    def * = (voteId, option, optionDesc,id) <> (VoteOption.tupled, VoteOption.unapply)
  }
  val voteOptions = TableQuery[VoteOptions]

  case class VoteResult(openId: String, voteId: Int, option: String, fromLocationX: String = "", fromLocationY: String = "", id: Int = 0)
  class VoteResults(tag: Tag) extends Table[VoteResult](tag, "vote_results") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def openId = column[String]("openId", O.NotNull, O.DBType("VARCHAR(100)"))
    def voteId = column[Int]("voteId", O.NotNull)
    def option = column[String]("option", O.NotNull)
    def fromLocationX = column[String]("fromLocationX", O.Default(""), O.DBType("VARCHAR(100)"))
    def fromLocationY = column[String]("fromLocationY", O.Default(""), O.DBType("VARCHAR(100)"))
    def * = (openId, voteId, option, fromLocationX, fromLocationY, id) <> (VoteResult.tupled, VoteResult.unapply)
  }
  val voteResults = TableQuery[VoteResults]

  case class AuditLog(fromUser: String, toUser: String, msgType: String, content: String, creationTime: Timestamp = new Timestamp(new Date().getTime()), id: Int = 0)
  class AuditLogs(tag: Tag) extends Table[AuditLog](tag, "audit_logs") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def fromUser = column[String]("fromUser", O.NotNull, O.DBType("VARCHAR(100)"))
    def toUser = column[String]("toUser", O.NotNull, O.DBType("VARCHAR(100)"))
    def msgType = column[String]("msgType", O.NotNull, O.DBType("VARCHAR(20)"))
    def content = column[String]("content", O.Default(""), O.DBType("VARCHAR(10000)"))
    def creationTime = column[Timestamp]("creationTime", O.Default(now()), O.DBType("Timestamp"))
    def * = (fromUser, toUser, msgType, content, creationTime, id) <> (AuditLog.tupled, AuditLog.unapply)
  }

  val auditLogs = TableQuery[AuditLogs]

  case class User(openId: String, nickname: String, sex: String = "", city: String = "", country: String = "", province: String = "", language: String = "", headimgurl: String = "", subscripted: Int = 1, lastUpdateTime: Timestamp = new Timestamp(new Date().getTime()), subscriptTime: Timestamp = new Timestamp(new Date().getTime()), locationX: String = "", locationY: String = "")
  class Users(tag: Tag) extends Table[User](tag, "users") {

    def openId = column[String]("openId", O.PrimaryKey, O.NotNull)
    def nickname = column[String]("nickname", O.NotNull)
    def sex = column[String]("sex", O.Nullable)
    def city = column[String]("city", O.Nullable)
    def country = column[String]("country", O.Nullable)
    def province = column[String]("province", O.Nullable)
    def language = column[String]("language", O.Nullable)
    def headimgurl = column[String]("headimgurl", O.Nullable)
    def subscriptTime = column[Timestamp]("subscript_time", O.Default(now()), O.DBType("Timestamp"))
    def lastUpdateTime = column[Timestamp]("last_update_time", O.Default(now()), O.DBType("Timestamp"))
    def locationX = column[String]("locationX", O.Default(""), O.DBType("VARCHAR(100)"))
    def locationY = column[String]("locationY", O.Default(""), O.DBType("VARCHAR(100)"))
    def subscripted = column[Int]("subscripted", O.Default(1), O.DBType("Int"))
    def * = (openId, nickname, sex, city, country, province, language, headimgurl, subscripted, lastUpdateTime, subscriptTime, locationX, locationY) <> (User.tupled, User.unapply)
  }

  val users = TableQuery[Users]

  case class Setting(name: String, content: String, updateTime: Timestamp = new Timestamp(new Date().getTime()), id: Int = 0)
  class Settings(tag: Tag) extends Table[Setting](tag, "settings") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name", O.NotNull, O.DBType("VARCHAR(100)"))
    def content = column[String]("content", O.NotNull, O.DBType("VARCHAR(10000)"))

    def updateTime = column[Timestamp]("updateTime", O.Default(now()), O.DBType("Timestamp"))
    def * = (name, content, updateTime, id) <> (Setting.tupled, Setting.unapply)
  }

  val settings = TableQuery[Settings]

  val tables = Map[String, TableQuery[_ <: Table[_]]]("accounts" -> accounts,
    "articles" -> articles, "actions" -> actions, "voteThreads" -> voteThreads, "voteOptions" -> voteOptions,"voteResults" -> voteResults,
    "auditLogs" -> auditLogs, "users" -> users,  "settings" -> settings)

  def now(): Timestamp = {
    new Timestamp(new Date().getTime())
  }

}

