package rarlog.me.MusicPlay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("rarlog.me.entity")
@EnableJpaRepositories("rarlog.me.repository")
public class MusicPlayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicPlayApplication.class, args);
	}

}
