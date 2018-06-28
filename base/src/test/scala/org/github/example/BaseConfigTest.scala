package org.github.example

import org.github.example.ConfigBuilder
import org.scalatest.FlatSpec

class BaseConfigTest extends FlatSpec {
  def createParam(param: String, value: String): String = {
    "-D" + param + "=" + value
  }

  it should "Parse command line args" in {
    val testProperty1 = "testProperty1"
    val testValue1 = "testValue1"
    val testProperty2 = "testProperty2"
    val testValue2 = "1"
    val configProperty = "config"
    val configValue = getClass.getResource("/config.yml").getPath

    val args = Array(createParam(testProperty1, testValue1),
      createParam(testProperty2, testValue2),
      createParam(configProperty, configValue))

    val config: TestConfig = ConfigBuilder.build[TestConfig](args)

    assert(config.testProperty1 == "testValue1")
    assert(config.testProperty2 == 1)
    assert(config._testProperty3 == 0.1)
  }

  it should "Parse command line args with space" in {
    val param = "-DtestProperty2=1"

    val config = ConfigBuilder.build[TestConfig](Array(param))

    assert(config.testProperty2 == 1)
  }
}