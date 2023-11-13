package model.builder;

import model.EBook;

import java.time.LocalDate;

public class EBookBuilder {
    private EBook eBook;
    public EBookBuilder(){
        eBook = new EBook();
    }

    public EBookBuilder setId(Long id){
        eBook.setId(id);
        return this;
    }

    public EBookBuilder setAuthor(String author){
        eBook.setAuthor(author);
        return this;
    }
    public EBookBuilder setTitle(String title){
        eBook.setTitle(title);
        return this;
    }

    public EBookBuilder setPublishedDate(LocalDate publishedDate){
        eBook.setPublishedDate(publishedDate);
        return this;
    }

    public EBook build(){
        return eBook;
    }

}
