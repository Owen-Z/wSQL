package com.Database;

import com.DBMS.proto.DBMS;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseDelete {

    public DatabaseDelete(){}

    public void BDDelete(String databaseName){
        File file = new File("src\\DBMS_ROOT\\database.db");
        if (file.exists()){
            try {
                //将数据库从db文件中删除
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[10240];
                input.read(buffer);
                input.close();
                DBMS.Database database = SerializationUtils.deserialize(buffer);
                List<DBMS.Database.DB> dbs = new ArrayList<>(database.getDbList());
                List<DBMS.Database.DB> dbs1 = new ArrayList<>();
                for (DBMS.Database.DB db : dbs){
                    if (!db.getName().equals(databaseName))
                        dbs1.add(db);
                }
                DBMS.Database database1 = DBMS.Database.newBuilder()
                        .addAllDb(dbs1)
                        .build();
                FileOutputStream fileOutputStream =
                        new FileOutputStream("src\\DBMS_ROOT\\database.db");
                byte[] val = SerializationUtils.serialize(database1);
                for(int i = 0; i < val.length;i++){
                    fileOutputStream.write(val[i]);
                }
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //删除数据库及数据库中的文件
        file = new File("src\\DBMS_ROOT\\data\\"+databaseName);
        FileDelete(file);
    }

    public void FileDelete(File file){
        try {
            if(file.exists()){
                File[] files = file.listFiles();
                if(files != null){
                    for (File file1 : files){
                        if(file1.isDirectory())
                            FileDelete(file1);
                        file1.delete();
                    }
                }
            }
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean DBCheck(String databaseName){
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
                        return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
