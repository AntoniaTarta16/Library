import model.Book;
import model.builder.BookBuilder;
import org.junit.jupiter.api.Test;
import repository.BookRepository;
import repository.BookRepositoryMock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookRepositoryMockTest {

    BookRepository bookRepository = new BookRepositoryMock();

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

    @Test
    void testFindById() {
        Book newBook = new BookBuilder()
                .setAuthor("Author3")
                .setTitle("Title3")
                .setPublishedDate(LocalDate.of(2010, 6, 2))
                .setId(Long.valueOf(3))
                .build();

        bookRepository.save(newBook);

        Optional<Book> book = bookRepository.findById(Long.valueOf(3));
        assertEquals(book.get().getAuthor(), "Author3");
    }
}