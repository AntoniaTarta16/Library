import database.JDBConnectionWrapper;
import model.builder.BookBuilder;
import org.junit.jupiter.api.Test;
import repository.BookRepository;
import repository.BookRepositoryMySQL;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookRepositoryMySQLTest {


    JDBConnectionWrapper connectionWrapper = new JDBConnectionWrapper("test_library");

    BookRepository bookRepository = new BookRepositoryMySQL(connectionWrapper.getConnection());


    @Test
    void testFindAll() {

        bookRepository.removeAll();

        Book book = new BookBuilder()
                .setAuthor("Author1")
                .setTitle("Title1")
                .setPublishedDate(LocalDate.of(2010, 6, 2))
                .build();

        bookRepository.save(book);

        Book book2 = new BookBuilder()
                .setAuthor("Author2")
                .setTitle("Title2")
                .setPublishedDate(LocalDate.of(2008, 8, 16))
                .build();

        bookRepository.save(book2);

        List<Book> books = bookRepository.findAll();
        assertEquals(books.size(), 2);
    }

}
