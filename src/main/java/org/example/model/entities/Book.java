package org.example.model.entities;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Book {
    private Integer id;
    private String title;
    private String author;
    private Integer year;
    private Integer stock;

    @Builder
    public Book (String title, String author, Integer year, Integer stock) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.stock = stock;
    }
}
