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

    //根据where查询
    public String select(List<String> result ,List<String> key ,List<String> val,List<String> exp){
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
                                                return "null";
                                            }
                                        }else if(columnList.get(j).getType().equals("DOUBLE")){
                                            try {
                                                double left = Double.parseDouble(list.get(z));
                                                double right = Double.parseDouble(val.get(i));
                                                if(left > right){
                                                    set1.add(z);
                                                }
                                            } catch (Exception e) {
                                                return "null";
                                            }
                                        }else {
                                            return null;
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
                                                return "null";
                                            }
                                        }else if(columnList.get(j).getType().equals("DOUBLE")){
                                            try {
                                                double left = Double.parseDouble(list.get(z));
                                                double right = Double.parseDouble(val.get(i));
                                                if(left >= right){
                                                    set1.add(z);
                                                }
                                            } catch (Exception e) {
                                                return "null";
                                            }
                                        }else {
                                            return null;
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
                                                return "null";
                                            }
                                        }else if(columnList.get(j).getType().equals("DOUBLE")){
                                            try {
                                                double left = Double.parseDouble(list.get(z));
                                                double right = Double.parseDouble(val.get(i));
                                                if(left < right){
                                                    set1.add(z);
                                                }
                                            } catch (Exception e) {
                                                return "null";
                                            }
                                        }else {
                                            return "null";
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
                                                return "null";
                                            }
                                        }else if(columnList.get(j).getType().equals("DOUBLE")){
                                            try {
                                                double left = Double.parseDouble(list.get(z));
                                                double right = Double.parseDouble(val.get(i));
                                                if(left <= right){
                                                    set1.add(z);
                                                }
                                            } catch (Exception e) {
                                                return "null";
                                            }
                                        }else {
                                            return "null";
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
                                        for (String key1 : map.keySet()){
                                            try {
                                                int left = Integer.parseInt(key1);
                                                System.out.println(left);
                                                int right = Integer.parseInt(val.get(i));
                                                System.out.println(right);
                                                if(left < right){
                                                    set1.addAll(map.get(key1));
                                                }else {
                                                    break;
                                                }
                                            } catch (Exception e) {
                                                return "null";
                                            }
                                        }
                                    }else if(columnList.get(j).getType().equals("DOUBLE")){
                                        Map<String,List<Integer>> map = new TreeMap<>(map1.get(columnList.get(j).getColumnName()));
                                        set1 = new HashSet<>();
                                        for (String key1 : map.keySet()){
                                            try {
                                                double left = Double.parseDouble(key1);
                                                double right = Double.parseDouble(val.get(i));
                                                if(left < right){
                                                    set1.addAll(map.get(key1));
                                                }else {
                                                    break;
                                                }
                                            } catch (Exception e) {
                                                return "null";
                                            }
                                        }
                                    }else {
                                        return "null";
                                    }
                                }else if(exp.get(i).equals("<=")){
                                    if(columnList.get(j).getType().equals("INTEGER")){
                                        Map<String,List<Integer>> map = new TreeMap<>(map1.get(columnList.get(j).getColumnName()));
                                        set1 = new HashSet<>();
                                        for (String key1 : map.keySet()){
                                            try {
                                                int left = Integer.parseInt(key1);
                                                System.out.println(left);
                                                int right = Integer.parseInt(val.get(i));
                                                System.out.println(right);
                                                if(left <= right){
                                                    set1.addAll(map.get(key1));
                                                }else {
                                                    break;
                                                }
                                            } catch (Exception e) {
                                                return "null";
                                            }
                                        }
                                    }else if(columnList.get(j).getType().equals("DOUBLE")){
                                        Map<String,List<Integer>> map = new TreeMap<>(map1.get(columnList.get(j).getColumnName()));
                                        set1 = new HashSet<>();
                                        for (String key1 : map.keySet()){
                                            try {
                                                double left = Double.parseDouble(key1);
                                                double right = Double.parseDouble(val.get(i));
                                                if(left <= right){
                                                    set1.addAll(map.get(key1));
                                                }else {
                                                    break;
                                                }
                                            } catch (Exception e) {
                                                return "null";
                                            }
                                        }
                                    }else {
                                        return "null";
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
                                        for (String key1 : map.keySet()){
                                            try {
                                                int left = Integer.parseInt(key1);
                                                System.out.println(left);
                                                int right = Integer.parseInt(val.get(i));
                                                System.out.println(right);
                                                if(left > right){
                                                    set1.addAll(map.get(key1));
                                                }else {
                                                    break;
                                                }
                                            } catch (Exception e) {
                                                return "null";
                                            }
                                        }
                                    }else if(columnList.get(j).getType().equals("DOUBLE")){
                                        Map<String,List<Integer>> map = new TreeMap<>(map1.get(columnList.get(j).getColumnName()));
                                        set1 = new HashSet<>();
                                        for (String key1 : map.keySet()){
                                            try {
                                                double left = Double.parseDouble(key1);
                                                double right = Double.parseDouble(val.get(i));
                                                if(left > right){
                                                    set1.addAll(map.get(key1));
                                                }else {
                                                    break;
                                                }
                                            } catch (Exception e) {
                                                return "null";
                                            }
                                        }
                                    }else {
                                        return "null";
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
                                        for (String key1 : map.keySet()){
                                            try {
                                                int left = Integer.parseInt(key1);
                                                System.out.println(left);
                                                int right = Integer.parseInt(val.get(i));
                                                System.out.println(right);
                                                if(left >= right){
                                                    set1.addAll(map.get(key1));
                                                }else {
                                                    break;
                                                }
                                            } catch (Exception e) {
                                                return "null";
                                            }
                                        }
                                    }else if(columnList.get(j).getType().equals("DOUBLE")){
                                        Map<String,List<Integer>> map = new TreeMap<>(map1.get(columnList.get(j).getColumnName()));
                                        set1 = new HashSet<>();
                                        for (String key1 : map.keySet()){
                                            try {
                                                double left = Double.parseDouble(key1);
                                                double right = Double.parseDouble(val.get(i));
                                                if(left >= right){
                                                    set1.addAll(map.get(key1));
                                                }else {
                                                    break;
                                                }
                                            } catch (Exception e) {
                                                return "null";
                                            }
                                        }
                                    }else {
                                        return "null";
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
                    String allKeys = "";
                    String allValues = "";
                    List<List<String>> aa = new ArrayList<>();;
                    for (Map.Entry<String,List<String>> entry:results.entrySet()){
                        String currentKey = "|" + entry.getKey();

                        System.out.println("KEY:"+entry.getKey());

                        currentKey = String.format("%-15s", currentKey);
                        allKeys += currentKey;
                        System.out.println("VALUE:"+entry.getValue());
                        aa.add(entry.getValue());
                    }
                    for(int i = 0; i < aa.get(0).size(); i++){
                        for(int k = 0; k < aa.size(); k++){
                            String currentValue =  "|" + aa.get(k).get(i) ;
                            currentValue = String.format("%-15s", currentValue);
                            allValues += currentValue;
                        }
                        allValues += "\n";
                    }

                    return allKeys + "\n" + allValues;
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
                        String allKeys = "";
                        String allValues = "";
                        List<List<String>> aa = new ArrayList<>();;
                        for (Map.Entry<String,List<String>> entry:results.entrySet()){
                            String currentKey = "|" + entry.getKey();

                            System.out.println("KEY:"+entry.getKey());

                            currentKey = String.format("%-15s", currentKey);
                            allKeys += currentKey;
                            System.out.println("VALUE:"+entry.getValue());
                            aa.add(entry.getValue());
                        }
                        for(int i = 0; i < aa.get(0).size(); i++){
                            for(int k = 0; k < aa.size(); k++){
                                String currentValue =  "|" + aa.get(k).get(i) ;
                                currentValue = String.format("%-15s", currentValue);
                                allValues += currentValue;
                            }
                            allValues += "\n";
                        }


                        return allKeys + "\n" + allValues;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "null";
    }


    //查询语句无需where
    public String select1(List<String> result){
        try{
            File file = new File("src\\DBMS_ROOT\\data\\"+dbName+"\\"+tbName+".ibd");
            if (file.exists()) {
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[10240];
                input.read(buffer);
                input.close();
                DBMS.Table table = SerializationUtils.deserialize(buffer);
                List<DBMS.Table.Column> columnList = new ArrayList<>(table.getColumnList());
                HashMap<String,List<String>> results = new HashMap<>();
                int j = 0;
                if(result.get(0).equals("*")){
                    for (DBMS.Table.Column column: columnList){
                        List<String> list = new ArrayList<>(column.getValList());
                        results.put(column.getColumnName(),list);
                    }
                    String allKeys = "";
                    String allValues = "";
                    List<List<String>> aa = new ArrayList<>();;
                    for (Map.Entry<String,List<String>> entry:results.entrySet()){
                        String currentKey = "|" + entry.getKey();

                        System.out.println("KEY:"+entry.getKey());

                        currentKey = String.format("%-15s", currentKey);
                        allKeys += currentKey;
                        System.out.println("VALUE:"+entry.getValue());
                        aa.add(entry.getValue());
                    }
                    for(int i = 0; i < aa.get(0).size(); i++){
                        for(int k = 0; k < aa.size(); k++){
                            String currentValue =  "|" + aa.get(k).get(i) ;
                            currentValue = String.format("%-15s", currentValue);
                            allValues += currentValue;
                        }
                        allValues += "\n";
                    }

                    return allKeys + "\n" + allValues;
                }else {
                    for (DBMS.Table.Column column: columnList){
                        for (String res : result){
                            if(column.getColumnName().equals(res)){
                                List<String> list = new ArrayList<>(column.getValList());
                                results.put(column.getColumnName(),list);
                            }
                            j++;
                        }
                    }
                    if(j == result.size()) {
                        String allKeys = "";
                        String allValues = "";
                        List<List<String>> aa = new ArrayList<>();;
                        for (Map.Entry<String,List<String>> entry:results.entrySet()){
                            String currentKey = "|" + entry.getKey();

                            System.out.println("KEY:"+entry.getKey());

                            currentKey = String.format("%-15s", currentKey);
                            allKeys += currentKey;
                            System.out.println("VALUE:"+entry.getValue());
                            aa.add(entry.getValue());
                        }
                        for(int i = 0; i < aa.get(0).size(); i++){
                            for(int k = 0; k < aa.size(); k++){
                                String currentValue =  "|" + aa.get(k).get(i) ;
                                currentValue = String.format("%-15s", currentValue);
                                allValues += currentValue;
                            }
                            allValues += "\n";
                        }


                        return allKeys + "\n" + allValues;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "null";
    }
}
