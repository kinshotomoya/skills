scalaVersion := "2.13.1"
name := "valueclass"

val circeVersion = "0.12.3"
val doobieVersion = "0.9.0"

lazy val root = (project in file("."))
    .settings(
      name:= "scala study",
      libraryDependencies ++= Seq(
        "eu.timepit" %% "refined"                 % "0.9.17",
        "io.estatico" %% "newtype" % "0.4.4",
        "io.circe" %% "circe-core" % circeVersion,
        "io.circe" %% "circe-generic" % circeVersion,
        "io.circe" %% "circe-parser" % circeVersion,
        "io.circe" %% "circe-refined" % circeVersion,
        "org.postgresql" % "postgresql" % "42.2.16",
        "org.tpolecat"  %% "doobie-core"      % doobieVersion,
        "org.tpolecat"  %% "doobie-postgres"  % doobieVersion,
        "org.tpolecat"  %% "doobie-refined"   % doobieVersion,
        compilerPlugin(
          "org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full
        ),
      ),
      scalacOptions ++= Seq(
        "-Xfatal-warnings",
        "-deprecation",
        "-feature",
        "-unchecked",
        "-language:existentials",
        "-language:higherKinds",
        "-language:implicitConversions",
        "-Ywarn-dead-code",
        "-Ymacro-annotations",
      )
    )


