package com.kindergarten.groups.feetemplate;

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

import com.kindergarten.data.FeeTemplate;
import com.kindergarten.service.FeeTemplateService;
import com.kindergarten.util.MessageBoxUtil;

public class FeeTemplateAddDialog extends Dialog
{
	private Text templateName;
	private Text feeAmount;

	private FeeTemplate selectedFeeTemplate;

	@Override
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText("新增收费标准");
	}

	public FeeTemplateAddDialog(Shell parentShell, FeeTemplate selected)
	{
		this(parentShell);
		this.selectedFeeTemplate = selected;
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @wbp.parser.constructor
	 */
	public FeeTemplateAddDialog(Shell parentShell)
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
		label.setBounds(10, 30, 61, 17);
		label.setText("标准名称：");

		templateName = new Text(container, SWT.BORDER);
		templateName.setBounds(77, 24, 239, 23);

		Label label_1 = new Label(container, SWT.NONE);
		label_1.setBounds(10, 83, 61, 17);
		label_1.setText("标准额度：");

		feeAmount = new Text(container, SWT.BORDER);
		feeAmount.setBounds(77, 77, 207, 23);
		
		Label label_2 = new Label(container, SWT.NONE);
		label_2.setBounds(290, 80, 26, 17);
		label_2.setText("/月");

		if (selectedFeeTemplate != null)
		{
			templateName.setText(selectedFeeTemplate.getFeeTemplateName());
			feeAmount.setText(String.valueOf(selectedFeeTemplate.getFeeAmount()));
		}
		return container;
	}

	@Override
	protected void okPressed()
	{
		String name = templateName.getText();
		if (StringUtils.isBlank(name))
		{
			MessageBoxUtil.showWarnMessageBox(getShell(), "名称不能为空");
			return;
		}
		String amountStr = feeAmount.getText();
		if (StringUtils.isBlank(name))
		{
			MessageBoxUtil.showWarnMessageBox(getShell(), "额度不能为空");
			return;
		}
		double amount = 0;
		try
		{
			amount = Double.parseDouble(amountStr);
		} catch (NumberFormatException e)
		{
			MessageBoxUtil.showWarnMessageBox(getShell(), "额度只能是数字");
			return;
		}
		if (this.selectedFeeTemplate != null)
		{
			new FeeTemplateService().updateFeeTemplate(selectedFeeTemplate.getId(), name, amount);

		} else
		{
			new FeeTemplateService().addFeeTemplate(name, amount);
		}
		super.okPressed();
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
		return new Point(450, 300);
	}
}
