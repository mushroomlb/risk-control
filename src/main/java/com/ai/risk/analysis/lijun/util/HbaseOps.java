package com.ai.risk.analysis.lijun.util;

import com.ai.risk.analysis.lijun.bean.CallCountUnit;
import com.ai.risk.analysis.lijun.factory.HbaseConnectionFactory;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 提供hbase相关函数
 *
 * @author lijun17
 */
@Component
public class HbaseOps {

    private static final byte[] FAMILY = Bytes.toBytes("base");
    private static final byte[] COLUMN = Bytes.toBytes("cnt");
    private static Connection connection;

    public final Connection getConnection(String address) {
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
     * 服务调用次数统计表
     */
    @Value("${spring.hbase.service-talbe}")
    private String serviceTable;

    /**
     * 工号调用次数统计表
     */
    @Value("${spring.hbase.opcode-table}")
    private String opCodeTable;

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
     * 统计类型-工号
     */
    @Value("${spring.hbase.ttl}")
    private int ttl;

    public void serviceAccumulator(String time, List<CallCountUnit> list) {
        accumulatorHbase(serviceTable, time, list);
    }

    public void opcodeAccumulator(String time, List<CallCountUnit> list) {
        accumulatorHbase(opCodeTable, time, list);
    }

    private void accumulatorHbase(String tableName, String time, List<CallCountUnit> list) {

        if (list.isEmpty() || list.size() == 0) {
            return;
        }

        try {
            Connection connection = getConnection(address);
            HTable table = getHbaseTable(connection, tableName);
            List<Put> puts = new ArrayList<>();
            for (CallCountUnit callCountUnit : list) {
                String rowKey = time + "-" + callCountUnit.getName();
                Put put = new Put(Bytes.toBytes(rowKey));
                put.addColumn(FAMILY, COLUMN, Bytes.toBytes(callCountUnit.getNumber()));

                puts.add(put);
            }

            if (puts.size() > 0) {
                table.put(puts);
                table.flushCommits();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取hbase表
     */
    private HTable getHbaseTable(Connection connection, String tableName) throws IOException {
        TableName tName = TableName.valueOf(tableName);
        Admin admin = connection.getAdmin();
        if (!admin.tableExists(tName)) {
            HTableDescriptor tableDescriptor = new HTableDescriptor(tName);
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(FAMILY);
            hColumnDescriptor.setTimeToLive(ttl);
            tableDescriptor.addFamily(hColumnDescriptor);
            admin.createTable(tableDescriptor);
        }
        HTable table = (HTable) connection.getTable(tName);
        table.setAutoFlushTo(false);
        return table;
    }
}
