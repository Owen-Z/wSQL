package GUI;
import javax.swing.*;
import java.io.*;

public class datasaver {
    String name;
    String database;
    String table;
    //String databaseUrl ="files\\DatabasenTable\\DatabasenTable.txt";

    void setName(String name) {
        this.name = name;
    }
    void database(String database) {
        this.database = database;
    }
    void table(String table) {
        this.table = table;
    }

    //检查文件是否存在
    static boolean DatabasenTable(String databaseUrl) {
        boolean out = false;
        databaseUrl ="files\\DatabasenTable\\DatabasenTable.txt";
        try {
            File f = new File(databaseUrl);
            if(!f.exists()) {						//若文件不存在
                f.createNewFile();					//新建该文件
				/*
				String sets=" attrib +H  "+f.getAbsolutePath(); 	//设置文件属性为隐藏
				Runtime.getRuntime().exec(sets);	*/		        //执行文件设置
                out = true;
            }
            out = true;
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unknown Wrong!", "Unknown Wrong", JOptionPane.ERROR_MESSAGE);
        }
        return out;
    }

    static boolean StringSearchLineDatabase(String database,String databaseUrl) {
        boolean out = false;
        databaseUrl ="files\\DatabasenTable\\DatabasenTable.txt";
        try {
            FileInputStream fis = new FileInputStream(databaseUrl);
            InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
            BufferedReader br = new BufferedReader(isr);

            String line = "";	                  				//暂存br中取出的每一行字符串
            while((line = br.readLine()) != null) {				//文件未读取完毕
                if(line.equals(database)) {						//若找到目标字符串，true
                    out = true;
                    break;
                }
            }
            //结束访问
            br.close();
            isr.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "The Database Is Already Exist!", "Username Exist", JOptionPane.ERROR_MESSAGE);
        }
        return out;
    }

    static boolean StringSearchLineTable(String table,String databaseUrl) {
        boolean out = false;
        databaseUrl ="files\\DatabasenTable\\DatabasenTable.txt";
        try {
            FileInputStream fis = new FileInputStream(databaseUrl);
            InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
            BufferedReader br = new BufferedReader(isr);

            String line = "";	                  				//暂存br中取出的每一行字符串
            while((line = br.readLine()) != null) {				//文件未读取完毕
                if(line.equals(table)) {						//若找到目标字符串，true
                    out = true;
                    break;
                }
            }
            //结束访问
            br.close();
            isr.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "The Table Is Already Exist!", "Username Exist", JOptionPane.ERROR_MESSAGE);
        }
        return out;
    }

    //创建数据库
    static boolean CreatDatabase(String database,String name) {
        boolean out = false;
        String databaseUrl ="files\\DatabasenTable\\DatabasenTable.txt";
        if(StringSearchLineDatabase("Database:  " + database,databaseUrl)) {
            return out;
        }else {
            StringWriteLine("Database:  " + database + "     User" + name , databaseUrl);
            out = true;
        }
        return out;
    }

    //创建数据库
    static boolean CreatTable(String table,String name) {
        boolean out = false;
        String databaseUrl ="files\\DatabasenTable\\DatabasenTable.txt";
        if(StringSearchLineDatabase("Table:  " + table,databaseUrl)) {
            return out;
        }else {
            StringWriteLine("Table:  " + table + "     User" + name , databaseUrl);
            out = true;
        }
        return out;
    }

    //向文件中写入内容
    static boolean StringWriteLine(String strIn,String fileUrl) {
        boolean out = false;

        try {
            FileWriter fw = new FileWriter(fileUrl,true);

            fw.write("\n"+strIn);				//写入内容
            fw.close();
            out = true;
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed To Write The Data!", "Data Fail", JOptionPane.ERROR_MESSAGE);
        }
        return out;
    }



}
