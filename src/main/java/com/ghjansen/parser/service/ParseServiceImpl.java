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

import com.ghjansen.parser.persistence.model.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParseServiceImpl implements ParseService {

    private static Logger logger = LoggerFactory.getLogger(ParseService.class);
    private boolean file = true;
    private String accessLogArg;
    private String startDateArg;
    private String durationArg;
    private String thresholdArg;
    private FileService fileService;
    private JobService jobService;

    public ParseServiceImpl(FileService fileService, JobService jobService) {
        this.fileService = fileService;
        this.jobService = jobService;
    }

    public void execute(String args[]){
        prepareArguments(args);
        parse();
    }

    private void prepareArguments(String args[]){
        getArguments(args);
        checkArguments();
    }

    private void getArguments(String args[]){
        for(String a : args){
            String i[] = a.split("=");
            if(i.length > 1){
                if(String.valueOf(i[0]).contains("accessLog")){
                    this.accessLogArg = String.valueOf(i[1]);
                } else if (String.valueOf(i[0]).contains("startDate")) {
                    this.startDateArg = String.valueOf(i[1]);
                } else if (String.valueOf(i[0]).contains("duration")) {
                    this.durationArg = String.valueOf(i[1]);
                } else if (String.valueOf(i[0]).contains("threshold")) {
                    this.thresholdArg = String.valueOf(i[1]);
                }
            }
        }
    }

    private void checkArguments(){
        boolean fail = false;
        if((accessLogArg == null || String.valueOf(accessLogArg).isEmpty())){
            logger.warn("Argument 'accessLog' not found (required at least for the first run)"); file = false;
        }
        if((startDateArg == null || String.valueOf(startDateArg).isEmpty())){
            logger.error("Argument 'startDate' is required"); fail = true;
        }
        if((durationArg == null || String.valueOf(durationArg).isEmpty())){
            logger.error("Argument 'duration' is required"); fail = true;
        }
        if((thresholdArg == null || String.valueOf(thresholdArg).isEmpty())){
            logger.error("Argument 'threshold' is required"); fail = true;
        }
        if(fail) System.exit(1);
    }

    private void parse(){
        if(file){
            File file = this.fileService.processFile(accessLogArg);
            if(file != null){
                executeParseJob(file);
            } else {
                logger.info("Parse job skipped");
            }
        } else {
            logger.warn("No file informed, parse job skipped, using existing database");
            if(this.fileService.isEmptyDatabase()){
                logger.error("No file was parsed before, the database is empty");
                System.exit(1);
            }
        }
    }

    private void executeParseJob(File file){
        this.jobService.executeParseJob(file);
    }

}
