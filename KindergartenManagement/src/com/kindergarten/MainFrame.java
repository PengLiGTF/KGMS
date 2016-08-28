package com.kindergarten;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.kindergarten.data.Module;
import com.kindergarten.data.ModuleFun;
import com.kindergarten.groups.IndexGroup;
import com.kindergarten.groups.classes.ClassesAddGroup;
import com.kindergarten.groups.feetemplate.FeeTemplateGroup;
import com.kindergarten.groups.kinder.ExpireFeeKindersGroup;
import com.kindergarten.groups.kinder.KinderInfoListGroup;
import com.kindergarten.groups.kinder.KinderLastFeeGroup;
import com.kindergarten.groups.kinder.KinderLeaveGroup;
import com.kindergarten.groups.kinder.KinderRecordGroup;
import com.kindergarten.groups.kinder.PreFeeKinderListGroup;
import com.kindergarten.groups.user.AuthorityGrantGroup;
import com.kindergarten.groups.user.UserListGroup;
import com.kindergarten.service.UserService;
import com.kindergarten.util.MessageBoxUtil;
import com.kindergarten.util.SWTResourceManager;

public class MainFrame
{
	private Composite comp;
	private String userId;

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public MainFrame(String userId)
	{
		this.userId = userId;
	}

	public static void main(String[] args)
	{
		MainFrame mainFrame = new MainFrame(null);
		mainFrame.open();
	}

	/**
	 * Open the window.
	 * 
	 */
	public void open()
	{
		Display display = Display.getDefault();
		final Shell shell = new Shell(SWT.CLOSE);
		shell.setMinimumSize(new Point(800, 600));
		shell.setImage(SWTResourceManager.getImage(MainFrame.class, "/images/index.ico"));
		shell.setSize(1024, 600);
		shell.setLocation(250, 120);
		shell.setText("金鹰卡通幼儿园收费管理系统");

		Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
		UserService userService = new UserService();
		List<Module> moduleList = userService.getUserOwnModules(userId);
		if (!moduleList.isEmpty())
		{
			for (Module module : moduleList)
			{
				String moduleName = module.getModuleName();
				List<ModuleFun> funList = module.getModuleFuns();
				if (!funList.isEmpty())
				{
					Menu userPermissionViewItem = buildMenuAddToMenuBar(shell, menuBar, moduleName);
					for (ModuleFun moduleFun : funList)
					{
						buildDropdownMenuItemToMenu(userPermissionViewItem, moduleFun);
					}
				}
			}
		}
		buildCommonhelpMenu(menuBar);
		// 左边树形菜单
		buildLeftTree(shell, moduleList);
		shell.open();
		shell.layout();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}
	}

	/**
	 * 所有界面上的帮助菜单
	 * */
	private void buildCommonhelpMenu(final Menu menuBar)
	{
		MenuItem logmanage = new MenuItem(menuBar, SWT.CASCADE);
		logmanage.setText("登陆管理");
		Menu loginItem = new Menu(logmanage);
		logmanage.setMenu(loginItem);
		MenuItem loginOut = new MenuItem(loginItem, SWT.NONE);
		loginOut.setImage(SWTResourceManager.getImage(MainFrame.class, "/images/help.ico"));
		loginOut.setText("注销登陆");
		loginOut.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Shell shell = menuBar.getShell();
				int choice = MessageBoxUtil.showConfirmMessageBox(shell, "确定注销当前用户？");
				if (SWT.OK == choice)
				{
					shell.close();
					new LoginFrame().openLoginFrame();
				}
			}
		});

		// loginOut.setAccelerator(SWT.F1);

		MenuItem manageHelp = new MenuItem(menuBar, SWT.CASCADE);
		manageHelp.setText("Help");
		Menu viewItem3 = new Menu(manageHelp);
		manageHelp.setMenu(viewItem3);
		MenuItem addhelpItem = new MenuItem(viewItem3, SWT.NONE);
		addhelpItem.setImage(SWTResourceManager.getImage(MainFrame.class, "/images/help.ico"));
		addhelpItem.setText("帮助\tF1");
		addhelpItem.setAccelerator(SWT.F1);

		MenuItem viewhelpItem = new MenuItem(viewItem3, SWT.NONE);
		viewhelpItem.setImage(SWTResourceManager.getImage(MainFrame.class, "/images/about.ico"));
		viewhelpItem.setText("help2\tF2");
		viewhelpItem.setAccelerator(SWT.F2);
	}

	/**
	 * 构造左边树形菜单
	 * */
	private void buildLeftTree(final Shell shell, List<Module> moduleList)
	{
		Tree tree = new Tree(shell, SWT.BORDER);
		tree.setBounds(0, 0, 185, 542);
		if (moduleList != null && !moduleList.isEmpty())
		{
			for (Module module : moduleList)
			{
				TreeItem treeItemP = new TreeItem(tree, SWT.NONE);
				treeItemP.setImage(SWTResourceManager.getImage(MainFrame.class, "/images/dot.ico"));
				treeItemP.setText(module.getModuleName());
				treeItemP.setData(module);
				List<ModuleFun> moduleFunList = module.getModuleFuns();
				if (moduleFunList != null && !moduleFunList.isEmpty())
				{
					for (ModuleFun moduleFun : moduleFunList)
					{
						final TreeItem treeItemC = new TreeItem(treeItemP, SWT.NONE);
						treeItemC.setImage(SWTResourceManager.getImage(MainFrame.class, "/images/sdot.ico"));
						treeItemC.setText(moduleFun.getFunName());
						treeItemC.setData(moduleFun);
					}
				}
			}
		}
		comp = new Composite(shell, SWT.NONE);
		comp.setBounds(191, 0, 817, 485);
		IndexGroup index = new IndexGroup(comp, SWT.NONE, userId);
		index.setBounds(0, 0, 816, 550);

		// coolItem.computeSize(size.x, size.y);

		tree.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				TreeItem selected = (TreeItem) e.item;
				Object obj = selected.getData();
				if (ModuleFun.class.isInstance(obj))
				{
					Control[] controls = comp.getChildren();
					if (controls != null && controls.length > 0)
					{
						comp.getChildren()[0].dispose();
					}
					menuOpenDialog(obj);
				}
			}

			private void menuOpenDialog(Object obj)
			{
				ModuleFun moduleFun = (ModuleFun) obj;
				String funName = moduleFun.getFunName();
				if (funName.equals("用户管理"))
				{
					new UserListGroup(comp, SWT.NONE, userId);
				} else if (funName.equals("权限管理"))
				{
					new AuthorityGrantGroup(comp, SWT.NONE, userId);
				} else if (funName.equals("班级管理"))
				{
					new ClassesAddGroup(comp, SWT.NONE, userId);
				} else if (funName.equals("收费标准管理"))
				{
					new FeeTemplateGroup(comp, SWT.NONE, userId);
				} else if (funName.equals("入园学生录入"))
				{
					new KinderRecordGroup(comp, SWT.NONE, userId);
				} else if (funName.equals("儿童信息管理"))
				{
					new KinderInfoListGroup(comp, SWT.NONE, userId);
				} else if (funName.equals("下月到期学生统计"))
				{
					new ExpireFeeKindersGroup(comp, SWT.NONE, userId);

				} else if (funName.equals("请假条录入"))
				{
					new KinderLeaveGroup(comp, SWT.NONE, userId);

				} else if (funName.equals("预缴费学生管理"))
				{
					new PreFeeKinderListGroup(comp, SWT.NONE, userId);
				} else if (funName.equals("续费学生录入"))
				{
					// new KinderFeeRenewGroup(comp, SWT.NONE, userId);
					new ExpireFeeKindersGroup(comp, SWT.NONE, userId, "");
				} else if (funName.equals("系统管理"))
				{
					new KinderLastFeeGroup(comp, SWT.NONE, userId, "");
				} else if (funName.equals("学生缴费修改"))
				{
					new KinderLastFeeGroup(comp, SWT.NONE, userId, "");
				} else
				{
					new IndexGroup(comp, SWT.NONE, userId);
				}
			}
		});
	}

	/**
	 * 添加菜单和菜单事件处理
	 * */
	private void buildDropdownMenuItemToMenu(Menu userPermissionViewItem, ModuleFun moduleFun)
	{
		MenuItem userMItem = new MenuItem(userPermissionViewItem, SWT.NONE);
		userMItem.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				Control[] controls = comp.getChildren();
				if (controls != null && controls.length > 0)
				{
					comp.getChildren()[0].dispose();
				}
				ModuleFun moduleFun = (ModuleFun) ((MenuItem) e.getSource()).getData();
				String funName = moduleFun.getFunName();
				if (funName.equals("用户管理"))
				{
					new UserListGroup(comp, SWT.NONE, userId);
				} else if (funName.equals("权限管理"))
				{
					new AuthorityGrantGroup(comp, SWT.NONE, userId);
				} else if (funName.equals("班级管理"))
				{
					new ClassesAddGroup(comp, SWT.NONE, userId);
				} else if (funName.equals("收费标准管理"))
				{
					new FeeTemplateGroup(comp, SWT.NONE, userId);
				} else if (funName.equals("入园学生录入"))
				{
					new KinderRecordGroup(comp, SWT.NONE, userId);
				} else if (funName.equals("儿童信息管理"))
				{
					new KinderInfoListGroup(comp, SWT.NONE, userId);
				} else if (funName.equals("下月到期学生统计"))
				{
					new ExpireFeeKindersGroup(comp, SWT.NONE, userId);

				} else if (funName.equals("请假条录入"))
				{
					new KinderLeaveGroup(comp, SWT.NONE, userId);

				} else if (funName.equals("预缴费学生管理"))
				{
					new PreFeeKinderListGroup(comp, SWT.NONE, userId);
				} else if (funName.equals("续费学生录入"))
				{
					new ExpireFeeKindersGroup(comp, SWT.NONE, userId, "");
					// new KinderFeeRenewGroup(comp, SWT.NONE, userId);
				} else if (funName.equals("系统管理"))
				{
					new KinderLastFeeGroup(comp, SWT.NONE, userId, "");
				} else if (funName.equals("学生缴费修改"))
				{
					new KinderLastFeeGroup(comp, SWT.NONE, userId, "");
				} else
				{
					new IndexGroup(comp, SWT.NONE, userId);
				}
			}
		});
		userMItem.setImage(SWTResourceManager.getImage(MainFrame.class, "/images/addbook.ico"));
		userMItem.setText(moduleFun.getFunName());
		userMItem.setData(moduleFun);
		// userMItem.setAccelerator(SWT.CTRL + 'I');
	}

	/**
	 * 将菜单添加到菜单bar上
	 * */
	private Menu buildMenuAddToMenuBar(final Shell shell, Menu menuBar, String moduleName)
	{
		MenuItem userPermissionMenu = new MenuItem(menuBar, SWT.CASCADE);
		userPermissionMenu.setText(moduleName);
		menuBar.setData(userPermissionMenu);// add to menu bar
		Menu userPermissionViewItem = new Menu(shell, SWT.DROP_DOWN);
		userPermissionMenu.setMenu(userPermissionViewItem);
		return userPermissionViewItem;
	}
}
