package com.example.apac.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String email;
    private String name;
    private String provider;
}
