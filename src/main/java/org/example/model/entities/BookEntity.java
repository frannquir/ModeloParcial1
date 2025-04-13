package org.example.model.entities;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class BookEntity {
    private Integer id;
    private String title;
    private String author;
    private Integer year;
    private Integer stock;

}
