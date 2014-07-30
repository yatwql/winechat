package wechat7.repo
import wechat7.util._

trait AdminRepo extends SlickRepo {
  import profile.simple._

  override def populateTable(tableName: String = "all"): String = {
     println("execute AdminRepo")
    conn.dbObject withSession { implicit session: Session =>
      tableName match {
        case "all" => {
          populateTable("auditLogs") + " , " + populateTable("settings") + " , " + super.populateTable(tableName)
        }

        case "settings" => {
          try {
            println("======================Insert settings into database ====================")
            settings.insert(Setting(Constants.MENU, WechatUtils.loadMenuFromFile))
            println("======================retrieve settings from database ====================")
            settings.list foreach println
            "<AdminRepo>Populate settings; "
          } catch {
            case ex: Exception => println(ex.getMessage); ""
          }
        }

        case _ => super.populateTable(tableName)
      }
    }
  }

  def audit(fromUser: String, toUser: String, msgType: String, requestXmlContent: String): Unit = {
    conn.dbObject withSession { implicit session: Session =>
      auditLogs.insert(AuditLog(fromUser, toUser, msgType, requestXmlContent))
    }
  }

  def loadLatestMenu(): Option[String] = {
    conn.dbObject withSession { implicit session: Session =>
      val query = settings.filter(_.name.===(Constants.MENU)).map(_.content)
      val result = query.list()
      Some(result.last)
    }
  }

  def saveMenu(menu: String) {
    conn.dbObject withSession { implicit session: Session =>
      settings.insert(Setting(Constants.MENU, menu))
    }
  }
}