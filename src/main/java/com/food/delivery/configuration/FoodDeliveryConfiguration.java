package com.food.delivery.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.food.delivery.repository.FoodDeliveryRepository;
import com.food.delivery.schedule.FoodDeliverySchedule;
import com.food.delivery.schedule.FoodDeliveryTicketScheduler;
import com.food.delivery.schedule.SqsFoodDeliveryTicketScheduler;
import com.food.delivery.service.MonitoringExecutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@RequiredArgsConstructor
@EnableScheduling

public class FoodDeliveryConfiguration
{
    private final FoodDeliveryRepository foodDeliveryRepository;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final MonitoringExecutorService monitoringExecutorService;

    @Value("${spring.food.delivery.batch.page.size:1000}")
    public int batchPageSize;

    @Value("${cloud.aws.credentials.access-key:''}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key:''}")
    private String secretKey;

    @Value("${cloud.aws.region:us-east-1}")
    private String region;

    @Value("${food.delivery.monitoring.queue:dev-food-delivery}")
    private String queueName;

    @Bean
    @ConditionalOnProperty(value="spring.food.delivery.priority.run.job", havingValue = "spring", matchIfMissing = true)
    public FoodDeliverySchedule foodDeliveryTicketScheduler()
    {
        return new FoodDeliveryTicketScheduler(foodDeliveryRepository, threadPoolTaskExecutor, monitoringExecutorService,batchPageSize);
    }

    @Bean
    @ConditionalOnProperty(value="spring.food.delivery.priority.run.job", havingValue = "sqs")
    public FoodDeliverySchedule foodDeliverySqsTicketScheduler()
    {
        return new SqsFoodDeliveryTicketScheduler(foodDeliveryRepository, amazonSQSAsync(), batchPageSize,queueName);
    }

    @Bean
    @Primary
    @ConditionalOnProperty(value="spring.food.delivery.priority.run.job", havingValue = "sqs")
    public AmazonSQSAsync amazonSQSAsync() {

        AmazonSQSAsyncClientBuilder amazonSQSAsyncClientBuilder = AmazonSQSAsyncClientBuilder.standard();
        amazonSQSAsyncClientBuilder.withRegion(Regions.fromName(region));
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
        amazonSQSAsyncClientBuilder.withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials));
        return amazonSQSAsyncClientBuilder.build();

    }
}
