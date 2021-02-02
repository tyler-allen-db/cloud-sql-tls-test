package com.example.cloudsqldemo;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

  private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public void beforeJob(JobExecution jobExecution) 
  {
      // do nothing
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info("!!! JOB FINISHED! Time to verify the results");

      jdbcTemplate.query("SELECT first_name, sex, qty FROM people",
        (rs, row) -> new Person(
          rs.getString(1),
          rs.getString(2),
          rs.getInt(3))
      ).forEach(person -> log.info("Found <" + person + "> in the database."));

      jdbcTemplate.execute("TRUNCATE TABLE people");

      // get job's start time
      Date start = jobExecution.getCreateTime();

      //  get job's end time
      Date end = jobExecution.getEndTime();

      // get diff between end time and start time
      long diff = end.getTime() - start.getTime();

      // log diff time
      log.info("Import Job Time: " + Long.toString(TimeUnit.SECONDS.convert(diff, TimeUnit.MILLISECONDS)) + " Seconds");


    }
  }
}