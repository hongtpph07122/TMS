package com.tms.api;

import java.util.concurrent.Executor;

import com.tms.api.task.CDRLogWriter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@ComponentScan(basePackages = {"com.tms"})
public class PowApplication {

	public static void main(String[] args) {
		SpringApplication.run(PowApplication.class, args);
	}

	@Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(6);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("LogWriter-");
        executor.initialize();
        return executor;
    }

  /*  @Bean
    @ConditionalOnProperty(value = "config.run-rate", matchIfMissing = true, havingValue = "true")
    public AgentRateJob scheduledJob() {
        return new AgentRateJob();
    }*/
   /* @Bean
    @ConditionalOnProperty(value = "config.run-basket", matchIfMissing = true, havingValue = "true")
    public BasketProcessor basketProcessor() {
        return new BasketProcessor();
    }*/

//    @Bean
//    @ConditionalOnProperty(value = "config.run-enddate", matchIfMissing = true, havingValue = "true")
//    public EndDateJob endDateJob() {
//        return new EndDateJob();
//    }
}
