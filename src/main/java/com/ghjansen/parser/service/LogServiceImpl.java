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

import com.ghjansen.parser.persistence.model.Log;
import com.ghjansen.parser.persistence.repository.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
public class LogServiceImpl implements LogService {

    private static Logger logger = LoggerFactory.getLogger(LogService.class);
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss z");
    private LogRepository logRepository;
    @Value("${parser.search.duration.limit.hourly:200}")
    private Long hourlyLimit;
    @Value("${parser.search.duration.limit.daily:500}")
    private Long dailyLimit;

    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public @NotNull Log save(Log log) {
        return this.logRepository.save(log);
    }

    @Override
    public @NotNull Log create(ZonedDateTime date, String ip, String request, Long status, String userAgent) {
        Log log = new Log();
        log.setDate(date);
        log.setIp(ip);
        log.setRequest(request);
        log.setStatus(status);
        log.setUserAgent(userAgent);
        return this.save(log);
    }

    @Override
    public void searchDatabase(@NotNull @NotEmpty String startDate, @NotNull @NotEmpty String duration, @NotNull @NotEmpty String threshold) {
        ZonedDateTime sd = validateStartDate(startDate);
        Long t = validateThreshold(threshold);
        ZonedDateTime ed = null;
        if (duration.toLowerCase().equals("hourly")) {
            if (t > hourlyLimit) t = hourlyLimit;
            ed = sd.plusHours(1);
        } else if (duration.toLowerCase().equals("daily")) {
            if (t > dailyLimit) t = dailyLimit;
            ed = sd.plusDays(1);
        } else {
            logger.error("Invalid duration '" + duration + "' (supported values are 'hourly' and 'daily')");
            System.exit(1);
        }
        logger.info("Searching database ...");
        Iterable<String> result = this.logRepository.findIpsByDateThreshold(sd, ed, t);
        if (result.iterator().hasNext()) {
            logger.info("The following result match with the search criteria:");
            logger.info("----------------------------------------------------");
            for (String s : result) {
                logger.info(s);
            }
        } else {
            logger.info("No result found matching with the search criteria");
        }
    }

    private Long validateThreshold(String threshold){
        Long t = 0L;
        try{
            t = Long.valueOf(threshold);
            if(t < 1){
                throw new Exception();
            }
        } catch (Exception e){
            logger.error("Invalid threshold '" + threshold + "' (must be a integer number greater than zero)");
            System.exit(1);
        }
        return t;
    }

    private ZonedDateTime validateStartDate(String startDate){
        ZonedDateTime sd = null;
        try {
            sd = ZonedDateTime.parse(startDate + " UTC", formatter);
        } catch (Exception e) {
            logger.error("Invalid startDate '" + startDate + "' (startDate format must be 'yyyy-MM-dd.HH:mm:ss')");
            System.exit(1);
        }
        return sd;
    }

}
