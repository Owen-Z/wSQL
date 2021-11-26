package API;

import com.DBMS.proto.DBMS;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateTable {
    String tableName;
    DBMS.Table table;
    List<DBMS.Table.Column> listColumn;
    String dbName;

    //传参构造实例
    public CreateTable(String tableName,String dbName){
        this.tableName = tableName;
        this.dbName = dbName;
        listColumn = new ArrayList<>();
    }

    //判断是否重复
    public boolean check(){
        boolean exist = false;
        // 读取指定文件，并比较每一行数据判断是否出现重复
        try{
            File file = new File("stc\\"+dbName);
            if(file.exists()){
                File[] fileArray= file.listFiles();
                for(int i = 0;i<fileArray.length;i++){
                    if(fileArray[i].getName().equals(dbName+".ibd")){
                        exist = true;
                        break;
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return exist;
    }

    //设置普通sql语句
    public void addColumn(HashMap<String,String> map){
        String name = null,type = null,length = null,def = null,comment = null,auto = null,
                primaryKey = null,notNUll = null,check = null,foreignKey=null;
        for (Map.Entry<String,String> entry:map.entrySet()){
            if(entry.getKey().equals("name"))
                name = entry.getValue();
            if(entry.getKey().equals("type"))
                type = entry.getValue();
            if(entry.getKey().equals("length"))
                length = entry.getValue();
            if(entry.getKey().equals("comment"))
                comment = entry.getValue();
            if(entry.getKey().equals("def"))
                def = entry.getValue();
            if(entry.getKey().equals("primaryKey"))
                primaryKey = entry.getValue();
            if(entry.getKey().equals("auto"))
                auto = entry.getValue();
            if(entry.getKey().equals("notNUll"))
                notNUll = entry.getValue();
            if(entry.getKey().equals("check"))
                check = entry.getValue();
            if(entry.getKey().equals("foreignKey"))
                foreignKey = entry.getValue();
        }
        List<DBMS.Table.Column.Constraint> list = new ArrayList<>();
        DBMS.Table.Column.Constraint constraint0 = DBMS.Table.Column.Constraint.newBuilder()
                .setConstraintName("primaryKey")
                .setDescribe(primaryKey)
                .build();
        DBMS.Table.Column.Constraint constraint1 = DBMS.Table.Column.Constraint.newBuilder()
                .setConstraintName("notNULL")
                .setDescribe(notNUll)
                .build();
        DBMS.Table.Column.Constraint constraint2 = DBMS.Table.Column.Constraint.newBuilder()
                .setConstraintName("check")
                .setDescribe(check)
                .build();
        DBMS.Table.Column.Constraint constraint3 = DBMS.Table.Column.Constraint.newBuilder()
                .setConstraintName("foreignKey")
                .setDescribe(foreignKey)
                .build();
        DBMS.Table.Column.Constraint constraint4 = DBMS.Table.Column.Constraint.newBuilder()
                .setConstraintName("comment")
                .setDescribe(comment)
                .build();
        DBMS.Table.Column.Constraint constraint5 = DBMS.Table.Column.Constraint.newBuilder()
                .setConstraintName("auto")
                .setDescribe(auto)
                .build();
        DBMS.Table.Column.Constraint constraint6 = DBMS.Table.Column.Constraint.newBuilder()
                .setConstraintName("def")
                .setDescribe(def)
                .build();

        list.add(constraint0);
        list.add(constraint1);
        list.add(constraint2);
        list.add(constraint3);
        list.add(constraint4);
        list.add(constraint5);
        list.add(constraint6);

        DBMS.Table.Column column = DBMS.Table.Column.newBuilder()
                .setColumnName(name)
                .setType(type)
                .setTypeLength(length)
                .addAllConstraint(list)
                .build();

        listColumn.add(column);

    }

    //
    public void setPrimaryKey(String columnName){
        for (DBMS.Table.Column column: listColumn){
            if (column.getColumnName().equals(columnName)){
                String name = null,type = null,length = null;
                name = column.getColumnName();
                type = column.getType();
                length = column.getTypeLength();
                List<DBMS.Table.Column.Constraint> list = new ArrayList<>(column.getConstraintList());
                for (DBMS.Table.Column.Constraint constraint:list){
                    if(constraint.getConstraintName().equals("primaryKey")){
                        list.remove(constraint);
                        break;
                    }
                }
                DBMS.Table.Column.Constraint constraint = DBMS.Table.Column.Constraint.newBuilder()
                        .setConstraintName("primaryKey")
                        .setDescribe("true")
                        .build();
                list.add(constraint);
                DBMS.Table.Column column1 = DBMS.Table.Column.newBuilder()
                        .setColumnName(name)
                        .setType(type)
                        .setTypeLength(length)
                        .addAllConstraint(list)
                        .build();
                listColumn.remove(column);
                listColumn.add(column1);
                return;
            }
        }
    }

    public void setForeignKey(String columnName){
        for (DBMS.Table.Column column: listColumn){
            if (column.getColumnName().equals(columnName)){
                String name = null, type = null, length = null;
                name = column.getColumnName();
                type = column.getType();
                length = column.getTypeLength();
                List<DBMS.Table.Column.Constraint> list = new ArrayList<>(column.getConstraintList());
                for (DBMS.Table.Column.Constraint constraint:list){
                    if(constraint.getConstraintName().equals("foreignKey")){
                        list.remove(constraint);
                        break;
                    }
                }
                DBMS.Table.Column.Constraint constraint = DBMS.Table.Column.Constraint.newBuilder()
                        .setConstraintName("foreignKey")
                        .setDescribe("true")
                        .build();
                list.add(constraint);
                DBMS.Table.Column column1 = DBMS.Table.Column.newBuilder()
                        .setColumnName(name)
                        .setType(type)
                        .setTypeLength(length)
                        .addAllConstraint(list)
                        .build();
                listColumn.remove(column);
                listColumn.add(column1);
                return;
            }
        }
    }

    public void create(){
        try {
            File file = new File("src\\data\\test\\"+tableName+".ibd");
            FileOutputStream fileOutputStream = new FileOutputStream("src\\data\\test\\"+tableName+".ibd");
            DBMS.Table table = DBMS.Table.newBuilder()
                    .setTableName(tableName)
                    .addAllColumn(listColumn)
                    .build();
            byte[] val = SerializationUtils.serialize(table);
            for(int i = 0; i < val.length;i++){
                fileOutputStream.write(val[i]);
            }
            System.out.println(val);
            DBMS.Table table1 = SerializationUtils.deserialize(val);
            System.out.println(table1.getTableName());
            System.out.println("11");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
