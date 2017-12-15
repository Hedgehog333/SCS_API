package dao

import java.sql.ResultSet

import play.api.db.{Database, Databases}

trait DAOJdbc[T] extends DAO[T]{
  protected[dao] def maker(resultSet: ResultSet): T
  protected[DAOJdbc] def withDatabase[T](block: Database => T): Unit = {
    Databases.withDatabase(
      driver = "com.mysql.jdbc.Driver",
      url = "jdbc:mysql://localhost:3306/smartCarStat",

      config = Map(
        "username" -> "root",
        "password" -> "root"
      )
    )(block)
  }
}