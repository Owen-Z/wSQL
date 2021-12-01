package com.Data;

import com.DBMS.proto.DBMS;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataInsert {
    public String tbName;
    public String dbName;

    public DataInsert(String dbName,String tbName){
        this.dbName = dbName;
        this.tbName = tbName;
    }

    public boolean DateCheck(HashMap<String,String> map){
        File file = new File("DBMS_ROOT\\data\\"+dbName + "\\" + tbName + ".ibd");
        if (file.exists()){
            try {
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[10240];
                input.read(buffer);
                input.close();
                DBMS.Table table = SerializationUtils.deserialize(buffer);
                List<DBMS.Table.Column> columnList = new ArrayList<>(table.getColumnList());
                int size = map.size();
                for(DBMS.Table.Column column : columnList){
                    if (map.containsKey(column.getColumnName())){
                        if(!column.getForeign().equals("null")){

                        }
                        String type = column.getType();
                        String val = map.get(column.getColumnName());
                        if(type.equals("INTEFER")){
                            try {
                                int i = Integer.parseInt(val);
                            }catch (Exception e){
                                return false;
                            }
                        }else if(type.equals("BOOL")){
                            if(!val.equals("TRUE") && val.equals("FALSE")){
                                return false;
                            }
                        }else if(type.equals("DOUBLE")){
                            try {
                                float i = Float.parseFloat(val);
                            }catch (Exception e){
                                return false;
                            }
                        }else if(type.equals("DATE")){

                        }else {
                            if (val.length() > Integer.parseInt(column.getTypeLength())){
                                return false;
                            }
                        }
                        size--;
                    }
                }
                if (size == 0){
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
