import sbt._
import Keys._
import org.scalatra.sbt._
import org.scalatra.sbt.PluginKeys._
import com.mojolly.scalate.ScalatePlugin._
import ScalateKeys._
import com.earldouglas.xsbtwebplugin.PluginKeys._
import com.earldouglas.xsbtwebplugin.WebPlugin._

object DragonStudioWebchatAppBuild extends Build {
  val Organization = "com.dragonstudio"
  val Name = "WineChat App"

  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.10.3"
  val ScalatraVersion = "2.2.2"

  lazy val project = Project (
    "dragon-studio-winechat-app",
    file("."),
    settings = seq(com.typesafe.sbt.SbtStartScript.startScriptForClassesSettings: _*) ++ Defaults.defaultSettings ++ ScalatraPlugin.scalatraWithJRebel ++ scalateSettings ++
    ScalatraPlugin.scalatraSettings ++
//     Seq(port in container.Configuration := 80) ++
    Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/",
      resolvers += "Sonatype Releases"  at "http://oss.sonatype.org/content/repositories/releases",
      resolvers += "spray repo" at "http://repo.spray.io",

      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
        "org.scalatra" %% "scalatra-auth" % ScalatraVersion,
        "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
        "ch.qos.logback" % "logback-classic" % "1.0.6" % "runtime",
        "org.scalatra" %% "scalatra-atmosphere" %  ScalatraVersion,
        "org.json4s"   %% "json4s-jackson" % "3.1.0",
        "org.eclipse.jetty" % "jetty-websocket" % "8.1.10.v20130312" % "container;provided",
        "org.eclipse.jetty" % "jetty-webapp" % "8.1.10.v20130312" % "compile;container",
        "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "compile;container;provided;test" artifacts (Artifact("javax.servlet", "jar", "jar")),
        "com.typesafe.slick" %% "slick" % "2.0.1",
         "c3p0" % "c3p0" % "0.9.1.2",
         "com.typesafe" % "config" % "1.2.0",
        "org.slf4j" % "slf4j-nop" % "1.6.4",
        "com.h2database" % "h2" % "1.3.166",
        "io.spray" % "spray-caching" % "1.3.1",
        "io.spray" % "spray-util" % "1.3.1",
        "com.googlecode.concurrentlinkedhashmap" % "concurrentlinkedhashmap-lru" % "1.4",
          "com.typesafe.akka" %% "akka-actor" % "2.2.3",
        "junit" % "junit" % "4.8.1" % "test"
      ),

      scalateTemplateConfig in Compile <<= (sourceDirectory in Compile){ base =>
        Seq(
          TemplateConfig(
            base / "webapp" / "WEB-INF" / "templates",
            Seq.empty,  /* default imports should be added here */
            Seq(
              Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
            ),  /* add extra bindings here */
            Some("templates")
          )
        )
      }
    )
  )
}
