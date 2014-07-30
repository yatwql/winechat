package wechat7.repo

trait VoteRepo extends SlickRepo {

  import profile.simple._

  override def populateTable(tableName: String = "all"): String = {
    println("execute VoteRepo " + tableName)
    conn.dbObject withSession { implicit session: Session =>
      tableName match {
        case "all" => {
          populateTable("voteThreads") + " , " + populateTable("voteOptions") + " , " + populateTable("voteResults") + " , " + super.populateTable(tableName)
        }
        case "voteThreads" => {
          try {
            println("======================Insert voteThreads into database ====================")
            voteThreads.insert(VoteThread(21, "红酒调查(地点)", "您喜欢以下哪个产地的红酒: "))
            voteThreads.insert(VoteThread(22, "红酒调查(口味)", "您喜欢哪种葡萄酒: "))
            voteThreads.insert(VoteThread(23, "红酒调查(价格)", "您觉得可接受的红酒价格为: "))
            println("======================retrieve voteThreads from database ====================")
            voteThreads.list foreach println
            "<VoteRepo>Populate voteThreads;"
          } catch {
            case ex: Exception => println(ex.getMessage); ""
          }
        }
        case "voteOptions" => {
          try {
            println("======================Insert voteOptions into database ====================")
            voteOptions.insert(VoteOption(21, "1", "智利"))
            voteOptions.insert(VoteOption(21, "2", "法国"))
            voteOptions.insert(VoteOption(21, "3", "巴拉圭"))
            voteOptions.insert(VoteOption(21, "4", "中国"))
            voteOptions.insert(VoteOption(21, "5", "其他"))
            voteOptions.insert(VoteOption(22, "1", "白葡萄酒"))
            voteOptions.insert(VoteOption(22, "2", "红葡萄酒"))
            voteOptions.insert(VoteOption(23, "1", "100以下"))
            voteOptions.insert(VoteOption(23, "2", "200至500"))
            voteOptions.insert(VoteOption(23, "3", "我不想花钱"))
            voteOptions.insert(VoteOption(23, "4", "大爷有钱,好喝就行"))
            println("======================retrieve voteOptions from database ====================")
            voteOptions.list foreach println
            "<VoteRepo>Populate voteOptions;"
          } catch {
            case ex: Exception => println(ex.getMessage); ""
          }
        }

        case "voteResults" => {
          ""
        }

        case _ => super.populateTable(tableName)
      }
    }
  }

  def getVoteThread(voteId: Int): Option[VoteThread] = {
    conn.dbObject withSession { implicit session: Session =>
      val result = voteThreads.filter(_.voteId === voteId).list
      result match {
        case a :: _ => Some(result.last)
        case _ => None
      }
    }
  }

  def updateVoteThread(voteName: String, description: String, voteMethod: Int, newVoteId: Int, qVoteId: Int, saveNew: Boolean = false) = {
    conn.dbObject withSession { implicit session: Session =>
      if (saveNew) {
        voteThreads.insert(VoteThread(newVoteId, voteName, description, voteMethod))
        actions.insert(Action("vote"+newVoteId,"vote"+newVoteId,"voting"+newVoteId))
      } else {
        val queryForThread = voteThreads.filter(_.voteId === qVoteId).map(s => (s.voteId, s.name, s.description, s.voteMethod))
        queryForThread.update(newVoteId, voteName, description, voteMethod)
        val queryForAction= actions.filter(_.actionKey=== "vote"+qVoteId).map(s=> (s.actionKey,s.currentAction,s.nextAction))
        queryForAction.update("vote"+newVoteId,"vote"+newVoteId,"voting"+newVoteId)
      }
      newVoteId
    }
  }

  def deleteVoteThread(voteId: Int) {
    conn.dbObject withSession { implicit session: Session =>

      val queryForThread = voteThreads.filter(_.voteId === voteId)
      queryForThread.delete
      val queryForOption = voteOptions.filter(_.voteId === voteId)
      queryForOption.delete
       val queryForAction= actions.filter(_.actionKey=== "vote"+voteId)
       queryForAction.delete
    }
  }

  def updateVoteOpton(newVoteId: Int, option: String, optionDesc: String, id: Int, qVoteId: Int, saveNew: Boolean = false) = {
    conn.dbObject withSession { implicit session: Session =>
      if (saveNew) {
        voteOptions.insert(VoteOption(newVoteId, option, optionDesc, id))
      } else {
        val q = voteOptions.filter(_.id === id).filter(_.voteId === qVoteId).map(s => (s.voteId, s.option, s.optionDesc))
        q.update(newVoteId, option, optionDesc)
        q
      }
      newVoteId
    }
  }

  def newVoteOpton(voteId: Int, option: String, optionDesc: String, id: Int = 0) = {
    conn.dbObject withSession { implicit session: Session =>
      voteOptions.insert(VoteOption(voteId, option, optionDesc, id))
    }
  }

  def newVoteThread(voteName: String, description: String, voteId: Int, voteMethod: Int = 1) = {
    conn.dbObject withSession { implicit session: Session =>
      voteThreads.insert(VoteThread(voteId, voteName, description, voteMethod))

    }
  }

  def getVoteTopics(take: Int = 20) = {
    conn.dbObject withSession { implicit session: Session =>
      // val query = for (v <-voteThreads) yield (v.name,"vote"+v.voteId)
      val query = voteThreads.map(v => (v.name, v.voteId, "vote" + (v.voteId).toString()))
      val result = query.take(take).list()
      result
    }
  }

  def getVoteOptions(voteId: Int): List[VoteOptions#TableElementType] = {
    conn.dbObject withSession { implicit session: Session =>
      voteOptions.filter(_.voteId === voteId).list
    }
  }

  def getVoteOptionsToTuples(voteId: Int): List[(Int, String, String)] = {
    conn.dbObject withSession { implicit session: Session =>
      voteOptions.filter(_.voteId === voteId).map(o => (o.id, o.option, o.optionDesc)).list
    }
  }

  def getVoteGrpResult(voteId: Int) = {
    conn.dbObject withSession { implicit session: Session =>
      val result = voteResults.filter(_.voteId === voteId).groupBy(_.option).map {
        case (option, ps) => (option, ps.length)
      }
      result.list().toMap
    }
  }

  def getVoteResult(openId: String, voteId: Int) = {
    conn.dbObject withSession { implicit session: Session =>
      val result = voteResults.filter(_.openId === openId).filter(_.voteId === voteId).list
      result match {
        case a :: _ => Some(result.last)
        case _ => None
      }
    }
  }

  def addVoteResult(openId: String, voteId: Int, option: String, fromLocationX: String = "", fromLocationY: String = "") {
    conn.dbObject withSession { implicit session: Session =>
      voteResults.insert(VoteResult(openId, voteId, option, fromLocationX, fromLocationY))
    }
  }

  def updateVoteResult(openId: String, voteId: Int, option: String) {
    conn.dbObject withSession { implicit session: Session =>
      val c = voteResults.filter(_.openId === openId).filter(_.voteId === voteId).map(_.option)
      c.update(option)

    }
  }
}