package GUI;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import GUI.Admin;
import GUI.AdminRegister;
import GUI.Login_Register;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;


public class AdminRegister extends JFrame{
    AdminRegister () {
        init();
    }


    void init() {

        JFrame frame = new JFrame("YourSQL Register");
        frame.setLayout(null);

        JLabel nameStr = new JLabel("Username:");
        nameStr.setBounds(250, 150, 100, 25);
        frame.add(nameStr);

        JLabel passwordStr = new JLabel("Password:");
        passwordStr.setBounds(250, 200, 100, 25);
        frame.add(passwordStr);

        JLabel confrimStr = new JLabel("CFM PW:");
        confrimStr.setBounds(250, 250, 100, 30);
        frame.add(confrimStr);

        JTextField userName = new JTextField();
        userName.setBounds(320, 150, 150, 25);
        frame.add(userName);

        JLabel permissionStr = new JLabel("Permission");
        permissionStr.setBounds(250,300,100,30);
        frame.add(permissionStr);

        JPasswordField password = new JPasswordField();
        password.setBounds(320, 200, 150, 25);
        frame.add(password);

        JPasswordField confrimPassword = new JPasswordField();
        confrimPassword.setBounds(320, 250, 150, 30);
        frame.add(confrimPassword);
        //权限按钮组
        ButtonGroup permissionGroup = new ButtonGroup();

        JRadioButton permission1 = new JRadioButton("Read-Only");
        permission1.setBounds(320,300,150,30);
        permissionGroup.add(permission1);
        frame.add(permission1);

        JRadioButton permission2 = new JRadioButton("root");
        permission2.setBounds(320,320,150,30);
        permissionGroup.add(permission2);
        frame.add(permission2);


        JButton buttonregister = new JButton("Add A New Account");
        buttonregister.setBounds(320, 360, 150, 25);
        frame.add(buttonregister);

        frame.setBounds(400, 100, 800, 640);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        //为注册按钮增加监听器
        buttonregister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = userName.getText();
                String passwd = new String (password.getPassword());
                String confrimpasswd = new String (confrimPassword.getPassword());

                //JRadioButton的监听
                String permission = new String("");

                if (permission1.isSelected()) {
                    permission = "Read-Only";
                }

                if (permission2.isSelected()) {
                    permission = "root";
                }

                //创建Register类
                String fileUrl = "files\\DBMSUser\\UserList.txt";    //文件写入路径,项目根目录下files文件
                Register register = new Register();
                register.setName(name);
                register.setPassword(passwd);
                register.setconfirmpasswd(confrimpasswd);
                register.setPermission(permission);

                register.FileExistTest(fileUrl);

                //如果注册成功，返回登录界面
                try {
                    if(register.JudgeRegister() && register.UserRegister(name,passwd,permission,fileUrl)) {
                        JOptionPane.showMessageDialog(null, "Register Successfully");//注册成功弹窗
                        frame.setVisible(false);

                        Login_Register login_register = new Login_Register();  //弹回登录界面
                    }
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    //e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            }


        });
    }
}