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

import com.ghjansen.parser.service.ParseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ParserApplication implements CustomCommandLineRunner {

    @Autowired
    private ParseService parseService;
    private static ConfigurableApplicationContext context;

    public static void main(String args[]){
        SpringApplication parser = new SpringApplication(ParserApplication.class);
        parser.setWebApplicationType(WebApplicationType.NONE);
        context = parser.run(args);

        /*
        JobLauncher jobLauncher = context.getBean(JobLauncher.class);
        Job job = context.getBean("logFileJob", Job.class);
        JobParameters jobParameters = new JobParametersBuilder().toJobParameters();

        JobExecution jobExecution = null;
        try {
            jobExecution = jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }
        BatchStatus batchStatus = jobExecution.getStatus();
        System.out.println("Status: " + batchStatus.getBatchStatus().name());
        */
    }

    public void run(ConfigurableApplicationContext context, String... args) throws Exception {
        parseService.execute(context, args);
        System.exit(0);
    }

}
