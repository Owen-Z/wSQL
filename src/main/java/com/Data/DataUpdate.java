package com.Data;

import com.DBMS.proto.DBMS;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DataUpdate {

    public String tbName;
    public String dbName;

    public DataUpdate(String dbName,String tbName){
        this.dbName = dbName;
        this.tbName = tbName;
    }

    public boolean update(String key, String val , List<String> fields,List<String> vals){
        try {
            File file = new File("src\\DBMS_ROOT\\data\\"+dbName+"\\"+tbName+".ibd");
            if (file.exists()){
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[10240];
                input.read(buffer);
                input.close();
                DBMS.Table table = SerializationUtils.deserialize(buffer);
                List<DBMS.Table.Column> columnList = new ArrayList<>(table.getColumnList());
                HashSet<Integer> set = new HashSet<>();
                HashMap<Integer,String> map = new HashMap<>();
                for(int i = 0; i < columnList.get(0).getValList().size();i++){
                    set.add(i);
                }
                int m = 0;
                for(int i = 0;i<fields.size();i ++){
                    for(int j = 0;j < columnList.size();j++){
                        if (fields.get(i).equals(columnList.get(j).getColumnName())){
                            HashSet<Integer> set1 = new HashSet<>();
                            List<String> list = new ArrayList<>(columnList.get(j).getValList());
                            for (int z = 0; z<list.size();z++){
                                if(list.get(z).equals(vals.get(i))){
                                    set1.add(z);
                                }
                            }
                            set.retainAll(set1);
                            m++;
                        }
                    }
                }
                if(m != fields.size()||set.size() == 0)
                    return false;

                List<DBMS.Table.Column> columnList1 = new ArrayList<>();
                for (DBMS.Table.Column column: columnList){
                    if (column.getColumnName().equals(key)){
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
                        if(type.equals("INTEFER")){
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
                                float i = Float.parseFloat(val);
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
                        for (Integer i : set){
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
        }catch (Exception e){
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
