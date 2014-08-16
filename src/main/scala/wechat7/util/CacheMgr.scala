package wechat7.util

import scala.xml.Node

import spray.caching.Cache
import spray.caching.LruCache
import scala.concurrent.duration._
object CacheMgr {
  val nicknameCache: Cache[Option[String]] = LruCache(maxCapacity = 500,timeToLive=(24 hours),timeToIdle=(12 hours))
  val userActionCache: Cache[Option[String]] = LruCache(maxCapacity = 500,timeToLive=(2 hours),timeToIdle=(1 hours))
  val nextActionCache: Cache[Option[String]] = LruCache(maxCapacity = 64,timeToLive=(2 hours),timeToIdle=(1 hours))
  val currentActionCache: Cache[Option[String]] = LruCache(maxCapacity = 64,timeToLive=(2 hours),timeToIdle=(1 hours))
  val articleCache: Cache[Seq[Node]] = LruCache(maxCapacity = 64,timeToLive=Duration(2,HOURS),timeToIdle=Duration(1,HOURS))
  val voteThreadCache: Cache[Option[(String, String, Int, Seq[String])]] = LruCache(maxCapacity = 64,timeToLive=(2 hours),timeToIdle=(1 hours))
}