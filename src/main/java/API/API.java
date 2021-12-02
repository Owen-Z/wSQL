package API;
import com.Data.DataDelete;
import com.Data.DataInsert;
import com.Data.DataSelect;
import com.Data.DataUpdate;
import com.Database.DatabaseCreate;
import com.Database.DatabaseDelete;
import com.Field.FieldAdd;
import com.Field.FieldDelete;
import com.Field.FieldModify;
import com.Index.IndexCreate;
import com.Index.IndexDelete;
import com.Table.TableCreate;
import com.Table.TableDelete;
import com.Transaction.Commit;
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
    private String dbName;      //当前使用的数据库
    private Commit commit;

    public API(){
//        userName = un;
//        dbName = dbn;
        commit = new Commit();
    }

    public void AddToLog(String dbName, SQLStatement sqlStatement){
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter("src/DBMS_ROOT/data/" + dbName + "/" + dbName + ".log", true));
            out.write(sqlStatement.toString());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public String parse(String statement) {
        // 使用druid解析语句
        // 第一个参数为SQL语句
        // 第二个参数为解析的数据库类型
        statement = statement.toUpperCase(Locale.ROOT);
        List<SQLStatement> statementList = SQLUtils.parseStatements(statement, JdbcConstants.MYSQL);
        // 单语句解析，只有一条数据
        if (!statement.isEmpty()) {
            SQLStatement sqlStatement = statementList.get(0);

            // 创建数据库语句
            if (sqlStatement instanceof SQLCreateDatabaseStatement) {
                // 转换语句格式
                SQLCreateDatabaseStatement createDatabaseStatement = (SQLCreateDatabaseStatement) sqlStatement;

                // 通过解析得到的数据库名含有''   e.g. 'test'
                String dbName = createDatabaseStatement.getName().toString();
                // 去除了''     e.g. test
                String storeDBName = dbName.substring(1, dbName.length() - 1);

                DatabaseCreate databaseCreate = new DatabaseCreate();
                if(databaseCreate.DBCheck(storeDBName) == 0){
                    System.out.println("数据库名称过长");
                }else if(databaseCreate.DBCheck(storeDBName) == 1){
                    System.out.println("数据库已存在");
                }else if(databaseCreate.DBCheck(storeDBName) == 2){
                    databaseCreate.DBCreate(storeDBName);
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
                DatabaseDelete databaseDelete = new DatabaseDelete();
                if (!databaseDelete.DBCheck(storeDBName)) {
                    System.out.println("数据库不存在");
                } else {
                    databaseDelete.BDDelete(storeDBName);
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
                TableCreate tableCreate = new TableCreate("MYSQLITE",storeTableName);
                if (!tableCreate.TBCheck()){
                    System.out.println("表已经存在");
                    return "false";
                }

                List<SQLTableElement> elements = sqlCreateTableStatement.getTableElementList();

                for (int i = 0; i < elements.size(); i++) {
                    if (elements.get(i) instanceof SQLColumnDefinition) {
                        HashMap<String, String> map = new HashMap<>();
                        //1.name
                        String name = ((SQLColumnDefinition) elements.get(i)).getNameAsString();
                        map.put("name", name);
                        //2.type
                        String type = ((SQLColumnDefinition) elements.get(i)).getDataType().toString();
                        map.put("type", type);
                        //3.def
                        String def = "null";
                        if (((SQLColumnDefinition) elements.get(i)).getDefaultExpr() != null) {
                            def = ((SQLColumnDefinition) elements.get(i)).getDefaultExpr().toString();
                        }
                        map.put("def", def);
                        //4.comment
                        String comment = "null";
                        if (((SQLColumnDefinition) elements.get(i)).getComment() != null) {
                            comment = ((SQLColumnDefinition) elements.get(i)).getDefaultExpr().toString();
                        }
                        map.put("comment", comment);
                        //5.auto
                        String auto = "false";
                        if (((SQLColumnDefinition) elements.get(i)).isAutoIncrement()) {
                            auto = "true";
                        }
                        map.put("auto", auto);
                        //6.7.8.9.10
                        List<SQLColumnConstraint> list =
                                ((SQLColumnDefinition) elements.get(i)).getConstraints();
                        String primaryKey = "false";
                        String notNUll = "false";
                        String check = "null";
                        String foreignKey = "false";
                        String unique = "false";
                        for (SQLColumnConstraint cons : list) {
                            if (cons instanceof SQLNotNullConstraint) {
                                notNUll = "true";
                            }
                            if (cons instanceof SQLColumnPrimaryKey) {
                                primaryKey = "true";
                            }
                            if (cons instanceof SQLColumnCheck) {
                                check = ((SQLColumnCheck) cons).getExpr().toString();
                            }
                            if (cons instanceof SQLColumnUniqueKey) {
                                unique = "true";
                            }
                        }
                        map.put("notNUll", notNUll);
                        map.put("primaryKey", primaryKey);
                        map.put("check", check);
                        map.put("foreignKey", foreignKey);
                        map.put("unique", unique);
                        tableCreate.TBCreateColumn(map);
                    } else if (elements.get(i) instanceof MySqlPrimaryKey) {
                        MySqlSchemaStatVisitor visitor1 = new MySqlSchemaStatVisitor();
                        elements.get(i).accept(visitor1);
                        String[] str = visitor1.getColumns().toString().split("\\[|\\.|\\]");
                        if(tableCreate.setPrimaryKey(str[2])){
                            System.out.println("主键设置成功");
                        }else{
                            System.out.println("主键已经存在");
                        }
                    } else if (elements.get(i) instanceof MysqlForeignKey) {
                        MySqlSchemaStatVisitor visitor1 = new MySqlSchemaStatVisitor();
                        elements.get(i).accept(visitor1);
                        String str = visitor1.getColumns().toString();
                        String result = str.substring(1,str.length()-1);
                        String[] s = result.split(" ");
                        String[] s1 = s[0].split("\\.|\\,");
                        String[] s2 = s[1].split("\\.");
                        int result1 = tableCreate.setForeign(s1[1],s2[0],s2[1]);
                        if(result1 == 0){
                            System.out.println("字段不存在");
                        }else if(result1 == 1){
                            System.out.println("外键不存在");
                        }else if(result1 == 2){
                            System.out.println("字段类型不匹配");
                        }else if(result1 == 3){
                            System.out.println("字段类型不适合");
                        }else if(result1 == 4){
                            System.out.println("外键设置成功");
                        }
                    }
                }
                tableCreate.create();
                System.out.println("表创建成功");



            }

            // 删除数据表
            if(sqlStatement instanceof SQLDropTableStatement){
                SQLDropTableStatement sqlDropTableStatement = (SQLDropTableStatement) sqlStatement;
                String tbName = sqlDropTableStatement.getTableSources().toString();
                System.out.println(tbName.substring(1,tbName.length()-1));
                String storeTableName = tbName.substring(1,tbName.length()-1);
                TableDelete tableDelete = new TableDelete("MYSQLITE",storeTableName.toUpperCase(Locale.ROOT));
                if (tableDelete.TBCheck()){
                    tableDelete.DBDelete();
                }else {
                    System.out.println("表不存在");
                }
            }

            // 修改数据表字段
            if(sqlStatement instanceof SQLAlterTableStatement){
                // 转换
                SQLAlterTableStatement sqlAlterTableStatement = (SQLAlterTableStatement) sqlStatement;
                String tbName = sqlAlterTableStatement.getTableSource().toString().toUpperCase(Locale.ROOT);

                for(SQLAlterTableItem element : sqlAlterTableStatement.getItems()){
                    if(element instanceof SQLAlterTableDropColumnItem){
//                        System.out.println(((SQLAlterTableDropColumnItem) element).getColumns());
                        for(SQLName dropElement : ((SQLAlterTableDropColumnItem) element).getColumns()){
//                            System.out.println(dropElement);
                            FieldDelete fieldDelete = new FieldDelete("MYSQLITE",tbName);
                            if(fieldDelete.FieldDrop(dropElement.toString().toUpperCase(Locale.ROOT))){
                                System.out.println("字段删除成功");
                            }else {
                                System.out.println("字段删除失败");
                            }
                        }
                    }else if(element instanceof SQLAlterTableAddColumn){
                        SQLColumnDefinition addElement = ((SQLAlterTableAddColumn) element).getColumns().get(0);
                        FieldAdd fieldAdd = new FieldAdd("MYSQLITE",tbName);
                        if (!fieldAdd.FieldCheck(addElement.getName().toString())){
                            System.out.println("字段增加失败");
                            return "true";
                        }
                        HashMap<String, String> map = new HashMap<>();
                        //1.name
                        String name = addElement.getName().toString();
                        map.put("name",name);
                        //2.type
                        String type = addElement.getDataType().toString();
                        map.put("type", type);
                        //3.def
                        String def = "null";
                        if(addElement.getDefaultExpr() != null){
                            def = addElement.getDefaultExpr().toString();
                        }
                        map.put("def", def);
                        //4.comment
                        String comment = "null";
                        if(addElement.getComment() != null){
                            comment = addElement.getComment().toString();
                        }
                        map.put("comment",comment);
                        //5.auto
                        String auto = "false";
                        if (addElement.isAutoIncrement()){
                            auto = "true";
                        }
                        map.put("auto",auto);

//                        System.out.println(addElement.getName());
//                        System.out.println(addElement.getDataType());
//
//                        System.out.println(addElement.getDefaultExpr().toString());
//                        System.out.println(addElement.isAutoIncrement());
//                        System.out.println(addElement.getComment());

                        // 同创建表约束
//                        System.out.println(addElement.getConstraints());
                        List<SQLColumnConstraint> list =
                                addElement.getConstraints();
                        String primaryKey = "false";
                        String notNUll = "false";
                        String check = "null";
                        String foreignKey = "false";
                        String unique = "false";
                        for (SQLColumnConstraint cons : list) {
                            if (cons instanceof SQLNotNullConstraint) {
                                notNUll = "true";
                            }
                            if (cons instanceof SQLColumnPrimaryKey) {
                                primaryKey = "true";
                            }
                            if (cons instanceof SQLColumnCheck) {
                                check = ((SQLColumnCheck) cons).getExpr().toString();
                            }
                            if (cons instanceof SQLColumnUniqueKey) {
                                unique = "true";
                            }
                        }
                        map.put("notNUll", notNUll);
                        map.put("primaryKey", primaryKey);
                        map.put("check", check);
                        map.put("foreignKey", foreignKey);
                        map.put("unique", unique);
                        fieldAdd.FieldCreate(map);
                        if(fieldAdd.FieldSave()){
                            System.out.println("字段增加成功");
                        }else {
                            System.out.println("字段增加失败");
                        }
//                        System.out.println("name:" + name);
//                        System.out.println("type:" + type);
//                        System.out.println("def:" + def);
//                        System.out.println("comment:" + comment);
//                        System.out.println("auto:" + auto);
//                        System.out.println("primaryKey:" + primaryKey);
//                        System.out.println("notNUll:" + notNUll);
//                        System.out.println("check:" + check);
//                        System.out.println("unique:" + unique);
//                        System.out.println("foreignKey:" + foreignKey);

                    }else if(element instanceof MySqlAlterTableModifyColumn){
                        HashMap<String,String> map = new HashMap<>();
                        // 获取要修改的列名
//                        System.out.println(((MySqlAlterTableModifyColumn) element).getNewColumnDefinition().getName());
                        String name = ((MySqlAlterTableModifyColumn) element).getNewColumnDefinition().getName().toString();
                        map.put("name",name);
                        // 获取要修改的字段属性
//                        System.out.println(((MySqlAlterTableModifyColumn) element).getNewColumnDefinition().getDataType());
                        String type = ((MySqlAlterTableModifyColumn) element).getNewColumnDefinition().getDataType().toString();

                        map.put("type",type);
                        // 默认值
                        String def = "null";
                        if(((MySqlAlterTableModifyColumn) element).getNewColumnDefinition().getDefaultExpr() != null){
                            def = ((MySqlAlterTableModifyColumn) element).getNewColumnDefinition().getDefaultExpr().toString();
                        }
                        map.put("def",def);
//                        System.out.println(((MySqlAlterTableModifyColumn) element).getNewColumnDefinition().getDefaultExpr());
                        //自增
                        String auto = "false";
                        if(((MySqlAlterTableModifyColumn) element).getNewColumnDefinition().isAutoIncrement()){
                            auto = "true";
                        }
                        auto = "true";
                        map.put("auto",auto);
                        //备注
                        String comment = "null";
                        if(((MySqlAlterTableModifyColumn) element).getNewColumnDefinition().getComment() != null){
                            comment = ((MySqlAlterTableModifyColumn) element).getNewColumnDefinition().getComment().toString();
                        }
                        map.put("comment",comment);

                        String primaryKey = "false";
                        String notNUll = "false";
                        String check = "null";
                        String foreignKey = "false";
                        String unique = "false";
                        List<SQLColumnConstraint> list =
                                ((MySqlAlterTableModifyColumn) element).getNewColumnDefinition().getConstraints();

                        for (SQLColumnConstraint cons : list) {
                            if (cons instanceof SQLNotNullConstraint) {
                                notNUll = "true";
                            }
                            if (cons instanceof SQLColumnPrimaryKey) {
                                primaryKey = "true";
                            }
                            if (cons instanceof SQLColumnCheck) {
                                check = ((SQLColumnCheck) cons).getExpr().toString();
                            }
                            if (cons instanceof SQLColumnUniqueKey) {
                                unique = "true";
                            }
                        }
                        map.put("notNUll", notNUll);
                        map.put("primaryKey", primaryKey);
                        map.put("check", check);
                        map.put("foreignKey", foreignKey);
                        map.put("unique", unique);
//                        System.out.println("name:" + name);
//                        System.out.println("type:" + type);
//                        System.out.println("def:" + def);
//                        System.out.println("comment:" + comment);
//                        System.out.println("auto:" + auto);
//                        System.out.println("primaryKey:" + primaryKey);
//                        System.out.println("notNUll:" + notNUll);
//                        System.out.println("check:" + check);
//                        System.out.println("unique:" + unique);
//                        System.out.println("foreignKey:" + foreignKey);
                        FieldModify fieldModify = new FieldModify("MYSQLITE",tbName);
                        if(!commit.getState()){
                            commit.setDbName("MYSQLITE");
                            commit.setTbName(tbName);
                            commit.getTB();
                        }
                        if(fieldModify.check(map)){
                            System.out.println("字段更新失败");
                        }else {
                            System.out.println("字段更新成功");
                        }
                    }
                }
            }

            //删除数据
            if(sqlStatement instanceof SQLDeleteStatement){
                // 转换
                SQLDeleteStatement sqlDeleteStatement = (SQLDeleteStatement) sqlStatement;

                // 需要删除字段的表名
//                System.out.println(sqlDeleteStatement.getTableName());

                // 获取where的查询条件
                SQLBinaryOpExpr where= (SQLBinaryOpExpr) sqlDeleteStatement.getWhere();

                List<String> key = new ArrayList<>();
                List<String> val = new ArrayList<>();
                List<String> exp = new ArrayList<>();

                while (where.getOperator().toString().equals("BooleanAnd")){
                    // 条件右边
//                    System.out.println(((SQLBinaryOpExpr)where.getRight()).getRight());
                    val.add(((SQLBinaryOpExpr)where.getRight()).getRight().toString());
                    // 条件左边
//                    System.out.println(((SQLBinaryOpExpr)where.getRight()).getLeft());
                    key.add(((SQLBinaryOpExpr)where.getRight()).getLeft().toString());
                    exp.add(((SQLBinaryOpExpr)where.getRight()).getOperator().getName());
                    where = (SQLBinaryOpExpr)where.getLeft();
                }
                key.add(where.getLeft().toString());
                val.add(where.getRight().toString());
                exp.add(where.getOperator().getName());
                DataDelete dataDelete = new DataDelete("MYSQLITE",sqlDeleteStatement.getTableName().toString());
                if(dataDelete.delete(key,val,exp)){
                    System.out.println("删除成功");
                }else {
                    System.out.println("删除失败");
                }
            }

            // 更新数据表数据
            if(sqlStatement instanceof SQLUpdateStatement){
                SQLUpdateStatement sqlUpdateStatement = (SQLUpdateStatement) sqlStatement;
                // 字段需要变成什么样

//                System.out.println(sqlUpdateStatement.getTableSource().toString());
                String tbName = sqlUpdateStatement.getTableSource().toString();
                if(!commit.getState()){
                    commit.setDbName("MYSQLITE");
                    commit.setTbName(tbName);
                    commit.getTB();
                }
                DataUpdate dataUpdate = new DataUpdate("MYSQLITE",tbName);
                String key = "";
                String val = "";
                for(SQLUpdateSetItem item : sqlUpdateStatement.getItems()){
                    key = item.getColumn().toString();
                    val = item.getValue().toString();
                }
                SQLBinaryOpExpr where= (SQLBinaryOpExpr) sqlUpdateStatement.getWhere();
                if(where == null){
                    if(dataUpdate.update1(key,val)){
                        System.out.println("更新成功");
                    }else {
                        System.out.println("更新失败");
                    }
                }else {
                    List<String> fields = new ArrayList<>();
                    List<String> vals = new ArrayList<>();
                    List<String> exps = new ArrayList<>();
                    while (where.getOperator().toString().equals("BooleanAnd")){
                        fields.add(((SQLBinaryOpExpr)where.getRight()).getLeft().toString());
                        vals.add(((SQLBinaryOpExpr)where.getRight()).getRight().toString());
                        exps.add(((SQLBinaryOpExpr)where.getRight()).getOperator().getName());
                        where = (SQLBinaryOpExpr)where.getLeft();
                    }
                    fields.add(where.getLeft().toString());
                    vals.add(where.getRight().toString());
                    exps.add(where.getOperator().getName());

                    if(dataUpdate.update(key,val,fields,vals,exps)){
                        System.out.println("更新成功");
                    }else {
                        System.out.println("更新失败");
                    }
                }
            }

            // 插入数据指令
            if(sqlStatement instanceof SQLInsertStatement){
                // 转换
                SQLInsertStatement insertStatement = (SQLInsertStatement) sqlStatement;

                // 获取列名
                List<SQLExpr> columns = insertStatement.getColumns();
                List<String> columnsName = new ArrayList<>(columns.size());
                for(SQLExpr column : columns){
                    columnsName.add(((SQLIdentifierExpr) column).getName());
                }
                // 获取值
                List<SQLInsertStatement.ValuesClause> valuesList = insertStatement.getValuesList();
                List<String> dataList = new ArrayList<>();
                for (SQLInsertStatement.ValuesClause valuesClause : valuesList) {
                    List<SQLExpr> values = valuesClause.getValues();
                    for (SQLExpr value : values) {
                        dataList.add(getValue(value).toString());
                    }
                }
                if(columnsName.size() != dataList.size()){
                    System.out.println("插入错误");
                    return "false";
                }

                DataInsert dataInsert = new DataInsert("MYSQLITE",insertStatement.getTableName().getSimpleName());
                HashMap<String,String> map = new HashMap<>();
                for (int i = 0;i<columnsName.size();i++){
                    map.put(columnsName.get(i),dataList.get(i));
                }
                if(!commit.getState()){
                    commit.setDbName("MYSQLITE");
                    commit.setTbName(insertStatement.getTableName().getSimpleName());
                    commit.getTB();
                }

                if(dataInsert.DateCheck(map)){
                    dataInsert.DataSave(map);
                    System.out.println("插入成功");
                }else {
                    System.out.println("插入失败");
                }
            }

            //查询指令
            if (sqlStatement instanceof SQLSelectStatement) {
                // 转换
                SQLSelectStatement sqlSelectStatement = (SQLSelectStatement) sqlStatement;
                MySqlSchemaStatVisitor visitor1 = new MySqlSchemaStatVisitor();
                sqlSelectStatement.accept(visitor1);

                //需要查询的表名
                String tbName = sqlSelectStatement.getSelect().getFirstQueryBlock().getFrom().toString();
                if(!commit.getState()){
                    commit.setDbName("MYSQLITE");
                    commit.setTbName(tbName);
                    commit.getTB();
                }
                DataSelect dataSelect = new DataSelect("MYSQLITE",tbName);
                // 所有要查的列
//                System.out.println(sqlSelectStatement.getSelect().getQueryBlock().getSelectList());
                List<String> result = new ArrayList<>();
                for(int i = 0; i < sqlSelectStatement.getSelect().getQueryBlock().getSelectList().size();i++){
                    result.add(sqlSelectStatement.getSelect().getQueryBlock().getSelectList().get(i).toString());
                }
                //根据是否存在where语句选取不同的方法
                SQLBinaryOpExpr where = (SQLBinaryOpExpr) sqlSelectStatement.getSelect().getQueryBlock().getWhere();
                if(where == null){
                    String s = dataSelect.select1(result);
                    if(!s.equals("null")){
                        System.out.println("查询成功");
                        return s;
                    }else {
                        System.out.println("查询失败");
                    }
                }else {
                    List<String> key = new ArrayList<>();
                    List<String> val = new ArrayList<>();
                    List<String> exp = new ArrayList<>();
                    while (where.getOperator().toString().equals("BooleanAnd")){
                        // 条件右边
                        val.add(((SQLBinaryOpExpr)where.getRight()).getRight().toString());
                        // 条件左边
                        key.add(((SQLBinaryOpExpr)where.getRight()).getLeft().toString());
                        // 条件符号
                        exp.add(((SQLBinaryOpExpr)where.getRight()).getOperator().getName());
                        where = (SQLBinaryOpExpr)where.getLeft();
                    }
                    key.add(where.getLeft().toString());
                    exp.add(where.getOperator().getName());
                    val.add(where.getRight().toString());
                    String s = dataSelect.select(result,key,val,exp);
                    if(!s.equals("null")){
                        System.out.println("查询成功");
                        return s;
                    }else {
                        System.out.println("查询失败");
                    }
                }

            }

            //创建索引
            if(sqlStatement instanceof SQLCreateIndexStatement){
                SQLCreateIndexStatement sqlCreateIndexStatement = (SQLCreateIndexStatement) sqlStatement;
////                 选择表名
//                System.out.println(sqlCreateIndexStatement.getTableName());
////                 索引名
//                System.out.println(sqlCreateIndexStatement.getName());
////                 索引对应行
//                System.out.println(sqlCreateIndexStatement.getItems().get(0).getExpr());

                String tbName = sqlCreateIndexStatement.getTableName();
                String ixName = sqlCreateIndexStatement.getName().toString();
                String cName = sqlCreateIndexStatement.getItems().get(0).getExpr().toString();
                IndexCreate indexCreate = new IndexCreate("MYSQLITE",tbName);
//                System.out.println(indexCreate.checkField(cName));
//                System.out.println(indexCreate.checkIndex(cName,ixName));
                if(indexCreate.checkField(cName)&&indexCreate.checkIndex(cName,ixName)){
                    indexCreate.create(cName,ixName);
                    System.out.println("索引建立成功");
                }else {
                    System.out.println("索引建立失败");
                }

            }

            //删除索引
            if(sqlStatement instanceof SQLDropIndexStatement){
                SQLDropIndexStatement sqlDropIndexStatement = (SQLDropIndexStatement) sqlStatement;
//                System.out.println(sqlDropIndexStatement.getTableName());
//                System.out.println(sqlDropIndexStatement.getIndexName());
                String tbName = sqlDropIndexStatement.getTableName().toString();
                String ixName = sqlDropIndexStatement.getIndexName().toString();
                IndexDelete indexDelete = new IndexDelete("MYSQLITE",tbName);

                if(indexDelete.delete(ixName)){
                    System.out.println("删除成功");
                }else {
                    System.out.println("删除失败");
                }
            }

        }
        return "true";
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

}
