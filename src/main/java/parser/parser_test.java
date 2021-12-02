package parser;

import API.API;

import java.io.*;
import java.sql.SQLSyntaxErrorException;

public class parser_test {

    public static void main(String[] args) throws SQLSyntaxErrorException, IOException {
        API api = new API();
//        SelectTable selectTable = new SelectTable("1","2");
//        selectTable.getTable();
//        selectTable.Select("`RUNOOB_ID`","29","*");
//        api.parse("CREATE TABLE Persons(Id_P int NOT NULL,LastName varchar(255) NOT NULL,FirstName varchar(255),Address varchar(255),City varchar(255),CHECK (Id_P>0))");


//        api.parse("CREATE TABLE IF NOT EXISTS `runoo233332333222b`(\n" +
//                "   `runoob_id` INTEGER UNSIGNED AUTO_INCREMENT DEFAULT '19301137' PRIMARY KEY NOT NULL COMMENT 'good' check (id > 10),\n" +
//                "   `runoob_title` VARCHAR(100) ,\n" +
//                "   `runoob_author` VARCHAR(40) NOT NULL,\n" +
//                "   `submission_date` DATE,\n" +
//                "   PRIMARY KEY ( `runoob_id` ),\n" +
//                "   FOREIGN KEY(deptId) REFERENCES tb_dept1(id)\n" +
//                ")");


//        api.parse("CREATE TABLE IF NOT EXISTS `runoob_tbl`(\n" +
//                "   `runoob_id` INT UNSIGNED AUTO_INCREMENT DEFAULT '19301137' PRIMARY KEY NOT NULL COMMENT 'good',\n" +
//                "   `runoob_title` VARCHAR(100) NOT NULL UNIQUE,\n" +
//                "   `runoob_author` VARCHAR(40) NOT NULL,\n" +
//                "   `submission_date` DATE check(submission_date>1000),\n" +
//                "   PRIMARY KEY ( `runoob_id` ),\n" +
//                "   FOREIGN KEY(deptId) REFERENCES tb_dept1(id)\n" +
//                ")");
//        api.parse("insert into runoob_tbl (runoob_id,runoob_title,runoob_author,submission_date) values (29,'P','lll','2001')");
//            api.parse("insert into runoob_tbl values(1,'2','3',2000.0.1)");
//        File file = new File("src\\data\\test\\runoob_tbl.ibd");
//        String path = "src\\data\\test\\runoob_tbl.ibd";
//        BufferedInputStream reader = new BufferedInputStream(new InputStream() {
//            @Override
//            public int read() throws IOException {
//                return 0;
//            }
//        });
//        byte[] val = new byte[0];
//        reader.read(val,0,1024);
//        System.out.println(val);
////        byte[] val1 = val.getBytes(StandardCharsets.UTF_8);
//        DBMS.Table table = SerializationUtils.deserialize(val);
//        System.out.println(table.getTableName());


//        FileInputStream input = new FileInputStream("src\\data\\test\\runoob_tbl.ibd");
//        byte[] buffer = new byte[10240];
//        int len = input.read(buffer);
//        String str = new String(buffer);
//        DBMS.Table table = SerializationUtils.deserialize(buffer);
//        System.out.println(len);
//        System.out.println(table.getColumn(0).getValList().get(0));
//        input.close();



//        api.parse("CREATE DATABASE 'TEST35528'");
//        api.parse("DROP DATABASE 'TEST35528'");
//        api.parse("update test set status='P' where id=20");
//        api.parse("insert into test (id,status,name,ce,acc) values (29,'P','lll','sxsx','Arferwg')");
//        api.parse("select s.sname FROM student s,\n" +
//                "(SELECT sno, avg(grade) avg FROM sc GROUP BY sno) a\n" +
//                "WHERE s.sno = a.sno AND avg = ( SELECT MAX(av) FROM ( SELECT avg( grade) av FROM sc GROUP BY sno) b)\n");
//        api.parse("select name, classname from student a , class b where (a.id = b.id OR a.name = b.name AND A.ID = 10) ");
//        api.parse("select * from student a where (id = 10) ");

//        api.parse("CREATE INDEX index_name ON table_name (column_list)");
//        }
        api.setUserName("User1");
        System.out.println(api.JudgeAuthority());
    }
}
