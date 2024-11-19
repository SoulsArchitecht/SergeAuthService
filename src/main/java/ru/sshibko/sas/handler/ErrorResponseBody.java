package ru.sshibko.sas.handler;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseBody {

    private String message;
    private String description;
}
