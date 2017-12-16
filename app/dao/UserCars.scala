package dao

import java.sql.{Date, ResultSet}

import models.UserCar

import scala.util.{Failure, Success, Try}

object UserCars extends DAOJdbc[UserCar] {
  def create(obj: UserCar): Try[Long] = {
    var data: Try[Long] = Try(throw new Exception("Error with insert User"))
    withDatabase{ database =>
      val connection = database.getConnection()
      val sql: String = "INSERT INTO `userCars` (`userId`, `carId`, `attachDate`) VALUES (?,?,?)"
      val stmt = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)
      stmt.setLong(1, obj.userId)
      stmt.setLong(2, obj.carId)
      stmt.setDate(3, obj.attachDate)

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

  def getById(userId: Long, carId: Long): Try[UserCar] = {
    var userCar: Try[UserCar] = Try(throw new Exception(s"User car id=$carId Not Found with user id=$userId"))
    withDatabase{ database =>
      val connection = database.getConnection()
      val sql: String = "select * from userCars where userId=? and carId=?"
      val stmt = connection.prepareStatement(sql)
      stmt.setLong(1, userId)
      stmt.setLong(2, carId)

      val rs: Try[ResultSet] = Try(stmt.executeQuery)
      rs match {
        case Success(v) => {
          if (v.next()) {
            userCar = Try(maker(v))
          }
        }
        case Failure(e) => println(e.getMessage)
      }

    }

    userCar
  }

  def getAll(idUser: Long): Try[Set[UserCar]] = {
    var set: Set[UserCar] = Set()
    withDatabase { database =>
      val connection = database.getConnection()
      val sql: String = "select * from userCars where `userId`=" + idUser

      val stmt = connection.prepareStatement(sql)
      val rs: ResultSet = stmt.executeQuery

      while (rs.next()) {
        set += maker(rs)
      }
    }

    Try(set)
  }

  def deleteCar(userId: Long, carId: Long): Try[Boolean] = {
    var data: Try[Boolean] = Try(throw new Exception("Error with delete user car"))
    withDatabase{ database =>
      val sql: String = "delete from userCars where `userId`=? and `carId`=?"
      val connection = Try(database.getConnection)

      connection match {
        case Success(v) => {
          val stmt = v.prepareStatement(sql)
          stmt.setLong(1, userId)
          stmt.setLong(2, carId)

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

  override protected[dao] def maker(resultSet: ResultSet): UserCar = {
    val id: Long = resultSet.getLong("id")
    val userId: Long = resultSet.getLong("userId")
    val carId: Long = resultSet.getLong("carId")
    val attachDate: Date = resultSet.getDate("attachDate")

    new UserCar(id, userId, carId, attachDate)
  }
}
