package com.Field;

import com.DBMS.proto.DBMS;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FieldDelete {
    public String tbName;
    public String dbName;

    public FieldDelete(String dbName,String tbName){
        this.dbName = dbName;
        this.tbName = tbName;
        System.out.println(tbName);
    }

    public boolean FieldDrop(String field){
        File file = new File("src\\DBMS_ROOT\\data\\"+dbName+"\\"+dbName+".tb");
        if(file.exists()){
            try {
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[10240];
                input.read(buffer);
                input.close();
                DBMS.TBMessage tbMessage = SerializationUtils.deserialize(buffer);
                List<DBMS.TBMessage.TB> tbs = new ArrayList<>(tbMessage.getTbList());
                for (DBMS.TBMessage.TB tb : tbs){
                    if (tb.getTBName().equals(tbName)) {
                        file = new File("src\\DBMS_ROOT\\data\\"+dbName+"\\"+tbName+".ibd");
                        input = new FileInputStream(file);
                        buffer = new byte[10240];
                        input.read(buffer);
                        input.close();
                        DBMS.Table table = SerializationUtils.deserialize(buffer);
                        List<DBMS.Table.Column> columns = new ArrayList<>(table.getColumnList());
                        int j = -1;
                        for (int i = 0;i<columns.size();i++){
                            if (columns.get(i).getColumnName().equals(field))
                                j = i;
                        }
                        if (j == -1)
                            return false;
                        DBMS.Table.Column column = columns.get(j);
                        if (column.getPrimary().equals("true"))
                            return false;
                        columns.remove(j);
                        DBMS.Table table1 = DBMS.Table.newBuilder()
                                .setTableName(tbName)
                                .addAllColumn(columns)
                                .build();
                        FileOutputStream fileOutputStream =
                                new FileOutputStream("src\\DBMS_ROOT\\data\\"+ dbName +"\\"+tbName+".ibd");
                        byte[] val = SerializationUtils.serialize(table1);
                        for(int i = 0; i < val.length;i++){
                            fileOutputStream.write(val[i]);
                        }
                        fileOutputStream.close();
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
