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

import com.ghjansen.parser.service.HelloMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ParserApplication implements CommandLineRunner {

    private static Logger log = LoggerFactory.getLogger(ParserApplication.class);

    @Autowired
    private HelloMessageService helloMessageService;


    public static void main(String args[]){
        SpringApplication.run(ParserApplication.class, args);
    }

    public void run(String... args) throws Exception {
        log.warn("Application started");
        if(args.length > 0) {
            System.out.println(helloMessageService.getMessage(args[0]));
        } else {
            System.out.println(helloMessageService.getMessage());
        }
        log.warn("Application finished");
        System.exit(0);
    }
}
