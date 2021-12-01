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
//        api.parse("ALTER TABLE SC ADD COLUMN NAME VARCHAR(40) DEFAULT WEF COMMENT 2233");

        //字段修改测试
//        API api = new API();
//        api.parse("ALTER TABLE SC MODIFY COLUMN NAME INTEGER NOt NULL CHECK(NAME > 10)");

        //字段修改测试
        API api = new API();
        api.parse("INSERT INTO SC (ID,NAME) VALUES (1,2)");


    }
}
