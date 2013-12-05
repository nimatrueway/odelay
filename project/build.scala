
object Build extends sbt.Build {
  import sbt._, sbt.Keys._
  object Common {
    val organization = "me.lessis"
    val version = "0.1.0-SNAPSHOT"
  }
  def module(mod: String) =
    Project(mod, file(mod), 
            settings = Defaults.defaultSettings ++ Seq(
              organization := Common.organization,
              name := s"odelay-$mod",
              version := Common.version,
              crossScalaVersions := Seq("2.9.3", "2.10.3"),
              scalacOptions ++= Seq(Opts.compile.deprecation)))
  lazy val root =
    Project("root", file("."))
      .settings(publish := {}, test := {})
      .aggregate(core, coreTests, netty3, netty, twttr, testing)
  lazy val core: Project = module("core")
    .settings(test := {}) // see coreTests module
  lazy val testing = module("testing")
    .settings(publish := {}, test := {})
    .dependsOn(core)
  lazy val coreTests = module("core-tests")
    .settings(publish := {})
    .dependsOn(testing % "test->test;compile->compile")
  lazy val netty3 = module("netty3")
    .dependsOn(core, testing % "test->test")
  lazy val netty = module("netty")
    .dependsOn(core, testing % "test->test")
  lazy val twttr = module("twitter")
    .dependsOn(core, testing % "test->test")
}
