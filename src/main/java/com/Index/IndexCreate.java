package com.Index;

import com.DBMS.proto.DBMS;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class IndexCreate {
    public String tbName;
    public String dbName;
    private DBMS.Table.Column Column;

    public IndexCreate(String dbName,String tbName){
        this.dbName = dbName;
        this.tbName = tbName;
    }

    public boolean checkField(String index){
        try {
            File file = new File("src\\DBMS_ROOT\\data\\"+dbName+"\\"+tbName+".ibd");
            if (file.exists()){
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[10240];
                input.read(buffer);
                input.close();
                DBMS.Table table = SerializationUtils.deserialize(buffer);
                List<DBMS.Table.Column> columns = new ArrayList<>(table.getColumnList());
                for(DBMS.Table.Column column:columns){
                    if(column.getColumnName().equals(index)){
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
