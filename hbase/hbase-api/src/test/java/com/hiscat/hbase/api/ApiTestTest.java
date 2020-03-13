package com.hiscat.hbase.api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.hadoop.hbase.client.ConnectionFactory.createConnection;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiTestTest {
    private static Admin admin;
    private static Connection connection;
    private static TableName tableName;

    private Table hiscat;

    @BeforeAll
    static void setup() throws IOException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "hadoop104,hadoop105,hadoop106");
        connection = createConnection(conf);
        admin = connection.getAdmin();
        tableName = TableName.valueOf("hiscat");
    }

    @AfterAll
    static void teardown() throws IOException {
        admin.close();
        connection.close();
    }

    @BeforeEach
    void prepare() throws IOException {
        if (!admin.tableExists(tableName)) {
            final HTableDescriptor desc = new HTableDescriptor(tableName);
            desc.addFamily(new HColumnDescriptor("info"));
            admin.createTable(desc);
        }
        hiscat = connection.getTable(tableName);
        hiscat.put(new Put("1".getBytes()).addColumn("info".getBytes(), "name".getBytes(), "world".getBytes()));
        hiscat.put(new Put("1".getBytes()).addColumn("info".getBytes(), "sex".getBytes(), "m".getBytes()));
    }

    @AfterEach
    void close() throws IOException {
        if (admin.tableExists(tableName)) {
            hiscat.delete(new Delete("1".getBytes()));
            hiscat.close();
        }
    }

    @Test
    void testExists() throws IOException {
        assertTrue(admin.tableExists(tableName));
    }

    @Test
    void createNamespace() throws IOException {
        final String ns = String.valueOf(System.currentTimeMillis());
        admin.createNamespace(NamespaceDescriptor.create(ns).build());
        admin.deleteNamespace(ns);
    }

    @Test
    void testCreateTable() throws IOException {
        if (!admin.tableExists(tableName)) {
            final HTableDescriptor desc = new HTableDescriptor(tableName);
            desc.addFamily(new HColumnDescriptor("info"));
            admin.createTable(desc);
        }
    }

    @Test
    void testDeleteTable() throws IOException {
        if (admin.tableExists(tableName)) {
            if (admin.isTableEnabled(tableName)) {
                admin.disableTable(tableName);
            }
            admin.deleteTable(tableName);
        }
    }

    @Test
    void testAddData() throws IOException {
        final Table table = connection.getTable(tableName);
        if (admin.tableExists(table.getName())) {
            table.put(new Put("1".getBytes()).addColumn("info".getBytes(), "name".getBytes(), "world".getBytes()));
            table.put(new Put("1".getBytes()).addColumn("info".getBytes(), "sex".getBytes(), "m".getBytes()));
        }
    }

    @Test
    void testDeleteData() throws IOException {
        final Delete name = new Delete("1".getBytes()).addColumn("info".getBytes(), "name".getBytes());
        final Delete sex = new Delete("1".getBytes()).addColumn("info".getBytes(), "sex".getBytes());
        List<Delete> deletes = new ArrayList<>();
        deletes.add(name);
        deletes.add(sex);
        hiscat.delete(deletes);
    }

    @Test
    void testScan() throws IOException {
        final ResultScanner scanner = hiscat.getScanner(new Scan());
        scanner.forEach(r -> Arrays.stream(r.rawCells()).forEach(cell -> {
            System.out.println(Bytes.toString(CellUtil.cloneRow(cell)));
            System.out.println(Bytes.toString(CellUtil.cloneFamily(cell)));
            System.out.println(Bytes.toString(CellUtil.cloneQualifier(cell)));
            System.out.println(Bytes.toString(CellUtil.cloneValue(cell)));
        }));
    }

    @Test
    void testGet() throws IOException {
        Arrays.stream(hiscat.get(new Get("1".getBytes())).rawCells())
                .forEach(cell -> {
                    System.out.println(Bytes.toString(CellUtil.cloneRow(cell)));
                    System.out.println(Bytes.toString(CellUtil.cloneFamily(cell)));
                    System.out.println(Bytes.toString(CellUtil.cloneQualifier(cell)));
                    System.out.println(Bytes.toString(CellUtil.cloneValue(cell)));
                });
    }
}