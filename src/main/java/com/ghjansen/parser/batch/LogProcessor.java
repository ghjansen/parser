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

import com.ghjansen.parser.persistence.dto.LogDTO;
import com.ghjansen.parser.persistence.model.Log;
import org.springframework.batch.item.ItemProcessor;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class LogProcessor implements ItemProcessor<LogDTO,Log> {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS z");

    @Override
    public Log process(LogDTO logDTO) throws Exception {
        Log log = new Log();
        log.setDate(ZonedDateTime.parse(logDTO.getDate() + " UTC", formatter));
        log.setIp(logDTO.getIp());
        log.setRequest(logDTO.getRequest());
        log.setStatus(logDTO.getStatus());
        log.setUserAgent(logDTO.getUserAgent());
        return log;
    }

}
