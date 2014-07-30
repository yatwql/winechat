package wechat7.repo

import scala.slick.driver.JdbcProfile
import scala.slick.driver.H2Driver
import scala.slick.driver.MySQLDriver
import scala.slick.driver.PostgresDriver
 
object SlickDBDriver {
val TEST = "test"
val DEV = "dev"
val PROD = "prod"
  
  
  
def getDriver: JdbcProfile = {
scala.util.Properties.envOrElse("runMode", PROD) match {
case TEST => H2Driver
case DEV => PostgresDriver
case PROD => PostgresDriver
case _ => PostgresDriver
}
}
}