package com.kindergarten.groups.user;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.kindergarten.data.Role;
import com.kindergarten.data.SexModel;
import com.kindergarten.data.User;
import com.kindergarten.service.RoleService;
import com.kindergarten.service.UserService;
import com.kindergarten.util.MessageBoxUtil;

public class UserAddDialog extends Dialog
{
	private Text user_id;
	private Text userName;
	private Text mail;
	private Text phone;
	private ComboViewer sex;
	private ComboViewer role;
	private User selectedUser;
	private List<Role> roleList;

	public UserAddDialog(Shell parentShell, User user)
	{
		this(parentShell);
		selectedUser = user;
		parentShell.setText("修改用户");
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @wbp.parser.constructor
	 */
	public UserAddDialog(Shell parentShell)
	{
		super(parentShell);
		setShellStyle(SWT.TITLE);
		parentShell.setText("用户增加");
		roleList = queryRole();
	}

	private List<Role> queryRole()
	{
		return new RoleService().queryAllRoles();
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FormLayout());

		Label useIdLabel = new Label(container, SWT.NONE);
		FormData fd_useIdLabel = new FormData();
		fd_useIdLabel.top = new FormAttachment(0, 18);
		fd_useIdLabel.left = new FormAttachment(0, 10);
		useIdLabel.setLayoutData(fd_useIdLabel);
		useIdLabel.setText("用户ID");

		user_id = new Text(container, SWT.BORDER);
		FormData fd_user_id = new FormData();
		fd_user_id.top = new FormAttachment(useIdLabel, -3, SWT.TOP);
		fd_user_id.left = new FormAttachment(useIdLabel, 26);
		fd_user_id.right = new FormAttachment(0, 199);
		user_id.setLayoutData(fd_user_id);

		if (selectedUser != null)
		{
			user_id.setEditable(false);
			user_id.setEnabled(false);
		}
		Label lblNewLabel = new Label(container, SWT.NONE);
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.top = new FormAttachment(useIdLabel, 0, SWT.TOP);
		fd_lblNewLabel.left = new FormAttachment(user_id, 33);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("用户名");

		userName = new Text(container, SWT.BORDER);
		FormData fd_userName = new FormData();
		fd_userName.right = new FormAttachment(lblNewLabel, 182, SWT.RIGHT);
		fd_userName.top = new FormAttachment(0, 15);
		fd_userName.left = new FormAttachment(lblNewLabel, 24);
		userName.setLayoutData(fd_userName);

		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		FormData fd_lblNewLabel_1 = new FormData();
		fd_lblNewLabel_1.top = new FormAttachment(useIdLabel, 29);
		fd_lblNewLabel_1.left = new FormAttachment(useIdLabel, 0, SWT.LEFT);
		lblNewLabel_1.setLayoutData(fd_lblNewLabel_1);
		lblNewLabel_1.setText("性别");

		mail = new Text(container, SWT.BORDER);
		FormData fd_mail = new FormData();
		fd_mail.right = new FormAttachment(userName, -1, SWT.RIGHT);
		fd_mail.left = new FormAttachment(user_id, 0, SWT.LEFT);
		mail.setLayoutData(fd_mail);

		Label label = new Label(container, SWT.NONE);
		fd_mail.top = new FormAttachment(label, -3, SWT.TOP);
		FormData fd_label = new FormData();
		fd_label.top = new FormAttachment(lblNewLabel_1, 29);
		fd_label.left = new FormAttachment(useIdLabel, 0, SWT.LEFT);
		label.setLayoutData(fd_label);
		label.setText("邮箱");

		Label label_1 = new Label(container, SWT.NONE);
		FormData fd_label_1 = new FormData();
		fd_label_1.top = new FormAttachment(lblNewLabel_1, 0, SWT.TOP);
		fd_label_1.left = new FormAttachment(lblNewLabel, 0, SWT.LEFT);
		label_1.setLayoutData(fd_label_1);
		label_1.setText("电话");

		phone = new Text(container, SWT.BORDER);
		FormData fd_phone = new FormData();
		fd_phone.right = new FormAttachment(userName, 0, SWT.RIGHT);
		fd_phone.top = new FormAttachment(userName, 23);
		fd_phone.left = new FormAttachment(userName, 0, SWT.LEFT);
		phone.setLayoutData(fd_phone);

		Label label_2 = new Label(container, SWT.NONE);
		FormData fd_label_2 = new FormData();
		fd_label_2.top = new FormAttachment(lblNewLabel_1, 75);
		fd_label_2.left = new FormAttachment(0, 10);
		label_2.setLayoutData(fd_label_2);
		label_2.setText("角色");

		sex = new ComboViewer(container, SWT.NONE);
		Combo combo = sex.getCombo();
		FormData fd_combo = new FormData();
		fd_combo.right = new FormAttachment(user_id, 0, SWT.RIGHT);
		fd_combo.top = new FormAttachment(lblNewLabel_1, -3, SWT.TOP);
		fd_combo.left = new FormAttachment(user_id, 0, SWT.LEFT);
		combo.setLayoutData(fd_combo);

		sex.setContentProvider(new ArrayContentProvider()
		{
			@Override
			public Object[] getElements(Object inputElement)
			{
				return (Object[]) inputElement;
			}
		});
		sex.setLabelProvider(new ILabelProvider()
		{
			@Override
			public Image getImage(Object element)
			{
				return null;
			}

			@Override
			public String getText(Object element)
			{
				SexModel sexModel = (SexModel) element;
				return sexModel.getName();
			}

			@Override
			public void addListener(ILabelProviderListener listener)
			{

			}

			@Override
			public void dispose()
			{

			}

			@Override
			public boolean isLabelProperty(Object element, String property)
			{
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener listener)
			{
				// TODO Auto-generated method stub

			}

		});
		SexModel[] sexs = new SexModel[]
		{ new SexModel("男", 'M'), new SexModel("女", 'W'), new SexModel("其他", 'O') };
		sex.setInput(sexs);
		StructuredSelection seleced = new StructuredSelection(new SexModel[]
		{ new SexModel("男", 'M') });// TODO
		sex.setSelection(seleced);

		role = new ComboViewer(container, SWT.NONE);
		Combo combo_1 = role.getCombo();
		FormData fd_combo_1 = new FormData();
		fd_combo_1.right = new FormAttachment(label_2, 197, SWT.RIGHT);
		fd_combo_1.top = new FormAttachment(mail, 23);
		fd_combo_1.left = new FormAttachment(label_2, 39);
		combo_1.setLayoutData(fd_combo_1);
		role.setContentProvider(new ArrayContentProvider()
		{
			@Override
			public Object[] getElements(Object inputElement)
			{
				return (Object[]) inputElement;
			}
		});

		role.setLabelProvider(new ILabelProvider()
		{
			@Override
			public void addListener(ILabelProviderListener listener)
			{

			}

			@Override
			public void dispose()
			{

			}

			@Override
			public boolean isLabelProperty(Object element, String property)
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener listener)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public Image getImage(Object element)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getText(Object element)
			{
				Role tmp = (Role) element;
				return tmp.getRoleName();
			}
		});
		role.setInput(roleList.toArray(new Role[0]));

		if (selectedUser != null)
		{
			user_id.setText(selectedUser.getUserId());
			userName.setText(selectedUser.getUserName());
			mail.setText(selectedUser.getUserMail() == null ? "" : selectedUser.getUserMail());
			phone.setText(selectedUser.getUserPhone() == null ? "" : selectedUser.getUserPhone());
			char selectedSexC = selectedUser.getUserSex();
			SexModel sexModel = new SexModel("男", 'M');// default
			if (selectedSexC == 'W')
			{
				sexModel = new SexModel("女", 'W');
			} else if (selectedSexC == 'O')
			{
				sexModel = new SexModel("其他", 'O');
			}
			StructuredSelection selectedSex = new StructuredSelection(new SexModel[]
			{ sexModel });
			sex.setSelection(selectedSex);
			List<Role> roleList = new RoleService().queryRolesByUserId(selectedUser.getUserId());
			if (roleList != null && !roleList.isEmpty())
			{
				role.setSelection(new StructuredSelection(roleList.get(0)));// TODO
			}
		}
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent)
	{
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize()
	{
		return new Point(513, 349);
	}

	@Override
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText("新增用户");
	}

	@Override
	protected void okPressed()
	{
		String userId = user_id.getText();
		if (StringUtils.isBlank(userId))
		{
			MessageBoxUtil.showWarnMessageBox(getShell(), "用户ID不能为空");
			return;
		}
		String userNameValue = userName.getText();
		if (StringUtils.isBlank(userNameValue))
		{
			MessageBoxUtil.showWarnMessageBox(getShell(), "用户名不能为空");
			return;
		}
		String mailValue = mail.getText();
		String phoneValue = phone.getText();
		char sexV = ((SexModel) ((StructuredSelection) sex.getSelection()).getFirstElement()).getCode();
		StructuredSelection ss = (StructuredSelection) role.getSelection();
		if (ss.isEmpty())
		{
			MessageBoxUtil.showWarnMessageBox(getShell(), "请为用户分配权限");
			return;
		}
		String roleId = ((Role) ((StructuredSelection) role.getSelection()).getFirstElement()).getRoleId();
		if (StringUtils.isBlank(roleId))
		{
			MessageBoxUtil.showWarnMessageBox(getShell(), "请为用户分配权限");
			return;
		}
		UserService userService = new UserService();
		if (selectedUser != null)
		{
			try
			{
				userService.updateUser(userId, userNameValue, sexV, phoneValue, mailValue, roleId);
				MessageBoxUtil.showWarnMessageBox(getShell(), "修改用户成功");
			} catch (SQLException e)
			{
				MessageBoxUtil.showWarnMessageBox(getShell(), "修改用户失败");
				return;
			}

		} else
		{
			if (userService.checkUserIsExisted(userId))
			{
				MessageBoxUtil.showWarnMessageBox(getShell(), "用户ID已经存在，请换一个用户ID");
				return;
			}
			try
			{
				userService.addUser(userId, userNameValue, sexV, phoneValue, mailValue, roleId);
				MessageBoxUtil.showWarnMessageBox(getShell(), "新增用户成功");
			} catch (SQLException e)
			{
				MessageBoxUtil.showWarnMessageBox(getShell(), "新增用户失败");
				return;
			}
			// RoleService roleService = new RoleService();
			// roleService.insertUserRole(userId, roleId);
		}
		super.okPressed();
	}
}
