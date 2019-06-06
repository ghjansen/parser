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

package com.ghjansen.parser.persistence.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Entity
public class File {

    @Id
    @NotNull(message = "File md5 is required")
    private String md5;
    @NotNull(message = "File date created is required")
    private ZonedDateTime dateCreated;
    @NotNull(message = "File name is required")
    private String fileName;
    @NotNull(message = "File path is required")
    private String filePath;

    public File(@NotNull(message = "File md5 is required") String md5, @NotNull(message = "File date created is required") ZonedDateTime dateCreated, @NotNull(message = "File name is required") String fileName, @NotNull(message = "File path is required") String filePath) {
        this.md5 = md5;
        this.dateCreated = dateCreated;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public File() {
    }
    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
    public ZonedDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(ZonedDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
