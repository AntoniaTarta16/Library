package model;

import java.time.LocalDate;
import java.util.Date;
// Java Bean  gettere settere constructor gol private toate serializable
//POJO - Plain Old Java Object - nu extinde nicio clasa, nu implementeaza nicio interfata
//        si nu are nicio adnotare( nu face nimic complex)
public class Book{
    private Long id;
    private String author;

    private String title;

    private LocalDate publishedDate;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    @Override
    public String toString(){
        return String.format("Book ID: %d | Author: %s | Title: %s | Published Date: %s \n", id, author, title, publishedDate);
    }

}
