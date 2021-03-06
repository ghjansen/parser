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

package com.ghjansen.parser.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JobNotificationListener extends JobExecutionListenerSupport {

    private static Logger logger = LoggerFactory.getLogger(JobNotificationListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        super.beforeJob(jobExecution);
        logger.info("Parsing file ...");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        super.afterJob(jobExecution);
        Date start = jobExecution.getStartTime();
        Date finish = jobExecution.getEndTime();
        long time = ChronoUnit.MILLIS.between(start.toInstant(), finish.toInstant());
        logger.info("Finished parsing file (time = " + time + "ms; " +
                "writeCount = "+jobExecution.getStepExecutions().iterator().next().getWriteCount()+"; " +
                "exitStatus = "+jobExecution.getExitStatus().getExitCode() + ")" );

    }
}
