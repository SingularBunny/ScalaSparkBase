import sbt.ExclusionRule

val HDP_MINOR_VERSION = "2.6.4.9-3"

val HBASE_VERSION = "1.1.2" + "." + HDP_MINOR_VERSION
val HADOOP_VERSION = "2.7.3" + "." + HDP_MINOR_VERSION

val SPARK_VERSION = "2.2.0" + "." + HDP_MINOR_VERSION
val JACKSON_VERSION = "2.6.5"
lazy val base = (project in file("base"))
  .settings(Common.commonSettings)
  .settings(libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % SPARK_VERSION % Provided,
    "org.apache.spark" %% "spark-sql" % SPARK_VERSION % Provided,
    "commons-cli" % "commons-cli" % "1.4" % Provided,
    "commons-codec" % "commons-codec" % "1.11" % Provided,
    "com.fasterxml.jackson.core" % "jackson-databind" % JACKSON_VERSION % Provided,
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % JACKSON_VERSION % Provided,
    "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % JACKSON_VERSION % Provided,
    "org.scalactic" %% "scalactic" % "3.0.3",
    "org.scalatest" %% "scalatest" % "3.0.3",
    "org.mockito" % "mockito-core" % "2.13.0",
    "com.beust" % "jcommander" % "1.72"))

lazy val root = (project in file("."))
  .settings(Common.commonSettings)
  .aggregate(base)
