package com.kindergarten.groups.kinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.util.Calendar;
import com.kindergarten.data.KinderFeeInfo;
import com.kindergarten.groups.AbstractGroup;
import com.kindergarten.service.KinderService;
import com.kindergarten.util.CommonUtil;
import com.kindergarten.util.print.PrinterUtil;

public class PreFeeKinderListGroup extends AbstractGroup
{
	private Table table;
	private Text kinderIdText;
	private Text kinderNameText;
	private DateTime feeDateTime;

	private Button btnPrint;

	private List<KinderFeeInfo> kinderFeeInfoList = new ArrayList<KinderFeeInfo>();

	public PreFeeKinderListGroup(final Composite parent, int style, final String userId)
	{
		super(parent, style, userId);
		composite.setBounds(10, 20, 859, 600);
		final Map<Object, Button> buttons = new HashMap<Object, Button>();
		this.setText("预缴费学生管理");
		final CheckboxTableViewer checkboxTableViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table = checkboxTableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setBounds(10, 57, 673, 335);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumn = tableViewerColumn.getColumn();
		tableColumn.setWidth(82);
		tableColumn.setText("学号");
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				KinderFeeInfo feeInfo = (KinderFeeInfo) element;
				return feeInfo.getKinderId();
			}
		});

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumn_1 = tableViewerColumn_1.getColumn();
		tableColumn_1.setWidth(77);
		tableColumn_1.setText("性别");
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				KinderFeeInfo feeInfo = (KinderFeeInfo) element;
				String sexName = CommonUtil.getSexNameByCode(feeInfo.getSex());
				return sexName;
			}
		});

		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumn_4 = tableViewerColumn_4.getColumn();
		tableColumn_4.setWidth(100);
		tableColumn_4.setText("预缴费金额");
		tableViewerColumn_4.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				KinderFeeInfo feeInfo = (KinderFeeInfo) element;
				return String.valueOf(feeInfo.getPreFeeMoney());
			}
		});

		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumn_5 = tableViewerColumn_5.getColumn();
		tableColumn_5.setWidth(100);
		tableColumn_5.setText("缴费日期");
		tableViewerColumn_5.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				KinderFeeInfo feeInfo = (KinderFeeInfo) element;
				return CommonUtil.formatDateToString(feeInfo.getFeeTime(), CommonUtil.TIME_FORMAT_PATTERN);
			}
		});

		TableViewerColumn tableViewerColumn_6 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumn_6 = tableViewerColumn_6.getColumn();
		tableColumn_6.setWidth(100);
		tableColumn_6.setText("操作");
		tableViewerColumn_6.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public void update(ViewerCell cell)
			{
				TableItem item = (TableItem) cell.getItem();
				Button button;
				if (buttons.containsKey(cell.getElement()))
				{
					button = buttons.get(cell.getElement());
				} else
				{
					button = new Button((Composite) cell.getViewerRow().getControl(), SWT.NONE);
					button.setText("办理入园缴费");
					buttons.put(cell.getElement(), button);
				}
				final KinderFeeInfo tempObj = (KinderFeeInfo) item.getData();
				button.setData(tempObj);
				button.addMouseListener(new MouseListener()
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
						Control[] controls = parent.getChildren();
						if (controls != null && controls.length > 0)
						{
							controls[0].dispose();
						}
						new KinderRecordGroup(parent, SWT.NONE, userId, tempObj);
					}
				});
				TableEditor editor = new TableEditor(item.getParent());
				editor.grabHorizontal = true;
				editor.grabVertical = true;
				editor.setEditor(button, item, cell.getColumnIndex());
				editor.layout();
			}
		});

		KinderService kinderService = new KinderService();
		Map<String, String> param = new HashMap<String, String>();
		kinderFeeInfoList = kinderService.queryPreFeeKinderListByCondition(param);
		checkboxTableViewer.setContentProvider(new ArrayContentProvider());
		checkboxTableViewer.setInput(kinderFeeInfoList.toArray(new KinderFeeInfo[0]));

		Label label = new Label(composite, SWT.NONE);
		label.setBounds(10, 20, 36, 17);
		label.setText("学号");

		kinderIdText = new Text(composite, SWT.BORDER);
		kinderIdText.setBounds(49, 14, 115, 23);

		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setBounds(209, 20, 36, 17);
		label_1.setText("姓名");

		kinderNameText = new Text(composite, SWT.BORDER);
		kinderNameText.setBounds(247, 14, 106, 23);

		Label label_2 = new Label(composite, SWT.NONE);
		label_2.setBounds(388, 20, 61, 17);
		label_2.setText("缴费日期");

		feeDateTime = new DateTime(composite, SWT.BORDER);
		feeDateTime.setBounds(477, 13, 88, 24);

		Button btnQuery = new Button(composite, SWT.NONE);
		btnQuery.setBounds(605, 14, 80, 27);
		btnQuery.setText("查询");

		Label label_3 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_3.setBounds(10, 49, 673, 2);

		Label label_4 = new Label(composite, SWT.SEPARATOR | SWT.VERTICAL);
		label_4.setBounds(689, 52, 2, 340);

		Button btnAdd = new Button(composite, SWT.NONE);
		btnAdd.setBounds(696, 57, 80, 27);
		btnAdd.setText("学生预缴费");

		btnPrint = new Button(composite, SWT.NONE);
		btnPrint.setBounds(696, 117, 80, 27);
		btnPrint.setText("打印");
		btnPrint.addMouseListener(new MouseListener()
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

				Table table = checkboxTableViewer.getTable();
				TableColumn[] tc = table.getColumns();
				TableColumn disposedColumn = null;
				for (TableColumn tableColumn : tc)
				{
					if (tableColumn.getText().equals("操作"))
					{
						disposedColumn = tableColumn;
						break;
					}
				}
				disposedColumn.dispose();
				PrinterUtil.printTable(table, getShell(), "学生预交费信息打印");
				parent.getChildren()[0].dispose();
				new PreFeeKinderListGroup(parent, SWT.NONE, userId);
			}
		});

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
				Control[] controls = parent.getChildren();
				if (controls != null && controls.length > 0)
				{
					controls[0].dispose();
				}
				new PreFeeKindersRecordGroup(parent, SWT.NONE, userId);
			}
		});

		btnQuery.addMouseListener(new MouseListener()
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
				String kinderId = kinderIdText.getText();
				String kinderName = kinderNameText.getText();
				Calendar cal = Calendar.getInstance();
				cal.set(feeDateTime.getYear(), feeDateTime.getMonth(), feeDateTime.getDay());
				String feeDateStr = CommonUtil.formatDateToString(cal.getTime(), CommonUtil.TIME_FORMAT_PATTERN);
				Map<String, String> param = new HashMap<String, String>();
				param.put("kinderId", kinderId);
				param.put("kinderName", kinderName);
				param.put("feeDate", feeDateStr);
				List<KinderFeeInfo> kinderList = new KinderService().queryPreFeeKinderListByCondition(param);

				if (kinderList != null)
				{
					Collection<Button> btnList = buttons.values();
					if (!btnList.isEmpty())
					{
						Iterator<Button> it = btnList.iterator();
						while (it.hasNext())
						{
							Button bt = it.next();
							bt.dispose();
						}
					}
					checkboxTableViewer.setInput(kinderList.toArray(new KinderFeeInfo[0]));
				}
			}
		});
	}
}
