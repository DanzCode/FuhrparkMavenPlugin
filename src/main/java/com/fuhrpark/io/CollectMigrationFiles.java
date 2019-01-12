package com.fuhrpark.io;

import org.apache.maven.model.Build;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

@Mojo(name = "collectMigrationFiles", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class CollectMigrationFiles extends AbstractMojo {
    @Parameter(defaultValue = "${project.version}")
    private String currentProjectVersion;

    @Parameter(defaultValue = "${project.basedir}")
    private File projectBaseDir;


    @Parameter(defaultValue = "${project.build}")
    private Build build;

    @Parameter
    private String migrationResourcePath;

    @Parameter
    private String migrationTarget;

    @Parameter
    private String resourceTarget;

    @Parameter String resourceFileExtension;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
//        Version version = Version.parseVersion(currentProjectVersion);
//
//        File migrationResourceDir = new File(projectBaseDir, migrationResourcePath);
//        Files.newDirectoryStream()
//        if (! migrationResourceDir.exists() || ! migrationResourceDir.isDirectory()) {
//            throw new MojoExecutionException("Configuration migration script root does not exist"+migrationResourceDir.getAbsolutePath());
//        }
//
//        getLog().info(currentProjectVersion);
//        getLog().info(migrationResourceDir.getAbsolutePath());
//
//        BiPredicate<Path, BasicFileAttributes> migrationFileFilter = (path, basicFileAttributes) -> basicFileAttributes.isRegularFile();
//        try (Stream<Path> scriptFileStream = Files.find(migrationResourceDir.toPath(), Integer.MAX_VALUE, migrationFileFilter, FileVisitOption.FOLLOW_LINKS)) {
//                scriptFileStream
//                    .forEach(path -> {
//                        try {
//                            Files.copy(path, Files.createDirectories(Paths.get(outputDirectory,"db","scripts")).resolve(path.getParent().getFileName()+"."+path.getFileName()));
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    });
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new MojoExecutionException("Fehler beim klopieren");
//        }


    }

    public static class VersionPath {
        public Path path;
        public Version version;

        public VersionPath(Path path, Version version) {
            this.path = path;
            this.version = version;
        }
    }
}
