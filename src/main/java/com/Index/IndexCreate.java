package com.Index;

import com.DBMS.proto.DBMS;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexCreate {
    public String tbName;
    public String dbName;
    private DBMS.Table.Column Column;

    public IndexCreate(String dbName,String tbName){
        this.dbName = dbName;
        this.tbName = tbName;
    }

    public boolean checkField(String columnName){
        try {
            File file = new File("src\\DBMS_ROOT\\data\\"+dbName+"\\"+tbName+".ibd");
            if (file.exists()){
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[10240];
                input.read(buffer);
                input.close();
                DBMS.Table table = SerializationUtils.deserialize(buffer);
                List<DBMS.Table.Column> columns = new ArrayList<>(table.getColumnList());
                for(DBMS.Table.Column column:columns){
                    if(column.getColumnName().equals(columnName)){
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkIndex(String columnName,String index){
        try {
            File file = new File("src\\DBMS_ROOT\\data\\"+dbName+"\\"+tbName+".index");
            if (file.exists()){
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[10240];
                input.read(buffer);
                input.close();
                DBMS.Index index1 = SerializationUtils.deserialize(buffer);
                List<DBMS.Index.IX> ixList = new ArrayList<>(index1.getIxList());
                for (DBMS.Index.IX ix : ixList){
                    if(ix.getCName().equals(columnName)){
                        return false;
                    }
                    if(ix.getIxName().equals(index)){
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void create(String columnName,String index){
        try {
            File file = new File("src\\DBMS_ROOT\\data\\"+dbName+"\\"+tbName+".ibd");
            HashMap<String,List<Integer>> map = new HashMap<>();
            FileInputStream input = new FileInputStream(file);
            byte[] buffer = new byte[10240];
            input.read(buffer);
            input.close();
            DBMS.Table table = SerializationUtils.deserialize(buffer);
            List<DBMS.Table.Column> columns = new ArrayList<>(table.getColumnList());
            for(DBMS.Table.Column column:columns){
                if(column.getColumnName().equals(columnName)){
                    List<String> list1 = new ArrayList<>(column.getValList());
                    for (int i = 0;i<list1.size();i++){
                        List<Integer> list;
                        if(map.containsKey(list1.get(i))){
                            list = new ArrayList<>(map.get(list1.get(i)));
                        }else {
                            list = new ArrayList<>();
                        }
                        list.add(i);
                        map.put(list1.get(i),list);
                    }
                }
            }
            List<DBMS.Index.IX.Map> mapList = new ArrayList<>();
            for (Map.Entry<String,List<Integer>> entry:map.entrySet()){
                DBMS.Index.IX.Map map1 = DBMS.Index.IX.Map.newBuilder()
                        .setKey(entry.getKey())
                        .addAllVal(entry.getValue())
                        .build();
                mapList.add(map1);
            }

            List<DBMS.Index.IX> ixList;
            file = new File("src\\DBMS_ROOT\\data\\"+dbName+"\\"+tbName+".index");
            if (file.exists()){
                input = new FileInputStream(file);
                buffer = new byte[10240];
                input.read(buffer);
                input.close();
                DBMS.Index index1 = SerializationUtils.deserialize(buffer);
                ixList = new ArrayList<>(index1.getIxCount());
            }else {
                file.createNewFile();
                ixList = new ArrayList<>();
            }
            DBMS.Index.IX ix = DBMS.Index.IX.newBuilder()
                    .setDbName(dbName)
                    .setTbName(tbName)
                    .setCName(columnName)
                    .setIxName(index)
                    .addAllMap(mapList)
                    .build();
            ixList.add(ix);
            DBMS.Index index1 = DBMS.Index.newBuilder()
                    .addAllIx(ixList)
                    .build();
            byte[] val = SerializationUtils.serialize(index1);
            FileOutputStream fileOutputStream =
                    new FileOutputStream("src\\DBMS_ROOT\\data\\"+dbName+"\\"+tbName+".index");
            for(int i = 0; i < val.length;i++){
                fileOutputStream.write(val[i]);
            }
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
