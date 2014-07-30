package wechat7.util

object Hex {
  def hex2Bytes(hex: String): Array[Byte] = {

    (for { i <- 0 to hex.length - 1 by 2 if i > 0 || !hex.startsWith("0x") }

      yield hex.substring(i, i + 2))

      .map(Integer.parseInt(_, 16).toByte).toArray

  }

  def bytes2Hex(buf: Array[Byte]): String = buf.map("%02X" format _).mkString

}