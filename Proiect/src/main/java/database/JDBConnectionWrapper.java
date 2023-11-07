package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBConnectionWrapper {
    private static final String JDBC_DRIVER= "com.mysql.cj.jdbc.Driver";
    private static final String  DB_URL= "jdbc:mysql://localhost/";
    private static final String USER="root";
    private static final String PASSWORD="facultate123";
    private static final int TIMEOUT=5;

    private Connection connection;

    public JDBConnectionWrapper(String schema){
        try {
            Class.forName(JDBC_DRIVER);
            connection= DriverManager.getConnection(DB_URL+ schema, USER, PASSWORD);
            createTable();
        }
        catch (ClassNotFoundException | SQLException e ) {
            e.printStackTrace();
        }
    }

    public boolean TestConnection() throws SQLException {
        return connection.isValid(TIMEOUT);
    }

    public Connection getConnection(){
        return connection;
    }

    private void createTable() throws SQLException{
        Statement statement= connection.createStatement();

        String sql="CREATE TABLE IF NOT EXISTS book(" +
                "id bigint NOT NULL AUTO_INCREMENT,"+
                "author varchar(500) NOT NULL," +
                "title varchar(500) NOT NULL," +
                "publishDate datetime DEFAULT NULL,"+
                "PRIMARY KEY(id)," +
                "UNIQUE KEY id_UNIQUE(id)" +
                ") ENGINE= InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;";

        statement.execute(sql);
    }


}