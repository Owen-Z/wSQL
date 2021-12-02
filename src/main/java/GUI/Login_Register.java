package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import GUI.Admin;
import GUI.AdminRegister;
import GUI.Login_Register;

public class Login_Register extends JFrame{

    Login_Register() {
        init();
    }
    //登录界面
    public void init() {
        JFrame frame = new JFrame("Login");
        frame.setLayout(null);

        JLabel nameStr = new JLabel("Username:");
        nameStr.setBounds(100, 100, 100, 25);
        frame.add(nameStr);

        JLabel passwordStr = new JLabel("Password:");
        passwordStr.setBounds(100, 150, 100, 25);
        frame.add(passwordStr);

        JTextField username = new JTextField();
        username.setBounds(170, 100, 150, 25);
        frame.add(username);

        JPasswordField password = new JPasswordField();
        password.setBounds(170, 150, 150, 25);
        frame.add(password);

        JButton buttonlogin = new JButton("Login");
        buttonlogin.setBounds(125, 200, 70, 25);
        frame.add(buttonlogin);

        JButton buttonregister = new JButton("Add Account");
        buttonregister.setBounds(125, 250, 120, 25);
        frame.add(buttonregister);

        frame.setBounds(400, 100, 500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        //监听器
        buttonlogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //读取输入框中的Username和Password
                String name = username.getText();
                String passwd = new String (password.getPassword());
                String fileUrl = "files\\DBMSUser\\UserList.txt";

                Admin admin = new Admin();

                admin.setName(name);
                admin.setPassword(passwd);

                if(admin.JudgeRegister() && admin.StringSearchLine(name,passwd,fileUrl)) {
                    JOptionPane.showMessageDialog(null, "Login Successfully");//注册成功弹窗
                    frame.setVisible(false);

                    //MainInterface maininterface = new MainInterface //弹出并不存在的主界面

                } else {
                    JOptionPane.showMessageDialog(null,"Wrong Username Or Password");
                    password.setText("");
                    username.setText("");
                }

                /*
                if (name == "") {
                	JOptionPane.showMessageDialog(null, "Enter The Username","Username",JOptionPane.WARNING_MESSAGE);
                }
                if (passwd == "") {
                	JOptionPane.showMessageDialog(null, "Enter The Password","Password",JOptionPane.WARNING_MESSAGE);
                } else {
                    Admin admin = new Admin();
                    admin.setName(name);
                    admin.setPassword(passwd);

                    //登录
                    if(Admin.StringSearchLine(name,passwd,fileUrl)) {
                    	//登录成功弹窗
                    	JOptionPane.showMessageDialog(null, "Login Successfully", "Login Successfully", JOptionPane.NO_OPTION);
                    	frame.setVisible(false);

                    } else {
                    	//错误弹窗
                    	JOptionPane.showMessageDialog(null, "Account does not exist or the password is wrong","Account does not exist or the password is wrong",JOptionPane.WARNING_MESSAGE);
                    	//清除密码框中的信息
                    	password.setText("");
                    	//清除账号框中的信息
                    	username.setText("");

                    	//MainInterface maininterface = new MainInterface //弹出并不存在的主界面
                    }*/
                    mainFrame mainFrame = new mainFrame();
                    mainFrame.showFrame();
            }
        });
        //Add Account键的监听
        buttonregister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                frame.setVisible(false);
                //注册页面
                AdminRegister ar = new AdminRegister();
            }
        });
    }

    public static void main(String []args) {
        Login_Register login_register = new Login_Register();
    }
}