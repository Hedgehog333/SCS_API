package dao

import java.sql.{Date, ResultSet}
import models.User
import scala.util.{Failure, Success, Try}

object Users extends DAOJdbc[User] with DAO[User]{
  override def create(obj: User): Try[Long] = {
    var data: Try[Long] = Try(throw new Exception("Error with insert User"))
    withDatabase{ database =>
      val connection = database.getConnection()
      val sql: String = "INSERT INTO `users` " +
        "(`fname`, `sname`, `lname`, `cityId`, `email`, `password`, `birthday`, `registrationDate`, `phoneNumber`) VALUES" +
        "(?,?,?,?,?,?,?,?,?)"
      val stmt = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)
      stmt.setString(1,obj.fname)
      stmt.setString(2,obj.sname)
      stmt.setString(3,obj.lname)
      stmt.setLong(4, obj.cityId)
      stmt.setString(5,obj.email)
      stmt.setString(6,obj.password)
      stmt.setDate(7,obj.birthday)
      stmt.setDate(8,obj.registrationDate)
      stmt.setString(9,obj.phoneNumber)

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

  override def getById(id: Long): Try[User] = {
    var user: Try[User] = Try(throw new Exception(s"User id=$id Not Found"))
    withDatabase{ database =>
      val connection = database.getConnection()
      val sql: String = "select * from users where id = " + id.toString
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

  def getByEmailAndPassword(obj: dto.UserLogin): Try[User] = {
    var user: Try[User] = Try(throw new Exception("User Not Found"))
    withDatabase{ database =>
      val connection = database.getConnection()
      val sql: String = "select * from users where email like ? and password=?"
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

  override def getAll(): Try[Set[User]] = {
    var set: Set[User] = Set()
    withDatabase { database =>
      val connection = database.getConnection()
      val sql: String = "select * from users"

      val stmt = connection.prepareStatement(sql)
      val rs: ResultSet = stmt.executeQuery

      while (rs.next()) {
        set += maker(rs)
      }
    }

    Try(set)
  }

  override def update(obj: User): Try[Boolean] = {
    var data: Try[Boolean] = Try(throw new Exception("Error with update User"))
    withDatabase{ database =>
      val connection = database.getConnection()
      val sql: String = "update users set `fname`=?, `sname`=?, `lname`=?, " +
        "`cityId`=?, `email`=?, `password`=?, " +
        "`birthday`=?, `registrationDate`=?, `phoneNumber`=? " +
        "WHERE `id`=" + obj.id

      val stmt = connection.prepareStatement(sql)
      stmt.setString(1, obj.fname)
      stmt.setString(2, obj.sname)
      stmt.setString(3, obj.lname)
      stmt.setLong(4, obj.cityId)
      stmt.setString(5, obj.email)
      stmt.setString(6, obj.password)
      stmt.setDate(7, obj.birthday)
      stmt.setDate(8, obj.registrationDate)
      stmt.setString(9, obj.phoneNumber)

      val result: Try[Int] = Try(stmt.executeUpdate)
      result match {
        case Success(v) => data = Try(v.equals(1))
        case Failure(e) => println(e.getMessage)
      }
    }

    data
  }

  override def delete(id: Long): Try[Boolean] = {
    var data: Try[Boolean] = Try(throw new Exception("Error with delete User"))
    withDatabase{ database =>
      val sql: String = "delete from users where id = ?"
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

  override protected[dao] def maker(resultSet: ResultSet): User = {
    val id: Long = resultSet.getLong("id")
    val fname: String = resultSet.getString("fname")
    val sname: String = resultSet.getString("sname")
    val lname: String = resultSet.getString("lname")
    val email: String = resultSet.getString("email")
    val password: String = resultSet.getString("password")
    val cityId: Long = resultSet.getLong("cityId")
    val birthday: Date = resultSet.getDate("birthday")
    val registrationDate: Date = resultSet.getDate("registrationDate")
    val phoneNumber: String = resultSet.getString("phoneNumber")

    new User(id,fname,sname,lname,email,password, cityId, birthday, registrationDate, phoneNumber)
  }
}