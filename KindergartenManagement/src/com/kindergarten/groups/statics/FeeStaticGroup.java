package com.kindergarten.groups.statics;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.kindergarten.data.FeeStaticModel;
import com.kindergarten.data.FeeStaticTypeModel;
import com.kindergarten.groups.AbstractGroup;
import com.kindergarten.service.FeeStaticService;
import com.kindergarten.util.ComboArrayContentProvider;
import com.kindergarten.util.ComboILabelProvider;
import com.kindergarten.util.CommonUtil;
import com.kindergarten.util.MyComboType;

/**
 * 费用统计
 * */
public class FeeStaticGroup extends AbstractGroup
{
	private Table table;
	private final TableViewer tableViewer;

	public FeeStaticGroup(Composite parent, int style, String userId)
	{
		super(parent, style, userId);
		this.setText("费用统计");

		Label label = new Label(composite, SWT.NONE);
		label.setBounds(7, 16, 61, 17);
		label.setText("统计类型");

		final ComboViewer comboViewer = new ComboViewer(composite, SWT.NONE);
		Combo staticCombo = comboViewer.getCombo();
		staticCombo.setBounds(79, 13, 160, 25);
		comboViewer.setContentProvider(new ComboArrayContentProvider(MyComboType.FEE_STATIC_TYPE));
		comboViewer.setLabelProvider(new ComboILabelProvider());
		comboViewer.setInput(new FeeStaticTypeModel[0]);

		comboViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			public void selectionChanged(SelectionChangedEvent event)
			{
				FeeStaticTypeModel temp = CommonUtil.getSelectedItem(comboViewer);
				if (temp != null)
				{
					String code = temp.getCode();
					List<FeeStaticModel> modelList = new FeeStaticService().doFeeStatic(code);
					tableViewer.setInput(modelList.toArray(new FeeStaticModel[0]));
				}
			}
		});

		Label label_1 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setBounds(7, 44, 783, 2);

		Button button = new Button(composite, SWT.NONE);
		button.setBounds(313, 11, 80, 27);
		button.setText("统计");
		button.setVisible(false);

		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setBounds(7, 52, 783, 372);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn = tableViewerColumn.getColumn();
		tblclmnNewColumn.setWidth(93);
		tblclmnNewColumn.setText("年级");
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				FeeStaticModel temp = (FeeStaticModel) element;
				return temp.getGradeName();
			}
		});

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_1 = tableViewerColumn_1.getColumn();
		tblclmnNewColumn_1.setWidth(92);
		tblclmnNewColumn_1.setText("班级");
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				FeeStaticModel temp = (FeeStaticModel) element;
				return temp.getClassName();
			}
		});

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_2 = tableViewerColumn_2.getColumn();
		tblclmnNewColumn_2.setWidth(100);
		tblclmnNewColumn_2.setText("总优惠金额");
		tableViewerColumn_2.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				FeeStaticModel temp = (FeeStaticModel) element;
				return String.valueOf(temp.getSumPrivilegeMoney());
			}
		});

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_3 = tableViewerColumn_3.getColumn();
		tblclmnNewColumn_3.setWidth(100);
		tblclmnNewColumn_3.setText("总其他费用");
		tableViewerColumn_3.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				FeeStaticModel temp = (FeeStaticModel) element;
				return String.valueOf(temp.getSumOtherMoney());
			}
		});

		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_4 = tableViewerColumn_4.getColumn();
		tblclmnNewColumn_4.setWidth(100);
		tblclmnNewColumn_4.setText("总剩余预缴费");
		tableViewerColumn_4.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				FeeStaticModel temp = (FeeStaticModel) element;
				return String.valueOf(temp.getSumPreMoney());
			}
		});

		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_5 = tableViewerColumn_5.getColumn();
		tblclmnNewColumn_5.setWidth(100);
		tblclmnNewColumn_5.setText("总抵扣预缴费");
		tableViewerColumn_5.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				FeeStaticModel temp = (FeeStaticModel) element;
				return String.valueOf(temp.getSumDeductionPreMoney());
			}
		});

		TableViewerColumn tableViewerColumn_6 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_6 = tableViewerColumn_6.getColumn();
		tblclmnNewColumn_6.setWidth(100);
		tblclmnNewColumn_6.setText("总实缴费用");
		tableViewerColumn_6.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				FeeStaticModel temp = (FeeStaticModel) element;
				return String.valueOf(temp.getSumActualMoney());
			}
		});

		TableViewerColumn tableViewerColumn_7 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_7 = tableViewerColumn_7.getColumn();
		tblclmnNewColumn_7.setWidth(100);
		tblclmnNewColumn_7.setText("总收缴费用");
		tableViewerColumn_7.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				FeeStaticModel temp = (FeeStaticModel) element;
				return String.valueOf(temp.getTotalMoney());
			}
		});

		tableViewer.setContentProvider(new ArrayContentProvider()
		{
			@Override
			public Object[] getElements(Object inputElement)
			{
				return (FeeStaticModel[]) inputElement;
			}
		});
	}
}
