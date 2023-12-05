package repository.book;

import model.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMock implements BookRepository{

    private List<Book> books;

    public BookRepositoryMock(){
        books = new ArrayList<>();
    }

    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return books.parallelStream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Book> findByTitle(String title) {
        return books.parallelStream()
                .filter(it -> it.getTitle().equals(title))
                .findFirst();
    }

    @Override
    public boolean save(Book book) {
        return books.add(book);
    }

    @Override
    public boolean updateStock(Long id, int newStock) {
        return true;
    }

    @Override
    public boolean updateBook(Book book) {
        return true;
    }

    @Override
    public boolean deleteBookByTitleAndAuthor(Book book){
        return true;
    }
    @Override
    public void removeAll() {
        books.clear();
    }
}