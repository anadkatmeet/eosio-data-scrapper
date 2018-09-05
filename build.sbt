name := "data-scrapper"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "org.mongodb" % "mongo-java-driver" % "3.8.1",
  "com.typesafe.play" %% "play-json" % "2.6.10",
  "commons-io" % "commons-io" % "2.6",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)

// Assembly
test in assembly := {}

assemblyMergeStrategy in assembly := {
  //  case "META-INF/services/org.apache.spark.sql.sources.DataSourceRegister" => MergeStrategy.concat
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

assemblyJarName in assembly := s"${name.value}-${version.value}.jar"
lazy val dataScrapper = (project in file(".")).settings(addArtifact(artifact in (Compile, assembly), assembly))