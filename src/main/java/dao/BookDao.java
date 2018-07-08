package dao;

import model.Book;

import java.util.List;

public interface BookDao {
    void addBook(Book book);
    List<Book> findBookByTitle(String tile);
    List<Book> findBookByAuthor(String author);
    List<Book> findAllBooks();
    void deleteBook(Book book);
}
