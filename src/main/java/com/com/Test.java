package com.com;

import API.API;
import com.DBMS.proto.DBMS;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException {
        //数据库创建
//        API api = new API();
//        api.parse("log.create database `mySqlite`");

        //数据库查询
//        File file = new File("src\\DBMS_ROOT\\database.db");
//        FileInputStream input = new FileInputStream(file);
//        byte[] buffer = new byte[10240];
//        input.read(buffer);
//        input.close();
//        DBMS.Database database = SerializationUtils.deserialize(buffer);
//        System.out.println(database.getDb(0).getCreateDate());

        //数据库删除
//        API api = new API();
//        api.parse("drop database `mySqlite`");

        //表创建
//        API api = new API();
//        api.parse("CREATE TABLE `dbs`(\n" +
//                "   `id` INT UNSIGNED PRIMARY KEY CHECK(ID > 1),\n" +
//                "   `name` VARCHAR(100) NOT NULL\n" +
//                ")");

        //表创建
//        API api = new API();
//        api.parse("CREATE TABLE IF NOT EXISTS `SC`(\n"+
//                "`ID` INTEGER PRIMARY KEY CHECK(ID > 1),\n"+
//                "`NAME` VARCHAR(20) NOT NULL\n"+
//                ")");

        //表删除
//        API api = new API();
//        api.parse("DROP TABLE DBS");

        //字段删除测试
//        API api = new API();
//        api.parse("ALTER TABLE SC DROP COLUMN NAME");

//        字段增加测试
//        API api = new API();
//        api.parse("ALTER TABLE SC ADD COLUMN CLASS VARCHAR(40)");

        //字段修改测试
//        API api = new API();
//        api.parse("ALTER TABLE DBS MODIFY COLUMN NAME INTEGER CHECK(NAME > 10 AND NAME < 10)");

        //数据增加测试
//        File file = new File("src\\DBMS_ROOT\\data\\MYSQLITE\\SC.ibd");
//        FileInputStream input = new FileInputStream(file);
//        byte[] buffer = new byte[10240];
//        input.read(buffer);
//        input.close();
//        DBMS.Table table = SerializationUtils.deserialize(buffer);
//        System.out.println(table.getColumnList().get(1).getType());

//        API api = new API();
//        api.parse("INSERT INTO DBS (ID,NAME) VALUES (2,1)");

//        File file = new File("src\\DBMS_ROOT\\data\\MYSQLITE\\DBS.ibd");
//        FileInputStream input = new FileInputStream(file);
//        byte[] buffer = new byte[10240];
//        input.read(buffer);
//        input.close();
//        DBMS.Table table = SerializationUtils.deserialize(buffer);
//        System.out.println(table.getColumnList().get(0).getCheck());

        //数据更新测试
//        API api = new API();
//        api.parse("UPDATE DBS SET NAME = 2 WHERE ID > 0");
//
//        File file = new File("src\\DBMS_ROOT\\data\\MYSQLITE\\DBS.ibd");
//        FileInputStream input = new FileInputStream(file);
//        byte[] buffer = new byte[10240];
//        input.read(buffer);
//        input.close();
//        DBMS.Table table = SerializationUtils.deserialize(buffer);
//        System.out.println(table);

        //数据删除测试
//        API api = new API();
//        api.parse("DELETE FROM SC WHERE ID = 2 AND ID = 3");

        //数据查询
//        API api = new API();
//        api.parse("SELECT * FROM DBS");

        //字段更新
//        API api = new API();
//        api.parse("ALTER TABLE DBS MODIFY COLUMN NAME VARCHAR(20) CHECK(DBS > 20 AND DBS < 20)");

        //索引建立
//        API api = new API();
//        api.parse("CREATE INDEX ID ON SC(ID)");

        //索引删除
//        API api = new API();
//        api.parse("DROP INDEX NAME ON SC");

        String string = "2014-3-17";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(string);
            System.out.println(date.toLocaleString().split(" ")[0]);//切割掉不要的时分秒数据
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static boolean isData(String str){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(str);
            System.out.println(date.toLocaleString().split(" ")[0]);//切割掉不要的时分秒数据
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
