package com.kindergarten.groups.kinder;

import java.util.List;

import org.eclipse.jface.viewers.CheckboxTableViewer;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.kindergarten.data.KinderFeeInfo;
import com.kindergarten.groups.AbstractGroup;
import com.kindergarten.service.KinderService;
import com.kindergarten.util.ButtonNameConstant;
import com.kindergarten.util.CommonUtil;
import com.kindergarten.util.print.PrinterUtil;

public class KinderFeeHistoryGroup extends AbstractGroup
{
	private Table table;
	private String kinderId;

	/**
	 * @wbp.parser.constructor
	 * 
	 */
	public KinderFeeHistoryGroup(final Composite parent, int style, final String userId, String kinderId)
	{
		this(parent, style, userId);
		composite.setBounds(10, 20, 935, 600);
		setText("历史缴费列表");
		this.kinderId = kinderId;
		final CheckboxTableViewer checkboxTableViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table = checkboxTableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(10, 46, 883, 327);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnId = tableViewerColumn.getColumn();
		tblclmnId.setWidth(100);
		tblclmnId.setText("学号");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnName = tableViewerColumn_1.getColumn();
		tblclmnName.setWidth(100);
		tblclmnName.setText("姓名");

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumnDays = tableViewerColumn_2.getColumn();
		tableColumnDays.setWidth(100);
		tableColumnDays.setText("缴费天数");

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumnFeeStart = tableViewerColumn_3.getColumn();
		tableColumnFeeStart.setWidth(100);
		tableColumnFeeStart.setText("缴费开始日期");

		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumnFeeExpire = tableViewerColumn_4.getColumn();
		tableColumnFeeExpire.setWidth(100);
		tableColumnFeeExpire.setText("费用到期日");

		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumnFeeChangeReason = tableViewerColumn_5.getColumn();
		tableColumnFeeChangeReason.setWidth(100);
		tableColumnFeeChangeReason.setText("缴费变更原因");

		Button btnClose = new Button(composite, SWT.NONE);
		btnClose.setBounds(557, 403, 80, 27);
		btnClose.setText("关闭");

		Button btnPrint = new Button(composite, SWT.NONE);
		btnPrint.setBounds(10, 403, 92, 27);
		btnPrint.setData(ButtonNameConstant.BTN_PRINT);
		btnPrint.setText("打印缴费历史");
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
				PrinterUtil.printTable(checkboxTableViewer.getTable(), getShell(), "缴费历史列表");

			}
		});

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
				parent.getChildren()[0].dispose();
				new KinderInfoListGroup(parent, SWT.NONE, userId);
			}
		});

		checkboxTableViewer.setContentProvider(new MyTableStructedProvider());
		checkboxTableViewer.setLabelProvider(new MyITableLabelProvider());
		KinderService feeService = new KinderService();
		List<KinderFeeInfo> feeHisList = feeService.queryFeeHistory(kinderId);
		checkboxTableViewer.setInput(feeHisList.toArray(new KinderFeeInfo[0]));
	}

	public KinderFeeHistoryGroup(Composite parent, int none, String userId)
	{
		super(parent, none, userId);
	}

	static class MyITableLabelProvider implements ITableLabelProvider
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
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener)
		{
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex)
		{
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex)
		{
			if (element != null)
			{
				KinderFeeInfo cm = (KinderFeeInfo) element;
				switch (columnIndex)
				{
				case 0:
					return String.valueOf(cm.getKinderId());
				case 1:
					return cm.getKinderName();
				case 2:
					return String.valueOf(cm.getFeeDays());
				case 3:
					return CommonUtil.formatDateToString(cm.getFeeTime(), CommonUtil.TIME_FORMAT_PATTERN);
				case 4:
					return CommonUtil.formatDateToString(cm.getFeeExpireTime(), CommonUtil.TIME_FORMAT_PATTERN);
				case 5:
					return cm.getFeeEvent();
				}
			}
			return null;
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
			KinderFeeInfo[] arr = (KinderFeeInfo[]) inputElement;
			return arr;
		}
	}
}
