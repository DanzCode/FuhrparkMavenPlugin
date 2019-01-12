package com.fuhrpark.io;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Mojo(name = "db")
public class CreateDBMigrationFile
        extends AbstractMojo {

    @Parameter(defaultValue = "${project.version}")
    private String currentProjectVersion;

    @Parameter
    private String migrationScriptRootDir;

    @Parameter(defaultValue = "${project.basedir}")
    private File projectBaseDir;

    @Parameter(property = "db.migration.timestampFormat", defaultValue = "yyyyMMddHHmmssSSS")
    private String timestampFormat;


    public void execute()
            throws MojoExecutionException {
        Version version = Version.parseVersion(currentProjectVersion);

        File migrationRootDir = new File(projectBaseDir,migrationScriptRootDir);

        if (! migrationRootDir.exists() || ! migrationRootDir.isDirectory()) {
            throw new MojoExecutionException("Configuration migration script root does not exist"+migrationRootDir.getAbsolutePath());
        }


        String versionStart = "/"+version.mayor + "." + version.minor;
        versionStart=versionStart+versionStart+"."+version.revision;

        File versionStartDir=new File(migrationRootDir,versionStart);
       // File startVersionDir=Arrays.stream(migrationRootDir.listFiles()).filter(file -> file.toPath().toAbsolutePath().equals(versionStartPath)).findFirst().orElseGet(() -> versionStartPath.toFile());
        if (! versionStartDir.exists()) {
            if (!versionStartDir.mkdirs()) {
                throw new MojoExecutionException("Could not create directory " +versionStartDir.getAbsolutePath());
            }
        }

        try {
            String migrationScriptName= LocalDateTime.now().format(DateTimeFormatter.ofPattern(timestampFormat))+"-migrate.sql";
            new File(versionStartDir,migrationScriptName).createNewFile();
        } catch (IOException e) {
            throw new MojoExecutionException("Fehler beim erstellen",e);
        }
    }
}
