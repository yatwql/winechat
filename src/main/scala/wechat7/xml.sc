package wechat7
import scala.xml._
import java.util.Date
import wechat7.util._
import scala.xml.transform._
object xml {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  val books = """<books>
      <book>
        <title>a</title>
        <price>100</price>
      </book>
      <book>
        <title>b</title>
        <price>200</price>
      </book>
      <book>
        <title>c</title>
        <price>300</price>
      </book>
    </books>"""                                   //> books  : String = <books>
                                                  //|       <book>
                                                  //|         <title>a</title>
                                                  //|         <price>100</price>
                                                  //|       </book>
                                                  //|       <book>
                                                  //|         <title>b</title>
                                                  //|         <price>200</price>
                                                  //|       </book>
                                                  //|       <book>
                                                  //|         <title>c</title>
                                                  //|         <price>300</price>
                                                  //|       </book>
                                                  //|     </books>

  val booksXml = XML.loadString(books)            //> booksXml  : scala.xml.Elem = <books>
                                                  //|       <book>
                                                  //|         <title>a</title>
                                                  //|         <price>100</price>
                                                  //|       </book>
                                                  //|       <book>
                                                  //|         <title>b</title>
                                                  //|         <price>200</price>
                                                  //|       </book>
                                                  //|       <book>
                                                  //|         <title>c</title>
                                                  //|         <price>300</price>
                                                  //|       </book>
                                                  //|     </books>
  val prices: Seq[(String, Double)] = for {
    book <- booksXml \ "book"
    title <- book \ "title"
    price <- book \ "price"
  } yield (title.text, price.text.toDouble)       //> prices  : Seq[(String, Double)] = List((a,100.0), (b,200.0), (c,300.0))
  // do something with prices
  prices foreach println                          //> (a,100.0)
                                                  //| (b,200.0)
                                                  //| (c,300.0)

  val wxr = """<xml>
<ToUserName><![CDATA[gh_c2bb951675bb]]></ToUserName>
<FromUserName><![CDATA[oIySzjrizSaAyqnlB57ggb0j2WNc]]></FromUserName>
<Content><![CDATA[dddd]]></Content>
<CreateTime>1397931201</CreateTime>
<MsgType><![CDATA[text]]></MsgType>
<MsgId>6004068790553573078</MsgId>
</xml>"""                                         //> wxr  : String = <xml>
                                                  //| <ToUserName><![CDATA[gh_c2bb951675bb]]></ToUserName>
                                                  //| <FromUserName><![CDATA[oIySzjrizSaAyqnlB57ggb0j2WNc]]></FromUserName>
                                                  //| <Content><![CDATA[dddd]]></Content>
                                                  //| <CreateTime>1397931201</CreateTime>
                                                  //| <MsgType><![CDATA[text]]></MsgType>
                                                  //| <MsgId>6004068790553573078</MsgId>
                                                  //| </xml>

  val wxl = XML.loadString(wxr)                   //> wxl  : scala.xml.Elem = <xml>
                                                  //| <ToUserName>gh_c2bb951675bb</ToUserName>
                                                  //| <FromUserName>oIySzjrizSaAyqnlB57ggb0j2WNc</FromUserName>
                                                  //| <Content>dddd</Content>
                                                  //| <CreateTime>1397931201</CreateTime>
                                                  //| <MsgType>text</MsgType>
                                                  //| <MsgId>6004068790553573078</MsgId>
                                                  //| </xml>
  val toUser = (wxl \ "ToUserName").text          //> toUser  : String = gh_c2bb951675bb
  val fromUser = (wxl \ "FromUserName").text      //> fromUser  : String = oIySzjrizSaAyqnlB57ggb0j2WNc

  val now = new Date().getTime()                  //> now  : Long = 1398877226913

  val response =
    <xml>
      <ToUserName>{ fromUser }</ToUserName>
      <FromUserName>{ toUser }</FromUserName>
      <Content><![CDATA[ccc]]></Content>
      <CreateTime>{ now }</CreateTime>
      <articles></articles>
      <MsgType><![CDATA[text]]></MsgType>
    </xml>                                        //> response  : scala.xml.Elem = <xml>
                                                  //|       <ToUserName>oIySzjrizSaAyqnlB57ggb0j2WNc</ToUserName>
                                                  //|       <FromUserName>gh_c2bb951675bb</FromUserName>
                                                  //|       <Content>ccc</Content>
                                                  //|       <CreateTime>1398877226913</CreateTime>
                                                  //|       <articles></articles>
                                                  //|       <MsgType>text</MsgType>
                                                  //|     </xml>

  (response \\ "FromUserName").text               //> res0: String = gh_c2bb951675bb

  val i1 = <item>cc1 </item>                      //> i1  : scala.xml.Elem = <item>cc1 </item>
  val i2 = <item> lsdkjf22</item>                 //> i2  : scala.xml.Elem = <item> lsdkjf22</item>

  val items = Seq(i1, i2)                         //> items  : Seq[scala.xml.Elem] = List(<item>cc1 </item>, <item> lsdkjf22</ite
                                                  //| m>)


  val dd = XmlUtils.addChildren(response, "articles", items)
                                                  //> List(<xml>
                                                  //|       <ToUserName>oIySzjrizSaAyqnlB57ggb0j2WNc</ToUserName>
                                                  //|       <FromUserName>gh_c2bb951675bb</FromUserName>
                                                  //|       <Content>ccc</Content>
                                                  //|       <CreateTime>1398877226913</CreateTime>
                                                  //|       <articles></articles>
                                                  //|       <MsgType>text</MsgType>
                                                  //|     </xml>, <item>cc1 </item>, <item> lsdkjf22</item>)
                                                  //| dd  : scala.xml.Node = <xml>
                                                  //|       <ToUserName>oIySzjrizSaAyqnlB57ggb0j2WNc</ToUserName>
                                                  //|       <FromUserName>gh_c2bb951675bb</FromUserName>
                                                  //|       <Content>ccc</Content>
                                                  //|       <CreateTime>1398877226913</CreateTime>
                                                  //|       <articles><item>cc1 </item><item> lsdkjf22</item></articles>
                                                  //|       <MsgType>text</MsgType>
                                                  //|     </xml>
                                                  
  val m=WechatUtils.getNewsMsg("a", "b", "c", items)
                                                  //> SLF4J: Class path contains multiple SLF4J bindings.
                                                  //| SLF4J: Found binding in [jar:file:/Users/wang/.ivy2/cache/org.slf4j/slf4j-n
                                                  //| op/jars/slf4j-nop-1.6.4.jar!/org/slf4j/impl/StaticLoggerBinder.class]
                                                  //| SLF4J: Found binding in [jar:file:/Users/wang/.ivy2/cache/ch.qos.logback/lo
                                                  //| gback-classic/jars/logback-classic-1.0.6.jar!/org/slf4j/impl/StaticLoggerBi
                                                  //| nder.class]
                                                  //| SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explana
                                                  //| tion.
                                                  //| SLF4J: Actual binding is of type [org.slf4j.helpers.NOPLoggerFactory]
                                                  //| List(<xml>
                                                  //|         <ToUserName>b</ToUserName>
                                                  //|         <FromUserName>a</FromUserName>
                                                  //|         <Content>c</Content>
                                                  //|         <CreateTime>1398877227352</CreateTime>
                                                  //|         <MsgType>news</MsgType>
                                                  //|         <ArticleCount>2</ArticleCount>
                                                  //|         <Articles>
                                                  //|         </Articles>
                                                  //|         <FuncFlag>0</FuncFlag>
                                                  //|       </xml>, <item>cc1 </item>, <item> lsdkjf22</item>)
                                                  //| 348
                                                  //| m  : String =
                                                  //| Output exceeds cutoff limit.
                       
                       
                                           
                                               
    val oldXML =
      <xml>
        <ToUserName>a</ToUserName>
        <FromUserName>b</FromUserName>
        <Content>c</Content>
        <CreateTime>now</CreateTime>
        <MsgType><![CDATA[news]]></MsgType>
        <ArticleCount>2</ArticleCount>
        <Articles>
        </Articles>
        <FuncFlag>0</FuncFlag>
      </xml>                                      //> oldXML  : scala.xml.Elem = <xml>
                                                  //|         <ToUserName>a</ToUserName>
                                                  //|         <FromUserName>b</FromUserName>
                                                  //|         <Content>c</Content>
                                                  //|         <CreateTime>now</CreateTime>
                                                  //|         <MsgType>news</MsgType>
                                                  //|         <ArticleCount>2</ArticleCount>
                                                  //|         <Articles>
                                                  //|         </Articles>
                                                  //|         <FuncFlag>0</FuncFlag>
                                                  //|       </xml>

    val message = XmlUtils.addChildren(oldXML, "Articles", items)
                                                  //> List(<xml>
                                                  //|         <ToUserName>a</ToUserName>
                                                  //|         <FromUserName>b</FromUserName>
                                                  //|         <Content>c</Content>
                                                  //|         <CreateTime>now</CreateTime>
                                                  //|         <MsgType>news</MsgType>
                                                  //|         <ArticleCount>2</ArticleCount>
                                                  //|         <Articles>
                                                  //|         </Articles>
                                                  //|         <FuncFlag>0</FuncFlag>
                                                  //|       </xml>, <item>cc1 </item>, <item> lsdkjf22</item>)
                                                  //| message  : scala.xml.Node = <xml>
                                                  //|         <ToUserName>a</ToUserName>
                                                  //|         <FromUserName>b</FromUserName>
                                                  //|         <Content>c</Content>
                                                  //|         <CreateTime>now</CreateTime>
                                                  //|         <MsgType>news</MsgType>
                                                  //|         <ArticleCount>2</ArticleCount>
                                                  //|         <Articles>
                                                  //|         <item>cc1 </item><item> lsdkjf22</item></Articles>
                                                  //|         <FuncFlag>0</FuncFlag>
                                                  //|       </xml>
                                                  
}