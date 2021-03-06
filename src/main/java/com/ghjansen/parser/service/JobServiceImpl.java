/*
 * PARSER - Text file parser and explorer
 * Copyright (C) 2019  Guilherme Humberto Jansen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.ghjansen.parser.service;

import com.ghjansen.parser.batch.JobNotificationListener;
import com.ghjansen.parser.batch.LogProcessor;
import com.ghjansen.parser.persistence.dto.LogDTO;
import com.ghjansen.parser.persistence.model.File;
import com.ghjansen.parser.persistence.model.Log;
import com.ghjansen.parser.persistence.repository.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.repository.core.support.DefaultCrudMethods;
import org.springframework.data.repository.core.support.DefaultRepositoryMetadata;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class JobServiceImpl implements JobService {

    private static Logger logger = LoggerFactory.getLogger(JobService.class);
    private LogRepository logRepository;
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private JobLauncher jobLauncher;
    private JobNotificationListener listener;
    @Value("${parser.job.execution.constraint.md5:true}")
    private boolean constraintMD5;

    public JobServiceImpl(LogRepository logRepository, JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, JobLauncher jobLauncher, JobNotificationListener listener) {
        this.logRepository = logRepository;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.jobLauncher = jobLauncher;
        this.listener = listener;
    }

    @Override
    public void executeParseJob(@NotNull File file) {
        Job job = createJob(file);
        JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
        JobExecution jobExecution = null;
        try{
            jobExecution = jobLauncher.run(job, jobParameters);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private Job createJob(File file){
        FlatFileItemReader<LogDTO> reader = createReader(file.getFilePath());
        ItemProcessor<LogDTO,Log> processor = new LogProcessor();
        RepositoryItemWriter<Log> writer = createWriter();
        Step step = createStep(reader, processor, writer);
        String jobName = createJobName(file.getMd5());
        return jobBuilderFactory.get(jobName)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step)
                .end()
                .build();
    }

    private FlatFileItemReader<LogDTO> createReader(String filePath){
        FlatFileItemReader<LogDTO> reader = new FlatFileItemReader();
        reader.setResource(new FileSystemResource(filePath));
        reader.setLineMapper(new DefaultLineMapper<LogDTO>() {{
            DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
            lineTokenizer.setDelimiter("|");
            lineTokenizer.setNames(new String[]{"date", "ip", "request", "status", "userAgent"});
            setLineTokenizer(lineTokenizer);
            setFieldSetMapper(new BeanWrapperFieldSetMapper<LogDTO>(){{
                setTargetType(LogDTO.class);
            }});
        }});
        return reader;
    }

    private RepositoryItemWriter<Log> createWriter(){
        DefaultRepositoryMetadata repositoryMetadata = new DefaultRepositoryMetadata(LogRepository.class);
        DefaultCrudMethods defaultCrudMethods = new DefaultCrudMethods(repositoryMetadata);
        RepositoryItemWriter<Log> writer = new RepositoryItemWriter<>();
        writer.setRepository(logRepository);
        writer.setMethodName(defaultCrudMethods.getSaveMethod().get().getName());
        return writer;
    }

    private Step createStep(FlatFileItemReader<LogDTO> reader, ItemProcessor<LogDTO,Log> processor, RepositoryItemWriter<Log> writer){
        return stepBuilderFactory.get("logFileStep")
                .<LogDTO,Log>chunk(1000)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    private String createJobName(String md5){
        String name = "logFileJob-" + md5;
        if(!constraintMD5){
            name += "-" + System.currentTimeMillis();
        }
        return name;
    }

}
