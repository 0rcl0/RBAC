package prv.rcl.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

//@Configuration
//@EnableJpaAuditing
public class JPAConfig {

    /**
     * 注入 动态获取 modify people modify time
     *  JPA 根据 {@link org.springframework.data.jpa.repository.Modifying} 字段更新
     * @return 编辑注入
     */
    @Bean
    @ConditionalOnMissingBean(name = "auditorAware")
    MyAuditorAwareImpl auditorAware() {
        return new MyAuditorAwareImpl();
    }

}
