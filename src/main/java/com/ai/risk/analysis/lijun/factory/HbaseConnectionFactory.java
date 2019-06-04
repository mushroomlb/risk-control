package com.ai.risk.analysis.lijun.factory;

import com.ai.risk.analysis.lijun.util.HbaseUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import java.io.IOException;

/**
 * 获取hbase连接
 * @author lijun17
 */
public class HbaseConnectionFactory {
    private static Connection connection = null;

    public static Connection getHbaseConnection(String address){
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", address);
        try {
            connection = ConnectionFactory.createConnection(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
