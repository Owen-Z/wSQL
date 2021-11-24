package parser;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlKey;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlPrimaryKey;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerInsertStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerUpdateStatement;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.util.JdbcConstants;

import java.io.*;
import java.sql.SQLOutput;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class parser_test {

    /**
     * 删除指定数据库
     *
     * @param  dbName, filePath
     */
    public void DeleteDatabase(String dbName, String filePath){
        // 读取指定文件，并比较每一行数据判断是否出现重复
        try{

            // 读文件获取除删除数据库外的其他数据库名，存储到lines当中
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            List<String> lines = new ArrayList<String>();
            String line = null;
            while((line= br.readLine()) != null ){
                if(!line.equals(dbName)){
                    lines.add(line);
                }
            }
            br.close();

            // 重新修改文件，将新的数据存入WSql.db当中
            OutputStream os = new FileOutputStream("src/data/database/WSql.db");
            PrintWriter pw = new PrintWriter(os);
            for(String s : lines){
                pw.println(s);
            }
            pw.close();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断数据库名或者表名是否重复
     *
     * 重复则返回true, 不重复则返回false
     * @param str, filePath
     * @return exist
     */
    public boolean CheckExist(String str, String filePath){
        boolean exist = false;  // 表示是否重复的BOOLEAN变量

        // 读取指定文件，并比较每一行数据判断是否出现重复
        try{
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line = null;
            while((line= br.readLine()) != null && !exist){
                if(line.equals(str)){
                    exist = true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return exist;
    }

    public void parse(String statement) {
        // 使用druid解析语句
        // 第一个参数为SQL语句
        // 第二个参数为解析的数据库类型
        List<SQLStatement> statementList = SQLUtils.parseStatements(statement, JdbcConstants.MYSQL);
        // 单语句解析，只有一条数据
        if (!statement.isEmpty()) {
            SQLStatement sqlStatement = statementList.get(0);
            // 插入语句解析

            // 创建数据库语句
            if(sqlStatement instanceof SQLCreateDatabaseStatement){
                // 转换语句格式
                SQLCreateDatabaseStatement createDatabaseStatement = (SQLCreateDatabaseStatement) sqlStatement;

                // 通过解析得到的数据库名含有''   e.g. 'test'
                String dbName = createDatabaseStatement.getName().toString();
                // 去除了''     e.g. test
                String storeDBName = dbName.substring(1, dbName.length() - 1);

                // 判断数据库名是否存在或者非法(长度大于120)，并将数据库名存储在WSql.db文件下
                if(storeDBName.length() >= 120){
                    System.out.println("数据库名称过长");
                }else if(CheckExist(storeDBName, "src/data/database/WSql.db")){
                    System.out.println("数据库已存在");
                }else{
                    OutputStream os = null;
                    PrintWriter pw = null;
                    try{
                        // true代表在文件末尾写入
                        os = new FileOutputStream("src/data/database/WSql.db", true);
                        pw = new PrintWriter(os);
                        pw.println(storeDBName);
                        pw.close();
                        os.close();

                        // 创建并存储该数据库的表信息
                        File file = new File("src/data/tables/" + storeDBName + ".tb");
                        file.createNewFile();
                    }catch (FileNotFoundException e){
                        System.out.println(e);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.println("数据库创建成功");
                }

            }

            // 删除数据库指令
            if(sqlStatement instanceof SQLDropDatabaseStatement){
                // 转换语句格式
                SQLDropDatabaseStatement sqlDropDatabaseStatement = (SQLDropDatabaseStatement) sqlStatement;

                // 通过解析得到的数据库名含有''   e.g. 'test'
                String dbName = sqlDropDatabaseStatement.getDatabase().toString();
                // 去除了''     e.g. test
                String storeDBName = dbName.substring(1, dbName.length() - 1);

                /*
                    判断数据库是否存在
                    存在则删除该数据库
                    否则提示该数据库不存在
                 */
                if(!CheckExist(storeDBName, "src/data/database/WSql.db")){
                    System.out.println("数据库不存在");
                }else{
                    DeleteDatabase(storeDBName, "src/data/database/WSql.db");
                    System.out.println("数据库删除成功");
                }

            }

            // 创建表指令
            if(sqlStatement instanceof SQLCreateTableStatement){
                SQLCreateTableStatement sqlCreateTableStatement = (SQLCreateTableStatement) sqlStatement;
                System.out.println(sqlCreateTableStatement);

                // 表名 e.g. 'test'
                String tableName = sqlCreateTableStatement.getName().toString();
                // 用以存储的表名 e.g. test
                String storeTableName = tableName.substring(1, tableName.length() - 1);


                List<SQLTableElement> elements = sqlCreateTableStatement.getTableElementList();

                SQLTableElement element = elements.get(4);
//                System.out.println(sqlCreateTableStatement.getTableElementList());
//                System.out.println(((SQLColumnDefinition) element).getName());
//                System.out.println(((MySqlPrimaryKey)elements.get(4)).getColumns().get(0));
                System.out.println(((MySqlPrimaryKey)element).isHasConstaint());
                MySqlASTVisitor visitor = new MySqlSchemaStatVisitor();
                element.accept(visitor);
                System.out.println(visitor.visit(((MySqlPrimaryKey)element)));
            }



            if (sqlStatement instanceof SQLServerInsertStatement) {
                // 转换
                SQLServerInsertStatement insertStatement = (SQLServerInsertStatement) sqlStatement;
                // 获取列名
                List<SQLExpr> columns = insertStatement.getColumns();
                List<String> columnsName = new ArrayList<>(columns.size());
                for (SQLExpr column : columns) {
                    columnsName.add(((SQLIdentifierExpr) column).getName());
                }
                System.out.println(columnsName);
                // 获取值
                List<SQLInsertStatement.ValuesClause> valuesList = insertStatement.getValuesList();
                List<List<Object>> dataList = new ArrayList<>();
                for (SQLInsertStatement.ValuesClause valuesClause : valuesList) {
                    List<SQLExpr> values = valuesClause.getValues();
                    List<Object> data = new ArrayList<>(columnsName.size());
                    for (SQLExpr value : values) {
                        data.add(getValue(value));
                    }
                    dataList.add(data);
                }
                System.out.println(dataList);
                // 获取表名
                System.out.println(insertStatement.getTableName().getSimpleName());
            } else if (sqlStatement instanceof SQLServerUpdateStatement) {
                // 更新语句解析
                SQLServerUpdateStatement updateStatement = (SQLServerUpdateStatement) sqlStatement;
                // 获取更新的值和内容
                List<SQLUpdateSetItem> items = updateStatement.getItems();
                Map<String, Object> updateMap = new HashMap<>(items.size());
                for (SQLUpdateSetItem item : items) {
                    updateMap.put(((SQLIdentifierExpr) item.getColumn()).getName(), getValue(item.getValue()));
                }
                System.out.println(updateMap);
                // 获取条件，条件比较复杂，需根据实际情况自行提取
                SQLBinaryOpExpr where = (SQLBinaryOpExpr) updateStatement.getWhere();
                System.out.println(where);
                // 获取表名
                System.out.println(updateStatement.getTableName().getSimpleName());
            }
        }

    }

    private static Object getValue(SQLExpr value) {
        // TODO 判断更多的种类
        if (value instanceof SQLIntegerExpr) {
            // 值是数字
            return ((SQLIntegerExpr) value).getNumber();
        } else if (value instanceof SQLCharExpr) {
            // 值是字符串
            return ((SQLCharExpr) value).getText();
        }
        return null;
    }

    public static void main(String[] args) throws SQLSyntaxErrorException {
        parser_test parser_test = new parser_test();
//        parser_test.parse("CREATE TABLE Persons(Id_P int NOT NULL,LastName varchar(255) NOT NULL,FirstName varchar(255),Address varchar(255),City varchar(255),CHECK (Id_P>0))");
        parser_test.parse("CREATE TABLE IF NOT EXISTS `runoob_tbl`(\n" +
                "   `runoob_id` INT UNSIGNED AUTO_INCREMENT COMMENT 'good',\n" +
                "   `runoob_title` VARCHAR(100) NOT NULL,\n" +
                "   `runoob_author` VARCHAR(40) NOT NULL,\n" +
                "   `submission_date` DATE,\n" +
                "   PRIMARY KEY ( `runoob_id` )\n" +
                ")");
//        parser_test.parse("CREATE DATABASE 'TEST3558'");
//        parser_test.parse("DROP DATABASE 'TEST223'");
//        parser_test.parse("update test set status='P' where id=20");
//        parser_test.parse("insert into test (id,status,name,ce,acc) values (29,'P','lll','sxsx','Arferwg')");








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
