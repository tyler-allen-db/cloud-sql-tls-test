package com.example.cloudsqldemo;

import javax.sound.sampled.Line;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class JooqTestTask implements Tasklet{

    private static final Logger log = LoggerFactory.getLogger(JooqTestTask.class);

    @Override
    public RepeatStatus execute(StepContribution stepContribution, 
      ChunkContext chunkContext) throws Exception {
        log.info("Hello World");
        return RepeatStatus.FINISHED;
    }
    
}
