package repository.book;

import model.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> findAll();
    Optional<Book> findById(Long id);
    Optional<Book> findByTitle(String title);
    boolean save(Book book);

    void removeAll();

    boolean deleteBookByTitleAndAuthor(Book book);

    boolean updateStock(Long id, int newStock);

    boolean updateBook(Book book);
}
