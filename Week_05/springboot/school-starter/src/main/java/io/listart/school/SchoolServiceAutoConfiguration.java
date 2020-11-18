package io.listart.school;

import io.listart.domain.Klass;
import io.listart.domain.School;
import io.listart.domain.Student;
import io.listart.school.properties.SchoolServiceProperties;
import io.listart.school.service.SchoolService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SchoolServiceProperties.class)
@ConditionalOnClass(SchoolService.class)
@ConditionalOnProperty(prefix = "school.leader.name", value="米小圈", matchIfMissing = true)
public class SchoolServiceAutoConfiguration {
    final private SchoolServiceProperties schoolServiceProperties;

    public SchoolServiceAutoConfiguration(SchoolServiceProperties schoolServiceProperties) {
        this.schoolServiceProperties = schoolServiceProperties;
    }

    @Bean
    public Student winnerLeader() {
        Student student = new Student();
        student.setId(schoolServiceProperties.getLeaderId());
        student.setName(schoolServiceProperties.getLeaderName());

        return student;
    }

    @Bean
    public Klass excellentKlass() {
        Klass klass = new Klass();
        klass.setLeader(winnerLeader());
        klass.setName(schoolServiceProperties.getExcellentKlassName());

        return klass;
    }

    @Bean
    public School winnerSchool() {
        School school = new School();
        school.setWonderfulKlass(excellentKlass());
        school.setName(schoolServiceProperties.getSchoolName());

        return school;
    }

    @Bean
    @ConditionalOnMissingBean(SchoolService.class)
    public SchoolService schoolService() {
        SchoolService schoolService = new SchoolService();

        schoolService.setWinnerSchool(winnerSchool());

        return schoolService;
    }
}
