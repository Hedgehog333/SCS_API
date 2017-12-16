package dao

import java.sql.{ResultSet, Time}
import models.Autostation
import scala.util.{Failure, Success, Try}

object Autostations extends DAOJdbc[Autostation] with DAO[Autostation] {
  override def create(obj: Autostation): Try[Long] = {
    var data: Try[Long] = Try(throw new Exception("Error with insert Autostation"))
    withDatabase{ database =>
      val connection = database.getConnection()
      val sql: String = "INSERT INTO `autostations` " +
        "(`name`, `cityId`, `countWorkplaces`, `address`, `startTime`, `endTime`, `email`, `password`) VALUES" +
        "(?,?,?,?,?,?,?,?)"
      val stmt = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)
      stmt.setString(1, obj.name)
      stmt.setLong(2, obj.cityId)
      stmt.setLong(3, obj.countWorkplaces)
      stmt.setString(4, obj.address)
      stmt.setTime(5, Time.valueOf(obj.startTime))
      stmt.setTime(6, Time.valueOf(obj.endTime))
      stmt.setString(7, obj.email)
      stmt.setString(8, obj.password)

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

  override def getById(id: Long): Try[Autostation] = {
    var user: Try[Autostation] = Try(throw new Exception(s"Autostation id=$id Not Found"))
    withDatabase{ database =>
      val connection = database.getConnection()
      val sql: String = "select * from autostations where id = " + id.toString
      val stmt = connection.prepareStatement(sql)
      val rs: Try[ResultSet] = Try(stmt.executeQuery)

      rs match {
        case Success(v) => {
          if (v.next()) {
            user = Try(maker(v))
            println(v.getTime("startTime"))
          }
        }
        case Failure(e) => println(e.getMessage)
      }
    }

    user
  }

  def getByEmailAndPassword(obj: dto.AutostationLogin): Try[Autostation] = {
    var user: Try[Autostation] = Try(throw new Exception("Autostation Not Found"))
    withDatabase{ database =>
      val connection = database.getConnection()
      val sql: String = "select * from autostations where email like ? and password=?"
      val stmt = connection.prepareStatement(sql)
      stmt.setString(1, obj.email)
      stmt.setString(2, obj.password)

      val rs: Try[ResultSet] = Try(stmt.executeQuery)
      rs match {
        case Success(v) => {
          if (v.next()) {
            user = Try(maker(v))
          }
        }
        case Failure(e) => println("Failure: " + e.getMessage)
      }
    }

    user
  }

  override def getAll(): Try[Set[Autostation]] = {
    var set: Set[Autostation] = Set()
    withDatabase { database =>
      val connection = database.getConnection()
      val sql: String = "select * from autostations"

      val stmt = connection.prepareStatement(sql)
      val rs: ResultSet = stmt.executeQuery

      while (rs.next()) {
        set += maker(rs)
      }
    }

    Try(set)
  }

  override def update(obj: Autostation): Try[Boolean] = {
    var data: Try[Boolean] = Try(throw new Exception("Error with update Autostation"))
    withDatabase{ database =>
      val connection = database.getConnection()
      val sql: String = "update autostations set `name`=?, `cityId`=?, `countWorkplaces`=?, `address`=?, " +
        "`startTime`=?, `endTime`=?, `email`=?, `password`=? " +
        "WHERE `id`=" + obj.id

      val stmt = connection.prepareStatement(sql)
      stmt.setString(1, obj.name)
      stmt.setLong(2, obj.cityId)
      stmt.setLong(3, obj.countWorkplaces)
      stmt.setString(4, obj.address)
      stmt.setTime(5, Time.valueOf(obj.startTime))
      stmt.setTime(6, Time.valueOf(obj.endTime))
      stmt.setString(7, obj.email)
      stmt.setString(8, obj.password)

      val result: Try[Int] = Try(stmt.executeUpdate)
      result match {
        case Success(v) => data = Try(v.equals(1))
        case Failure(e) => println(e.getMessage)
      }
    }

    data
  }

  override def delete(id: Long): Try[Boolean] = {
    var data: Try[Boolean] = Try(throw new Exception("Error with delete Car"))
    withDatabase{ database =>
      val sql: String = "delete from autostations where id = ?"
      val connection = Try(database.getConnection)

      connection match {
        case Success(v) => {
          val stmt = v.prepareStatement(sql)
          stmt.setLong(1, id)

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

  override protected[dao] def maker(resultSet: ResultSet): Autostation = {
    val id = resultSet.getLong("id")
    val name = resultSet.getString("name")
    val cityId = resultSet.getLong("cityId")
    val countWorkplaces = resultSet.getLong("countWorkplaces")
    val address = resultSet.getString("address")
    val startTime = resultSet.getTime("startTime")
    val endTime = resultSet.getTime("endTime")
    val email = resultSet.getString("email")
    val password = resultSet.getString("password")

    new Autostation(id, name, cityId, countWorkplaces, address, startTime.toString, endTime.toString, email, password)
  }
}