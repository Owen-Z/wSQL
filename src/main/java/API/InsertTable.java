package API;

import com.DBMS.proto.DBMS;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class InsertTable {
    String tableName;
    DBMS.Table table;
    List<DBMS.Table.Column> listColumn;

    public InsertTable(String s){
        tableName = s;
    }

    public void getTable(){
        try {
            FileInputStream input = new FileInputStream("src\\data\\test\\runoob_tbl.ibd");
            byte[] buffer = new byte[1024];
            int len = input.read(buffer);
            String str = new String(buffer);
            table = SerializationUtils.deserialize(buffer);
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTable(List<String> val){
        List<DBMS.Table.Column> list = new ArrayList<>(table.getColumnList());
        List<DBMS.Table.Column> cList = new ArrayList<>();
        for (int i = 0;i<list.size();i++){
            List<String> valList = new ArrayList<>(list.get(i).getValList());
            valList.add(val.get(i));
            String name = list.get(i).getColumnName();
            String type = list.get(i).getType();
            String length = list.get(i).getTypeLength();
            List<DBMS.Table.Column.Constraint> constraintList =
                    new ArrayList<>(list.get(i).getConstraintList());
            DBMS.Table.Column column = DBMS.Table.Column.newBuilder()
                    .setColumnName(name)
                    .setType(type)
                    .setTypeLength(length)
                    .addAllVal(valList)
                    .addAllConstraint(constraintList)
                    .build();
            cList.add(column);
        }
        DBMS.Table table1 = DBMS.Table.newBuilder()
                .setTableName(tableName)
                .addAllColumn(cList)
                .build();
        table = table1;

        try {
            File file = new File("src\\data\\test\\"+tableName+".ibd");
            FileOutputStream fileOutputStream = new FileOutputStream("src\\data\\test\\"+tableName+".ibd");
            System.out.println(table.getColumn(1).getValList().get(0));
            byte[] vals = SerializationUtils.serialize(table);
            for(int i = 0; i < vals.length;i++){
                fileOutputStream.write(vals[i]);
            }
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
