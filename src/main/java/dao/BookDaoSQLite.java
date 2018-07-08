package dao;

import model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDaoSQLite implements BookDao {

    private Connection connection;

    public BookDaoSQLite() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:library.db");
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBook(Book book) {
        String title = book.getTitle();
        String author = book.getAuthor();
        String sql = "INSERT INTO Book(title, author) VALUES(" +
                "'" + title + "', '" + author + "')";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Book findBook(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String title = resultSet.getString("title");
        String author = resultSet.getString("author");
        return new Book(id, title, author);
    }

    public List<Book> findBookByTitle(String title) {
        String sql = "SELECT * FROM Book WHERE title LIKE '%" + title + "%'";
        List<Book> bookListByTitle = new ArrayList<Book>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                bookListByTitle.add(findBook(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookListByTitle;
    }

    public List<Book> findBookByAuthor(String author) {
        String sql = "SELECT * FROM Book WHERE author LIKE '%" + author + "%'";
        List<Book> bookListByAuthor = new ArrayList<Book>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                bookListByAuthor.add(findBook(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookListByAuthor;
    }

    public List<Book> findAllBooks() {
        String sql = "SELECT * FROM Book";
        List<Book> allBooks = new ArrayList<Book>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                allBooks.add(findBook(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allBooks;
    }

    public void deleteBook(Book book) {
        String sql = "DELETE FROM Book WHERE id = " + book.getId();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Book(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "author TEXT NOT NULL)";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
