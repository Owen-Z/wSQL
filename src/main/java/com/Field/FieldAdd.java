package com.Field;

import com.DBMS.proto.DBMS;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FieldAdd {
    public String tbName;
    public String dbName;
    private DBMS.Table.Column Column;

    public FieldAdd(String dbName,String tbName){
        this.dbName = dbName;
        this.tbName = tbName;
    }

    public void FieldCreate(HashMap<String,String> map){
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
        Column = DBMS.Table.Column
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
    }

    public boolean FieldSave(){
        try {
            File file = new File("src\\DBMS_ROOT\\data\\"+ dbName +"\\"+tbName+".ibd");
            if(file.exists()) {
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[10240];
                input.read(buffer);
                input.close();
                DBMS.Table table = SerializationUtils.deserialize(buffer);
                List<DBMS.Table.Column> columnList = new ArrayList<>(table.getColumnList());
                int len = 0;
                if(Column.getPrimary().equals("true")){
                    for(DBMS.Table.Column column : columnList){
                        if (column.getPrimary().equals("true"))
                            return false;
                    }
                }
                if(columnList.size() >= 1){
                    len = columnList.get(0).getValList().size();
                }
                List<String> strings = new ArrayList<>();
                for (int i = 0; i < len;i++){
                    if(Column.getDefault().equals("null")){
                        String s = null;
                        strings.add(s);
                    } else {
                        strings.add(Column.getDefault());
                    }
                }
                String name = Column.getColumnName(),
                        type = Column.getType(),
                        def = Column.getDefault(),
                        comment = Column.getComment(),
                        auto = Column.getIdentity(),
                        primaryKey = Column.getPrimary(),
                        notNUll = Column.getNotNull(),
                        check = Column.getCheck(),
                        foreignKey= Column.getForeign(),
                        unique = Column.getUnique(),
                        length = Column.getTypeLength();
                Column = DBMS.Table.Column
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
                        .addAllVal(strings)
                        .build();
                columnList.add(Column);
                DBMS.Table table1 = DBMS.Table.newBuilder()
                        .setTableName(table.getTableName())
                        .addAllColumn(columnList)
                        .build();
                byte[] val = SerializationUtils.serialize(table1);
                FileOutputStream fileOutputStream =
                        new FileOutputStream("src\\DBMS_ROOT\\data\\"+ dbName +"\\"+tbName+".ibd");
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

    public boolean FieldCheck(String field){
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
                        file = new File("src\\DBMS_ROOT\\data\\"+dbName+"\\"+tbName+".ibd");
                        input = new FileInputStream(file);
                        buffer = new byte[10240];
                        input.read(buffer);
                        input.close();
                        DBMS.Table table = SerializationUtils.deserialize(buffer);
                        List<DBMS.Table.Column> columns = new ArrayList<>(table.getColumnList());
                        int j = -1;
                        for (int i = 0;i<columns.size();i++){
                            if (columns.get(i).getColumnName().equals(field))
                                j = i;
                        }
                        if (j == -1){
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }




}
