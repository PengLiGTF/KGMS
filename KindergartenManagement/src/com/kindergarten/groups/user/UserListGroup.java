package com.kindergarten.groups.user;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.kindergarten.data.User;
import com.kindergarten.groups.AbstractGroup;
import com.kindergarten.service.UserService;
import com.kindergarten.util.ButtonNameConstant;
import com.kindergarten.util.CommonUtil;
import com.kindergarten.util.MessageBoxUtil;
import com.kindergarten.util.print.PrinterUtil;

public class UserListGroup extends AbstractGroup
{
	private Text userID;
	private Text userName;
	private Table userTable;
	private final TableViewer tableViewer;

	public UserListGroup(final Composite parent, int style, final String userId)
	{
		super(parent, style, userId);
		this.setText("用户列表");
		composite.setBounds(10, 16, 731, 448);

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBounds(10, 10, 61, 17);
		lblNewLabel.setText("用户名:");

		userID = new Text(composite, SWT.BORDER);
		userID.setBounds(100, 7, 93, 23);

		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setBounds(251, 10, 61, 17);
		lblNewLabel_1.setText("用户名：");
		lblNewLabel_1.setVisible(false);

		userName = new Text(composite, SWT.BORDER);
		userName.setBounds(318, 7, 102, 23);
		userName.setVisible(false);

		final Button btnQuery = new Button(composite, SWT.NONE);
		btnQuery.setBounds(524, 5, 80, 27);
		btnQuery.setText("查询");
		btnQuery.setData(ButtonNameConstant.BTN_QUERY);
		btnQuery.addMouseListener(new UserOperMouseListener(getShell()));

		Label label = new Label(composite, SWT.NONE);
		label.setBounds(10, 48, 61, 17);
		label.setText("用户列表");

		Label label_1 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setText("用户列表");
		label_1.setBounds(10, 71, 594, 2);

		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		userTable = tableViewer.getTable();
		userTable.setLinesVisible(true);
		userTable.setHeaderVisible(true);
		userTable.setBounds(10, 82, 594, 337);

		TableViewerColumn userIdColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnid = userIdColumn.getColumn();
		tblclmnid.setWidth(100);
		tblclmnid.setText("用户ID");
		userIdColumn.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				User user = (User) element;
				return user.getUserId();
			}
		});
		tblclmnid.dispose();

		TableViewerColumn userNameColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnUerName = userNameColumn.getColumn();
		tblclmnUerName.setWidth(100);
		tblclmnUerName.setText("用户名");
		userNameColumn.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				User user = (User) element;
				return user.getUserName();
			}
		});

		TableViewerColumn userSexColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnUerSex = userSexColumn.getColumn();
		tblclmnUerSex.setWidth(100);
		tblclmnUerSex.setText("性别");
		userSexColumn.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				User user = (User) element;
				return CommonUtil.getSexNameByCode(user.getUserSex());
			}
		});

		TableViewerColumn userPhoneColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewPhone = userPhoneColumn.getColumn();
		tblclmnNewPhone.setWidth(100);
		tblclmnNewPhone.setText("电话号码");
		userPhoneColumn.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				User user = (User) element;
				return user.getUserPhone();
			}
		});

		TableViewerColumn userMailColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnMail = userMailColumn.getColumn();
		tblclmnMail.setWidth(100);
		tblclmnMail.setText("邮箱");
		userMailColumn.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				User user = (User) element;
				return user.getUserMail();
			}
		});

		tableViewer.setContentProvider(new MyTableStructedProvider());
		// tableViewer.setLabelProvider(new MyITableLabelProvider());

		Map<String, String> inputMap = new HashMap<String, String>();
		tableViewer.setInput(inputMap);

		Button btnAdd = new Button(composite, SWT.NONE);
		btnAdd.setData("btnAdd");
		btnAdd.setBounds(627, 82, 80, 27);
		btnAdd.setText("增加");
		btnAdd.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseDoubleClick(MouseEvent e)
			{

			}

			@Override
			public void mouseDown(MouseEvent e)
			{

			}

			@Override
			public void mouseUp(MouseEvent e)
			{
				int openState = new UserAddDialog(parent.getShell()).open();
				if (0 == openState)
				{
					Map<String, String> map = new HashMap<String, String>();
					tableViewer.setInput(map);
				}
			}
		});

		Label label_2 = new Label(composite, SWT.SEPARATOR | SWT.VERTICAL);
		label_2.setBounds(619, 82, 2, 337);

		Button btnEdit = new Button(composite, SWT.NONE);
		btnEdit.setData(ButtonNameConstant.BTN_EDIT);
		btnEdit.setBounds(627, 142, 80, 27);
		btnEdit.setText("修改");
		btnEdit.addMouseListener(new UserOperMouseListener(getShell()));

		Button btnDelete = new Button(composite, SWT.NONE);
		btnDelete.setData(ButtonNameConstant.BTN_DELETE);
		btnDelete.setBounds(627, 205, 80, 27);
		btnDelete.setText("删除");
		btnDelete.addMouseListener(new UserOperMouseListener(getShell()));

		Button btnPrint = new Button(composite, SWT.NONE);
		btnPrint.setBounds(627, 262, 80, 27);
		btnPrint.setText("打印");
		btnPrint.setData(ButtonNameConstant.BTN_PRINT);
		btnPrint.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				Table table = tableViewer.getTable();
				PrinterUtil.printTable(table, getShell(), "用户列表打印");
			}
		});
		this.setText("用户列表");
	}

	class UserOperMouseListener implements MouseListener
	{
		private Shell shell;

		public UserOperMouseListener(Shell shell)
		{
			super();
			this.shell = shell;
		}

		@Override
		public void mouseDoubleClick(MouseEvent e)
		{

		}

		@Override
		public void mouseDown(MouseEvent e)
		{

		}

		@Override
		public void mouseUp(MouseEvent e)
		{
			Button button = (Button) e.getSource();
			String data = (String) button.getData();
			if (ButtonNameConstant.BTN_QUERY.equals(data))
			{
				Map<String, String> map = new HashMap<String, String>();
				String userIdValue = userID.getText();
				String userNameValue = userName.getText();
				if (!StringUtils.isBlank(userIdValue))
				{
					map.put("userId", userIdValue);
				}
				if (!StringUtils.isBlank(userNameValue))
				{
					map.put("userName", userNameValue);
				}
				tableViewer.setInput(map);
			} else if (ButtonNameConstant.BTN_EDIT.equals(data))
			{
				StructuredSelection structuredSelectioin = (StructuredSelection) tableViewer.getSelection();
				Object object = structuredSelectioin.getFirstElement();
				if (object != null)
				{
					User user = (User) object;
					UserAddDialog userEditDialog = new UserAddDialog(shell, user);
					int openState = userEditDialog.open();
					if (0 == openState)
					{
						Map<String, String> map = new HashMap<String, String>();
						tableViewer.setInput(map);
					}
				} else
				{
					MessageBoxUtil.showWarnMessageBox(shell, "请选择要编辑的用户");
				}
			} else if (ButtonNameConstant.BTN_DELETE.equals(data))
			{
				StructuredSelection structuredSelectioin = (StructuredSelection) tableViewer.getSelection();
				Object object = structuredSelectioin.getFirstElement();
				if (object != null)
				{
					User user = (User) object;
					try
					{
						int result = MessageBoxUtil.showConfirmMessageBox(getShell(), "确定要删除用户：" + user.getUserName() + "吗？");
						if (result == SWT.OK)
						{
							new UserService().deleteUser(user.getUserId());
							Map<String, String> map = new HashMap<String, String>();
							MessageBoxUtil.showWarnMessageBox(getShell(), "用户删除成功");
							tableViewer.setInput(map);
						}
					} catch (SQLException e1)
					{
						MessageBoxUtil.showWarnMessageBox(getShell(), "用户删除失败");
						return;
					}
				} else
				{
					MessageBoxUtil.showWarnMessageBox(shell, "请选择要删除的用户");
					return;
				}
			}
		}
	}

	static class MyTableStructedProvider implements IStructuredContentProvider
	{
		@Override
		public void dispose()
		{

		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
		{

		}

		@Override
		public Object[] getElements(Object inputElement)
		{
			@SuppressWarnings("unchecked")
			Map<String, String> inputMap = (Map<String, String>) inputElement;
			String userId = inputMap.get("userId");
			String userName = inputMap.get("userName");
			UserService servie = new UserService();
			List<User> userList = servie.queryUserByCondition(userId, userName);
			return userList.toArray(new User[0]);
		}
	}
}
