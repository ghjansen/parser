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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@Transactional
public class FileServiceImpl implements FileService {

    private static Logger logger = LoggerFactory.getLogger(FileService.class);

    private FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public @NotNull File save(File file) {
        return this.fileRepository.save(file);
    }

    @Override
    public @NotNull File create(@NotNull @NotEmpty String md5, @NotNull @NotEmpty String fileName, @NotNull @NotEmpty String filePath) {
        File file = new File();
        file.setMd5(md5);
        file.setDateCreated(ZonedDateTime.now(ZoneId.of("UTC")));
        file.setFileName(fileName);
        file.setFilePath(filePath);
        return this.save(file);
    }

    @Override
    public File processFile(String filePath) {
        java.io.File systemFile = getFile(filePath);
        String md5 = generateMD5(filePath);
        File file = this.fileRepository.findById(md5).orElse(null);
        if (file != null) {
            logger.info("File already parsed before");
            return null;
        } else {
            file = create(md5, systemFile.getName(), filePath);
        }
        return file;
    }

    @Override
    public boolean isEmptyDatabase() {
        return !this.fileRepository.findAll().iterator().hasNext();
    }

    private java.io.File getFile(String filePath) {
        java.io.File systemFile = new java.io.File(filePath);
        if (!systemFile.exists()) {
            logger.error("File " + filePath + " does not exist");
            System.exit(1);
        }
        return systemFile;
    }

    private String generateMD5(String filePath) {
        String md5 = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(Files.readAllBytes(Paths.get(filePath)));
            byte[] digest = md.digest();
            md5 = DatatypeConverter.printHexBinary(digest).toUpperCase();
            logger.info("File MD5 is " + md5);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        return md5;
    }

    private boolean isAlreadyProcessed(String md5) {
        File recordFile = this.fileRepository.findById(md5).orElse(null);
        return recordFile != null ? true : false;
    }
}
