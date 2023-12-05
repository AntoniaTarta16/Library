package service.book;

import model.Book;

import java.util.List;

public interface BookService {
    List<Book> findAll();
    Book findById(Long id);
    Book findByTitle(String title);
    boolean save(Book book);
    int getAgeOfBook(Long id);

    boolean updateStock(Long id, int newStock);
    boolean updateBook(Book book);

    boolean deleteBookByTitleAndAuthor(Book book);

}
