package wechat7.repo
import java.util.Date
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse
import org.json4s.jvalue2extractable
import org.json4s._
import org.json4s.string2JsonInput
import java.sql._

trait UserRepo extends SlickRepo {

  import profile.simple._

  override def populateTable(tableName: String = "all"): String = {
    println("execute UserRepo")
    conn.dbObject withSession { implicit session: Session =>
      tableName match {
        case "all" => {
          populateTable("accounts") + " , " + populateTable("users") + " , " + super.populateTable(tableName)
        }

        case "accounts" => {
          try {
            println("======================Will insert data for accounts ====================")
            accounts.insert(Account("stallman", "Stallman", "stallman.wang@foxmail.com", "stallman", "admin"))
            accounts.insert(Account("yatwql", "joe", "yatwql@qq.com", "yatwql", "admin"))
            println("======================retrieve accounts from database ====================")
            accounts.list foreach println
            "<UserRepo>Populate accounts; "
          } catch {
            case ex: Exception => println(ex.getMessage); ""
          }
        }
        
        case "users" => {
          ""
        }

        case _ => super.populateTable(tableName)
      }
    }
  }

  def addUser(openId: String, nickname: String, sex: String = "", city: String = "", country: String = "", province: String = "", language: String = "", headimgurl: String = "", subscripted: Int = 1, lastUpdateTime: Timestamp = new Timestamp(new Date().getTime()), subscribeTime: Timestamp = new Timestamp(new java.util.Date().getTime()), locationX: String = "", locationY: String = "") {
    conn.dbObject withSession { implicit session: Session =>
      users.insert(User(openId, nickname, sex, city, country, province, language, headimgurl, subscripted, lastUpdateTime, subscribeTime, locationX, locationY))
    }
  }

  def addUser(json: JValue): String = {
    val openid = (json \ "openid").extract[String]
    val nickname = (json \ "nickname").extract[String]
    val sex = (json \ "sex").extract[String]
    val language = (json \ "language").extract[String]
    val city = (json \ "city").extract[String]
    val province = (json \ "province").extract[String]
    val country = (json \ "country").extract[String]
    val headimgurl = (json \ "headimgurl").extract[String]
    val subscribeTime = (json \ "subscribe_time").extract[String]
    addUser(openid, nickname, sex, city, country, province, language, headimgurl)
    nickname
  }

  def removeUser(openId: String): Unit = {
    conn.dbObject withSession { implicit session: Session =>
      val l = users.filter(_.openId.===(openId))
      l.delete
    }
  }

  def updateSubscriptStatus(openId: String, subscripted: Int): Unit = {
    conn.dbObject withSession { implicit session: Session =>
      val l = users.filter(_.openId.===(openId)).map(_.subscripted)
      l.update(subscripted)
    }
  }

  def getNickname(openId: String): Option[String] = {
    val query = for (u <- users if u.openId === (openId)) yield u.nickname
    conn.dbObject withSession { implicit session: Session =>
      query.firstOption
    }
  }
}