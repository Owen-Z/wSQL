package com.Field;

import com.DBMS.proto.DBMS;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FieldModify {
    public String tbName;
    public String dbName;
    private DBMS.Table.Column Column;

    public FieldModify(String dbName,String tbName){
        this.dbName = dbName;
        this.tbName = tbName;
    }

    public boolean check(HashMap<String,String> map){
        String  name = map.get("name"),
                type = map.get("type"),
                def = map.get("def"),
                comment = map.get("comment"),
                auto = map.get("auto"),
                primaryKey = map.get("primaryKey"),
                notNUll = map.get("notNUll"),
                check = map.get("check"),
                foreignKey= map.get("foreignKey"),
                unique = map.get("unique"),
                length = getLength(type);
        try {
            File file = new File("src\\DBMS_ROOT\\data\\"+dbName+"\\"+tbName+".ibd");
            if (file.exists()){
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[10240];
                input.read(buffer);
                input.close();
                DBMS.Table table = SerializationUtils.deserialize(buffer);
                List<DBMS.Table.Column> columns = new ArrayList<>(table.getColumnList());
                List<DBMS.Table.Column> columns1 = new ArrayList<>();
                for (DBMS.Table.Column column : columns){
                    if (column.getColumnName().equals(name)){
                        if (!column.getForeign().equals("null")){
                            return false;
                        }
                        List<String> val = new ArrayList<>(column.getValList());
                        DBMS.Table.Column column1 = DBMS.Table.Column
                                .newBuilder()
                                .setColumnName(name)
                                .setType(type)
                                .setTypeLength(length)
                                .setDefault(def)
                                .setComment(comment)
                                .setForeign(foreignKey)
                                .setIdentity(auto)
                                .setCheck(check)
                                .setUnique(unique)
                                .setNotNull(notNUll)
                                .setPrimary(primaryKey)
                                .addAllVal(val)
                                .build();
                        columns1.add(column1);
                    }else {
                        columns1.add(column);
                    }
                }
                DBMS.Table table1 = DBMS.Table.newBuilder()
                        .setTableName(table.getTableName())
                        .addAllColumn(columns1)
                        .build();
                FileOutputStream fileOutputStream = new FileOutputStream("src\\DBMS_ROOT\\data\\"+ dbName +"\\"+tbName+".ibd");
                byte[] val = SerializationUtils.serialize(table1);
                for(int i = 0; i < val.length;i++){
                    fileOutputStream.write(val[i]);
                }
                fileOutputStream.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
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

}
