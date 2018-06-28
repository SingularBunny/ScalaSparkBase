package org.github.example

import com.fasterxml.jackson.annotation.JsonProperty

class BaseTestConfig {
  var testProperty1: String = _
}

class TestConfig extends BaseTestConfig {
  var testProperty2: Int = _
  @JsonProperty("testProperty3") var _testProperty3: Double = _
}
