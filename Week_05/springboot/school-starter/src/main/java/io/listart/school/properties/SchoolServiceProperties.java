package io.listart.school.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@Data
@ConfigurationProperties(prefix = "school")
public class SchoolServiceProperties {
    private static final String SCHOOL_NAME = "school";
    private static final String EXCELLENT_KLASS_NAME = "klass";
    private static final int LEADER_ID = 180900;
    private static final String LEADER_NAME = "leader";

    private String schoolName = SCHOOL_NAME;
    private String excellentKlassName = EXCELLENT_KLASS_NAME;
    private int leaderId = LEADER_ID;
    private String leaderName = LEADER_NAME;
}
