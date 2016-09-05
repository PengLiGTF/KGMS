package com.kindergarten.groups.kinder;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.util.Calendar;
import com.kindergarten.data.KinderLeaveInfo;
import com.kindergarten.data.User;
import com.kindergarten.groups.AbstractGroup;
import com.kindergarten.groups.IndexGroup;
import com.kindergarten.service.KinderLeaveService;
import com.kindergarten.service.KinderService;
import com.kindergarten.service.UserService;
import com.kindergarten.util.ButtonNameConstant;
import com.kindergarten.util.CommonUtil;
import com.kindergarten.util.MessageBoxUtil;

public class KinderLeaveGroup extends AbstractGroup
{
	private Text kinderNameText;
	private Text leaveDayText;
	private Text operText;
	private Text kinderIdText;
	private StyledText leaveDescText;
	private DateTime leaveStartTime;
	private DateTime leaveEndTime;
	private DateTime operTime;

	public KinderLeaveGroup(final Composite parent, int style, final String userId)
	{
		super(parent, style, userId);
		this.setText("请假条录入");

		Label label = new Label(composite, SWT.NONE);
		label.setBounds(340, 41, 61, 17);
		label.setText("学生姓名");

		kinderNameText = new Text(composite, SWT.BORDER);
		kinderNameText.setBounds(407, 35, 174, 23);

		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setBounds(10, 88, 61, 17);
		label_1.setText("请假天数");

		leaveDayText = new Text(composite, SWT.BORDER);
		leaveDayText.setBounds(77, 82, 504, 23);

		leaveDayText.addModifyListener(new ModifyListener()
		{
			@Override
			public void modifyText(ModifyEvent e)
			{
				setFeeExpireTimeAccordingStartTime(leaveEndTime, leaveStartTime);
			}
		});

		Label label_2 = new Label(composite, SWT.NONE);
		label_2.setBounds(10, 164, 61, 17);
		label_2.setText("请假说明");

		leaveDescText = new StyledText(composite, SWT.BORDER);
		leaveDescText.setBounds(10, 194, 571, 158);

		Label label_3 = new Label(composite, SWT.NONE);
		label_3.setBounds(10, 375, 61, 19);
		label_3.setText("操作人");

		operText = new Text(composite, SWT.BORDER);
		List<User> userList = new UserService().queryUserByCondition(userId, "");
		operText.setBounds(77, 369, 155, 25);
		operText.setEditable(false);
		operText.setText(userList.get(0).getUserName());

		Label label_4 = new Label(composite, SWT.NONE);
		label_4.setBounds(285, 375, 61, 19);
		label_4.setText("操作时间");

		operTime = new DateTime(composite, SWT.BORDER);
		operTime.setBounds(372, 369, 209, 25);

		Button btnSave = new Button(composite, SWT.NONE);
		btnSave.setBounds(372, 400, 80, 27);
		btnSave.setText("保存");
		btnSave.setData(ButtonNameConstant.BTN_SAVE);
		btnSave.addMouseListener(new MouseListener()
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
				String stuId = kinderIdText.getText();
				if (StringUtils.isBlank(stuId))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "请输入请假学生学号");
					return;
				}
				String stuName = kinderNameText.getText();
				if (StringUtils.isBlank(stuName))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "请输入请假学生姓名");
					return;
				}
				String stuLeaveDays = leaveDayText.getText();
				if (StringUtils.isBlank(stuLeaveDays))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "请输入请假天数");
					return;
				} else if (!CommonUtil.isDigital(stuLeaveDays))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "请假天数只能包含数字");
					return;
				}
				int stuLeaveDay = Integer.parseInt(stuLeaveDays);

				String stuLeaveDesc = leaveDescText.getText();
				if (StringUtils.isBlank(stuLeaveDesc))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "请假理由不能为空");
					return;
				}
				KinderLeaveInfo leaveInfo = new KinderLeaveInfo();
				leaveInfo.setKinderId(stuId);
				leaveInfo.setKinderName(stuName);
				leaveInfo.setLeaveDays(stuLeaveDay);
				leaveInfo.setLeaveDesc(stuLeaveDesc);
				Calendar cal = Calendar.getInstance();
				cal.set(leaveStartTime.getYear(), leaveStartTime.getMonth(), leaveStartTime.getDay());
				leaveInfo.setLeaveStartTime(cal.getTime());
				cal.set(leaveEndTime.getYear(), leaveEndTime.getMonth(), leaveEndTime.getDay());
				leaveInfo.setLeaveEndTime(cal.getTime());
				leaveInfo.setOperId(userId);
				cal.set(operTime.getYear(), operTime.getMonth(), operTime.getDay());
				leaveInfo.setOperTime(cal.getTime());
				try
				{
					KinderService kinderService = new KinderService();
					if (!kinderService.isKinderExist(stuId))
					{
						MessageBoxUtil.showWarnMessageBox(getShell(), "输入学生学号不存在");
						return;
					}
					new KinderLeaveService().addLeave(leaveInfo);
					MessageBoxUtil.showWarnMessageBox(getShell(), "请假条录入成功");
					parent.getChildren()[0].dispose();
					new KinderFeeHistoryGroup(parent, SWT.None, userId, stuId);
				} catch (SQLException e1)
				{
					e1.printStackTrace();
					MessageBoxUtil.showWarnMessageBox(getShell(), "请假条录入失败");
					return;
				}
			}
		});

		Button button_1 = new Button(composite, SWT.NONE);
		button_1.setBounds(502, 400, 80, 27);
		button_1.setText("关闭");
		button_1.addMouseListener(new MouseListener()
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
				Control[] childs = parent.getChildren();
				if (childs.length > 0)
				{
					childs[0].dispose();
				}
				new IndexGroup(parent, SWT.NONE, userId);
			}
		});

		Label label_5 = new Label(composite, SWT.NONE);
		label_5.setBounds(10, 129, 72, 17);
		label_5.setText("请假开始时间");

		leaveStartTime = new DateTime(composite, SWT.BORDER);
		leaveStartTime.setBounds(98, 122, 153, 24);
		leaveStartTime.addSelectionListener(new SelectionListener()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				setFeeExpireTimeAccordingStartTime(leaveEndTime, leaveStartTime);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{
			}
		});

		Label label_6 = new Label(composite, SWT.NONE);
		label_6.setBounds(285, 129, 72, 17);
		label_6.setText("请假结束时间");

		leaveEndTime = new DateTime(composite, SWT.BORDER);
		leaveEndTime.setBounds(372, 122, 209, 24);

		Label label_7 = new Label(composite, SWT.NONE);
		label_7.setBounds(10, 41, 61, 17);
		label_7.setText("学号");

		kinderIdText = new Text(composite, SWT.BORDER);
		kinderIdText.setBounds(77, 35, 174, 23);
	}

	private void setFeeExpireTimeAccordingStartTime(final DateTime feeExpireTime, final DateTime feeStartTime)
	{
		String value = leaveDayText.getText();
		if (StringUtils.isBlank(value))
		{
			MessageBoxUtil.showWarnMessageBox(getShell(), "请假天数不能为空");
			return;
		} else if (!CommonUtil.isDigital(value))
		{
			MessageBoxUtil.showWarnMessageBox(getShell(), "请假天数只能输入数字且不能以0开头");
			return;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.set(feeStartTime.getYear(), feeStartTime.getMonth(), feeStartTime.getDay());
		calendar.add(Calendar.DATE, Integer.parseInt(value) - 1);
		feeExpireTime.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
	}
}
