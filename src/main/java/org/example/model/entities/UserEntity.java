package org.example.model.entities;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class UserEntity {
    private Integer id;
    private String name;
    private String email;

}
