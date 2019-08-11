package com.histcat.parameterized.test;

public class Book {
    private String title;

//    public static Book of(String title) {
//        Book book = new Book();
//        book.title = title;
//        return book;
//    }
    public Book(String title) {
        this.title = title;
    }

    String getTitle() {
        return this.title;
    }
}
