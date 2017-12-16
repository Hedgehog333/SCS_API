package dao

import java.sql.{Date, ResultSet}

import models.Car

import scala.util.{Failure, Success, Try}

object Cars extends DAOJdbc[Car] with DAO[Car] {
  override def create(obj: Car): Try[Long] = {
    var data: Try[Long] = Try(throw new Exception("Error with insert User"))
    withDatabase{ database =>
      val connection = database.getConnection()
      val sql: String = "INSERT INTO `cars` " +
        "(`vin`, `colorId`, `modelId`, `yearOfIssue`) VALUES" +
        "(?,?,?,?)"
      val stmt = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)
      stmt.setString(1,obj.vin)
      stmt.setLong(2,obj.colorId)
      stmt.setLong(3, obj.modelId)
      stmt.setDate(4,obj.yearOfIssue)

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

  override def getById(id: Long): Try[Car] = {
    var user: Try[Car] = Try(throw new Exception(s"Car id=$id Not Found"))
    withDatabase{ database =>
      val connection = database.getConnection()
      val sql: String = "select * from cars where id = " + id.toString
      val stmt = connection.prepareStatement(sql)

      val rs: Try[ResultSet] = Try(stmt.executeQuery)
      rs match {
        case Success(v) => {
          if (v.next()) {
            user = Try(maker(v))
          }
        }
        case Failure(e) => println(e.getMessage)
      }

    }

    user
  }

  override def getAll(): Try[Set[Car]] = {
    var set: Set[Car] = Set()
    withDatabase { database =>
      val connection = database.getConnection()
      val sql: String = "select * from cars"

      val stmt = connection.prepareStatement(sql)
      val rs: ResultSet = stmt.executeQuery

      while (rs.next()) {
        set += maker(rs)
      }
    }

    Try(set)
  }

  override def update(obj: Car): Try[Boolean] = {
    var data: Try[Boolean] = Try(throw new Exception("Error with update Car"))
    withDatabase{ database =>
      val connection = database.getConnection()
      val sql: String = "update cars set `vin`=?, `colorId`=?, `modelId`=?, `yearOfIssue`=?" +
        "WHERE `id`=" + obj.id

      val stmt = connection.prepareStatement(sql)
      stmt.setString(1, obj.vin)
      stmt.setLong(2, obj.colorId)
      stmt.setLong(3, obj.modelId)
      stmt.setDate(4, obj.yearOfIssue)


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
      val sql: String = "delete from cars where id = ?"
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

  override protected[dao] def maker(resultSet: ResultSet): Car = {
    val id: Long = resultSet.getLong("id")
    val vin: String = resultSet.getString("vin")
    val colorId: Long = resultSet.getLong("colorId")
    val modelId: Long = resultSet.getLong("modelId")
    val yearOfIssue: Date = resultSet.getDate("yearOfIssue")

    new Car(id,vin,colorId,modelId,yearOfIssue)
  }
}
