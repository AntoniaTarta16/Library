package repository;

import model.EBook;
import model.builder.EBookBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EBookRepositoryMySQL implements EBookRepository{
    private final Connection connection;

    public EBookRepositoryMySQL(Connection connection){
        this.connection = connection;
    }

    @Override
    public List<EBook> findAll() {

        List<EBook> eBooks = new ArrayList<>();
        return eBooks;
    }

    @Override
    public Optional<EBook> findById(Long id) {
        Optional<EBook> eBook = Optional.empty();

        return eBook;
    }

    @Override
    public boolean save(EBook book) {
        return false;
    }

    @Override
    public void removeAll() {

    }


    private EBook getBookFromResultSet(ResultSet resultSet) throws SQLException {
        return new EBookBuilder()
                .setId(resultSet.getLong("id"))
                .setAuthor(resultSet.getString("author"))
                .setTitle(resultSet.getString("title"))
                .setPublishedDate(new java.sql.Date((resultSet.getDate("publishedDate")).getTime()).toLocalDate())
                .build();
    }
}
