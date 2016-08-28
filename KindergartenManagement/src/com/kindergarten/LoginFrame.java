package com.kindergarten;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.kindergarten.data.User;
import com.kindergarten.groups.user.ChangePwdDialog;
import com.kindergarten.service.UserService;
import com.kindergarten.util.MessageBoxUtil;
import com.kindergarten.util.SWTResourceManager;
import com.kindergarten.util.TipBox;
import com.kindergarten.util.ValidateData;

public class LoginFrame
{
	private Text username;
	private Text password;

	public void openLoginFrame()
	{
		Display display = Display.getDefault();
		final Shell shell = new Shell(SWT.MIN | SWT.CLOSE);
		shell.setBackground(SWTResourceManager.getColor(255, 240, 245));
		shell.setToolTipText("user login");
		shell.setSize(450, 300);
		shell.setLocation(400, 200);
		shell.setText("用户登录");
		shell.setImage(SWTResourceManager.getImage(LoginFrame.class, "/images/logo.png"));

		shell.setBackgroundImage(SWTResourceManager.getImage(LoginFrame.class, "/images/loginBG.jpg"));

		Label lblUsername = new Label(shell, SWT.SHADOW_NONE | SWT.CENTER);
		lblUsername.setBounds(71, 55, 86, 20);
		lblUsername.setText("用户ID");

		Label lblPassword = new Label(shell, SWT.CENTER);
		lblPassword.setBounds(71, 111, 86, 20);
		lblPassword.setText("密码");

		username = new Text(shell, SWT.BORDER);
		username.setBounds(162, 55, 127, 20);

		password = new Text(shell, SWT.BORDER | SWT.PASSWORD);
		password.setBounds(162, 111, 127, 20);

		Button btnLogin = new Button(shell, SWT.NONE);
		btnLogin.addMouseListener(new MouseAdapter()
		{
			public void mouseUp(MouseEvent e)
			{
				ValidateData vd = new ValidateData();
				String userId = username.getText();
				String passWord = password.getText();
				if (vd.validLogin(shell, userId, passWord))
				{
					UserService adminService = new UserService();
					User user = adminService.userIsValid(userId, passWord);
					if (user != null)
					{
						if (user.getIsFirst() == 'T')
						{
							int result = new ChangePwdDialog(shell, userId).open();
							while (result != 0)
							{
								MessageBoxUtil.showWarnMessageBox(shell, "请修改密码");
								result = new ChangePwdDialog(shell, userId).open();
							}
						}
						shell.close();
						new MainFrame(userId).open();
					} else
					{
						TipBox.Message(shell, "error", "用户名或密码错误");
					}
				}
			}
		});
		btnLogin.setBounds(123, 171, 69, 24);
		btnLogin.setText("登录");

		Button button_1 = new Button(shell, SWT.NONE);
		button_1.addMouseListener(new MouseAdapter()
		{
			public void mouseUp(MouseEvent e)
			{
				username.setText("");
				password.setText("");
			}
		});
		button_1.setBounds(220, 171, 69, 24);
		button_1.setText("取消");

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

	public static void main(String[] args)
	{
		new LoginFrame().openLoginFrame();
	}
}
