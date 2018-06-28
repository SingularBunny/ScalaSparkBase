import java.net.{InetAddress, InetSocketAddress, Socket}

import sbt.{Resolver, _}


object ResolverUtils {
  private val customArtifactory = "https://artifactory.custom.ru:443/"

  // Check for a network environment of current build.
  // For HCFB network (office or vpn) use different resolvers than for local usage
  val projectResolvers: Seq[Resolver] = Seq(Resolver.defaultLocal, Resolver.mavenLocal) ++
    (if (isCustomNetwork) {
      customResolvers
    } else {
      localResolvers
    })

  private lazy val customResolvers = Seq(
    "custom-cloudera-repo" at customArtifactory + "cloudera-remote-cache",
    "custom-maven-repo" at customArtifactory + "remote-repos",
    "custom-jcenter-repo" at customArtifactory + "jcenter-cache",
    "custom-spring-plugins" at customArtifactory + "spring-plugins-release-remote")

  private lazy val localResolvers = Seq(
    "mvnrepository-repository" at "https://mvnrepository.com/artifact/",
    "Hortonworks Repository" at "http://repo.hortonworks.com/content/repositories/releases/",
    "Hortonworks Jetty Repository" at "http://repo.hortonworks.com/content/repositories/jetty-hadoop/",
    Resolver.sonatypeRepo("releases"))

  private def isCustomNetwork : Boolean = {
    try {
      val addr = InetAddress.getByName("artifactory.custom.ru")

      val isReachable = Option(addr.getAddress).fold(false)(_ => {
        val s = new Socket()
        try {
          s.connect(new InetSocketAddress(addr, 443), 1000)
          s.isConnected
        } finally {
          s.close()
        }
      })

      isReachable

    } catch {
      case _: Exception => false
    }
  }
}
