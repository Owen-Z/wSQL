package tools;

import com.DBMS.proto.DBMS;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class Tools {
    public Tools(){}

    public List<String> getDB(){
        List<String> DB = new ArrayList<>();
        try {
            File file = new File("src\\DBMS_ROOT\\database.db");
            FileInputStream input = new FileInputStream(file);
            byte[] buffer = new byte[10240];
            input.read(buffer);
            input.close();
            DBMS.Database database = SerializationUtils.deserialize(buffer);
            List<DBMS.Database.DB> dbs = new ArrayList<>(database.getDbList());
            for (DBMS.Database.DB db : dbs){
                DB.add(db.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DB;
    }

    public List<String> getTB(String dbName){
        List<String> TB = new ArrayList<>();
        try {
            File file = new File("src\\DBMS_ROOT\\data\\"+dbName+"\\"+dbName+".tb");
            FileInputStream input = new FileInputStream(file);
            byte[] buffer = new byte[10240];
            input.read(buffer);
            input.close();
            DBMS.TBMessage tbMessage = SerializationUtils.deserialize(buffer);
            List<DBMS.TBMessage.TB> tbs = new ArrayList<>(tbMessage.getTbList());
            for (DBMS.TBMessage.TB tb : tbs){
                TB.add(tb.getTBName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TB;
    }
}
