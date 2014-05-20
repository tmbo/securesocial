import sbt._
import Keys._
import play.Project._

object Publish {
  object TargetRepository {
    def scmio: Def.Initialize[Option[sbt.Resolver]] = version { (version: String) =>
      val rootDir = "/srv/maven/"
      val path =
        if (version.trim.endsWith("SNAPSHOT"))
          rootDir + "snapshots/" 
        else 
          rootDir + "releases/" 
      Some(Resolver.sftp("scm.io intern repo", "maven.scm.io", 44144, path))
    }
  }
  lazy val settings = Seq(
    organization := "com.scalableminds",
    publishMavenStyle := true,
    publishTo <<= TargetRepository.scmio,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
    homepage := Some(url("http://scm.io")))
}

object ApplicationBuild extends Build {

    val appName         = "securesocial"
    val appVersion      = "2.2.0.12-SCM"

    val appDependencies = Seq(
      "com.typesafe" %% "play-plugins-util" % "2.2.0",
      "com.typesafe" %% "play-plugins-mailer" % "2.2.0",
      cache
    )

    val main = play.Project(appName, appVersion, appDependencies, settings = Publish.settings).settings(
    ).settings(
      resolvers ++= Seq(
        "jBCrypt Repository" at "http://repo1.maven.org/maven2/org/",
        "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
      )
    )

}
