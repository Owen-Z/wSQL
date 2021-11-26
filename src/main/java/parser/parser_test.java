package parser;

import API.API;
import com.DBMS.proto.DBMS;
import org.apache.commons.lang3.SerializationUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLSyntaxErrorException;

public class parser_test {

    public static void main(String[] args) throws SQLSyntaxErrorException, IOException {
        API api = new API("root", "test");

//        api.parse("CREATE TABLE Persons(Id_P int NOT NULL,LastName varchar(255) NOT NULL,FirstName varchar(255),Address varchar(255),City varchar(255),CHECK (Id_P>0))");
        api.parse("CREATE TABLE IF NOT EXISTS `runoob_tbl`(\n" +
                "   `runoob_id` INT UNSIGNED AUTO_INCREMENT DEFAULT '19301137' PRIMARY KEY NOT NULL COMMENT 'good',\n" +
                "   `runoob_title` VARCHAR(100) NOT NULL,\n" +
                "   `runoob_author` VARCHAR(40) NOT NULL,\n" +
                "   `submission_date` DATE check(submission_date>1000),\n" +
                "   PRIMARY KEY ( `runoob_id` ),\n" +
                "   FOREIGN KEY(deptId) REFERENCES tb_dept1(id)\n" +
                ")");
        api.parse("insert into runoob_tbl (runoob_id,runoob_title,runoob_author,submission_date) values (29,'P','lll','2001')");
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


        FileInputStream input = new FileInputStream("src\\data\\test\\runoob_tbl.ibd");
        byte[] buffer = new byte[10240];
        int len = input.read(buffer);
        String str = new String(buffer);
        DBMS.Table table = SerializationUtils.deserialize(buffer);
        System.out.println(len);
        System.out.println(table.getColumn(0).getValList().get(0));
        input.close();
//        api.parse("CREATE DATABASE 'TEST35528'");
//        api.parse("DROP DATABASE 'TEST35528'");
//        api.parse("update test set status='P' where id=20");
//        api.parse("insert into test (id,status,name,ce,acc) values (29,'P','lll','sxsx','Arferwg')");








//        String sql = "SELECT ID, NAME, AGE FROM USER a , student b WHERE a.ID = b.id and a.name = b.name";
//        String sql = "CREATE TABLE `define_cdetag` (\n" +
//                "  `id` int(32) unsigned NOT NULL COMMENT '配置项使用时标识',\n" +
//                "  `thiSection` varchar(32) DEFAULT NULL COMMENT 'StageID白名单区间',\n" +
//                "  `thiExceptList` text COMMENT 'test名单列表',\n" +
//                "  PRIMARY KEY (`id`)\n" +
//
//               ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='test';";
//        String sql = "CREATE DATABASE 'TEST' CREATE DATABASE 'TEST1'";
//        String dbType = JdbcConstants.MYSQL;
//        //格式化输出
//        String result = SQLUtils.format(sql, dbType);
////        System.out.println(result); // 缺省大写格式
//        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
//        //解析出的独立语句的个数
//        System.out.println("size is:" + stmtList.size());
//        System.out.println(result);
////        System.out.println(stmtList.get(0).getClass() == com.alibaba.druid.sql.ast.statement.SQLCreateDatabaseStatement);
//        SQLCreateDatabaseStatement stmt = (SQLCreateDatabaseStatement) stmtList.get(0);
//        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
//        stmt.accept(visitor);
//        System.out.println(stmt.getName());
//        for(int i = 0; i < stmtList.size(); i++){
//            SQLSelectStatement stmt = (SQLSelectStatement) stmtList.get(i);
//            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
//            stmt.accept(visitor);
//            SQLSelectQuery sqlSelectQuery = stmt.getSelect().getQuery();
//            SQLSelectQueryBlock sqlSelectQueryBlock = (SQLSelectQueryBlock) sqlSelectQuery;
//            System.out.println(sqlSelectQueryBlock.getFrom());
//            System.out.println(sqlSelectQueryBlock.getWhere());
//            SQLBinaryOpExpr sqlBinaryOpExpr = (SQLBinaryOpExpr) sqlSelectQueryBlock.getWhere();
//            SQLExpr left            = sqlBinaryOpExpr.getLeft();
//            SQLBinaryOperator operator        = sqlBinaryOpExpr.getOperator();
//            SQLExpr           right           = sqlBinaryOpExpr.getRight();
//            SQLBinaryOpExpr chidl_sqlBinaryOpExpr = (SQLBinaryOpExpr) sqlBinaryOpExpr.getChildren().get(0);
//            System.out.println(chidl_sqlBinaryOpExpr.getLeft());
//        }
//        for (int i = 0; i < stmtList.size(); i++) {
//
//            SQLStatement stmt = stmtList.get(i);
//            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
//            stmt.accept(visitor);
//            //获取表名称
//            System.out.println("Tables : " + visitor.getCurrentTable());
//            //获取操作方法名称,依赖于表名称
//            System.out.println("Manipulation : " + visitor.getTables());
//            //获取字段名称
//            System.out.println("fields : " + visitor.getColumns());
//            System.out.println(visitor.getColumn("define_cdetag","thiSection").getDataType());
//            System.out.println(visitor.getColumn("define_cdetag","thiSection").getFullName());
//            System.out.println(visitor.getColumn("define_cdetag","id").isPrimaryKey());
//            System.out.println(visitor.getTableStat("define_cdetag"));
//        }

    }
}
