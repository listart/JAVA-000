package io.listart.spring.ioc.domain;

import lombok.Data;

import java.beans.ConstructorProperties;

@Data
public class Student {
    private int id;
    private String name;

    public Student() {
        id = 1908000;
        name = "米小圈";
    }

    // constructor-based assembled bean with constructor-arg byName 注解示例
    @ConstructorProperties({"id", "name"})
    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // static factory method assembled bean 示例
    public static Student createInstance(int id, String name) {
        return new Student(id, name);
    }
}
