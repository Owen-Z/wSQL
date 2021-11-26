package API;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.*;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.*;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.*;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerInsertStatement;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerUpdateStatement;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import jdk.swing.interop.SwingInterOpUtils;

import java.io.*;
import java.util.*;

public class API {
    private String userName;    //使用当前模块的用户名
    private String dbName; //当前使用的数据库

    public API(String un, String dbn){
        userName = un;
        dbn = dbName;
    }

    public String getDbName() {
        return dbName;
    }

    public String getUserName() {
        return userName;
    }

    /**
     * 删除指定数据库
     *
     * @param  dbName, filePath
     */
    public void DeleteDatabase(String dbName, String filePath){
        try{

            File file = new File(filePath+"\\"+dbName);
            System.out.println(filePath+"\\"+dbName);
            file.delete();
        } catch (Exception e) {
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
            File file = new File(filePath);
            if(file.exists()){
                File[] fileArray= file.listFiles();
                for(int i = 0;i<fileArray.length;i++){
                    if(fileArray[i].getName().equals(str)){
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

    public void parse(String statement) {
        // 使用druid解析语句
        // 第一个参数为SQL语句
        // 第二个参数为解析的数据库类型
        statement = statement.toUpperCase(Locale.ROOT);
        List<SQLStatement> statementList = SQLUtils.parseStatements(statement, JdbcConstants.MYSQL);
        // 单语句解析，只有一条数据
        if (!statement.isEmpty()) {
            SQLStatement sqlStatement = statementList.get(0);
            // 插入语句解析

            // 创建数据库语句
            if (sqlStatement instanceof SQLCreateDatabaseStatement) {
                // 转换语句格式
                SQLCreateDatabaseStatement createDatabaseStatement = (SQLCreateDatabaseStatement) sqlStatement;

                // 通过解析得到的数据库名含有''   e.g. 'test'
                String dbName = createDatabaseStatement.getName().toString();
                // 去除了''     e.g. test
                String storeDBName = dbName.substring(1, dbName.length() - 1);

                // 判断数据库名是否存在或者非法(长度大于120)，并将数据库名存储在WSql.db文件下
                if (storeDBName.length() >= 120) {
                    System.out.println("数据库名称过长");
                } else if (CheckExist(storeDBName, "src\\data")) {
                    System.out.println("数据库已存在");
                } else {
                    try {
                        // 创建并存储该数据库的表信息
                        File file = new File("src\\data\\" + storeDBName);
                        file.mkdir();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("数据库创建成功");
                }
            }

            // 删除数据库指令
            if (sqlStatement instanceof SQLDropDatabaseStatement) {
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
                if (!CheckExist(storeDBName, "src\\data")) {
                    System.out.println("数据库不存在");
                } else {
                    DeleteDatabase(storeDBName, "src\\data");
                    System.out.println("数据库删除成功");
                }

            }

            // 创建表指令
            if (sqlStatement instanceof SQLCreateTableStatement) {
                SQLCreateTableStatement sqlCreateTableStatement = (SQLCreateTableStatement) sqlStatement;
                System.out.println(sqlCreateTableStatement);

                // 表名 e.g. 'test'
                String tableName = sqlCreateTableStatement.getName().toString();
                // 用以存储的表名 e.g. test
                String storeTableName = tableName.substring(1, tableName.length() - 1);

                //实例化创建表
                CreateTable createTable = new CreateTable(storeTableName, getDbName());

                List<SQLTableElement> elements = sqlCreateTableStatement.getTableElementList();

                SQLTableElement element;
                for (int i = 0; i < elements.size(); i++) {
                    if (elements.get(i) instanceof SQLColumnDefinition) {
                        HashMap<String, String> map = new HashMap<>();
                        //1.name
                        String name = ((SQLColumnDefinition) elements.get(i)).getNameAsString();
                        map.put("name", name);
                        //2.type
                        String type = ((SQLColumnDefinition) elements.get(i)).getDataType().toString();
                        map.put("type", type);
                        //3.length
                        String length = "";
                        if (type.equals("INT")) {
                            length = "32";
                        } else if (type.equals("INT UNSIGNED")) {
                            length = "32";
                        } else if (type.equals("INTEGER")) {
                            length = "32";
                        } else if (type.equals("BOOL")) {
                            length = "8";
                        } else if (type.equals("DOUBLE")) {
                            length = "16";
                        } else if (type.equals("DATE")) {
                            length = "108";
                        } else {
                            length = type.substring(8, type.length() - 1);
                        }
                        map.put("length", length);
                        //4.def
                        String def = "null";
                        if (((SQLColumnDefinition) elements.get(i)).getDefaultExpr() != null) {
                            def = ((SQLColumnDefinition) elements.get(i)).getDefaultExpr().toString();
                        }
                        map.put("def", def);
                        //5.comment
                        String comment = "null";
                        if (((SQLColumnDefinition) elements.get(i)).getComment() != null) {
                            comment = ((SQLColumnDefinition) elements.get(i)).getDefaultExpr().toString();
                        }
                        map.put("comment", comment);
                        //6.auto
                        String auto = "";
                        if (((SQLColumnDefinition) elements.get(i)).isAutoIncrement()) {
                            auto = "true";
                        }
                        map.put("auto", auto);
                        //7.8.9.10
                        List<SQLColumnConstraint> list =
                                ((SQLColumnDefinition) elements.get(i)).getConstraints();
                        String primaryKey = "false";
                        String notNUll = "false";
                        String check = "null";
                        String foreignKey = "false";
                        for (SQLColumnConstraint cons : list) {
                            if (cons instanceof SQLNotNullConstraint) {
                                notNUll = "true";
                            }
                            if (cons instanceof MySqlPrimaryKey) {
                                primaryKey = "true";
                            }
                            if (cons instanceof SQLColumnCheck) {
                                check = ((SQLColumnCheck) cons).getExpr().toString();
                            }
                        }
                        map.put("notNUll", notNUll);
                        map.put("primaryKey", primaryKey);
                        map.put("check", check);
                        map.put("foreignKey", foreignKey);
                        System.out.println(i + "name:" + name);
                        System.out.println(i + "type:" + type);
                        System.out.println(i + "length:" + length);
                        System.out.println(i + "def:" + def);
                        System.out.println(i + "comment:" + comment);
                        System.out.println(i + "auto:" + auto);
                        System.out.println(i + "primaryKey:" + primaryKey);
                        System.out.println(i + "notNUll:" + notNUll);
                        System.out.println(i + "check:" + check);
                        createTable.addColumn(map);
                    } else if (elements.get(i) instanceof MySqlPrimaryKey) {
                        MySqlSchemaStatVisitor visitor1 = new MySqlSchemaStatVisitor();
                        elements.get(i).accept(visitor1);
                        String[] str = visitor1.getColumns().toString().split("\\[|\\.|\\]");
                        System.out.println(str[2]);
                        createTable.setPrimaryKey(str[2]);
                    } else if (elements.get(i) instanceof MysqlForeignKey) {
                        MySqlSchemaStatVisitor visitor1 = new MySqlSchemaStatVisitor();
                        elements.get(i).accept(visitor1);
                        String[] str = visitor1.getColumns().toString().split("\\[|\\.|\\]");
                        String[] str1 = str[2].split(",");
                        System.out.println(str1[0]);
                        createTable.setForeignKey(str1[0]);
                    }
                }
                createTable.create();
                // 普通约束
//                System.out.println(sqlCreateTableStatement.getTableElementList());

//                MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
//                ((MysqlForeignKey)element).accept(visitor);
//                System.out.println(visitor.getColumns());
//                System.out.println(((MysqlForeignKey)element).getText());

//                System.out.println(((MySqlPrimaryKey)element).getName());
//                MySqlSchemaStatVisitor visitor1 = new MySqlSchemaStatVisitor();
//                element.accept(visitor1);
//                String[] str = visitor1.getColumns().toString().split("\\[|\\.|\\]");
//                System.out.println(str[2]);


            }


            if (sqlStatement instanceof SQLSelectStatement) {
                // 转换
                SQLSelectStatement sqlSelectStatement = (SQLSelectStatement) sqlStatement;
//                System.out.println(sqlSelectStatement);


                MySqlASTVisitor visitor = new MySqlASTVisitorAdapter();
                MySqlSchemaStatVisitor visitor1 = new MySqlSchemaStatVisitor();
                sqlSelectStatement.accept(visitor1);

                System.out.println(visitor1.getTables().getClass());

                // 获取所有的普通值限制
                List<TableStat.Condition> conditions = visitor1.getConditions();
                for (TableStat.Condition condition : conditions) {
                    if (condition.getValues().size() != 0) {
                        System.out.println(condition.getColumn());  // 需要查的表名+列名
                        System.out.println(condition.getValues().get(0));   // 对应的值
                    }
                }

                // 连续查询两个表
                if (visitor1.getTables().size() > 1) {
                    // 两个表有连接查询
                    if (visitor1.getRelationships().size() > 0) {
                        System.out.println(visitor1.getRelationships());    //  两个表的表名.列名 = 表名.列名
                    }
                }


//                MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
                sqlSelectStatement.accept(visitor);
//                System.out.println(visitor.visit(sqlSelectStatement));
                SQLSelectQuery sqlSelectQuery = sqlSelectStatement.getSelect().getQuery();

//                System.out.println(sqlSelectStatement.getSelect());
//                System.out.println(sqlSelectStatement.getSelect().getQueryBlock()); // whole query command


//                System.out.println(sqlSelectStatement.getSelect().getQueryBlock().getWhere().getClass());
//                SQLBinaryOpExpr expr = (SQLBinaryOpExpr) sqlSelectStatement.getSelect().getQueryBlock().getWhere();
//                System.out.println(expr);



                /*
                    首先判断是否为普通表 instanceof SQLExprTableSource
                    否则是SQLJoinTableSource 有左值，右值
                    设置原表名和表名的hashmap
                 */
//                SQLJoinTableSource from = (SQLJoinTableSource) sqlSelectStatement.getSelect().getQueryBlock().getFrom();
//                String[] names = from.toString().split(",");
//                HashMap<String, String > name_RName = new HashMap<>();
//                for(int i = names.length - 1; i > 1; i--){
//                   String str = from.getRight().getAlias();
//                   if(str != null){
//                       name_RName.put(names[i], str);
//                   }else{
//                       name_RName.put(names[i], names[i]);
//                   }
//                   from = (SQLJoinTableSource) from.getLeft();
//                }
//                String rstr = from.getRight().getAlias();
//                String lstr = from.getLeft().getAlias();
//                if(rstr != null){
//                    name_RName.put(names[1], rstr);
//                }else{
//                    name_RName.put(names[1], names[1]);
//                }
//                if(lstr != null){
//                    name_RName.put(names[0], lstr);
//                }else{
//                    name_RName.put(names[0], names[0]);
//                }


//                System.out.println(from.getRight().getAlias());  //获取别名


//                System.out.println(sqlSelectStatement.getSelect().getQueryBlock().getGroupBy());
//                System.out.println(sqlSelectStatement.getSelect().getQueryBlock().getOrderBy());
//                System.out.println(sqlSelectStatement.getSelect().getQueryBlock().getLimit());

            }

        }

    }

}
