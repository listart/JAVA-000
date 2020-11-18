package io.listart;

import io.listart.school.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SchoolApplication {
    @Autowired
    SchoolService schoolService;

    @RequestMapping("/")
    public String index() {
        return schoolService.getWinnerSchool().toString();
    }

    public static void main(String[] args) {
        SpringApplication.run(SchoolApplication.class, args);
    }
}
