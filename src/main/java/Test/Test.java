package Test;

import com.DBMS.proto.DBMS;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerOutput;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Test {
    static class saveTest{
        public static void main(String[] args){
            String path = "Data\\model";
            File file = new File(path);
            file.mkdir();
        }
    }

    static class databaseTest{
        public static void main(String[] args){
            List<String> list =new ArrayList<>();
            list.add("1");
            list.add("2");
            list.add("3");
            list.add("4");
            list.add("5");
            DBMS.Table.Column column = DBMS.Table.Column.newBuilder()
                    .addAllVal(list)
                    .build();
            System.out.println(column.getValList());
        }
    }
}
