package com.hiscat.codec.hadoophello;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * @author hiscat
 */
@Slf4j
public class FileSystemPrinter {

    private final FileSystem fs;
    private final LinkedList<Path> dirs;

    public static FileSystemPrinter create(FileSystem fs, String path) {
        return new FileSystemPrinter(fs, path);
    }

    private FileSystemPrinter(FileSystem fs, String path) {
        this.fs = fs;
        this.dirs = new LinkedList<>();
        this.dirs.push(new Path(path));
    }

    public void print() throws IOException {
        do {
            Path path = this.dirs.pop();
            LOGGER.info("dir:{}", path);
            Map<Boolean, List<FileStatus>> collect = Arrays.stream(fs.listStatus(path)).collect(groupingBy(FileStatus::isFile));
            collect.getOrDefault(Boolean.TRUE, emptyList()).forEach(file -> LOGGER.info("file:{}", file));
            this.dirs.addAll(collect.getOrDefault(Boolean.FALSE, emptyList()).stream().map(FileStatus::getPath).collect(toList()));
        } while (!this.dirs.isEmpty());

    }
}
