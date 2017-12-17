package dao

import java.sql.ResultSet
import models.Statistic
import scala.util.{Failure, Success, Try}

object Statistics extends DAOJdbc[Statistic] {
  def create(obj: Statistic): Try[Long] = {
    var data: Try[Long] = Try(throw new Exception("Error with insert Spare"))
    withDatabase{ database =>
      val connection = database.getConnection()
      val sql: String = "INSERT INTO `carStatistics` " +
        "(`detailNumber`, `spareId`, `userCarId`, `state`, `typeSpareId`, `seal`) VALUES" +
        "(?,?,?,?,?,?)"
      val stmt = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)
      stmt.setLong(1,obj.detailNumber)
      stmt.setLong(2,obj.spareId)
      stmt.setLong(3, obj.userCarId)
      stmt.setLong(4,obj.state)
      stmt.setLong(5,obj.typeSpareId)
      stmt.setLong(6,obj.seal)

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

  def getStatCarSpare(userCarId: Long, detailNumber: Long): Try[Statistic] = {
    var spare: Try[Statistic] = Try(throw new Exception("Statistic Not Found"))
    withDatabase{ database =>
      val connection = database.getConnection()
      val sql: String = "select * from carStatistics where userCarId=? and detailNumber=? "
      val stmt = connection.prepareStatement(sql)
      stmt.setLong(1, userCarId)
      stmt.setLong(2, detailNumber)

      val rs: Try[ResultSet] = Try(stmt.executeQuery)
      rs match {
        case Success(v) => {
          if (v.next()) {
            spare = Try(maker(v))
          }
        }
        case Failure(e) => println(e.getMessage)
      }

    }

    spare
  }

  def getAll(carId: Long): Try[Set[Statistic]] = {
    var set: Set[Statistic] = Set()
    withDatabase { database =>
      val connection = database.getConnection()
      val sql: String = "select * from carStatistics where `userCarId`=?"

      val stmt = connection.prepareStatement(sql)
      stmt.setLong(1, carId)

      val rs: ResultSet = stmt.executeQuery
      while (rs.next()) {
        set += maker(rs)
      }
    }

    Try(set)
  }

  def update(obj: Statistic): Try[Boolean] = {
    var data: Try[Boolean] = Try(throw new Exception("Error with update Statistic"))
    withDatabase{ database =>
      val connection = database.getConnection()
      //
      val sql: String = "update carStatistics set `spareId`=?, `state`=?, `typeSpareId`=?, `seal`=? " +
        "WHERE `userCarId`=? and `detailNumber`=?"

      val stmt = connection.prepareStatement(sql)
      stmt.setLong(1, obj.spareId)
      stmt.setLong(2, obj.state)
      stmt.setLong(3, obj.typeSpareId)
      stmt.setLong(4, obj.seal)
      stmt.setLong(5, obj.userCarId)
      stmt.setLong(6, obj.detailNumber)


      val result: Try[Int] = Try(stmt.executeUpdate)
      result match {
        case Success(v) => data = Try(v.equals(1))
        case Failure(e) => println(e.getMessage)
      }
    }

    data
  }

  def delete(userCarId: Long, detailNumber: Long): Try[Boolean] = {
    var data: Try[Boolean] = Try(throw new Exception("Error with delete statistic"))
    withDatabase{ database =>
      val sql: String = "delete from carStatistics where `userCarId`=? and `detailNumber`=?"
      val connection = Try(database.getConnection)
      connection match {
        case Success(v) => {
          val stmt = v.prepareStatement(sql)
          stmt.setLong(1, userCarId)
          stmt.setLong(2, detailNumber)

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

  protected[dao] def maker(resultSet: ResultSet): Statistic = {
    val id = resultSet.getLong("id")
    val detailNumber = resultSet.getLong("detailNumber")
    val spareId = resultSet.getLong("spareId")
    val carId = resultSet.getLong("userCarId")
    val state = resultSet.getLong("state")
    val typeStareId = resultSet.getLong("typeSpareId")
    val seal = resultSet.getLong("seal")

    new Statistic(id, detailNumber, spareId, carId, state, typeStareId, seal)
  }
}