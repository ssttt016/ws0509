package com.kbstar.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Chart {
    int month;
    String gender;
    int total;
}
