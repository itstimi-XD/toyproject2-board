package fastcampus.toyproject2board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Toyproject2BoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(Toyproject2BoardApplication.class, args);
    }

}
