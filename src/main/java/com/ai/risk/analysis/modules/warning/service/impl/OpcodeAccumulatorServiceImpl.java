package com.ai.risk.analysis.modules.warning.service.impl;

import com.ai.risk.analysis.modules.collect.entity.po.CallCountUnit;
import com.ai.risk.analysis.modules.warning.service.IOpcodeAccumulatorService;
import com.ai.risk.analysis.modules.warning.util.HbaseOps;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ThinkPad
 */
@Service
public class OpcodeAccumulatorServiceImpl implements IOpcodeAccumulatorService {

    /**
     * 工号调用次数统计表
     */
    @Value("${spring.hbase.opcode-table}")
    private String opCodeTable;

    @Override
    public void opcodeAccumulator(String time, List<CallCountUnit> list) {
        accumulatorHbase(opCodeTable, time, list);
    }

    private void accumulatorHbase(String tableName, String time, List<CallCountUnit> list) {

        if (list.isEmpty() || list.size() == 0) {
            return;
        }

        try {
            HTable table = HbaseOps.getHbaseTable(tableName);
            List<Put> puts = new ArrayList<>();
            for (CallCountUnit callCountUnit : list) {
                String rowKey = time + "-" + callCountUnit.getName();
                Put put = new Put(Bytes.toBytes(rowKey));
                put.addColumn(HbaseOps.CF_BASE, HbaseOps.COL_CNT, Bytes.toBytes(callCountUnit.getCount()));

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
}
