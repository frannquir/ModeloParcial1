package org.example.model.entities;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    private Integer id;
    private Integer bookId;
    private Integer userId;
    private LocalDate dateLoan;
    private LocalDate datePaid;

    @Builder
    public Loan (Integer bookId, Integer userId, LocalDate datePaid){
        this.bookId = bookId;
        this.userId = userId;
        this.datePaid = datePaid;
    }
}
