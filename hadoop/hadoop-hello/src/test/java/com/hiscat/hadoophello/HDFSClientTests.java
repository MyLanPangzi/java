package com.hiscat.hadoophello;


import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

/**
 * create delete
 * upload download
 * list append
 * rename
 */
@Slf4j
public class HDFSClientTests {

    static FileSystem fs;

    static Configuration conf = new Configuration();

    @BeforeAll
    static void setup() throws IOException, InterruptedException {
        conf.set("fs.defaultFS", "hdfs://hadoop101:9000");
        conf.set("dfs.replication", "2");

        fs = FileSystem.get(URI.create("hdfs://hadoop101:9000"), conf, "atguigu");
    }

    @AfterAll
    static void teardown() throws IOException {
        fs.close();
    }

    @Test
    void testPrinter() throws IOException {
        FileSystemPrinter printer = FileSystemPrinter.create(fs, "/");
        printer.print();
    }



    @Test
    void testConnect() throws IOException, InterruptedException {
        LOGGER.info("start connect hdfs");
        RemoteIterator<LocatedFileStatus> files = fs.listFiles(new Path("/user/atguigu"), true);
        while (files.hasNext()) {
            LocatedFileStatus next = files.next();
            String permission = next.getPermission().toString();
            String owner = next.getOwner();
            String group = next.getGroup();
            long size = next.getLen();
            short replication = next.getReplication();
            long blockSize = next.getBlockSize();
            String name = next.getPath().getName();
            long accessTime = next.getAccessTime();
            long modificationTime = next.getModificationTime();
            boolean file = next.isFile();
            boolean directory = next.isDirectory();
            LOGGER.info("file:\tpermission\t owner\t \tgroup\t size\t replication\t blockSize\t name\t accessTime\t modificationTime\t file\t directory\t");
            LOGGER.info("\t{}\t {}\t {}\t {}\t {}\t {}\t {}\t {}\t {}\t {}\t {}\t", permission, owner, group, size, replication, blockSize, name, accessTime, modificationTime, file, directory);

            Arrays.stream(next.getBlockLocations())
                    .forEach(l -> {
                        try {
                            String[] cachedHosts = l.getCachedHosts();
                            String[] hosts = l.getHosts();
                            long length = l.getLength();
                            String[] names = l.getNames();
                            long offset = l.getOffset();
                            String[] storageIds = l.getStorageIds();
                            String[] topologyPaths = l.getTopologyPaths();
                            StorageType[] storageTypes = l.getStorageTypes();
                            LOGGER.info("bl: cachedHosts\t hosts\tlength\tnames\toffset\tstorageIds\ttopologyPaths\tstorageTypes\t");
                            LOGGER.info("\t\t{}\t{}\t{}\t{}\t{}\t{}\t", Arrays.toString(cachedHosts),
                                    Arrays.toString(hosts), length,
                                    Arrays.toString(names),
                                    offset,
                                    Arrays.toString(storageIds),
                                    Arrays.toString(topologyPaths),
                                    Arrays.toString(storageTypes)
                            );
                            LOGGER.info("");
                        } catch (IOException e) {
                        }
                    });
            LOGGER.info("");
        }
        LOGGER.info(" disconnect hdfs");
    }

    @Test
    void testUploadDownload() throws IOException {
        Path dst = new Path("logback.xml");
        fs.delete(dst, true);

        ClassLoader cl = this.getClass().getClassLoader();
        Path src = new Path(cl.getResource("logback.xml").getFile());
        LOGGER.info("src:{}, dst:{}", src, dst);
        fs.copyFromLocalFile(src, dst);

        String path = cl.getResource("").getPath() + dst.getName();
        fs.copyToLocalFile(false, dst, new Path(path), true);
        LOGGER.info("src:{}, dst:{}", dst, path);

    }

    @Test
    void testRename() throws IOException {
        Path dst = new Path("logback.xml");
        fs.delete(dst, true);

        Path src = new Path(this.getClass().getClassLoader().getResource("logback.xml").getFile());
        fs.copyFromLocalFile(src, dst);
        Path logback = new Path("logback");
        fs.rename(dst, logback);
        fs.copyToLocalFile(true, logback, new Path(this.getClass().getClassLoader().getResource("").getFile() + "logback"), true);

    }
}
