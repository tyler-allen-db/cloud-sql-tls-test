package com.example.cloudsqldemo;

import java.sql.DriverManager;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import java.sql.Connection;

public class JooqTestTask implements Tasklet{

  @Value( "${spring.datasource.url}" )
  private String url;

  @Value( "${spring.datasource.username}" )
  private String userName;

  @Value( "${spring.datasource.password}" )
  private String password;

    private static final Logger log = LoggerFactory.getLogger(JooqTestTask.class);

    @Override
    public RepeatStatus execute(StepContribution stepContribution, 
      ChunkContext chunkContext) throws Exception {

        log.info("Sleep 10 sec");
        Thread.sleep(10000);

        Connection conn = DriverManager.getConnection(url, userName, password);
        DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);
        
        log.info("Writing to PosgreSQL via JOOQ");
        createValues(context);
        
        log.info("Closing JOOQ DB connection");
        conn.close();

        return RepeatStatus.FINISHED;
    }

    private void createValues(DSLContext context){
      context.query("INSERT INTO people (first_name, sex, qty) VALUES ('Test1', '', 1)").execute();
      context.query("INSERT INTO people (first_name, sex, qty) VALUES ('Test2', '', 2)").execute();
      context.query("INSERT INTO people (first_name, sex, qty) VALUES ('Test3', '', 3)").execute();
      context.query("INSERT INTO people (first_name, sex, qty) VALUES ('Test4', '', 4)").execute();
      context.query("INSERT INTO people (first_name, sex, qty) VALUES ('test5', '', 5)").execute();
    }
    
}
