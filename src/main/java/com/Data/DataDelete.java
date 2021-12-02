package com.Data;

import com.DBMS.proto.DBMS;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DataDelete {
    public String tbName;
    public String dbName;

    public DataDelete(String dbName,String tbName){
        this.dbName = dbName;
        this.tbName = tbName;
    }

    public boolean delete(List<String> fields,List<String> vals){
        try{
            File file = new File("src\\DBMS_ROOT\\data\\"+dbName+"\\"+tbName+".ibd");
            if (file.exists()) {
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[10240];
                input.read(buffer);
                input.close();
                DBMS.Table table = SerializationUtils.deserialize(buffer);
                List<DBMS.Table.Column> columnList = new ArrayList<>(table.getColumnList());
                HashSet<Integer> set = new HashSet<>();
                for (int i = 0; i < fields.size(); i++) {
                    for (int j = 0; j < columnList.size(); j++) {
                        if (fields.get(i).equals(columnList.get(j).getColumnName())) {
                            List<String> list = new ArrayList<>(columnList.get(j).getValList());
                            for (int z = 0; z < list.size(); z++) {
                                if (list.get(z).equals(vals.get(i))) {
                                    set.add(z);
                                }
                            }
                        }
                    }
                }
                if (set.size() == 0) {
                    return false;
                }
                List<DBMS.Table.Column> columnList1 = new ArrayList<>();
                for (DBMS.Table.Column column: columnList){
                    List<String> val = new ArrayList<>(column.getValList());
                    List<String> val1 = new ArrayList<>();
                    for (int i = 0;i < val.size();i++){
                        if(!set.contains(i)){
                            val1.add(val.get(i));
                        }
                    }
                    System.out.println(val1.size());
                    DBMS.Table.Column column1 = DBMS.Table.Column
                            .newBuilder()
                            .setColumnName(column.getColumnName())
                            .setType(column.getType())
                            .setTypeLength(column.getTypeLength())
                            .setComment(column.getComment())
                            .setCheck(column.getCheck())
                            .setDefault(column.getDefault())
                            .setIdentity(column.getIdentity())
                            .addAllVal(val1)
                            .setPrimary(column.getPrimary())
                            .setForeign(column.getForeign())
                            .setNotNull(column.getNotNull())
                            .setUnique(column.getUnique())
                            .build();
                    columnList1.add(column1);
                }
                DBMS.Table table1 = DBMS.Table.newBuilder()
                        .setTableName(table.getTableName())
                        .addAllColumn(columnList1)
                        .build();
                FileOutputStream fileOutputStream = new FileOutputStream("src\\DBMS_ROOT\\data\\"+ dbName +"\\"+tbName+".ibd");
                byte[] val1 = SerializationUtils.serialize(table1);
                for(int i = 0; i < val1.length;i++){
                    fileOutputStream.write(val1[i]);
                }
                fileOutputStream.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
