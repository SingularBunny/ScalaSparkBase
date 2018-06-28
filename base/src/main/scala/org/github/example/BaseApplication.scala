package org.github.example

import org.apache.spark.sql.SparkSession

import scala.reflect.ClassTag

trait BaseApplication[CONFIG <: BaseConfig] {

  /**
    * Custom logic of Spark job. implement yor code here.
    *
    * @param sparkSession Spark session.
    * @param config       custom config. Child of @see [[BaseConfig]]
    */
  def run(sparkSession: SparkSession, config: CONFIG): Unit

  def init(args: Array[String])(implicit ct: ClassTag[CONFIG]): Unit = {
    val session = SparkSession
      .builder()
      .appName(getClass.getSimpleName)
      .getOrCreate()

    run(session, ConfigBuilder.build[CONFIG](args))
  }
}