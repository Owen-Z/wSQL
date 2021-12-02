package com.Table;

import com.DBMS.proto.DBMS;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TableCreate {

    private String tbName;
    private String dbName;
    private List<DBMS.Table.Column> listColumn;

    public TableCreate(String dbName,String tbName){
        this.dbName = dbName;
        this.tbName = tbName;
        listColumn = new ArrayList<>();
    }



    public void TBCreateColumn(HashMap<String,String> map){
        String name = map.get("name"),
                type = map.get("type"),
                def = map.get("def"),
                comment = map.get("comment"),
                auto = map.get("auto"),
                primaryKey = map.get("primaryKey"),
                notNUll = map.get("notNUll"),
                check = map.get("check"),
                foreignKey= map.get("foreignKey"),
                unique = map.get("unique");
        String length = getLength(type);
        DBMS.Table.Column column = DBMS.Table.Column
                .newBuilder()
                .setColumnName(name)
                .setType(type)
                .setTypeLength(length)
                .setDefault(def)
                .setComment(comment)
                .setIdentity(auto)
                .setPrimary(primaryKey)
                .setNotNull(notNUll)
                .setCheck(check)
                .setForeign(foreignKey)
                .setUnique(unique)
                .build();
        listColumn.add(column);
//        System.out.println("name:" + name);
//        System.out.println("type:" + type);
//        System.out.println("def:" + def);
//        System.out.println("comment:" + comment);
//        System.out.println("auto:" + auto);
//        System.out.println("primaryKey:" + primaryKey);
//        System.out.println("notNUll:" + notNUll);
//        System.out.println("check:" + check);
//        System.out.println("unique:" + unique);
//        System.out.println("foreignKey:" + foreignKey);

    }

    public boolean setPrimaryKey(String columnName){
        String  name = null,
                type = null,
                def = null,
                comment = null,
                auto  = null,
                primaryKey = null,
                notNUll = null,
                check = null,
                foreignKey = null,
                unique = null,
                length = null;
        int j = 0;
        for (int i = 0;i<listColumn.size();i++){
            if(listColumn.get(i).getColumnName().equals(columnName)){
                j = i;
                name = listColumn.get(i).getColumnName();
                type = listColumn.get(i).getType();
                def = listColumn.get(i).getDefault();
                comment = listColumn.get(i).getComment();
                auto = listColumn.get(i).getIdentity();
                primaryKey = "true";
                notNUll = listColumn.get(i).getNotNull();
                check = listColumn.get(i).getCheck();
                foreignKey = listColumn.get(i).getForeign();
                unique = listColumn.get(i).getUnique();
                length = listColumn.get(i).getTypeLength();
            }else if(listColumn.get(i).getPrimary().equals("true")){
                return false;
            }
        }
        listColumn.remove(j);
        DBMS.Table.Column column = DBMS.Table.Column
                .newBuilder()
                .setColumnName(name)
                .setType(type)
                .setTypeLength(length)
                .setDefault(def)
                .setComment(comment)
                .setIdentity(auto)
                .setPrimary(primaryKey)
                .setNotNull(notNUll)
                .setCheck(check)
                .setForeign(foreignKey)
                .setUnique(unique)
                .build();
        listColumn.add(column);
        return true;
    }

    public int setForeign(String columnName, String TBName, String cname){
        File file = new File("src\\DBMS_ROOT\\"+dbName +"\\"+dbName+".tb");
        String  name = null,
                type = null,
                def = null,
                comment = null,
                auto  = null,
                primaryKey = null,
                notNUll = null,
                check = null,
                foreignKey = null,
                unique = null,
                length = null;
        boolean a = false;
        int j = 0;
        for (int i = 0;i<listColumn.size();i++){
            if(listColumn.get(i).getColumnName().equals(columnName)){
                a = true;
                j = i;
                name = listColumn.get(i).getColumnName();
                type = listColumn.get(i).getType();
                def = listColumn.get(i).getDefault();
                comment = listColumn.get(i).getComment();
                auto = listColumn.get(i).getIdentity();
                primaryKey = listColumn.get(i).getPrimary();
                notNUll = listColumn.get(i).getNotNull();
                check = listColumn.get(i).getCheck();
                foreignKey = TBName + "," + cname;
                unique = listColumn.get(i).getUnique();
                length = listColumn.get(i).getTypeLength();
            }
        }
        if (a == false){
            return 0;
        }
        if(file.exists()){
            try {
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[10240];
                input.read(buffer);
                input.close();
                DBMS.TBMessage tbMessage = SerializationUtils.deserialize(buffer);
                List<DBMS.TBMessage.TB> tbs = new ArrayList<>(tbMessage.getTbList());
                for (DBMS.TBMessage.TB tb : tbs){
                    if (tb.getTBName() == TBName){
                        file = new File("src\\DBMS_ROOT\\"+dbName +"\\"+TBName+".ibd");
                        input = new FileInputStream(file);
                        buffer = new byte[10240];
                        input.read(buffer);
                        input.close();
                        DBMS.Table table = SerializationUtils.deserialize(buffer);
                        List<DBMS.Table.Column> columns = new ArrayList<>(table.getColumnList());
                        for (DBMS.Table.Column column:columns){
                            if (column.getColumnName().equals(cname)){
                                if(!column.getType().equals(type))
                                    return 2;
                                if (column.getPrimary() != "true")
                                    return 3;
                                listColumn.remove(j);
                                DBMS.Table.Column column1 = DBMS.Table.Column
                                        .newBuilder()
                                        .setColumnName(name)
                                        .setType(type)
                                        .setTypeLength(length)
                                        .setDefault(def)
                                        .setComment(comment)
                                        .setIdentity(auto)
                                        .setPrimary(primaryKey)
                                        .setNotNull(notNUll)
                                        .setCheck(check)
                                        .setForeign(foreignKey)
                                        .setUnique(unique)
                                        .build();
                                listColumn.add(column1);
                                return 4;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 1;
    }

    public String getLength(String type){
        String length = "";
        if (type.equals("INT")) {
            length = "11";
        } else if (type.equals("INT UNSIGNED")) {
            length = "11";
        } else if (type.equals("INTEGER")) {
            length = "11";
        } else if (type.equals("BOOL")) {
            length = "8";
        } else if (type.equals("DOUBLE")) {
            length = "11";
        } else if (type.equals("DATE")) {
            length = "20";
        } else {
            length = type.substring(8, type.length() - 1);
        }
        return length;
    }

    public boolean TBCheck(){
        File file = new File("src\\DBMS_ROOT\\data\\"+dbName+"\\"+dbName+".tb");
        if(file.exists()){
            try {
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[10240];
                input.read(buffer);
                input.close();
                DBMS.TBMessage tbMessage = SerializationUtils.deserialize(buffer);
                List<DBMS.TBMessage.TB> tbs = new ArrayList<>(tbMessage.getTbList());
                for (DBMS.TBMessage.TB tb : tbs){
                    if (tb.getTBName().equals(tbName)) {
                        return false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public void create(){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
            Date date = new Date();// 获取当前时间
            String time = sdf.format(date);
            DBMS.TBMessage.TB tb = DBMS.TBMessage.TB.newBuilder()
                    .setTBName(tbName)
                    .setCreateDate(time)
                    .build();
            List<DBMS.TBMessage.TB> tbs;
            File file = new File("src\\DBMS_ROOT\\data\\"+dbName + "\\" +dbName+".tb");
            if(file.exists()){
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[10240];
                input.read(buffer);
                input.close();
                DBMS.TBMessage tbMessage = SerializationUtils.deserialize(buffer);
                tbs = new ArrayList<>(tbMessage.getTbList());
            }else{
                tbs = new ArrayList<>();
            }
            tbs.add(tb);
            DBMS.TBMessage tbMessage = DBMS.TBMessage.newBuilder()
                    .addAllTb(tbs)
                    .build();
            FileOutputStream fileOutputStream =
                    new FileOutputStream("src\\DBMS_ROOT\\data\\"+dbName+"\\"+dbName+".tb");
            byte[] val = SerializationUtils.serialize(tbMessage);
            for(int i = 0; i < val.length;i++){
                fileOutputStream.write(val[i]);
            }
            fileOutputStream.close();

            fileOutputStream = new FileOutputStream("src\\DBMS_ROOT\\data\\"+ dbName +"\\"+tbName+".ibd");
            DBMS.Table table = DBMS.Table.newBuilder()
                    .setTableName(tbName)
                    .addAllColumn(listColumn)
                    .build();
            val = SerializationUtils.serialize(table);
            for(int i = 0; i < val.length;i++){
                fileOutputStream.write(val[i]);
            }
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
