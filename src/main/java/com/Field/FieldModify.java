package com.Field;

import com.DBMS.proto.DBMS;

import java.util.HashMap;

public class FieldModify {
    public String tbName;
    public String dbName;
    private DBMS.Table.Column Column;

    public FieldModify(String dbName,String tbName){
        this.dbName = dbName;
        this.tbName = tbName;
    }

    public boolean check(HashMap<String,String> map){
        String name = map.get("name"),
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


        return true;
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
