package API;

import com.DBMS.proto.DBMS;
import org.apache.commons.lang3.SerializationUtils;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class SelectTable {
    private String dbName;
    private String tbName;
    private DBMS.Table table;

    public SelectTable(String dbName,String tbName){
        this.dbName = dbName;
        this.tbName = tbName;
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

    public void Select(String columnName,String val,String columns){
        List<DBMS.Table.Column> list = new ArrayList<>(table.getColumnList());
        List<Integer> list1 = new ArrayList<>();
        for (int i = 0; i < list.size();i++){
            if (list.get(i).getColumnName().equals(columnName)){
                List<String> list2 = new ArrayList<>(list.get(i).getValList());
                for (int j = 0;j < list2.size();j++){
                    if (list2.get(j).equals(val)){
                        list1.add(j);
                        System.out.println("*");
                    }
                }
                break;
            }
        }
        if(columns.equals("*")){
            for (int i = 0; i < list.size();i++) {
                List<String> list2 = new ArrayList<>(list.get(i).getValList());
                for (int j = 0;j<list1.size();j++){
                    System.out.println(list2.get(list1.get(j)));
                }
            }
        }
    }

    

}
