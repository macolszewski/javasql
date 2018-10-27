package bazydanych;

import java.sql.*;

import java.util.Map;
import java.util.Properties;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws SQLException {
        App main = new App();
        try {
            Connection connection = main.getConnection();
//            main.updateTable(connection,"author","name","Janusz","Adam");
//            main.deleteFromTable(connection,"author","name","Adam");
//            main.deleteFromTable(connection,"book","nazwa","Ciekawa książka");
            main.createAuthorTable(connection);
            main.createBookTable(connection);
//            main.insertAuthor(connection, "Kamilek", "Andrzejek");
//            main.insertBook(connection, "Ciekawa książka","12345", "Andrzejek");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Connection getConnection() throws SQLException {
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", "root");
        connectionProps.put("password", "root");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", connectionProps);
        System.out.println("CONNECTED!");
        return conn;
    }

    public void createBookTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "CREATE TABLE IF NOT EXISTS book (" +
                "book_id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT, " +
                "nazwa VARCHAR(128) NOT NULL, " +
                "ISBN VARCHAR(10) NULL, " +
                "author_id INT(11) UNSIGNED NOT NULL, " +
                "FOREIGN KEY (author_id) " +
                "REFERENCES author (author_id), " +
                "PRIMARY KEY (book_id));";
        statement.execute("drop table book;");
        statement.execute(query);
        statement.close();
    }

    public void createAuthorTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "CREATE TABLE IF NOT EXISTS author (" +
                "author_id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT, " +
                "name VARCHAR(128) NOT NULL, " +
                "surname VARCHAR(128) NOT NULL, " +
                "PRIMARY KEY (author_id));";
        statement.execute(query);
        statement.close();
    }

    public void insertAuthor(Connection connection, String name, String surname) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "INSERT into author (name, surname) values (" + "\"" + name + "\", \"" + surname + "\"" + ");";
        statement.execute(query);
        statement.close();
    }

    public void insertBook(Connection connection, String name, String ISBN, String surname) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "INSERT into book (nazwa, ISBN, author_id) values (" + "\"" + name + "\", \"" + ISBN + "\"," +
                "(SELECT author_id FROM author where surname ="+"\"" + surname+"\"));";
        statement.execute(query);
        statement.close();
    }

    public void updateTable(Connection connection, String table, String field, String search_string, String update_string) {
        String updateString = "update "+table+" set "+field+" = ? where "+ field +" = ?";
        PreparedStatement update = null;
        try {
            update = connection.prepareStatement(updateString);
            update.setString(1, update_string);
            update.setString(2, search_string);
            update.executeUpdate();
//            connection.commit();
            update.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteFromTable(Connection connection, String table, String field, String search_string) {
        String deleteString = "delete from "+table+" where "+ field +" = ?";
        PreparedStatement delete = null;
        try {
            delete = connection.prepareStatement(deleteString);
            delete.setString(1, search_string);
            delete.executeUpdate();
//            connection.commit();
            delete.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

