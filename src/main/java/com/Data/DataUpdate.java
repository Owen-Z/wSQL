package com.Data;

import com.DBMS.proto.DBMS;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataUpdate {

    public String tbName;
    public String dbName;

    public DataUpdate(String dbName,String tbName){
        this.dbName = dbName;
        this.tbName = tbName;
    }

    public boolean update(String key1, String val1 , List<String> key,List<String> val,List<String> exp){
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
                    if (column.getColumnName().equals(key1)){
                        String type = column.getType();
                        if(!column.getForeign().equals("false")){
                            String[] ss = column.getForeign().split(",");
                            File file1 = new File("src\\DBMS_ROOT\\data\\"+dbName + "\\" + ss[0] + ".ibd");
                            FileInputStream input1 = new FileInputStream(file1);
                            byte[] buffer1 = new byte[10240];
                            input1.read(buffer1);
                            input1.close();
                            DBMS.Table table1 = SerializationUtils.deserialize(buffer);
                            List<DBMS.Table.Column> columnList2 = new ArrayList<>(table1.getColumnList());
                            boolean t = false;
                            for (DBMS.Table.Column columns : columnList2){
                                if (columns.getColumnName().equals(ss[1])){
                                    List<String> strings = new ArrayList<>(columns.getValList());
                                    for (String s:strings){
                                        if (s.equals(val)){
                                            t = true;
                                        }
                                    }
                                }
                            }
                            if(!t){
                                return false;
                            }
                        }
                        if(type.equals("INTEGER")){
                            try {
                                int i = Integer.parseInt(val1);
                            }catch (Exception e){
                                return false;
                            }
                        }else if(type.equals("BOOL")){
                            if(!(val.equals("TRUE") || val.equals("FALSE"))){
                                return false;
                            }
                        }else if(type.equals("DOUBLE")){
                            try {
                                double i = Double.parseDouble(val1);
                            }catch (Exception e){
                                return false;
                            }
                        }else if(type.equals("DATE")){
                            if (!isData(val1))
                                return false;
                        }else {
                            if (val1.length() > Integer.parseInt(column.getTypeLength())){
                                return false;
                            }
                        }

                        List<String> list = new ArrayList<>(column.getValList());
                        for (Integer i : set){
                            list.set(i,val1);
                        }
                        DBMS.Table.Column column1 = DBMS.Table.Column
                                .newBuilder()
                                .setColumnName(column.getColumnName())
                                .setType(column.getType())
                                .setTypeLength(column.getTypeLength())
                                .setComment(column.getComment())
                                .setCheck(column.getCheck())
                                .setDefault(column.getDefault())
                                .setIdentity(column.getIdentity())
                                .addAllVal(list)
                                .setPrimary(column.getPrimary())
                                .setForeign(column.getForeign())
                                .setNotNull(column.getNotNull())
                                .setUnique(column.getUnique())
                                .build();
                        columnList1.add(column1);
                    }else {
                        columnList1.add(column);
                    }
                }
                DBMS.Table table1 = DBMS.Table.newBuilder()
                        .setTableName(table.getTableName())
                        .addAllColumn(columnList1)
                        .build();
                FileOutputStream fileOutputStream = new FileOutputStream("src\\DBMS_ROOT\\data\\"+ dbName +"\\"+tbName+".ibd");
                byte[] vals1 = SerializationUtils.serialize(table1);
                for(int i = 0; i < vals1.length;i++){
                    fileOutputStream.write(vals1[i]);
                }
                fileOutputStream.close();
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //更新无需where
    public boolean update1(String key,String val){
        try {
            File file = new File("src\\DBMS_ROOT\\data\\"+dbName+"\\"+tbName+".ibd");
            if (file.exists()) {
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[10240];
                input.read(buffer);
                input.close();
                DBMS.Table table = SerializationUtils.deserialize(buffer);
                List<DBMS.Table.Column> columnList = new ArrayList<>(table.getColumnList());

                List<DBMS.Table.Column> columnList1 = new ArrayList<>();
                for (DBMS.Table.Column column:columnList){
                    if(column.getColumnName().equals(key)){
                        if(!column.getForeign().equals("false")){
                            String[] ss = column.getForeign().split(",");
                            File file1 = new File("src\\DBMS_ROOT\\data\\"+dbName + "\\" + ss[0] + ".ibd");
                            FileInputStream input1 = new FileInputStream(file1);
                            byte[] buffer1 = new byte[10240];
                            input1.read(buffer1);
                            input1.close();
                            DBMS.Table table1 = SerializationUtils.deserialize(buffer);
                            List<DBMS.Table.Column> columnList2 = new ArrayList<>(table1.getColumnList());
                            boolean t = false;
                            for (DBMS.Table.Column columns : columnList2){
                                if (columns.getColumnName().equals(ss[1])){
                                    List<String> strings = new ArrayList<>(columns.getValList());
                                    for (String s:strings){
                                        if (s.equals(val)){
                                            t = true;
                                        }
                                    }
                                }
                            }
                            if(!t){
                                return false;
                            }
                        }
                        String type = column.getType();
                        if(type.equals("INTEGER")){
                            try {
                                int i = Integer.parseInt(val);
                            }catch (Exception e){
                                return false;
                            }
                        }else if(type.equals("BOOL")){
                            if(!(val.equals("TRUE") || val.equals("FALSE"))){
                                return false;
                            }
                        }else if(type.equals("DOUBLE")){
                            try {
                                double i = Double.parseDouble(val);
                            }catch (Exception e){
                                return false;
                            }
                        }else if(type.equals("DATE")){
                            if (!isData(val))
                                return false;
                        }else {
                            if (val.length() > Integer.parseInt(column.getTypeLength())){
                                return false;
                            }
                        }


                        List<String> list = new ArrayList<>(column.getValList());
                        for (int i = 0;i< list.size();i++){
                            list.set(i,val);
                        }

                        DBMS.Table.Column column1 = DBMS.Table.Column
                                .newBuilder()
                                .setColumnName(column.getColumnName())
                                .setType(column.getType())
                                .setTypeLength(column.getTypeLength())
                                .setComment(column.getComment())
                                .setCheck(column.getCheck())
                                .setDefault(column.getDefault())
                                .setIdentity(column.getIdentity())
                                .addAllVal(list)
                                .setPrimary(column.getPrimary())
                                .setForeign(column.getForeign())
                                .setNotNull(column.getNotNull())
                                .setUnique(column.getUnique())
                                .build();
                        columnList1.add(column1);
                    }else {
                        columnList1.add(column);
                    }
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

    public boolean isData(String str){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            simpleDateFormat.format(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
