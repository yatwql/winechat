package wechat7.util

import java.net.HttpURLConnection
import java.net.URL

object HttpUtils {
  def post(urlStr: String, content: String): String = {
    try {
      val url = new URL(urlStr);
      val conn = url.openConnection()
      val http: HttpURLConnection = conn.asInstanceOf[HttpURLConnection]

      http.setRequestMethod("POST");
      http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      http.setDoOutput(true);
      http.setDoInput(true);
      System.setProperty("sun.net.client.defaultConnectTimeout", "30000"); //连接超时30秒
      System.setProperty("sun.net.client.defaultReadTimeout", "30000"); //读取超时30秒
      http.connect();
      val os = http.getOutputStream();
      os.write(content.getBytes("UTF-8")); //传入参数    
      os.flush();
      os.close();

      val is = http.getInputStream();
      val size = is.available();
      val responseData = new Array[Byte](size)
      is.read(responseData);
      val message = new String(responseData, "UTF-8");
      message
    } catch {
      case e: Exception => e.printStackTrace(); "";
    }
  }

  def get(urlStr: String): String = {
    try {
      val url = new URL(urlStr);
      val conn = url.openConnection()
      val http: HttpURLConnection = conn.asInstanceOf[HttpURLConnection]

      http.setRequestMethod("GET"); //必须是get方式请求
      http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      http.setDoOutput(true);
      http.setDoInput(true);
      System.setProperty("sun.net.client.defaultConnectTimeout", "30000"); //连接超时30秒
      System.setProperty("sun.net.client.defaultReadTimeout", "30000"); //读取超时30秒
      http.connect();

      val is = http.getInputStream();
      val size = is.available();
      val responseData = new Array[Byte](size)
      is.read(responseData);
      val message = new String(responseData, "UTF-8");
      message
    } catch {
      case e: Exception => e.printStackTrace(); "";
    }
  }
  
}