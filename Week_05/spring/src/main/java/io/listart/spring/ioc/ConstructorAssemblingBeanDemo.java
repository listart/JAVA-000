package io.listart.spring.ioc;

import io.listart.spring.ioc.domain.Student;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

public class ConstructorAssemblingBeanDemo {
    public static void main(String[] args) {
        ListableBeanFactory listableBeanFactory = new ClassPathXmlApplicationContext("classpath:META-INF/constructor-assembling-context.xml");

        Map<String, Student> students = listableBeanFactory.getBeansOfType(Student.class);

        System.out.println(students);
    }
}
