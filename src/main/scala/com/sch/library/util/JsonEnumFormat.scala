package com.sch.library.util

import spray.json.{JsString, JsValue, JsonFormat, DeserializationException}

/**
  * User: schirkov
  * Date: 06.09.2016
  */
object JsonEnumFormat {
  def format[T <: Enumeration](enu: T) = new JsonFormat[T#Value] {
    def write(obj: T#Value) = JsString(obj.toString)

    def read(json: JsValue) = json match {
      case JsString(txt) => enu.withName(txt)
      case something => throw new DeserializationException(s"Expected a value from enum $enu instead of $something")
    }
  }
}