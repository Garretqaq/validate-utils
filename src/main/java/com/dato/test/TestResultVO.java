package com.dato.test;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TestResultVO {
    private String name;

    private long phone;

    private String company;
}
