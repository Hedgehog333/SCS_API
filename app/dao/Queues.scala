package dao

import java.sql.{Date, ResultSet, Time}

import models.{Queue, Car}

import scala.util.{Failure, Success, Try}

object Queues extends DAOJdbc[Queue] {
  def create(obj: Queue): Try[Long] = {
    var data: Try[Long] = Try(throw new Exception("Error with insert User to queue"))
    withDatabase{ database =>
      val connection = database.getConnection()
      val sql: String = "INSERT INTO `queues` " +
        "(`userCarId`, `autostationId`, `date`, `time`, `lengthTime`, `workplaceNumber`, `status`) " +
        "VALUES (?,?,?,?,?,?,?)"
      val stmt = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)
      stmt.setLong(1, obj.carId)
      stmt.setLong(2, obj.autostationId)
      stmt.setDate(3, obj.date)
      stmt.setTime(4, Time.valueOf(obj.time))
      stmt.setTime(5, Time.valueOf(obj.lenhthTime))
      stmt.setLong(6, obj.workplaceNumber)
      stmt.setLong(7, obj.status)

      data = Try(stmt.executeUpdate)
      data match {
        case Success(v) => {
          val lastIdSet: ResultSet = stmt.getGeneratedKeys
          if(lastIdSet.next()) {
            data = Try(lastIdSet.getLong(1))
          }
        }
        case Failure(e) => println(e.getMessage)
      }
    }

    data
  }

  def getAll(autostatuinId: Long, date: Date): Try[Set[Queue]] = {
    var set: Set[Queue] = Set()
    withDatabase { database =>
      val connection = database.getConnection()
      val sql: String = "select * from queues " +
        "WHERE `autostationId`=? and `date` like ?"

      val stmt = connection.prepareStatement(sql)
      stmt.setLong(1, autostatuinId)
      stmt.setDate(2, date)

      val rs: ResultSet = stmt.executeQuery
      while (rs.next()) {
        set += maker(rs)
      }
    }

    Try(set)
  }

  def getCarsByDate(autostatuinId: Long, date: Date): Try[Set[Car]] = {
    var set: Set[Car] = Set()
    withDatabase { database =>
      val connection = database.getConnection()
      val sql: String = "SELECT c.* FROM queues q, cars c, userCars uc " +
        "WHERE q.userCarId = uc.id and uc.carId = c.id and q.date like ? and q.autostationId = ?"

      val stmt = connection.prepareStatement(sql)
      stmt.setDate(1, date)
      stmt.setLong(2, autostatuinId)

      val rs: ResultSet = stmt.executeQuery
      while (rs.next()) {
        set += Cars.maker(rs)
      }
    }

    Try(set)
  }

  def getCarByDate(autostatuinId: Long, date: Date, carId: Long): Try[Car] = {
    var data: Try[Car] = Try(throw new Exception("Error with select Car in queue"))
    withDatabase { database =>
      val connection = database.getConnection()
      val sql: String = "SELECT c.* FROM queues q, cars c, userCars uc " +
        "WHERE q.userCarId = uc.id and uc.carId = c.id and q.date like ? and q.autostationId = ? and c.id = ?"

      val stmt = connection.prepareStatement(sql)
      stmt.setDate(1, date)
      stmt.setLong(2, autostatuinId)
      stmt.setLong(3, carId)

      val rs: ResultSet = stmt.executeQuery
      if (rs.next()) {
        data = Try(Cars.maker(rs))
      }
    }

    data
  }

  def update(queue: Queue): Try[Boolean] = {
    var data: Try[Boolean] = Try(throw new Exception("Error with update Queue"))
    withDatabase { database =>
      val connection = database.getConnection()
      val sql: String = "UPDATE `queues` " +
      "SET `time`=?, `lengthTime`=?, `workplaceNumber`=?, `status`=? " +
      "WHERE `userCarId`=? and `autostationId`=? and `date` like ?"

      val stmt = connection.prepareStatement(sql)
      stmt.setTime(1, Time.valueOf(queue.time))
      stmt.setTime(2, Time.valueOf(queue.lenhthTime))
      stmt.setLong(3, queue.workplaceNumber)
      stmt.setLong(4, queue.status)
      stmt.setLong(5, queue.carId)
      stmt.setLong(6, queue.autostationId)
      stmt.setDate(7, queue.date)

      val result: Try[Int] = Try(stmt.executeUpdate)
      result match {
        case Success(v) => data = Try(v.equals(1))
        case Failure(e) => println(e.getMessage)
      }
    }

    data
  }

  def delete(autostationId: Long, date: Date, userCarId: Long): Try[Boolean] = {
    var data: Try[Boolean] = Try(throw new Exception("Error with delete Queue"))
    withDatabase{ database =>
      val sql: String = "DELETE FROM `queues` WHERE `autostationId`=? and `date` like ? and `userCarId` = ?"
      val connection = Try(database.getConnection)

      connection match {
        case Success(v) => {
          val stmt = v.prepareStatement(sql)
          stmt.setLong(1, autostationId)
          stmt.setDate(2, date)
          stmt.setLong(3, userCarId)
          println(stmt)
          val result: Try[Int] = Try(stmt.executeUpdate)
          result match {
            case Success(v) => data = Try(v.equals(1))
            case Failure(e) => println(e.getMessage)
          }
        }
        case Failure(e) => println(e.getMessage)
      }
    }

    data
  }

  override protected[dao] def maker(resultSet: ResultSet): Queue = {
    val id: Long = resultSet.getLong("id")
    val carId: Long = resultSet.getLong("userCarId")
    val autostationId: Long = resultSet.getLong("autostationId")
    val date: Date = resultSet.getDate("date")
    val time: String = resultSet.getTime("time").toString
    val lenhthTime: String = resultSet.getTime("lengthTime").toString
    val workplaceNumber: Long = resultSet.getLong("workplaceNumber")
    val status: Long = resultSet.getLong("status")

    new Queue(id, carId, autostationId, date, time, lenhthTime, workplaceNumber, status)
  }
}