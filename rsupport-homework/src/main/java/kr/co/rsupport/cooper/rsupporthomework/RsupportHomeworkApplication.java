package kr.co.rsupport.cooper.rsupporthomework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RsupportHomeworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(RsupportHomeworkApplication.class, args);
	}

}
