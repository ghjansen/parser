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

package com.ghjansen.parser.persistence.repository;

import com.ghjansen.parser.persistence.model.Log;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;

public interface LogRepository extends CrudRepository<Log,Long> {

    @Query(value = "SELECT counter.ip FROM (SELECT DISTINCT(l.ip) AS ip, count(*) AS occurrences FROM Log l WHERE (l.date BETWEEN :startDate AND :endDate) GROUP BY l.ip) counter WHERE counter.occurrences >= :threshold", nativeQuery = true)
    Iterable<String> findIpsByDateThreshold(@Param("startDate")ZonedDateTime startDate, @Param("endDate") ZonedDateTime endDateTime, @Param("threshold") Long threshold);

}
