package ru.otus.spring.course.data;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class Line {
    private String question;
    private List<String> options;
    private int answer;
}
