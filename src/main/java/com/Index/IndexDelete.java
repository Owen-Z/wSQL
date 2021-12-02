package com.Index;

import com.DBMS.proto.DBMS;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexDelete {
    public String tbName;
    public String dbName;

    public IndexDelete(String dbName,String tbName){
        this.dbName = dbName;
        this.tbName = tbName;
    }

    public boolean delete(String ixName){
        try {
            List<DBMS.Index.IX> ixList;
            File file = new File("src\\DBMS_ROOT\\data\\"+dbName+"\\"+tbName+".index");
            if (file.exists()){
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[10240];
                input.read(buffer);
                input.close();
                DBMS.Index index1 = SerializationUtils.deserialize(buffer);
                ixList = new ArrayList<>(index1.getIxList());
            }else {
                return false;
            }
            List<DBMS.Index.IX> ixList1 = new ArrayList<>();
            for (DBMS.Index.IX ix:ixList1){
                if (!ix.getIxName().equals(ixName)){
                    ixList1.add(ix);
                }
            }
            if(ixList1.size() == ixList.size())
                return false;
            DBMS.Index index1 = DBMS.Index.newBuilder()
                    .addAllIx(ixList1)
                    .build();
            byte[] val = SerializationUtils.serialize(index1);
            FileOutputStream fileOutputStream =
                    new FileOutputStream("src\\DBMS_ROOT\\data\\"+dbName+"\\"+tbName+".index");
            for(int i = 0; i < val.length;i++){
                fileOutputStream.write(val[i]);
            }
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
