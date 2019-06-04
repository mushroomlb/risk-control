package com.ai.risk.analysis.lijun.util;

import com.ai.risk.analysis.lijun.bean.ServiceAndOpCodeBean;
import com.ai.risk.analysis.lijun.factory.HbaseConnectionFactory;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 提供hbase相关函数
 * @author lijun17
 */
public class HbaseUtil {
    private static final byte[] FAMILY = Bytes.toBytes("base");
    private static final byte[] COLUMN = Bytes.toBytes("raw");

    /**
     * hbase连接地址
     */
    @Value("${spring.hbase.zookeeper.server-list}")
    private static String address;

    /**
     * 服务调用次数统计表
     */
    @Value("${spring.hbase.service-talbe}")
    private static String serviceTable;

    /**
     * 工号调用次数统计表
     */
    @Value("${spring.hbase.opcode-table}")
    private static String opCodeTable;

    /**
     * 统计类型-服务名
     */
    @Value("${bean.service}")
    private static String serviceName;

    /**
     * 统计类型-工号
     */
    @Value("${bean.opcode}")
    private static String opCode;

    public static void accumulatorHbase(List<ServiceAndOpCodeBean> list, String time){
        if(list.isEmpty() || list.size() == 0){
            return;
        }
        try {
            Connection connection = HbaseConnectionFactory.getHbaseConnection(address);
            HTable serviceHTable = getHbaseTable(connection, serviceTable);
            HTable opCodeHTable = getHbaseTable(connection, opCodeTable);

            List<Put> servicePuts = new ArrayList<Put>();
            List<Put> opCodePuts = new ArrayList<Put>();
            for(ServiceAndOpCodeBean serviceAndOpCodeBean : list){
                String rowKey = serviceAndOpCodeBean.getName() + "-" + time;
                Put put = new Put(Bytes.toBytes(rowKey));
                put.addColumn(FAMILY, COLUMN, Bytes.toBytes(serviceAndOpCodeBean.getNumber()));

                if(StringUtils.equals(serviceAndOpCodeBean.getType(), serviceName)){
                    servicePuts.add(put);
                }else if(StringUtils.equals(serviceAndOpCodeBean.getType(), opCode)){
                    opCodePuts.add(put);
                }
            }
            if(servicePuts.size() > 0){
                serviceHTable.put(servicePuts);
                serviceHTable.flushCommits();
            }

            if(opCodePuts.size() > 0){
                opCodeHTable.put(opCodePuts);
                opCodeHTable.flushCommits();
            }
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取hbase表
     */
    private static HTable getHbaseTable(Connection connection, String tableName) throws IOException {
        TableName tName = TableName.valueOf(tableName);
        Admin admin = connection.getAdmin();
        if(!admin.tableExists(tName)) {
            HTableDescriptor tableDescriptor = new HTableDescriptor(tName);
            tableDescriptor.addFamily(new HColumnDescriptor(FAMILY));
            admin.createTable(tableDescriptor);
        }
        HTable table = (HTable) connection.getTable(tName);
        table.setAutoFlushTo(false);
        return table;
    }
}
