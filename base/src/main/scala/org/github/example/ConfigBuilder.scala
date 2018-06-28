package org.github.example

import java.io.FileInputStream
import java.util

import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.commons.cli.{DefaultParser, OptionBuilder}
import org.apache.commons.io.IOUtils
import org.apache.log4j.{LogManager, Logger}

import scala.collection.JavaConversions._
import scala.reflect.ClassTag

object ConfigBuilder {
  @transient lazy val LOG: Logger = LogManager.getLogger(ConfigBuilder.getClass)

  val OPTION = "D"
  val CONFIG_PROPERTY = "config"
  val DEFAULT_CONFIG = "/aeroflot-config.yml"

  /**
    * Fill config from command line args.
    *
    * @param args command line args.
    */
  def build[CONFIG](args: Array[String])(implicit ct: ClassTag[CONFIG]): CONFIG = {

    val properties = parseCommandLineArgs(args)

    val reader = Option(properties
      .getProperty(CONFIG_PROPERTY))
      .map(path => new FileInputStream(path))
      .orElse({
        LOG.warn("Config property has not been set. Try to find in the resources.")
        Option(getClass.getResourceAsStream(DEFAULT_CONFIG))
      })
      .getOrElse({
      LOG.warn("Config file has not found in resources.")
      //Empty stream needs for making empty config object from YAMl parser.
      //It is similar to an empty config.
      IOUtils.toInputStream("{}")
    })

    val mapper = new ObjectMapper(new YAMLFactory())
      .registerModule(DefaultScalaModule)

    val configRoot = mapper.readValue(reader, classOf[JsonNode])

    properties.foreach(kv => if (kv._1 != CONFIG_PROPERTY) configRoot
      .asInstanceOf[ObjectNode]
      .put(kv._1, kv._2))

    mapper
      .convertValue(
        configRoot, ct.runtimeClass.asInstanceOf[Class[CONFIG]])
  }

  def parseCommandLineArgs(args: Array[String]): util.Properties = {
    val option = org.apache.commons.cli.Option.builder(OPTION).hasArgs.required.valueSeparator('=').build()

    new DefaultParser()
      .parse(new org.apache.commons.cli.Options().addOption(option), args)
      .getOptionProperties(OPTION)
  }
}
