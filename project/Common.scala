import sbt.Keys._
import sbt._


object Common {

  private val SCALA_VERSION = "2.11.8"

  // Use the prefix for artifactory jars in the project
  val prefix: String = "bdp-"

  val commonSettings: Seq[Def.Setting[_]] = Seq(
    resolvers := ResolverUtils.projectResolvers,
    parallelExecution := false,
    scalaVersion := SCALA_VERSION,
    updateOptions := updateOptions.value.withCachedResolution(true))
}
