package com.Database;

import com.DBMS.proto.DBMS;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseCreate {

    public DatabaseCreate(){}

    public void DBCreate(String databaseName){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
            Date date = new Date();// 获取当前时间
            String time = sdf.format(date);
            DBMS.Database.DB db = DBMS.Database.DB.newBuilder()
                    .setName(databaseName)
                    .setType("user")
                    .setFilePath("src\\DBMS_ROOT\\databaseName")
                    .setCreateDate(time)
                    .build();

            File file;
            //将数据库写入db文件
            file = new File("src\\DBMS_ROOT\\database.db");
            List<DBMS.Database.DB> dbs;
            if(file.exists()) {
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[10240];
                input.read(buffer);
                input.close();
                DBMS.Database database = SerializationUtils.deserialize(buffer);
                dbs = new ArrayList<>(database.getDbList());
            }else {
                dbs = new ArrayList<>();
            }
            dbs.add(db);
            DBMS.Database database1 = DBMS.Database.newBuilder()
                    .addAllDb(dbs)
                    .build();
            FileOutputStream fileOutputStream =
                    new FileOutputStream("src\\DBMS_ROOT\\database.db");
            byte[] val = SerializationUtils.serialize(database1);
            for(int i = 0; i < val.length;i++){
                fileOutputStream.write(val[i]);
            }
            fileOutputStream.close();

            //创建数据库文件夹,表记录文件以及日志文件
            file = new File("src\\DBMS_ROOT\\data\\"+databaseName);
            file.mkdir();
            file = new File("src\\DBMS_ROOT\\data\\"+databaseName+"\\"+databaseName+".tb");
            file.createNewFile();
            file = new File("src\\DBMS_ROOT\\data\\"+databaseName+"\\"+databaseName+".log");
            file.createNewFile();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int DBCheck(String databaseName){
        if (databaseName.length() >= 120) {
            return 0;
        }else {
            File file = new File("src\\DBMS_ROOT\\database.db");
            if(file.exists()){
                try {
                    FileInputStream input = new FileInputStream(file);
                    byte[] buffer = new byte[10240];
                    input.read(buffer);
                    input.close();
                    DBMS.Database database = SerializationUtils.deserialize(buffer);
                    List<DBMS.Database.DB> dbs = new ArrayList<>(database.getDbList());
                    for (DBMS.Database.DB db : dbs){
                        if (db.getName().equals(databaseName))
                            return 1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return 2;
        }
    }

}
