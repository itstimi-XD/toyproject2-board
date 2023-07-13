package fastcampus.toyproject2board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class Toyproject2BoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(Toyproject2BoardApplication.class, args);
    }

}
