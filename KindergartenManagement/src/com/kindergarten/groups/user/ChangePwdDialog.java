package com.kindergarten.groups.user;

import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.kindergarten.service.UserService;
import com.kindergarten.util.MessageBoxUtil;

public class ChangePwdDialog extends Dialog
{
	@Override
	protected void okPressed()
	{
		String newPwd = pwd.getText();
		String newPwdConfirm = pwdConfirm.getText();
		if (StringUtils.isBlank(newPwdConfirm) || StringUtils.isBlank(newPwd))
		{
			MessageBoxUtil.showWarnMessageBox(getShell(), "密码不能为空");
			pwd.setText("");
			pwdConfirm.setText("");
			return;
		} else if (!newPwdConfirm.equals(newPwd))
		{
			MessageBoxUtil.showWarnMessageBox(getShell(), "输入密码前后不一致");
			pwd.setText("");
			pwdConfirm.setText("");
			return;
		}
		UserService user = new UserService();
		try
		{
			int affectedRow = user.updatePwd(userId, newPwd);
			if (affectedRow > 0)
			{
				MessageBoxUtil.showWarnMessageBox(getShell(), "修改密码成功");
			} else
			{
				MessageBoxUtil.showWarnMessageBox(getShell(), "密码修改失败");
				return;
			}
		} catch (SQLException e)
		{
			e.printStackTrace();
			MessageBoxUtil.showWarnMessageBox(getShell(), "修改密码失败");
			return;
		}
		super.okPressed();
	}

	private Text pwd;
	private Text pwdConfirm;

	private String userId;

	public ChangePwdDialog(Shell parentShell, String userId)
	{
		this(parentShell);
		this.userId = userId;
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public ChangePwdDialog(Shell parentShell)
	{
		super(parentShell);
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
		container.setLayout(null);

		Label label = new Label(container, SWT.NONE);
		label.setBounds(52, 57, 61, 17);
		label.setText("输入新密码");

		Label label_1 = new Label(container, SWT.NONE);
		label_1.setBounds(52, 114, 61, 17);
		label_1.setText("确认新密码");

		pwd = new Text(container, SWT.BORDER | SWT.PASSWORD);
		pwd.setBounds(151, 51, 194, 23);

		pwdConfirm = new Text(container, SWT.BORDER | SWT.PASSWORD);
		pwdConfirm.setBounds(151, 114, 194, 23);

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

	@Override
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText("首次密码修改");
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize()
	{
		return new Point(450, 300);
	}
	
	
}
