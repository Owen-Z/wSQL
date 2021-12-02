package com.Transaction;

import com.DBMS.proto.DBMS;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Commit {
    private String tbName;
    private String dbName;
    private DBMS.Table table;
    private boolean state;

    public Commit(){
        state = false;
    }

    public boolean getState(){
        return state;
    }

    public void setTbName(String tbName){
        this.tbName = tbName;
    }

    public void setDbName(String dbName){
        this.dbName = dbName;
    }

    public void getTB() {
        try {
            File file = new File("src\\DBMS_ROOT\\data\\" + dbName + "\\" + tbName + ".ibd");
            if (file.exists()) {
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[10240];
                input.read(buffer);
                input.close();
                table = SerializationUtils.deserialize(buffer);
                state = true;
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void saveTB() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("src\\DBMS_ROOT\\data\\"+ dbName +"\\"+tbName+".ibd");
        byte[] val = SerializationUtils.serialize(table);
        for(int i = 0; i < val.length;i++){
            fileOutputStream.write(val[i]);
        }
        fileOutputStream.close();
    }


}
