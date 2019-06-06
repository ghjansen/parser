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

package com.ghjansen.parser;

import com.ghjansen.parser.batch.JobCompletionNotificationListener;
import com.ghjansen.parser.batch.LogProcessor;
import com.ghjansen.parser.persistence.dto.LogDTO;
import com.ghjansen.parser.persistence.model.Log;
import com.ghjansen.parser.persistence.repository.LogRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.DefaultCrudMethods;
import org.springframework.data.repository.core.support.DefaultRepositoryMetadata;

@Configuration
@EnableBatchProcessing
public class ParserConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
/*
    @Bean
    @StepScope
    public FlatFileItemReader<LogDTO> logFileReader(){
        FlatFileItemReader<LogDTO> reader = new FlatFileItemReader();
        //TODO set next line at runtime!
        reader.setResource(new ClassPathResource("access.log"));
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

    @Bean
    @StepScope
    public ItemProcessor<LogDTO,Log> logFileProcessor(){
        return new LogProcessor();
    }

    @Bean
    public RepositoryMetadata repositoryMetadata(){
        return new DefaultRepositoryMetadata(LogRepository.class);
    }

    @Bean
    @StepScope
    public RepositoryItemWriter<Log> logFileWriter(LogRepository logRepository){
        DefaultCrudMethods defaultCrudMethods = new DefaultCrudMethods(repositoryMetadata());
        RepositoryItemWriter<Log> writer = new RepositoryItemWriter<>();
        writer.setRepository(logRepository);
        writer.setMethodName(defaultCrudMethods.getSaveMethod().get().getName());
        return writer;
    }

    @Bean
    public Step logFileStep(LogRepository logRepository){
        return stepBuilderFactory.get("logFileStep")
                .<LogDTO,Log>chunk(1000)
                .reader(logFileReader())
                .processor(logFileProcessor())
                .writer(logFileWriter(logRepository))
                .build();
    }

    @Bean
    public Job logFileJob(JobCompletionNotificationListener listener, LogRepository logRepository){
        //TODO append md5 to job name so it enforces a new unique run
        return jobBuilderFactory.get("logFileJob-" + System.currentTimeMillis())
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(logFileStep(logRepository))
                .end()
                .build();
    }
    */
}
