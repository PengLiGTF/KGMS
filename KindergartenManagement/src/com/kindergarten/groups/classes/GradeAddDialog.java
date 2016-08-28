package com.kindergarten.groups.classes;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.kindergarten.data.Grade;
import com.kindergarten.service.ClassManageService;
import com.kindergarten.util.MessageBoxUtil;

public class GradeAddDialog extends Dialog
{
	private Grade grade;

	public Grade getGrade()
	{
		return grade;
	}

	public void setGrade(Grade grade)
	{
		this.grade = grade;
	}

	@Override
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText("新增年级");
	}

	private Text gradeName;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public GradeAddDialog(Shell parentShell)
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
		label.setBounds(35, 45, 61, 17);
		label.setText("年级名称");

		gradeName = new Text(container, SWT.BORDER);
		gradeName.setBounds(102, 42, 230, 23);

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
		return new Point(450, 218);
	}

	@Override
	protected void okPressed()
	{
		String gradeNameText = gradeName.getText();
		if (gradeNameText == null || "".equals(gradeNameText))
		{
			MessageBoxUtil.showWarnMessageBox(getShell(), "年级名称不能为空");
			return;
		}
		ClassManageService cmService = new ClassManageService();
		List<Grade> list = cmService.queryGradeByName(gradeNameText);
		if (!list.isEmpty())
		{
			MessageBoxUtil.showWarnMessageBox(getShell(), "年级名称已经存在");
			return;
		}
		this.grade = cmService.addGrade(gradeNameText);
		super.okPressed();
	}
}
