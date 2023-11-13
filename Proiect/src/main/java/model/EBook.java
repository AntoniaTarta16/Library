package model;

import java.time.LocalDate;

public class EBook {
    private Long id;
    private String author;
    private String title;
    private LocalDate publishedDate;
    private String format;

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
    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
    @Override
    public String toString(){
        return String.format("EBook ID: %d | Author: %s | Title: %s | Published Date: %s \n", id, author, title, publishedDate);
    }

}