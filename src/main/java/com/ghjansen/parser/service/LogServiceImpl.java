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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Service
@Transactional
public class LogServiceImpl implements LogService {

    private LogRepository logRepository;

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



}
