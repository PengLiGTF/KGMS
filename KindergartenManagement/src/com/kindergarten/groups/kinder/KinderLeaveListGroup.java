package com.kindergarten.groups.kinder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.kindergarten.data.KinderLeaveInfo;
import com.kindergarten.groups.AbstractGroup;
import com.kindergarten.service.KinderLeaveService;
import com.kindergarten.util.CommonUtil;
import com.kindergarten.util.print.PrinterUtil;

public class KinderLeaveListGroup extends AbstractGroup
{
	private Text kinderIdText;
	private Text kinderNameText;
	private Table table;

	private final CheckboxTableViewer checkboxTableViewer;

	private String kinderId;

	public KinderLeaveListGroup(final Composite parent, int style, final String userId, String kinderId)
	{
		this(parent, style, userId);
		this.kinderId = kinderId;

		Map<String, String> param = new HashMap<String, String>();
		param.put("kinderId", kinderId);
		List<KinderLeaveInfo> leaveInfoList = new KinderLeaveService().queryLeaveInfoByCondition(param);
		checkboxTableViewer.setInput(leaveInfoList.toArray(new KinderLeaveInfo[0]));
	}

	public KinderLeaveListGroup(final Composite parent, int style, final String userId)
	{
		super(parent, style, userId);
		setText("学生请假历史");

		Label label = new Label(composite, SWT.NONE);
		label.setBounds(10, 20, 38, 17);
		label.setText("学号");

		kinderIdText = new Text(composite, SWT.BORDER);
		kinderIdText.setBounds(54, 14, 162, 23);

		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setBounds(298, 20, 38, 17);
		label_1.setText("姓名");

		kinderNameText = new Text(composite, SWT.BORDER);
		kinderNameText.setBounds(342, 14, 183, 23);

		Label label_2 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_2.setBounds(10, 52, 662, 2);

		checkboxTableViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table = checkboxTableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(10, 60, 662, 306);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn = tableViewerColumn.getColumn();
		tblclmnNewColumn.setWidth(140);
		tblclmnNewColumn.setText("学号");
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider()
		{

			@Override
			public String getText(Object element)
			{
				KinderLeaveInfo leaveInfo = (KinderLeaveInfo) element;
				return leaveInfo.getKinderId();
			}
		});

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumn = tableViewerColumn_1.getColumn();
		tableColumn.setWidth(100);
		tableColumn.setText("姓名");
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider()
		{

			@Override
			public String getText(Object element)
			{
				KinderLeaveInfo leaveInfo = (KinderLeaveInfo) element;
				return leaveInfo.getKinderName();
			}
		});

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumn_1 = tableViewerColumn_2.getColumn();
		tableColumn_1.setWidth(100);
		tableColumn_1.setText("请假天数");
		tableViewerColumn_2.setLabelProvider(new ColumnLabelProvider()
		{

			@Override
			public String getText(Object element)
			{
				KinderLeaveInfo leaveInfo = (KinderLeaveInfo) element;
				return String.valueOf(leaveInfo.getLeaveDays());
			}
		});

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumn_2 = tableViewerColumn_3.getColumn();
		tableColumn_2.setWidth(100);
		tableColumn_2.setText("请假开始日期");
		tableViewerColumn_3.setLabelProvider(new ColumnLabelProvider()
		{

			@Override
			public String getText(Object element)
			{
				KinderLeaveInfo leaveInfo = (KinderLeaveInfo) element;
				return CommonUtil.formatDateToString(leaveInfo.getLeaveStartTime(), "yyyy-MM-dd");
			}
		});

		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumn_3 = tableViewerColumn_4.getColumn();
		tableColumn_3.setWidth(100);
		tableColumn_3.setText("请假结束日期");
		tableViewerColumn_4.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				KinderLeaveInfo leaveInfo = (KinderLeaveInfo) element;
				return CommonUtil.formatDateToString(leaveInfo.getLeaveEndTime(), "yyyy-MM-dd");
			}
		});

		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumn_5 = tableViewerColumn_5.getColumn();
		tableColumn_5.setWidth(100);
		tableColumn_5.setText("请假原因");
		tableViewerColumn_5.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				KinderLeaveInfo leaveInfo = (KinderLeaveInfo) element;
				return leaveInfo.getLeaveDesc();
			}
		});

		checkboxTableViewer.setContentProvider(new ArrayContentProvider()
		{
			@Override
			public Object[] getElements(Object inputElement)
			{
				return (KinderLeaveInfo[]) inputElement;
			}
		});

		Button btnQuery = new Button(composite, SWT.NONE);
		btnQuery.setBounds(546, 10, 80, 27);
		btnQuery.setText("查询");

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
				Map<String, String> param = new HashMap<String, String>();
				param.put("kinderId", kinderId);
				param.put("kinderName", kinderName);
				List<KinderLeaveInfo> leaveInfoList = new KinderLeaveService().queryLeaveInfoByCondition(param);
				checkboxTableViewer.setInput(leaveInfoList.toArray(new KinderLeaveInfo[0]));
			}
		});

		Button btnClose = new Button(composite, SWT.NONE);
		btnClose.setBounds(689, 153, 101, 27);
		btnClose.setText("关闭");
		btnClose.addMouseListener(new MouseListener()
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
				Control[] ctrls = parent.getChildren();
				if (ctrls.length > 0)
				{
					ctrls[0].dispose();
				}
				new KinderLeaveGroup(parent, SWT.NONE, userId);
			}
		});

		Button btnPrinter = new Button(composite, SWT.NONE);
		btnPrinter.setBounds(689, 75, 101, 27);
		btnPrinter.setText("打印");
		btnPrinter.addMouseListener(new MouseListener()
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
				PrinterUtil.printTable(table, getShell(), "学生请假信息打印");
			}
		});
	}
}
