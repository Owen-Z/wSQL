package com.Data;

import com.DBMS.proto.DBMS;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class DataSelect {
    public String tbName;
    public String dbName;

    public DataSelect(String dbName,String tbName){
        this.dbName = dbName;
        this.tbName = tbName;
    }

    public boolean select(List<String> result ,List<String> key ,List<String> val ){
        try {
            File file = new File("src\\DBMS_ROOT\\data\\"+dbName+"\\"+tbName+".ibd");
            if (file.exists()) {
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[10240];
                input.read(buffer);
                input.close();
                DBMS.Table table = SerializationUtils.deserialize(buffer);
                List<DBMS.Table.Column> columnList = new ArrayList<>(table.getColumnList());
                file = new File("src\\DBMS_ROOT\\data\\"+dbName+"\\"+tbName+".index");
                HashMap<String,HashMap<String,List<Integer>>> map1 = new HashMap<>();
                if(file.exists()){
                    input = new FileInputStream(file);
                    buffer = new byte[10240];
                    input.read(buffer);
                    input.close();
                    DBMS.Index index = SerializationUtils.deserialize(buffer);
                    List<DBMS.Index.IX> ixList = new ArrayList<>(index.getIxList());
                    for (DBMS.Index.IX ix:ixList){
                        String cName = ix.getCName();
                        List<DBMS.Index.IX.Map> mapList = new ArrayList<>(ix.getMapList());
                        HashMap<String,List<Integer>> listHashMap = new HashMap<>();
                        for (DBMS.Index.IX.Map map:mapList){
                            listHashMap.put(map.getKey(),map.getValList());
                        }
                        map1.put(cName,listHashMap);
                    }
                }
                HashSet<Integer> set = new HashSet<>();
                HashMap<Integer, String> map = new HashMap<>();
                for (int i = 0; i < columnList.get(0).getValList().size(); i++) {
                    set.add(i);
                }
                for(int i = 0;i<key.size();i ++){
                    for(int j = 0;j < columnList.size();j++){
                        if (key.get(i).equals(columnList.get(j).getColumnName())){
                            HashSet<Integer> set1;
                            if(!map1.containsKey(columnList.get(j).getColumnName())){
                                set1 = new HashSet<>();
                                List<String> list = new ArrayList<>(columnList.get(j).getValList());
                                for (int z = 0; z<list.size();z++){
                                    if(list.get(z).equals(val.get(i))){
                                        set1.add(z);
                                    }
                                }
                            }else {
                                set1 = new HashSet<>(map1.get(columnList.get(j).getColumnName()).get(val.get(i)));
                            }
                            set.retainAll(set1);
                        }
                    }
                }
                HashMap<String,List<String>> results = new HashMap<>();
                int j = 0;
                if(result.get(0).equals("*")){
                    for (DBMS.Table.Column column: columnList){
                        List<String> list = new ArrayList<>(column.getValList());
                        List<String> list1 = new ArrayList<>();
                        for (Integer i : set){
                            list1.add(list.get(i));
                        }
                        results.put(column.getColumnName(),list1);
                    }
                    for (Map.Entry<String,List<String>> entry:results.entrySet()){
                        System.out.println("KEY:"+entry.getKey());
                        System.out.println("VALUE:"+entry.getValue());
                    }
                    return true;
                }else {
                    for (DBMS.Table.Column column: columnList){
                        for (String res : result){
                            if(column.getColumnName().equals(res)){
                                List<String> list = new ArrayList<>(column.getValList());
                                List<String> list1 = new ArrayList<>();
                                for (Integer i : set){
                                    list1.add(list.get(i));
                                }
                                results.put(column.getColumnName(),list1);
                            }
                            j++;
                        }
                    }
                    if(j == result.size()) {
                        for (Map.Entry<String,List<String>> entry:results.entrySet()){
                            System.out.println("KEY:"+entry.getKey());
                            System.out.println("VALUE:"+entry.getValue());
                        }
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
