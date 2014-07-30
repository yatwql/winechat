package wechat7.repo

import scala.slick.driver.JdbcProfile

import org.json4s.DefaultFormats

class SlickRepo(override val profile: JdbcProfile = SlickDBDriver.getDriver) extends DomainComponent with Profile {
  implicit val formats = DefaultFormats
  import profile.simple._
  val conn = new DBConnection(profile)

  def populateTable(tableName: String = "all") = {
    ""
  }

  def test: String = {
    doAction("all", "flush")
  }

  def doAction(tableName: String, action: String): String = {
    action match {
      case "create" => doActionImpl(tableName, action)
      case "drop" => doActionImpl(tableName, action)
      case "populate" => doActionImpl(tableName, action)
      case "flush" => {
        doActionImpl(tableName, "drop") + doActionImpl(tableName, "create") + doActionImpl(tableName, "populate")
      }
      case _ => " Unknown action " + action + " for table " + tableName
    }

  }
  def doActionImpl(tableName: String, action: String): String = {
    tableName match {
      case "all" => {
        var message = ""
        tables.keys.foreach((t) => { message = message + doAction(t, action); })
        message
      }

      case _ => {
        tables.get(tableName) match {
          case Some(table) => doActionImpl(tableName,table, action)
          case None => "Table " + tableName + " not found; "
        }
      }

    }
  }
  def doActionImpl(tableName: String,tableQuery: TableQuery[_ <: Table[_]], action: String): String = {
    conn.dbObject withSession { implicit session: Session =>
      try {
        action match {
          case "create" => {
            tableQuery.ddl.create
            "Create table " + tableQuery.baseTableRow.tableName + ";"
          }
          case "drop" => {
            tableQuery.ddl.drop
            "Drop table " + tableQuery.baseTableRow.tableName + ";"
          }

          case "populate" => {
            populateTable(tableName)
          }

 
          case _ => "Unknown action " + action + " for table " + tableQuery.baseTableRow.tableName + ";"
        }

      } catch {
        case ex: Exception => println(ex.getMessage); ""
      }
    }
  }

}
object SlickRepoApp extends App {
  (new SlickRepo with AdminRepo with ActionRepo with UserRepo with VoteRepo).test
}
