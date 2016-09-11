package com.kindergarten.groups.kinder;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
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
		composite.setBounds(10, 20, 1000, 600);
		setText("历史缴费列表");
		this.kinderId = kinderId;
		final CheckboxTableViewer checkboxTableViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table = checkboxTableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(10, 46, 883, 327);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnId = tableViewerColumn.getColumn();
		tblclmnId.setWidth(90);
		tblclmnId.setText("学号");
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				KinderFeeInfo obj = (KinderFeeInfo) element;
				return obj.getKinderId();
			}
		});

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnName = tableViewerColumn_1.getColumn();
		tblclmnName.setWidth(60);
		tblclmnName.setText("姓名");
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				KinderFeeInfo obj = (KinderFeeInfo) element;
				return obj.getKinderName();
			}
		});

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumnDays = tableViewerColumn_2.getColumn();
		tableColumnDays.setWidth(60);
		tableColumnDays.setText("缴费天数");
		tableViewerColumn_2.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				KinderFeeInfo obj = (KinderFeeInfo) element;
				return String.valueOf(obj.getFeeDays());
			}
		});

		TableViewerColumn tableViewerColumn_actualFee = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumnActualFee = tableViewerColumn_actualFee.getColumn();
		tableColumnActualFee.setWidth(60);
		tableColumnActualFee.setText("实缴费用");
		tableViewerColumn_actualFee.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				KinderFeeInfo obj = (KinderFeeInfo) element;
				return String.valueOf(obj.getActualMoney());
			}
		});

		TableViewerColumn tableViewerColumn_PreFee = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumnPreFee = tableViewerColumn_PreFee.getColumn();
		tableColumnPreFee.setWidth(60);
		tableColumnPreFee.setText("预缴费用");
		tableViewerColumn_PreFee.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				KinderFeeInfo obj = (KinderFeeInfo) element;
				return String.valueOf(obj.getPreFeeMoney());
			}
		});

		TableViewerColumn tableViewerColumn_deductionFee = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumnDeductionFee = tableViewerColumn_deductionFee.getColumn();
		tableColumnDeductionFee.setWidth(60);
		tableColumnDeductionFee.setText("抵扣预缴费用");
		tableViewerColumn_deductionFee.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				KinderFeeInfo obj = (KinderFeeInfo) element;
				return String.valueOf(obj.getDeductionPreFee());
			}
		});

		// 优惠额
		TableViewerColumn tableViewerColumn_priviFee = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumnPriviFee = tableViewerColumn_priviFee.getColumn();
		tableColumnPriviFee.setWidth(60);
		tableColumnPriviFee.setText("优惠金额");
		tableViewerColumn_priviFee.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				KinderFeeInfo obj = (KinderFeeInfo) element;
				return String.valueOf(obj.getPrivilegeMoney());
			}
		});
		// 其他费用
		TableViewerColumn tableViewerColumn_otherFee = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumnOtherFee = tableViewerColumn_otherFee.getColumn();
		tableColumnOtherFee.setWidth(60);
		tableColumnOtherFee.setText("其他费用");
		tableViewerColumn_otherFee.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				KinderFeeInfo obj = (KinderFeeInfo) element;
				return String.valueOf(obj.getOtherMoney());
			}
		});
		// 状态
		TableViewerColumn tableViewerColumn_feeStatus = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumnFeeStatus = tableViewerColumn_feeStatus.getColumn();
		tableColumnFeeStatus.setWidth(60);
		tableColumnFeeStatus.setText("状态");
		tableViewerColumn_feeStatus.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				KinderFeeInfo obj = (KinderFeeInfo) element;
				return CommonUtil.getFeeStatusNameByCode(obj.getFeeVoucherStatus());
			}
		});

		// 操作时间
		TableViewerColumn tableViewerColumn_operTime = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumnOperTime = tableViewerColumn_operTime.getColumn();
		tableColumnOperTime.setWidth(80);
		tableColumnOperTime.setText("操作时间");
		tableViewerColumn_operTime.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				KinderFeeInfo obj = (KinderFeeInfo) element;
				return CommonUtil.formatDateToString(obj.getOperTime(),CommonUtil.TIME_FORMAT_PATTERN);
			}
		});

		// TableViewerColumn tableViewerColumn_3 = new
		// TableViewerColumn(checkboxTableViewer, SWT.NONE);
		// TableColumn tableColumnFeeStart = tableViewerColumn_3.getColumn();
		// tableColumnFeeStart.setWidth(80);
		// tableColumnFeeStart.setText("缴费开始日期");
		// tableViewerColumn_3.setLabelProvider(new ColumnLabelProvider()
		// {
		// @Override
		// public String getText(Object element)
		// {
		// KinderFeeInfo obj = (KinderFeeInfo) element;
//		 return CommonUtil.formatDateToString(obj.getFeeTime(),
//		 CommonUtil.TIME_FORMAT_PATTERN);
		// }
		// });
		//
		// TableViewerColumn tableViewerColumn_4 = new
		// TableViewerColumn(checkboxTableViewer, SWT.NONE);
		// TableColumn tableColumnFeeExpire = tableViewerColumn_4.getColumn();
		// tableColumnFeeExpire.setWidth(80);
		// tableColumnFeeExpire.setText("费用到期日");
		// tableViewerColumn_4.setLabelProvider(new ColumnLabelProvider()
		// {
		// @Override
		// public String getText(Object element)
		// {
		// KinderFeeInfo obj = (KinderFeeInfo) element;
		// return CommonUtil.formatDateToString(obj.getFeeExpireTime(),
		// CommonUtil.TIME_FORMAT_PATTERN);
		// }
		// });

		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumnFeeChangeReason = tableViewerColumn_5.getColumn();
		tableColumnFeeChangeReason.setWidth(100);
		tableColumnFeeChangeReason.setText("缴费变更原因");
		tableViewerColumn_5.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				KinderFeeInfo obj = (KinderFeeInfo) element;
				return obj.getFeeEvent();
			}
		});

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

		checkboxTableViewer.setContentProvider(new ArrayContentProvider()
		{
			@Override
			public Object[] getElements(Object inputElement)
			{
				KinderFeeInfo[] arr = (KinderFeeInfo[]) inputElement;
				return arr;
			}
		});
		KinderService feeService = new KinderService();
		List<KinderFeeInfo> feeHisList = feeService.queryFeeHistory(kinderId);
		checkboxTableViewer.setInput(feeHisList.toArray(new KinderFeeInfo[0]));
	}

	public KinderFeeHistoryGroup(Composite parent, int none, String userId)
	{
		super(parent, none, userId);
	}

}
