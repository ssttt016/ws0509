package com.kbstar.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Msg {
    private String sendid;
    private String receiveid;
    private String content1;
}
