package com.kindergarten.groups.feetemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.kindergarten.data.FeeTemplate;
import com.kindergarten.groups.AbstractGroup;
import com.kindergarten.service.FeeTemplateService;
import com.kindergarten.util.ButtonNameConstant;
import com.kindergarten.util.MessageBoxUtil;
import com.kindergarten.util.print.PrinterUtil;

public class FeeTemplateGroup extends AbstractGroup
{
	private Text feeTemplateName;
	private Text feeAmount;
	private Table feeTemplateTable;
	final CheckboxTableViewer checkboxTableViewer;

	public FeeTemplateGroup(Composite parent, int style, String userId)
	{
		super(parent, style, userId);
		this.setText("费用标准管理");

		Label label = new Label(composite, SWT.NONE);
		label.setBounds(10, 10, 61, 17);
		label.setText("标准名称");

		feeTemplateName = new Text(composite, SWT.BORDER);
		feeTemplateName.setBounds(77, 4, 144, 23);

		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setBounds(293, 10, 61, 17);
		label_1.setText("标准额度");

		feeAmount = new Text(composite, SWT.BORDER);
		feeAmount.setBounds(360, 4, 155, 23);

		Button btnQuery = new Button(composite, SWT.NONE);
		btnQuery.setBounds(576, 0, 80, 27);
		btnQuery.setText("查询");
		btnQuery.setData(ButtonNameConstant.BTN_QUERY);
		btnQuery.addMouseListener(new FeeTemplateOperMouseListener(getShell()));

		Label label_2 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_2.setBounds(10, 33, 678, 2);

		checkboxTableViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER | SWT.FULL_SELECTION);
		feeTemplateTable = checkboxTableViewer.getTable();
		feeTemplateTable.setLinesVisible(true);
		feeTemplateTable.setHeaderVisible(true);
		feeTemplateTable.setBounds(10, 41, 505, 376);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn id = tableViewerColumn.getColumn();
		id.setWidth(100);
		id.setText("序号");
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				FeeTemplate feeTemplate = (FeeTemplate) element;
				return String.valueOf(feeTemplate.getId());
			}
		});

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnTemplateName = tableViewerColumn_1.getColumn();
		tblclmnTemplateName.setWidth(150);
		tblclmnTemplateName.setText("标准名称");
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				FeeTemplate feeTemplate = (FeeTemplate) element;
				return feeTemplate.getFeeTemplateName();
			}
		});


		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnAmount = tableViewerColumn_2.getColumn();
		tblclmnAmount.setWidth(100);
		tblclmnAmount.setText("标准额度");
		tableViewerColumn_2.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				FeeTemplate feeTemplate = (FeeTemplate) element;
				return String.valueOf(feeTemplate.getFeeAmount());
			}
		});
		
		
		checkboxTableViewer.setContentProvider(new ArrayContentProvider()
		{
			@Override
			public Object[] getElements(Object inputElement)
			{
				@SuppressWarnings("unchecked")
				Map<String, String> paramMap = (Map<String, String>) inputElement;
				String name = paramMap.get("feeTemplateName");
				BigDecimal bigData = null;
				String amountStr = paramMap.get("feeAmount");
				if (!StringUtils.isBlank(amountStr))
				{
					bigData = new BigDecimal(amountStr);
				}
				FeeTemplateService service = new FeeTemplateService();
				return service.queryByCondition(name, bigData).toArray(new FeeTemplate[0]);
			}
		});
		// checkboxTableViewer.setLabelProvider(new MyITableLabelProvider());
		checkboxTableViewer.setInput(new HashMap<String, String>());

		Label label_3 = new Label(composite, SWT.SEPARATOR | SWT.VERTICAL);
		label_3.setBounds(544, 41, 2, 376);

		Button btnAdd = new Button(composite, SWT.NONE);
		btnAdd.setData(ButtonNameConstant.BTN_ADD);
		btnAdd.setBounds(576, 70, 80, 27);
		btnAdd.setText("新增标准");
		btnAdd.addMouseListener(new FeeTemplateOperMouseListener(getShell()));

		Button btnEdit = new Button(composite, SWT.NONE);
		btnEdit.setData(ButtonNameConstant.BTN_EDIT);
		btnEdit.setBounds(576, 140, 80, 27);
		btnEdit.setText("修改标准");
		btnEdit.addMouseListener(new FeeTemplateOperMouseListener(getShell()));

		Button btnDelete = new Button(composite, SWT.NONE);
		btnDelete.setData(ButtonNameConstant.BTN_DELETE);
		btnDelete.setBounds(576, 214, 80, 27);
		btnDelete.setText("删除标准");

		Button btnPrint = new Button(composite, SWT.NONE);
		btnPrint.setBounds(576, 309, 80, 27);
		btnPrint.setText("打印收费标准");
		btnPrint.setData(ButtonNameConstant.BTN_PRINT);
		btnPrint.addMouseListener(new FeeTemplateOperMouseListener(getShell()));

		btnDelete.addMouseListener(new FeeTemplateOperMouseListener(getShell()));

	}

	class FeeTemplateOperMouseListener implements MouseListener
	{
		private Shell shell;

		public FeeTemplateOperMouseListener(Shell shell)
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
			if (ButtonNameConstant.BTN_PRINT.equals(data))
			{
				PrinterUtil.printTable(checkboxTableViewer.getTable(), shell, "收费标准打印");
			} else if (ButtonNameConstant.BTN_QUERY.equals(data))
			{
				String name = feeTemplateName.getText();
				String amountStr = feeAmount.getText();
				Map<String, String> map = new HashMap<String, String>();
				map.put("feeTemplateName", name);
				map.put("feeAmount", amountStr);
				checkboxTableViewer.setInput(map);
			} else if (ButtonNameConstant.BTN_ADD.equals(data))
			{
				FeeTemplateAddDialog addDialog = new FeeTemplateAddDialog(shell);
				int result = addDialog.open();
				if (result == 0)
				{
					Map<String, Object> map = new HashMap<String, Object>();
					checkboxTableViewer.setInput(map);
				}
			} else if (ButtonNameConstant.BTN_EDIT.equals(data))
			{
				Object[] objs = checkboxTableViewer.getCheckedElements();
				if (objs == null || objs.length <= 0)
				{
					MessageBoxUtil.showWarnMessageBox(shell, "请选择一个费用模板进行编辑");
					return;
				}
				FeeTemplate feeTemplate = (FeeTemplate) objs[0];
				FeeTemplateAddDialog addDialog = new FeeTemplateAddDialog(shell, feeTemplate);
				int result = addDialog.open();
				if (result == 0)
				{
					Map<String, Object> map = new HashMap<String, Object>();
					checkboxTableViewer.setInput(map);
				}
			} else if (ButtonNameConstant.BTN_DELETE.equals(data))
			{

				Object[] objs = checkboxTableViewer.getCheckedElements();
				if (objs == null || objs.length <= 0)
				{
					MessageBoxUtil.showWarnMessageBox(shell, "请至少选择一个费用模板进行删除");
					return;
				}
				List<FeeTemplate> templateList = new ArrayList<FeeTemplate>();
				for (Object tem : objs)
				{
					FeeTemplate template = (FeeTemplate) tem;
					templateList.add(template);
				}
				int confirm = MessageBoxUtil.showConfirmMessageBox(shell, "确定要进行删除操作么？");
				if (confirm == SWT.OK)
				{
					new FeeTemplateService().deleteFeeTemplates(templateList);
					Map<String, Object> map = new HashMap<String, Object>();
					checkboxTableViewer.setInput(map);
				}
			}
		}
	}
}
