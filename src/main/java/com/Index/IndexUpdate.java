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

public class IndexUpdate {
    public String tbName;
    public String dbName;
    private DBMS.Table.Column Column;

    public IndexUpdate(String dbName,String tbName){
        this.dbName = dbName;
        this.tbName = tbName;
    }

    public void update(){
        try{
            File file = new File("src\\src\\data\\"+dbName+"\\"+tbName+".index");
            if(file.exists()){
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[10240];
                input.read(buffer);
                input.close();
                DBMS.Index index = SerializationUtils.deserialize(buffer);
                List<DBMS.Index.IX> ixList = new ArrayList<>(index.getIxList());

                file = new File("src\\src\\data\\"+dbName+"\\"+tbName+".ibd");
                input = new FileInputStream(file);
                buffer = new byte[10240];
                input.read(buffer);
                input.close();
                DBMS.Table table = SerializationUtils.deserialize(buffer);
                List<DBMS.Table.Column> columnList = new ArrayList<>(table.getColumnList());

//                HashMap<String,HashMap<String,List<Integer>>> mapList = new HashMap<>();

                List<DBMS.Index.IX> ixList1 = new ArrayList<>();
                if(ixList.size() != 0){
                    for (DBMS.Index.IX ix : ixList){
                        for(DBMS.Table.Column column : columnList){
                            if(ix.getCName().equals(column.getColumnName())){

                                HashMap<String,List<Integer>> map = new HashMap<>();
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
                                List<DBMS.Index.IX.Map> mapList = new ArrayList<>();
                                for (Map.Entry<String,List<Integer>> entry:map.entrySet()){
                                    DBMS.Index.IX.Map map1 = DBMS.Index.IX.Map.newBuilder()
                                            .setKey(entry.getKey())
                                            .addAllVal(entry.getValue())
                                            .build();
                                    mapList.add(map1);
                                }

                                DBMS.Index.IX ixs = DBMS.Index.IX.newBuilder()
                                        .setDbName(ix.getDbName())
                                        .setTbName(ix.getTbName())
                                        .setCName(ix.getCName())
                                        .setIxName(ix.getIxName())
                                        .addAllMap(mapList)
                                        .build();
                                ixList1.add(ixs);
                            }
                        }

                    }
                }
                DBMS.Index index1 = DBMS.Index.newBuilder()
                        .addAllIx(ixList1)
                        .build();
                byte[] val = SerializationUtils.serialize(index1);
                FileOutputStream fileOutputStream =
                        new FileOutputStream("src\\DBMS_ROOT\\data\\"+dbName+"\\"+tbName+".index");
                for(int i = 0; i < val.length;i++){
                    fileOutputStream.write(val[i]);
                }
                fileOutputStream.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
