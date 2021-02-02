package com.example.cloudsqldemo;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

  @Autowired
  public JobBuilderFactory jobBuilderFactory;

  @Autowired
  public StepBuilderFactory stepBuilderFactory;

  @Bean
  public JooqTestTask jooqTestTask() {
      return new JooqTestTask();
  }

  @Bean
  public FlatFileItemReader<Person> reader() {
    return new FlatFileItemReaderBuilder<Person>()
      .name("personItemReader")
      .resource(new ClassPathResource("yob2019.csv"))
      .delimited()
      .names(new String[]{"firstName", "sex", "qty"})
      .fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
        setTargetType(Person.class);
      }})
      .build();
  }

  @Bean
  public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<Person>()
      .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
      .sql("INSERT INTO people (first_name, sex, qty) VALUES (:firstName, :sex, :qty)")
      .dataSource(dataSource)
      .build();
  }

  @Bean
  public Job importUserJob(JobCompletionNotificationListener listener, Step writePsqlWithJDBC, Step testJooq) {
    return jobBuilderFactory.get("importUserJob")
      .incrementer(new RunIdIncrementer())
      .listener(listener)
      .start(writePsqlWithJDBC)
      .next(testJooq)
      .build();
  }

  @Bean
  public Step writePsqlWithJDBC(JdbcBatchItemWriter<Person> writer) {
    return stepBuilderFactory.get("writePsqlWithJDBC")
      .<Person, Person> chunk(10)
      .reader(reader())
      .writer(writer)
      .build();
  }

  @Bean
  public Step testJooq(){
    return stepBuilderFactory.get("testJooq")
    .tasklet(jooqTestTask())
    .build();
  }


}