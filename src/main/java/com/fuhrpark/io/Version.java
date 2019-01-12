package com.fuhrpark.io;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Version implements Comparable<Version> {
    private static final Pattern VERSION_PATTERN = Pattern.compile("([0-9]+)\\.([0-9]+)\\.([0-9]+)(\\.([0-9]+))?([^0-9].*)?");

    public int mayor;
    public int minor;
    public int revision;
    public int changeNumber;
    public String status;

    public static Version tryVersion(String versionString) {
        Matcher matcher=VERSION_PATTERN.matcher(versionString);
        if (! matcher.matches()) {
            return null;
        }
        return new Version(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)),matcher.groupCount()>4?Integer.parseInt(matcher.group(5)):0,matcher.groupCount()>5?matcher.group(6):"");
    }
    public static Version parseVersion(String versionString) {
        Matcher matcher=VERSION_PATTERN.matcher(versionString);
        if (! matcher.matches()) {
            throw new RuntimeException("Versionstring "+versionString+" is not well formatted");
        }
        return new Version(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)),matcher.groupCount()>4?Integer.parseInt(matcher.group(5)):0,matcher.groupCount()>5?matcher.group(6):"");
    }

    public Version(int mayor, int minor, int revision) {
        this.mayor = mayor;
        this.minor = minor;
        this.revision = revision;
    }

    public Version(int mayor, int minor, int revision, int changeNumber, String status) {
        this.mayor = mayor;
        this.minor = minor;
        this.revision = revision;
        this.changeNumber = changeNumber;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version = (Version) o;
        return mayor == version.mayor &&
                minor == version.minor &&
                revision == version.revision;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mayor, minor, revision);
    }

    @Override
    public int compareTo(Version o) {
        int[] compares = {Integer.compare(mayor, o.mayor), Integer.compare(minor, o.minor), Integer.compare(revision, this.revision), Integer.compare(changeNumber, this.changeNumber)};
        for (int compare : compares)
            if (compare != 0) return compare;
        return 0;
    }

    public String formatReleaseVersion() {
        return String.format("%d.%d",mayor,minor);
    }

    public String formatBugfixVersion() { return String.format("%d.%d",formatReleaseVersion(), revision); }
}
