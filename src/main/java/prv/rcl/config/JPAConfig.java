package prv.rcl.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JPAConfig {

    @Bean
    @ConditionalOnMissingBean(name = "auditorAware")
    MyAuditorAwareImpl auditorAware() {
        return new MyAuditorAwareImpl();
    }

}