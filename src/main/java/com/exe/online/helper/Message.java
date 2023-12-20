package com.exe.online.helper;

import lombok.*;
import org.hibernate.annotations.SecondaryRow;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String content;
    private String typeOfMessage;


}
