package com.hiscat.hbase.api;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.exceptions.DeserializationException;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

import static org.apache.hadoop.hbase.client.ConnectionFactory.createConnection;
import static org.apache.hadoop.hbase.filter.CompareFilter.CompareOp.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiTestTest {
    private static Admin admin;
    private static Connection connection;
    private static TableName tableName;

    private Table hiscat;

    @BeforeAll
    static void setup() throws IOException, DeserializationException {
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
    void testCoprocessor() throws IOException {
        final HTableDescriptor desc = new HTableDescriptor(TableName.valueOf("coprocessor"));
        desc.addCoprocessor("com.hiscat.coprocessor.FruitCoprocessor");
        desc.addFamily(new HColumnDescriptor("info"));
        admin.createTable(desc);
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
        for (int i = 0; i < 100; i++) {
            final byte[] rowKey = Bytes.toBytes(i + "");
            final byte[] cf = "info".getBytes();
            final Put name = new Put(rowKey).addColumn(cf, "name".getBytes(), RandomStringUtils.random(10, true, true).getBytes());
            final Put sex = new Put(rowKey).addColumn(cf, "sex".getBytes(), RandomStringUtils.random(1, "mf").getBytes());
            final Put height = new Put(rowKey).addColumn(cf, "height".getBytes(), Bytes.toBytes(RandomUtils.nextInt(140, 200) + ""));
            final Put weight = new Put(rowKey).addColumn(cf, "weight".getBytes(), Bytes.toBytes(RandomUtils.nextInt(40, 100) + ""));
            final Put birthday = new Put(rowKey).addColumn(cf, "birthday".getBytes(),
                    LocalDate.of(RandomUtils.nextInt(1990, 2000), RandomUtils.nextInt(1, 13), RandomUtils.nextInt(1, 29)).toString().getBytes());
            hiscat.put(Arrays.asList(name, sex, height, weight, birthday));
        }
    }

    @Test
    void testDeleteData() throws IOException {
        final byte[] cf = "info".getBytes();
        hiscat.delete(new Delete("98".getBytes()).addFamily(cf, System.currentTimeMillis()));
        hiscat.delete(new Delete("97".getBytes()).addFamily(cf));
        hiscat.delete(new Delete("96".getBytes()).addFamilyVersion(cf, 1584165650054L));
        //删除列，所有数据无效
        hiscat.delete(new Delete("99".getBytes()).addColumns(cf, "weight".getBytes()));
        //删除列，指定版本及以后的数据无效
        hiscat.delete(new Delete("99".getBytes()).addColumns(cf, "sex".getBytes(), 1584165650069L));

        //标记最新一条数据无效，后面一条会生效，被查出来
        hiscat.delete(new Delete("99".getBytes()).addColumn(cf, "name".getBytes()));
        //标记指定版本数据无效，后面一条会生效，被查出来
        hiscat.delete(new Delete("99".getBytes()).addColumn(cf, "name".getBytes(), 1584165650069L));
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
        scanner.close();
    }

    @Test
    void testFilter() throws IOException {
//        final Scan scan = new Scan().setFilter(new SingleColumnValueFilter("info".getBytes(),
//                "sex".getBytes(), CompareFilter.CompareOp.EQUAL, "m".getBytes()));
        final byte[] cf = "info".getBytes();
        final byte[] height = "height".getBytes();
        final byte[] weight = "weight".getBytes();
        FilterList filterList = new FilterList(
                new SingleColumnValueFilter(cf, "sex".getBytes(), EQUAL, "f".getBytes()),
                new SingleColumnValueFilter(cf, "birthday".getBytes(), GREATER_OR_EQUAL, new BinaryComparator("1993".getBytes())),
                new SingleColumnValueFilter(cf, height, GREATER_OR_EQUAL, new BinaryComparator("160".getBytes())),
                new SingleColumnValueFilter(cf, height, LESS_OR_EQUAL, new BinaryComparator("170".getBytes())),
                new SingleColumnValueFilter(cf, weight, GREATER, new BinaryComparator("40".getBytes())),
                new SingleColumnValueFilter(cf, weight, LESS, new BinaryComparator("60".getBytes()))
//                new RowFilter(EQUAL, new SubstringComparator("8"))
        );
        final Scan scan = new Scan().setFilter(filterList);
        final ResultScanner scanner = hiscat.getScanner(scan);
        scanner.forEach(r -> Arrays.stream(r.rawCells()).forEach(cell -> {
            System.out.printf("row:%s,cf:%s,col:%s,val:%s\n",
                    Bytes.toString(CellUtil.cloneRow(cell)),
                    Bytes.toString(CellUtil.cloneFamily(cell)),
                    Bytes.toString(CellUtil.cloneQualifier(cell)),
                    Bytes.toString(CellUtil.cloneValue(cell))
            );
        }));
        scanner.close();
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