package com.com;

import API.API;
import com.DBMS.proto.DBMS;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException {
        //数据库创建
//        API api = new API();
//        api.parse("create database `mySqlite`");

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
//        api.parse("CREATE TABLE IF NOT EXISTS `runoob_tbl`(\n" +
//                "   `runoob_id` INT UNSIGNED AUTO_INCREMENT DEFAULT '19301137' PRIMARY KEY NOT NULL COMMENT 'good',\n" +
//                "   `runoob_title` VARCHAR(100) NOT NULL UNIQUE,\n" +
//                "   `runoob_author` VARCHAR(40) NOT NULL,\n" +
//                "   `submission_date` DATE check(submission_date>1000),\n" +
//                "   PRIMARY KEY ( `runoob_id` ),\n" +
//                "   FOREIGN KEY(deptId) REFERENCES tb_dept1(id)\n" +
//                ")");

        //表创建
//        API api = new API();
//        api.parse("CREATE TABLE IF NOT EXISTS `SC`(\n"+
//                "`ID` INTEGER PRIMARY KEY,\n"+
//                "`NAME` VARCHAR(20) NOT NULL\n"+
//                ")");

        //表删除
//        API api = new API();
//        api.parse("DROP TABLE runoob_tbl");

        //字段删除测试
//        API api = new API();
//        api.parse("ALTER TABLE SC DROP COLUMN NAME");

        //字段增加测试
//        API api = new API();
//        api.parse("ALTER TABLE SC ADD COLUMN CLASS VARCHAR(40)");

        //字段修改测试
//        API api = new API();
//        api.parse("ALTER TABLE SC MODIFY COLUMN NAME INTEGER NOt NULL CHECK(NAME > 10)");

        //数据增加测试
//        File file = new File("src\\DBMS_ROOT\\data\\MYSQLITE\\SC.ibd");
//        FileInputStream input = new FileInputStream(file);
//        byte[] buffer = new byte[10240];
//        input.read(buffer);
//        input.close();
//        DBMS.Table table = SerializationUtils.deserialize(buffer);
//        System.out.println(table.getColumnList().get(1).getType());

//        API api = new API();
//        api.parse("INSERT INTO SC (ID,NAME) VALUES (3,3)");

//        File file = new File("src\\DBMS_ROOT\\data\\MYSQLITE\\SC.ibd");
//        FileInputStream input = new FileInputStream(file);
//        byte[] buffer = new byte[10240];
//        input.read(buffer);
//        input.close();
//        DBMS.Table table = SerializationUtils.deserialize(buffer);
//        System.out.println(table.getColumnList().get(1).getValList().get(2));

        //数据更新测试
//        API api = new API();
//        api.parse("UPDATE SC SET CLASS = 3 WHERE ID = 1");
//
//        File file = new File("src\\DBMS_ROOT\\data\\MYSQLITE\\SC.ibd");
//        FileInputStream input = new FileInputStream(file);
//        byte[] buffer = new byte[10240];
//        input.read(buffer);
//        input.close();
//        DBMS.Table table = SerializationUtils.deserialize(buffer);
//        System.out.println(table.getColumnList().get(1).getValList().get(0));

        //数据删除测试
//        API api = new API();
//        api.parse("DELETE FROM SC WHERE ID = 2 AND ID = 3");

        //数据查询
//        API api = new API();
//        api.parse("SELECT * FROM SC WHERE (ID = 1)");

        //字段更新
//        API api = new API();
//        api.parse("ALTER TABLE SC MODIFY COLUMN CLASS VARCHAR(20) ");


    }
}
