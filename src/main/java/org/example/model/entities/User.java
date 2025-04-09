package org.example.model.entities;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {
    private Integer id;
    private String name;
    private String email;

    @Builder
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
