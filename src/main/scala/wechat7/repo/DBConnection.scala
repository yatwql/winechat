package wechat7.repo

import scala.slick.driver.JdbcProfile
import wechat7.util._

class DBConnection(override val profile: JdbcProfile) extends Profile {
import profile.simple._
def dbObject(): Database = {

val url = Constants.config.getString("db.url")
val username = Constants.config.getString("db.username")
val password = Constants.config.getString("db.password")
val driver = Constants.config.getString("db.driver")
println("Connection info =>" + "Run mode: " + Constants.env + ", db url: " + url + ", driver: " + driver)
Database.forURL(url, username, password, null, driver)
}
}