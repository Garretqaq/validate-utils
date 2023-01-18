package com.dato.test;

import com.dato.annotion.constraint.NotNull;
import com.dato.annotion.constraint.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TestParam {

    @NotNull
    private String a;

    @Size(max = 3)
    private List<Integer> list;
}
