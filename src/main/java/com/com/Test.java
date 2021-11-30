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
        API api = new API();
        api.parse("create database `mySqlite`");

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


    }
}
