package com.food.delivery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration;
import org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EntityScan({"com.food.delivery.domain"})
@EnableJpaRepositories(basePackages = {"com.food.delivery.repository"})
@EnableAutoConfiguration(exclude = {ContextInstanceDataAutoConfiguration.class, ContextStackAutoConfiguration.class})

@SpringBootApplication
@EnableJpaAuditing
public class FoodDeliveryApplication
{
    @Value("${spring.food.delivery.pool.size:100}")
    private int poolSize;
    @Value("${spring.food.delivery.max.pool.size:200}")
    private int maxPoolSize;
    @Value("${spring.food.delivery.queue.capacity:300}")
    private int queueCapacity;


    public static void main(String[] args) {
        SpringApplication.run(FoodDeliveryApplication.class, args);
    }

    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(poolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setThreadNamePrefix("threadPoolTaskExecutor-");
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

}
