package com.ai.risk.analysis.modules.warning.util;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 提供hbase相关函数
 *
 * @author lijun17
 */
@Component
public class HbaseOps {

    public static final byte[] CF_BASE = Bytes.toBytes("base");
    public static final byte[] COL_CNT = Bytes.toBytes("cnt");
    private static Connection connection;

    public static final Connection getConnection(String address) {
        if (null == connection) {
            connection = HbaseConnectionFactory.getHbaseConnection(address);
        }
        return connection;
    }

    /**
     * hbase连接地址
     */
    @Value("${spring.hbase.zookeeper.server-list}")
    private String address;

    /**
     * 预警信息统计表
     */
    @Value("${spring.hbase.warning-table}")
    private String warningTable;

    /**
     * 统计类型-服务名
     */
    @Value("${bean.service}")
    private String serviceName;

    /**
     * 统计类型-工号
     */
    @Value("${bean.opcode}")
    private String opCode;

    /**
     * hbase生命周期
     */
    @Value("${spring.hbase.ttl}")
    private static int ttl;

    /**
     * oracle日志库url
     */
    @Value("${spring.datasource.url}")
    private String url;

    /**
     * oracle日志库user
     */
    @Value("${spring.datasource.user}")
    private String user;

    /**
     * oracle日志库password
     */
    @Value("${spring.datasource.password}")
    private String password;

    /**
     * 获取hbase表
     */
    public static HTable getHbaseTable(String tableName) throws IOException {
        TableName tName = TableName.valueOf(tableName);
        Admin admin = connection.getAdmin();
        if (!admin.tableExists(tName)) {
            HTableDescriptor tableDescriptor = new HTableDescriptor(tName);
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(CF_BASE);
            hColumnDescriptor.setTimeToLive(ttl);
            tableDescriptor.addFamily(hColumnDescriptor);
            admin.createTable(tableDescriptor);
        }
        HTable table = (HTable) connection.getTable(tName);
        table.setAutoFlushTo(false);
        return table;
    }

}
