package app.groopy.userservice.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("app.groopy.providers.firebase")
public class FirebaseConfiguration {
}
