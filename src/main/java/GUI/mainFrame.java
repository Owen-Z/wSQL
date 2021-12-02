package GUI;
import API.*;
import com.com.*;
import tools.Tools;
import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import static jdk.javadoc.doclet.DocletEnvironment.ModuleMode.API;

class mainFrame{
	private JFrame mainFr;
	private JPanel mainPaHead;
	//main Gui stucture
	private JPanel mainPaHeadL1;
	private JPanel mainPaHeadL2;
	private JPanel mainPaMidL;
	private JPanel mainPaMidM;
	private JPanel mainPaMidMH;
	private JPanel mainPaMidMF;
	//head button level1

	//head button level2

	//
	public mainFrame() {}
	//设置主窗口
	public JFrame setMainFr()
	{
		mainFr = new JFrame("work place");
//		窗口关闭方式
		mainFr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		设置窗口出现的位置和大小
		mainFr.setBounds(100,100,1000,618);
//		显示窗口
		mainFr.setVisible(true);
//		自适应
//		mainFr.pack();
		return mainFr;
	}
	//设置头组件
	public JPanel setMainPaHead()
	{
		mainPaHead = new JPanel();
//		Color color = new Color(42, 81, 182);
//		mainPaHead.setBackground(color);
		mainPaHead.setVisible(true);
		mainPaHead.add(mainPaHeadL1);
		mainPaHead.add(mainPaHeadL2);
		return mainPaHead;
	}
	//创建左子窗体
	public JPanel setMainPaMidL()
	{
		mainPaMidL = new JPanel();
//		Color color = new Color(182, 24, 24);
//		mainPaMidL.setBackground(color);
		return mainPaMidL;
	}
	//创建中子窗体
	public JPanel setMainPaMidM()
	{
		mainPaMidM = new JPanel();
//		Color color = new Color(0x4FCE9B);
//		mainPaMidM.setBackground(color);
		return mainPaMidM;
	}
	//设置头组件1层
	public JPanel setMainPaHeadL1()
	{
		mainPaHeadL1 = new JPanel();
		mainPaHeadL1.setBounds(0,0,900,200);
//		Color color = new Color(158, 46, 46);
//		mainPaHeadL1.setBackground(color);
		mainPaHeadL1.setVisible(true);
		return mainPaHeadL1;
	}
	//设置头组件2层
	public JPanel setMainPaHeadL2()
	{
		mainPaHeadL2 = new JPanel();
		return mainPaHeadL2;
	}
	//add menu item
	public void addMenuItem()
	{

	}

	//实例化level1
	//todo.........


	public void fileTreeBaseListen(DefaultMutableTreeNode node){
		//创建库文件右键选项
		JPopupMenu baseMenu = new JPopupMenu();
		JMenuItem createSchame= new JMenuItem("create schame");
		JMenuItem createTable= new JMenuItem("create table");
		JMenuItem addTable= new JMenuItem("add table");
		JMenuItem dropSchame= new JMenuItem("drop schame");
//		JMenuItem = new JMenuItem("");
//		JMenuItem = new JMenuItem("");
		baseMenu.add(createSchame);
		baseMenu.add(createTable);
		baseMenu.add(addTable);
		baseMenu.add(dropSchame);


	}
	public ArrayList<DefaultMutableTreeNode> initFileTree()
	{
		//创建文件节点
		//获取该用户对应的库文件
		//按行读取文件获取数据库跟
		//此处以读取wsql.db为例
		//获取文件 String baseFileName = getbaseFile();
		ArrayList<DefaultMutableTreeNode> fileTreeList = new ArrayList<DefaultMutableTreeNode>();
		//getBaseFile();
		ArrayList baseFile = new ArrayList();
		int i = 0;
//		调用返回list的方法，假设为arrayList baselist= getBaseList()

		List<String> dbLIST = new ArrayList<>();
		Tools dbNames = new Tools();
		dbLIST = dbNames.getDB();
		Iterator<String> iterDB = dbLIST.iterator();
		while(iterDB.hasNext()){  //执行过程中会执行数据锁定，性能稍差，若在循环过程中要去掉某个元素只能调用iter.remove()方法。
			String str = iterDB.next();
			ArrayList tableName = new ArrayList();
			baseFile.add(str);

			List<String> TBLIST = dbNames.getTB(str);
			Iterator<String> iterTB = TBLIST.iterator();
			while(iterTB.hasNext())
			{
				tableName.add(iterTB.next());
			}
			DefaultMutableTreeNode baseRoot = new DefaultMutableTreeNode(baseFile.get(i));
			DefaultMutableTreeNode tableItem = new DefaultMutableTreeNode("Tables");
			System.out.println(tableName.size());
			for(int count = 0;count < tableName.size();count++)
			{
				DefaultMutableTreeNode tItem = new DefaultMutableTreeNode(tableName.get(count));
				tableItem.add(tItem);
			}
			baseRoot.add(tableItem);
			fileTreeList.add(baseRoot);
			//创建库文件右键选项
			JPopupMenu baseMenu = new JPopupMenu();
			JMenuItem createSchame= new JMenuItem("create schame");
			JMenuItem createTable= new JMenuItem("create table");
			JMenuItem addTable= new JMenuItem("add table");
			JMenuItem dropSchame= new JMenuItem("drop schame");

			baseMenu.add(createSchame);
			baseMenu.add(createTable);
			baseMenu.add(addTable);
			baseMenu.add(dropSchame);
			i++;
		}

//		String baseFileName = "D:\\Users\\Desktop\\Desktop\\wSQL\\src\\data\\database\\WSql.db";
//		try(Scanner scBas = new Scanner(new FileReader(baseFileName))) {
//			while(scBas.hasNextLine())
//			{
//				ArrayList tableName = new ArrayList();
//				baseFile.add(scBas.nextLine());
//
//
//				//对于每一个库创建子树--表
//				//读取该库文件中的表名
//					//getTableFile();
//					Scanner scTab = new Scanner(new FileReader("D:\\Users\\Desktop\\Desktop\\wSQL\\src\\data\\database\\tableTry.txt"));
//					while(scTab.hasNextLine())
//					{
//						tableName.add(scTab.nextLine());
//					}
//
//
//				DefaultMutableTreeNode baseRoot = new DefaultMutableTreeNode(baseFile.get(i));
//				DefaultMutableTreeNode tableItem = new DefaultMutableTreeNode("Tables");
//				System.out.println(tableName.size());
//				for(int count = 0;count < tableName.size();count++)
//				{
//					DefaultMutableTreeNode tItem = new DefaultMutableTreeNode(tableName.get(count));
//					tableItem.add(tItem);
//				}
//				baseRoot.add(tableItem);
//				fileTreeList.add(baseRoot);
//				//创建库文件右键选项
//				JPopupMenu baseMenu = new JPopupMenu();
//				JMenuItem createSchame= new JMenuItem("create schame");
//				JMenuItem createTable= new JMenuItem("create table");
//				JMenuItem addTable= new JMenuItem("add table");
//				JMenuItem dropSchame= new JMenuItem("drop schame");
////				JMenuItem = new JMenuItem("");
////				JMenuItem = new JMenuItem("");
//				baseMenu.add(createSchame);
//				baseMenu.add(createTable);
//				baseMenu.add(addTable);
//				baseMenu.add(dropSchame);
//				//
//
//				i++;
//			}

		return fileTreeList;
	}
	//设置列表组件
	//设置sql语句组件
//	public String getClickName()
//	{
//		String name = null;
//
//		return name;
//	}
//	public String newAtable()
//	{
//		String newSuccess = "creating successfully!";
//
//		return newSuccess;
//	}
	public void showFrame(String userName) {
		API startWsql = new API();
		startWsql.setUserName(userName);

		JFrame mainFr;
		JPanel mainPaHead;
		JPanel mainPaHeadL1;
		JPanel mainPaHeadL2;
		JPanel mainPaMidL;
		JPanel mainPaMidM;
		JTree FileTree;

		//level2 button
//		JButton PaHeadBtnAddSqlE = new JButton("AddSqlE");//create a new sql tab for executing querise
//		JButton PaHeadBtnOpenSql = new JButton("penSql");//open a sql script file in a new query tab
//		JButton PaHeadBtnOpenIns = new JButton("OpenIns"); //open inspector for seleting object
//		JButton PaHeadBtnCreaSch = new JButton("CreaSch"); //create a new schema in the connected server
//		JButton PaHeadBtnCreaTab = new JButton("CreaTab"); //create a new table in the active schema in the connect server
//		JButton PaHeadBtnCreaVie = new JButton("CreaVie"); //create a new view int the active schema in the connect server
//		JButton PaHeadBtnCreaStP = new JButton("CreaStP"); //create a new stored procedure in the active schema in the connect server
//		JButton PaHeadBtnCreaFun = new JButton("CreaFun"); //create a new function in the active schema in the connect server
//		JButton PaHeadBtnSearTaT = new JButton("SearTaT"); //serach table text for object selected in the connect server
//		JButton PaHeadBtnReconne = new JButton("Reconne"); //reconnect to DBMS
		mainFrame test_frame = new mainFrame();
		//创建实例
		//level1
		//menubar
//		JMenuBar menuBar = new JMenuBar();
		//menu
//		JMenu menuEdit = new JMenu("EDIT");
//		JMenu menuFile = new JMenu("FILE");
//		JMenu menuView = new JMenu("View");
//		JMenu menuQuery = new JMenu("Query");
//		JMenu menuDatabase = new JMenu("Database");
//		JMenu menuServer = new JMenu("Server");
//		JMenu menuTools = new JMenu("Tools");
//		JMenu menuScripting = new JMenu("Scripting");
//		JMenu menuHelp = new JMenu("Help");
		//menuitem
//		JMenuItem = new JMenuItem("");
		//add menu item of File
//		JMenuItem mItemFile1 = new JMenuItem("");
//		JMenuItem mItemFile1 = new JMenuItem("");
//		JMenuItem mItemFile1 = new JMenuItem("");
//		JMenuItem mItemFile1 = new JMenuItem("");
//		JMenuItem mItemFile1 = new JMenuItem("");
//		JMenuItem mItemFile1 = new JMenuItem("");
//		JMenuItem mItemFile1 = new JMenuItem("");
//		JMenuItem mItemFile1 = new JMenuItem("");
//		JMenuItem mItemFile1 = new JMenuItem("");
		//add menu item of tree


		mainPaHeadL1 = test_frame.setMainPaHeadL1();
		mainPaHeadL2 = test_frame.setMainPaHeadL2();
		mainPaHead = test_frame.setMainPaHead();
		mainFr = test_frame.setMainFr();
		mainPaMidL = test_frame.setMainPaMidL();
		mainPaMidM = test_frame.setMainPaMidM();

		//设置布局方式
		mainFr.setLayout(new BorderLayout());
		mainPaHead.setLayout(new BorderLayout());
		mainPaHeadL1.setLayout(new FlowLayout());
		mainPaHeadL2.setLayout(new FlowLayout());
		//添加元素
		System.out.println("gui start.....................");
		//顶部组件添加元素
		mainFr.add(mainPaHead,BorderLayout.NORTH);

		mainFr.add(mainPaMidM,BorderLayout.CENTER);
		mainPaHead.add(mainPaHeadL1,BorderLayout.NORTH);
		mainPaHead.add(mainPaHeadL2,BorderLayout.SOUTH);
		//level1 添加菜单
//		mainPaHeadL1.add(menuFile);
//		mainPaHeadL1.add(menuEdit);
//		mainPaHeadL1.add(menuView);
//		mainPaHeadL1.add(menuQuery);
//		mainPaHeadL1.add(menuDatabase);
//		mainPaHeadL1.add(menuServer);
//		mainPaHeadL1.add(menuTools);
//		mainPaHeadL1.add(menuScripting);
//		mainPaHeadL1.add(menuHelp);
		//level2
//		mainPaHeadL2.add(PaHeadBtnAddSqlE);
//		mainPaHeadL2.add(PaHeadBtnOpenSql);
//		mainPaHeadL2.add(PaHeadBtnOpenIns);
//		mainPaHeadL2.add(PaHeadBtnCreaSch);
//		mainPaHeadL2.add(PaHeadBtnCreaTab);
//		mainPaHeadL2.add(PaHeadBtnCreaVie);
//		mainPaHeadL2.add(PaHeadBtnCreaStP);
//		mainPaHeadL2.add(PaHeadBtnCreaFun);
//		mainPaHeadL2.add(PaHeadBtnSearTaT);
//		mainPaHeadL2.add(PaHeadBtnReconne);

		//level1添加菜单项

		//左部组件添加元素
		//JTree
		//初始化树
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("all   the   database");
		ArrayList<DefaultMutableTreeNode> treeList= test_frame.initFileTree();
		for (int i = 0;i < treeList.size();i++)
		{
			root.add(treeList.get(i));
		}
		JTree jTree = new JTree(root);
		//为文件树加滚动条

		JScrollPane scrollPane = new JScrollPane(mainPaMidL);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		mainPaMidL.add(jTree);
		mainFr.add(scrollPane,BorderLayout.WEST);
		//设置大小
		//sqlText部件添加元素
//		sqlText.setLineWrap(true);
		JTextPane sqlText = new JTextPane();
		Font f = new Font("仿宋",Font.BOLD,20);
		sqlText.setFont(f);
		JScrollPane scrollPane1 = new JScrollPane(sqlText);
		scrollPane1.setPreferredSize(new Dimension((int)(mainPaMidM.getWidth()),(int)(mainFr.getHeight() * 0.7)));
		mainPaMidM.setLayout(new BorderLayout());
		mainPaMidM.add(scrollPane1,BorderLayout.CENTER);
//		mainFr.add(sqlText,BorderLayout.CENTER);
		//中间层apply
		JButton sqlAppply = new JButton("APPLY");
		sqlAppply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//获取文本的信息
				String realSen = sqlText.getText().trim();
				String str = startWsql.parse(realSen);
				if(realSen.contains("select"))
				{
					sqlText.setText(str);
				}
			}
		});
		JButton sqlCancel = new JButton("CANCEL");
		sqlCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sqlText.setText("");
			}
		});
		JButton reflash = new JButton("reflash");
		reflash.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				mainFr.remove(mainPaMidL);
//				JPanel mainPaMidL = new JPanel();
//				DefaultMutableTreeNode root = new DefaultMutableTreeNode("all   the   database");
//				ArrayList<DefaultMutableTreeNode> treeList= test_frame.initFileTree();
//				for (int i = 0;i < treeList.size();i++)
//				{
//					root.add(treeList.get(i));
//				}
//				JTree jTree = new JTree(root);
//				//为文件树加滚动条
//
//				JScrollPane scrollPane = new JScrollPane(mainPaMidL);
//				scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//				mainPaMidL.add(jTree);
//				mainPaMidL.revalidate();
//				mainPaMidL.repaint();
//				mainFr.add(scrollPane,BorderLayout.WEST);
				mainFr.dispose();
				mainFrame mainf = new mainFrame();
				mainf.showFrame(userName);
			}
		});
		JPanel operation = new JPanel();
		operation.add(sqlAppply);
		operation.add(sqlCancel);
		operation.add(reflash);
		mainPaMidM.add(operation,BorderLayout.SOUTH);
		//底部文本框显示操作状态
		JTextPane bottomText = new JTextPane();
		JScrollPane scrollPane2 = new JScrollPane(bottomText);
		scrollPane2.setPreferredSize(new Dimension((int)(mainFr.getWidth()),(int)(mainFr.getHeight() * 0.3)));
		mainFr.add(scrollPane2,BorderLayout.SOUTH);
		//点击数的节点，获取表名，再获取相应的数据显示在中心面板上
		//左键
		jTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				TreePath selPath;
				if(e.getSource() == jTree && e.getClickCount() == 1)
				{
					selPath = jTree.getPathForLocation(e.getX(),e.getY());
//					DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
//					System.out.println();

					System.out.println(selPath.getPath()[1]);
					startWsql.setDbName(selPath.getPath()[1].toString());
				}
				if (e.getSource() == jTree && e.getClickCount() == 2){
					//节点路径
					selPath = jTree.getPathForLocation(e.getX(),e.getY());
					if(selPath != null)
					{
						System.out.println(selPath);
						System.out.println(selPath.getPathCount());
						//获取双击的节点名称
//						DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
//						System.out.println(node.toString());
						//查看表的信息
						//得到一个sql语句,调用API
						if (selPath.getPathCount() == 4)//双击的是一个表，根据表名创建sql语句
						{
							DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
							String strTbName = node.toString();
							//sql语句

//									startWsql.parse(sqlSentence);
							String sqlSentence  = "SELECT * FROM" + " " +strTbName;
							System.out.println(sqlSentence);
							System.out.println(startWsql.parse(sqlSentence));
							//append() JTEXTPANE sqlTEXT
							if (startWsql.parse(sqlSentence) != "true" || startWsql.parse(sqlSentence) != "false")
							{
								sqlText.setText(startWsql.parse(sqlSentence));
//								Document document = ("selecting     " + strTbName+ "       successful........................" + "\n");
							}
						}
					}
				}
				if (e.isMetaDown())
				{
					selPath = jTree.getPathForLocation(e.getX(),e.getY());

					if(selPath != null)
					{
						if(selPath.getPathCount() == 2)//说明在库文件右击
						{
							DefaultMutableTreeNode node1 = (DefaultMutableTreeNode) selPath.getLastPathComponent();
							//创建库文件右键选项
							JPopupMenu baseMenu = new JPopupMenu();
							JMenuItem createSchame= new JMenuItem("create schame");
							createSchame.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									String strBsae = "log.create database" + " "+ node1.toString();
									System.out.println(strBsae);
								}
							});
							JMenuItem createTable= new JMenuItem("create table");
							JMenuItem addTable= new JMenuItem("add table");
							JMenuItem dropSchame= new JMenuItem("drop schame");
//		JMenuItem = new JMenuItem("");
//		JMenuItem = new JMenuItem("");
							baseMenu.add(createSchame);
							baseMenu.add(createTable);
							baseMenu.addSeparator();//添加一条分割符
							baseMenu.add(addTable);
							baseMenu.add(dropSchame);
							//将选项菜单添加到对应的节点上
							DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
							//显示弹出菜单
							baseMenu.show(e.getComponent(),e.getX(),e.getY());
						}
						else if (selPath.getPathCount() == 4)//说明在表文件右击
						{
							DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
							//创建表文件右键菜单
							JPopupMenu tableMenu = new JPopupMenu();
							JMenuItem selectRow = new JMenuItem("select rows");
//							JMenuItem tabInspetor = new JMenuItem("Table Inspetor");
							JMenuItem dropTable= new JMenuItem("drop tables");
							dropTable.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
//									System.out.println(node.toString());
									String strTbName = node.toString();
									String sqlsentence = "DROP TABLE" + " " + strTbName;
									startWsql.parse(sqlsentence);
								}
							});
							JMenuItem alterTable= new JMenuItem("alter tables");
							alterTable.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
//									String strAlterSen = ""
								}
							});
							JMenuItem createTables= new JMenuItem("create table");
							createTables.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									//弹出创建表的表格，可直接编辑
//									CreateTableUI createTableUI = new CreateTableUI();
//									createTableUI.showCtableUI();

								}
							});
							JMenuItem TruncateTable= new JMenuItem("truncate table");

							tableMenu.add(selectRow);
//							tableMenu.add(tabInspetor);
							tableMenu.add(dropTable);
							tableMenu.add(alterTable);
							tableMenu.add(createTables);
//							tableMenu.add(TruncateTable);

							tableMenu.show(e.getComponent(),e.getX(),e.getY());
							//添加表右击的监听事件

						}
					}
				}
			}
		});
		//右键

		//创建编写sql语句的中心面板

	}
}