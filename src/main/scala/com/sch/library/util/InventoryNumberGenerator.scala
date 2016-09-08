package com.sch.library.util

import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.security.MessageDigest
import java.util.Date

import org.apache.commons.codec.digest.DigestUtils

import scala.collection.mutable.ArrayBuffer

/**
 * User: schirkov
 * Date: 9/8/2016
 */
object InventoryNumberGenerator {

  private val charset = Charset.forName("UTF-8")

  def generate(title: String, authors: String, date: Date): String = {
    var buffer = new ArrayBuffer[Byte]

    val byteBuffer = ByteBuffer.allocate(java.lang.Long.SIZE)
    byteBuffer.putLong(0, date.getTime)

    buffer ++= byteBuffer.array()
    buffer ++= title.getBytes(charset)
    buffer ++= authors.getBytes(charset)

    DigestUtils.md5Hex(buffer.toArray)
  }
}
