package API;

import com.DBMS.proto.DBMS;

import java.util.List;

public class InsertTable {
    String tableName;
    DBMS.Table.Column column;
    DBMS.Table table;
    List<DBMS.Table.Column> listColumn;

    public InsertTable(String s){
        tableName = s;
    }

}
