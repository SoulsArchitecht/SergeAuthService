package ru.sshibko.sas.model;

import lombok.*;
import ru.sshibko.sas.entity.RoleType;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {

    private String username;
    private String email;
    private Set<RoleType> roles;
    private String password;
}
