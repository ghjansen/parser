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
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Validated
public interface LogService {

    @NotNull Log save(Log log);

    @NotNull Log create(ZonedDateTime date, String ip, String request, Long status, String userAgent);

}
