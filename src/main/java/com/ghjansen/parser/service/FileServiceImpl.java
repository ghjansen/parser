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
import com.ghjansen.parser.persistence.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@Transactional
public class FileServiceImpl implements FileService {

    private static Logger log = LoggerFactory.getLogger(FileService.class);

    private FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public void processFile(@NotNull @NotEmpty String filePath) {
        log.info("Checking file: " + filePath);
        java.io.File systemFile = getFile(filePath);
        String md5 = generateMD5(filePath);
        log.info("File MD5: " + md5);
        if(isAlreadyProcessed(md5)){
            log.info("File already processed before, parsing phase skipped");
        } else {
            log.info("File not processed yet, about to start parsing file ...");
            File file = create(systemFile.getName(), md5);
        }
    }

    @Override
    public @NotNull File save(File file) {
        return this.fileRepository.save(file);
    }

    @Override
    public @NotNull File create(@NotNull @NotEmpty String fileName, @NotNull @NotEmpty String md5) {
        File file = new File();
        file.setDateCreated(ZonedDateTime.now(ZoneId.of("UTC")));
        file.setFileName(fileName);
        file.setMd5(md5);
        return this.save(file);
    }

    private java.io.File getFile(String filePath){
        java.io.File systemFile = new java.io.File(filePath);
        if(!systemFile.exists()){
            log.error("File " + filePath + " does not exist");
            System.exit(1);
        }
        return systemFile;
    }

    private String generateMD5(String filePath){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(Files.readAllBytes(Paths.get(filePath)));
            byte[] digest = md.digest();
            return DatatypeConverter.printHexBinary(digest).toUpperCase();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    private boolean isAlreadyProcessed(String md5){
        File recordFile = this.fileRepository.findById(md5).orElse(null);
        return recordFile != null ? true : false;
    }
}
