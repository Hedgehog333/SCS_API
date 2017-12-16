package dao

import java.sql.ResultSet

import models.Spare

import scala.util.{Failure, Success, Try}

object Spares extends DAOJdbc[Spare] with DAO[Spare] {
  override def create(obj: Spare): Try[Long] = {
    var data: Try[Long] = Try(throw new Exception("Error with insert Spare"))
    withDatabase{ database =>
      val connection = database.getConnection()
      val sql: String = "INSERT INTO `spares` " +
        "(`vin`, `typeSpareId`, `name`, `description`) VALUES" +
        "(?,?,?,?)"
      val stmt = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)
      stmt.setString(1,obj.vin)
      stmt.setLong(2,obj.typeSpareId)
      stmt.setString(3, obj.name)
      stmt.setString(4,obj.description)

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

  override def getById(id: Long): Try[Spare] = {
    var spare: Try[Spare] = Try(throw new Exception(s"Spare id=$id Not Found"))
    withDatabase{ database =>
      val connection = database.getConnection()
      val sql: String = "select * from spares where id = " + id.toString
      val stmt = connection.prepareStatement(sql)

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

  override def getAll(): Try[Set[Spare]] = {
    var set: Set[Spare] = Set()
    withDatabase { database =>
      val connection = database.getConnection()
      val sql: String = "select * from spares"

      val stmt = connection.prepareStatement(sql)
      val rs: ResultSet = stmt.executeQuery

      while (rs.next()) {
        set += maker(rs)
      }
    }

    Try(set)
  }

  override def update(obj: Spare): Try[Boolean] = {
    var data: Try[Boolean] = Try(throw new Exception("Error with update Spare"))
    withDatabase{ database =>
      val connection = database.getConnection()
      val sql: String = "update spares set `vin`=?, `typeSpareId`=?, `name`=?, `description`=?" +
        "WHERE `id`=" + obj.id

      val stmt = connection.prepareStatement(sql)
      stmt.setString(1, obj.vin)
      stmt.setLong(2, obj.typeSpareId)
      stmt.setString(3, obj.name)
      stmt.setString(4, obj.description)


      val result: Try[Int] = Try(stmt.executeUpdate)
      result match {
        case Success(v) => data = Try(v.equals(1))
        case Failure(e) => println(e.getMessage)
      }
    }

    data
  }

  override def delete(id: Long): Try[Boolean] = {
    var data: Try[Boolean] = Try(throw new Exception("Error with delete Spare"))
    withDatabase{ database =>
      val sql: String = "delete from spares where id = ?"
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

  override protected[dao] def maker(resultSet: ResultSet): Spare = {
    val id = resultSet.getLong("id")
    val vin = resultSet.getString("vin")
    val typeSpareId = resultSet.getLong("typeSpareId")
    val name = resultSet.getString("name")
    val descriprion = resultSet.getString("description")

    new Spare(id, vin, typeSpareId, name, descriprion)
  }
}