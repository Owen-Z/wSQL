package GUI;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class Admin {
    private String name;          //用户名
    private String password;        //密码
    private String fileUrl;	      //文件的存储路径

    void setName(String name) {
        this.name=name;
    }

    void setPassword(String password) {
        this.password=password;
    }

    void setfileUrl(String fileUrl ) {
        this.fileUrl=fileUrl;
    }

    String getfileUrl() {
        return this.fileUrl;
    }

    String getName() {
        return this.name;
    }
    String getPassword() {
        return this.password;
    }

    //判断用户的登录操作是否合规
    boolean JudgeRegister() {

        if(this.name.equals("")) {
            JOptionPane.showMessageDialog(null, "Enter The Username��", "Username", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(this.password.equals("")) {
            JOptionPane.showMessageDialog(null, "Enter The Password��", "Password", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    static boolean StringSearchLine(String name,String password,String fileUrl) {
        boolean out = false;
        try {
            FileInputStream fis = new FileInputStream(fileUrl);
            InputStreamReader isr = new InputStreamReader(fis,"UTF-8");
            BufferedReader br = new BufferedReader(isr);

            String line = "";	                  				                     //暂存br中取出的每一行字符串
            while((line = br.readLine()) != null) {                                  //文件未读取完毕
                //先查询用户名
                if(line.equals("Username:  " + name)) {
                    if(br.readLine().equals("Password:  " + password)) {		//检查密码是否正确
                        out = true;									//若正确返回True
                    }
                    break;
                }
            }
            //结束访问
            br.close();
            isr.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Wrong Username Or Password", "Wrong Username Or Password", JOptionPane.ERROR_MESSAGE);
        }
        return out;

    }

}