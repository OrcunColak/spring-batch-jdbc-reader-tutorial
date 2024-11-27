package com.colak.springtutorial.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchConfig {

    @Autowired
    private final DataSource dataSource;

    // Create a job with one step. The step executes as chunk
    @Bean
    public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("sortNumbersInNaturalOrderJob", jobRepository)
                .preventRestart()
                .start(sortNumbersInNaturalOrderStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    Step sortNumbersInNaturalOrderStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("sortNumbersInNaturalOrderStep", jobRepository)
                .<Map<Integer, Integer>, Map<Integer, Integer>>chunk(10, transactionManager)
                .reader(cursorItemReader())
                .processor(new SortNumberProcessor())
                .writer(new NumberWriter(dataSource))
                .build();
    }

    @Bean
    public JdbcCursorItemReader<Map<Integer, Integer>> cursorItemReader(){
        JdbcCursorItemReader<Map<Integer, Integer>> reader = new JdbcCursorItemReader<>();

        reader.setSql("SELECT * FROM numbers");
        reader.setDataSource(dataSource);
        reader.setRowMapper(new DatabaseRowMapper());

        return reader;
    }

}
