package GUI;
import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;


public class Register {

    String name;
    String password;
    String confirmpassword;
    String permission;

    void setName(String name) {
        this.name = name;
    }
    void setPassword(String password) {
        this.password = password;
    }
    void setconfirmpasswd(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }
    void setPermission(String permission) {
        this.permission = permission;
    }


    //判断用户注册操作是否合规
    boolean JudgeRegister() throws SQLException, ClassNotFoundException {

        if(this.name.equals("")) {
            JOptionPane.showMessageDialog(null, "Enter The Username！", "Username", JOptionPane.ERROR_MESSAGE);
            return false;
        }


        if(this.password.equals("")) {
            JOptionPane.showMessageDialog(null, "Enter The Password！", "Password", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(!this.password.equals(this.confirmpassword)) {
            JOptionPane.showMessageDialog(null, "Two Password Is Not the Same！", "CFM Password", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(this.permission.equals("")) {
            JOptionPane.showMessageDialog(null, "Choose A Permission！", "Permission", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        //addAdmin();
        return true;
    }

    //检查文件是否存在，fileUrl是文件的路径
    static boolean FileExistTest(String fileUrl) {
        boolean out = false;
        try {
            File f = new File(fileUrl);
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


    /*查询目标是否存在，strDst是目标字符串，fileUrl是路径，成功返回则true*/
    static boolean StringSearchLine(String strDst,String fileUrl) {
        boolean out = false;

        try {
            FileInputStream fis = new FileInputStream(fileUrl);
            InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
            BufferedReader br = new BufferedReader(isr);

            String line = "";	                  				//暂存br中取出的每一行字符串
            while((line = br.readLine()) != null) {				//文件未读取完毕
                if(line.equals(strDst)) {						//若找到目标字符串，true
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
            JOptionPane.showMessageDialog(null, "The User Is Already Exist!", "Username Exist", JOptionPane.ERROR_MESSAGE);
        }
        return out;
    }

    /*向文件中写入内容，strIn为要写入的内容，fileUrl是路径，写入成功返回true*/
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

    /* 注册
         String userName 注册操作输入的用户名
         String password 注册操作输入的密码
         String fileUrl  需要访问的文件路径（存储本地用户名和密码的文件）注册成功则返回true*/
    static boolean UserRegister(String name,String passwd,String permission,String fileUrl) {
        boolean out = false;

        if(StringSearchLine("Username:  " + name, fileUrl)) {				//搜索name检验用户是否已存在
            return out;
        }else {										                    	//若不存在，依次写入 用户名 权限 密码
            StringWriteLine("Username:  " + name, fileUrl);
            StringWriteLine("Password:  " + passwd,fileUrl);
            StringWriteLine("Permission:  " + permission, fileUrl);
            out = true;
        }
        return out;
    }


}