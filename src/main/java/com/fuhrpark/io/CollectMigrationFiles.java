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
import java.util.regex.Pattern;
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

    @Parameter
    String resourceFileExtension;

    private static final String versionDirPattern = "[0-9]+(\\.[0-9]+)*";

    private void deleteAll(Path path) throws IOException {
        if (Files.exists(path)) {
            if (Files.isDirectory(path)) {
                try (DirectoryStream<Path> childPathsStream = Files.newDirectoryStream(path)) {
                    for (Path child : childPathsStream)
                        deleteAll(child);
                }
                Files.delete(path);
            } else {
                Files.delete(path);
            }
        }
    }
    private boolean isMigrationFile(Path filePath, BasicFileAttributes basicFileAttributes) {
        return basicFileAttributes.isRegularFile() && filePath.toString().endsWith(resourceFileExtension) && filePath.getParent().getFileName().toString().matches(versionDirPattern);
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println("start collection");
        Path targetPath = Paths.get(build.getOutputDirectory(), migrationTarget);
        if (Files.exists(targetPath)) {
            try {
                deleteAll(targetPath);
            } catch (IOException pE) {
                throw new MojoExecutionException("Could not delete target path",pE);
            }
        }
        if (!targetPath.toFile().mkdirs()) {
            throw new MojoExecutionException("Could not create target path " + targetPath);
        }

        try (Stream<Path> migrationFileStream = Files.find(Paths.get(projectBaseDir.getAbsolutePath(), migrationResourcePath), Integer.MAX_VALUE, this::isMigrationFile)) {
            migrationFileStream.forEach(fs -> {
                Path targetFilePath = targetPath.resolve("V0." + fs.getFileName().toString().replaceFirst("-","__"));
                try {
                    Files.copy(fs, targetFilePath);
                } catch (IOException e) {
                    throw new RuntimeException("Could not copy " + fs + " to " + targetFilePath, e);
                }
            });

        } catch (Exception pE) {
            throw new MojoExecutionException("", pE);
        }

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
