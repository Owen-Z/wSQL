package com.Data;

import com.DBMS.proto.DBMS;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

public class DataDelete {
    public String tbName;
    public String dbName;

    public DataDelete(String dbName,String tbName){
        this.dbName = dbName;
        this.tbName = tbName;
    }

    public boolean delete(List<String> key,List<String> val,List<String> exp){
        try{
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
                for(int i = 0; i < columnList.get(0).getValList().size();i++){
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
                                    if(exp.get(i).equals("=")&&list.get(z).equals(val.get(i))){
                                        set1.add(z);
                                    }else if(exp.get(i).equals(">")){
                                        if(columnList.get(j).getType().equals("INTEGER")){
                                            try {
                                                int left = Integer.parseInt(list.get(z));
                                                int right = Integer.parseInt(val.get(i));
                                                if(left > right){
                                                    set1.add(z);
                                                }
                                            } catch (Exception e) {
                                                return false;
                                            }
                                        }else if(columnList.get(j).getType().equals("DOUBLE")){
                                            try {
                                                double left = Double.parseDouble(list.get(z));
                                                double right = Double.parseDouble(val.get(i));
                                                if(left > right){
                                                    set1.add(z);
                                                }
                                            } catch (Exception e) {
                                                return false;
                                            }
                                        }else {
                                            return false;
                                        }
                                    }else if(exp.get(i).equals(">=")){
                                        if(columnList.get(j).getType().equals("INTEGER")){
                                            try {
                                                int left = Integer.parseInt(list.get(z));
                                                int right = Integer.parseInt(val.get(i));
                                                if(left >= right){
                                                    set1.add(z);
                                                }
                                            } catch (Exception e) {
                                                return false;
                                            }
                                        }else if(columnList.get(j).getType().equals("DOUBLE")){
                                            try {
                                                double left = Double.parseDouble(list.get(z));
                                                double right = Double.parseDouble(val.get(i));
                                                if(left >= right){
                                                    set1.add(z);
                                                }
                                            } catch (Exception e) {
                                                return false;
                                            }
                                        }else {
                                            return false;
                                        }
                                    }else if(exp.get(i).equals("<")){
                                        if(columnList.get(j).getType().equals("INTEGER")){
                                            try {
                                                int left = Integer.parseInt(list.get(z));
                                                int right = Integer.parseInt(val.get(i));
                                                if(left < right){
                                                    set1.add(z);
                                                }
                                            } catch (Exception e) {
                                                return false;
                                            }
                                        }else if(columnList.get(j).getType().equals("DOUBLE")){
                                            try {
                                                double left = Double.parseDouble(list.get(z));
                                                double right = Double.parseDouble(val.get(i));
                                                if(left < right){
                                                    set1.add(z);
                                                }
                                            } catch (Exception e) {
                                                return false;
                                            }
                                        }else {
                                            return false;
                                        }
                                    }else if(exp.get(i).equals("<=")){
                                        if(columnList.get(j).getType().equals("INTEGER")){
                                            try {
                                                int left = Integer.parseInt(list.get(z));
                                                int right = Integer.parseInt(val.get(i));
                                                if(left <= right){
                                                    set1.add(z);
                                                }
                                            } catch (Exception e) {
                                                return false;
                                            }
                                        }else if(columnList.get(j).getType().equals("DOUBLE")){
                                            try {
                                                double left = Double.parseDouble(list.get(z));
                                                double right = Double.parseDouble(val.get(i));
                                                if(left <= right){
                                                    set1.add(z);
                                                }
                                            } catch (Exception e) {
                                                return false;
                                            }
                                        }else {
                                            return false;
                                        }
                                    }
                                }
                            }else {
                                if(exp.get(i).equals("=")){
                                    set1 = new HashSet<>(map1.get(columnList.get(j).getColumnName()).get(val.get(i)));
                                }else if(exp.get(i).equals("<")){
                                    if(columnList.get(j).getType().equals("INTEGER")){
                                        Map<String,List<Integer>> map = new TreeMap<>(map1.get(columnList.get(j).getColumnName()));
                                        set1 = new HashSet<>();
                                        for (String keys1 : map.keySet()){
                                            try {
                                                int left = Integer.parseInt(keys1);
                                                System.out.println(left);
                                                int right = Integer.parseInt(val.get(i));
                                                System.out.println(right);
                                                if(left < right){
                                                    set1.addAll(map.get(keys1));
                                                }else {
                                                    break;
                                                }
                                            } catch (Exception e) {
                                                return false;
                                            }
                                        }
                                    }else if(columnList.get(j).getType().equals("DOUBLE")){
                                        Map<String,List<Integer>> map = new TreeMap<>(map1.get(columnList.get(j).getColumnName()));
                                        set1 = new HashSet<>();
                                        for (String keys1 : map.keySet()){
                                            try {
                                                double left = Double.parseDouble(keys1);
                                                double right = Double.parseDouble(val.get(i));
                                                if(left < right){
                                                    set1.addAll(map.get(keys1));
                                                }else {
                                                    break;
                                                }
                                            } catch (Exception e) {
                                                return false;
                                            }
                                        }
                                    }else {
                                        return false;
                                    }
                                }else if(exp.get(i).equals("<=")){
                                    if(columnList.get(j).getType().equals("INTEGER")){
                                        Map<String,List<Integer>> map = new TreeMap<>(map1.get(columnList.get(j).getColumnName()));
                                        set1 = new HashSet<>();
                                        for (String keys1 : map.keySet()){
                                            try {
                                                int left = Integer.parseInt(keys1);
                                                System.out.println(left);
                                                int right = Integer.parseInt(val.get(i));
                                                System.out.println(right);
                                                if(left <= right){
                                                    set1.addAll(map.get(keys1));
                                                }else {
                                                    break;
                                                }
                                            } catch (Exception e) {
                                                return false;
                                            }
                                        }
                                    }else if(columnList.get(j).getType().equals("DOUBLE")){
                                        Map<String,List<Integer>> map = new TreeMap<>(map1.get(columnList.get(j).getColumnName()));
                                        set1 = new HashSet<>();
                                        for (String keys1 : map.keySet()){
                                            try {
                                                double left = Double.parseDouble(keys1);
                                                double right = Double.parseDouble(val.get(i));
                                                if(left <= right){
                                                    set1.addAll(map.get(keys1));
                                                }else {
                                                    break;
                                                }
                                            } catch (Exception e) {
                                                return false;
                                            }
                                        }
                                    }else {
                                        return false;
                                    }
                                }else if(exp.get(i).equals(">")){
                                    if(columnList.get(j).getType().equals("INTEGER")){
                                        Map<String,List<Integer>> maps = new TreeMap<>(map1.get(columnList.get(j).getColumnName()));
                                        Map<String,List<Integer>> map = new TreeMap<>(
                                                new Comparator<String>() {
                                                    public int compare(String obj1, String obj2) {
                                                        // 降序排序
                                                        return obj2.compareTo(obj1);
                                                    }
                                                });
                                        map.putAll(maps);
                                        set1 = new HashSet<>();
                                        for (String keys1 : map.keySet()){
                                            try {
                                                int left = Integer.parseInt(keys1);
                                                System.out.println(left);
                                                int right = Integer.parseInt(val.get(i));
                                                System.out.println(right);
                                                if(left > right){
                                                    set1.addAll(map.get(keys1));
                                                }else {
                                                    break;
                                                }
                                            } catch (Exception e) {
                                                return false;
                                            }
                                        }
                                    }else if(columnList.get(j).getType().equals("DOUBLE")){
                                        Map<String,List<Integer>> maps = new TreeMap<>(map1.get(columnList.get(j).getColumnName()));
                                        Map<String,List<Integer>> map = new TreeMap<>(
                                                new Comparator<String>() {
                                                    public int compare(String obj1, String obj2) {
                                                        // 降序排序
                                                        return obj2.compareTo(obj1);
                                                    }
                                                });
                                        map.putAll(maps);
                                        set1 = new HashSet<>();
                                        for (String keys1 : map.keySet()){
                                            try {
                                                double left = Double.parseDouble(keys1);
                                                double right = Double.parseDouble(val.get(i));
                                                if(left > right){
                                                    set1.addAll(map.get(keys1));
                                                }else {
                                                    break;
                                                }
                                            } catch (Exception e) {
                                                return false;
                                            }
                                        }
                                    }else {
                                        return false;
                                    }
                                }else if(exp.get(i).equals(">=")){
                                    if(columnList.get(j).getType().equals("INTEGER")){
                                        Map<String,List<Integer>> maps = new TreeMap<>(map1.get(columnList.get(j).getColumnName()));
                                        Map<String,List<Integer>> map = new TreeMap<>(
                                                new Comparator<String>() {
                                                    public int compare(String obj1, String obj2) {
                                                        // 降序排序
                                                        return obj2.compareTo(obj1);
                                                    }
                                                });
                                        map.putAll(maps);
                                        set1 = new HashSet<>();
                                        for (String keys1 : map.keySet()){
                                            try {
                                                int left = Integer.parseInt(keys1);
                                                System.out.println(left);
                                                int right = Integer.parseInt(val.get(i));
                                                System.out.println(right);
                                                if(left >= right){
                                                    set1.addAll(map.get(keys1));
                                                }else {
                                                    break;
                                                }
                                            } catch (Exception e) {
                                                return false;
                                            }
                                        }
                                    }else if(columnList.get(j).getType().equals("DOUBLE")){
                                        Map<String,List<Integer>> maps = new TreeMap<>(map1.get(columnList.get(j).getColumnName()));
                                        Map<String,List<Integer>> map = new TreeMap<>(
                                                new Comparator<String>() {
                                                    public int compare(String obj1, String obj2) {
                                                        // 降序排序
                                                        return obj2.compareTo(obj1);
                                                    }
                                                });
                                        map.putAll(maps);
                                        set1 = new HashSet<>();
                                        for (String keys1 : map.keySet()){
                                            try {
                                                double left = Double.parseDouble(keys1);
                                                double right = Double.parseDouble(val.get(i));
                                                if(left >= right){
                                                    set1.addAll(map.get(keys1));
                                                }else {
                                                    break;
                                                }
                                            } catch (Exception e) {
                                                return false;
                                            }
                                        }
                                    }else {
                                        return false;
                                    }
                                }
                                else {
                                    set1 = new HashSet<>();
                                }
                            }
                            set.retainAll(set1);
                        }
                    }
                }
                List<DBMS.Table.Column> columnList1 = new ArrayList<>();
                for (DBMS.Table.Column column: columnList){
                    List<String> vals = new ArrayList<>(column.getValList());
                    List<String> val1 = new ArrayList<>();
                    for (int i = 0;i < vals.size();i++){
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
