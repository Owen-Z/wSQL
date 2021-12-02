package com.Table;

import com.DBMS.proto.DBMS;
import org.apache.commons.lang3.SerializationUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TableDelete {
    private String dbName;
    private String tbName;

    public TableDelete(String dbName,String tbName){
        this.dbName = dbName;
        this.tbName = tbName;
    }

    public boolean TBCheck(){
        File file = new File("src\\DBMS_ROOT\\data\\"+dbName + "\\" +dbName+".tb");
        if(file.exists()){
            try {
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[10240];
                input.read(buffer);
                input.close();
                DBMS.TBMessage tbMessage = SerializationUtils.deserialize(buffer);
                List<DBMS.TBMessage.TB> tbs = new ArrayList<>(tbMessage.getTbList());
                for (DBMS.TBMessage.TB tb : tbs){
                    if (tb.getTBName() == tbName)
                        System.out.println(tb.getTBName());
                        return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void DBDelete(){
        File file = new File("src\\DBMS_ROOT\\data\\"+dbName + "\\" +dbName+".tb");
        if(file.exists()){
            try{
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[10240];
                input.read(buffer);
                input.close();
                DBMS.TBMessage tbMessage = SerializationUtils.deserialize(buffer);
                List<DBMS.TBMessage.TB> tbs = new ArrayList<>(tbMessage.getTbList());
                int j = 0;
                for (int i = 0; i < tbs.size();i++){
                    if (tbs.get(i).getTBName().equals(tbName))
                        j = i;
                }
                tbs.remove(j);
                DBMS.TBMessage tbMessage1 = DBMS.TBMessage.newBuilder()
                        .addAllTb(tbs)
                        .build();
                FileOutputStream fileOutputStream =
                        new FileOutputStream("src\\DBMS_ROOT\\data\\"+dbName+"\\"+dbName+".tb");
                byte[] val = SerializationUtils.serialize(tbMessage1);
                for(int i = 0; i < val.length;i++){
                    fileOutputStream.write(val[i]);
                }
                fileOutputStream.close();

                file = new File("src\\DBMS_ROOT\\data\\"+ dbName +"\\"+tbName+".ibd");
                file.delete();
                file = new File("src\\DBMS_ROOT\\data\\"+ dbName +"\\"+tbName+".index");
                file.delete();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
