package org.example.model.entities;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanEntity {
    private Integer id;
    private Integer bookId;
    private Integer userId;
    private LocalDate dateLoan;
    private LocalDate datePaid;

}
