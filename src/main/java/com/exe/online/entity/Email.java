package com.exe.online.entity;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Email {
    private String to;
    private String subject;
    private String massage;
    private String file;
}
