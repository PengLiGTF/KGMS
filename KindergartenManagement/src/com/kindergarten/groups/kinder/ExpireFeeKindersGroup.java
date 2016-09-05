package com.kindergarten.groups.kinder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.kindergarten.data.ClassModel;
import com.kindergarten.data.FeeExpireKinder;
import com.kindergarten.data.Grade;
import com.kindergarten.groups.AbstractGroup;
import com.kindergarten.service.ClassManageService;
import com.kindergarten.service.KinderService;
import com.kindergarten.util.ComboArrayContentProvider;
import com.kindergarten.util.ComboILabelProvider;
import com.kindergarten.util.CommonUtil;
import com.kindergarten.util.FormatData;
import com.kindergarten.util.MyComboType;
import com.kindergarten.util.print.PrinterUtil;

public class ExpireFeeKindersGroup extends AbstractGroup
{
	private Text kinderIdText;
	private Text kinderNameText;
	private final ComboViewer comboViewerGrade;
	private final ComboViewer comboViewerClass;
	private KinderService kinderService = new KinderService();
	private final CheckboxTableViewer checkboxTableViewer;
	private String title;
	public ExpireFeeKindersGroup(final Composite parent, int style, final String userId, String title)
	{
		this(parent,style,userId);
		this.title = "学生续费管理";
		this.setText(this.title);
	}

	public ExpireFeeKindersGroup(final Composite parent, int style, final String userId)
	{
		super(parent, style, userId);
		this.title = "下月费用到期学生统计";
		this.setText(this.title);
		Label label = new Label(composite, SWT.NONE);
		label.setBounds(10, 10, 34, 17);
		label.setText("学号");

		kinderIdText = new Text(composite, SWT.BORDER);
		kinderIdText.setBounds(50, 4, 152, 23);

		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setBounds(339, 10, 34, 17);
		label_1.setText("姓名");

		kinderNameText = new Text(composite, SWT.BORDER);
		kinderNameText.setBounds(379, 7, 142, 23);

		Label label_2 = new Label(composite, SWT.NONE);
		label_2.setBounds(10, 55, 34, 17);
		label_2.setText("年级");

		comboViewerGrade = new ComboViewer(composite, SWT.NONE);
		Combo comboGrade = comboViewerGrade.getCombo();
		comboGrade.setBounds(49, 47, 152, 25);
		comboViewerGrade.setContentProvider(new ComboArrayContentProvider(MyComboType.GRADE));
		comboViewerGrade.setLabelProvider(new ComboILabelProvider());
		comboViewerGrade.setInput(new Grade[0]);

		comboViewerGrade.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			public void selectionChanged(SelectionChangedEvent event)
			{
				StructuredSelection sts = (StructuredSelection) event.getSelection();
				if (sts != null)
				{
					Grade grade = (Grade) sts.getFirstElement();
					ClassManageService service = new ClassManageService();
					int gradeId = grade.getGradeId();
					ClassModel[] grades = service.queryClassesByGradeId(gradeId).toArray(new ClassModel[0]);
					comboViewerClass.setInput(grades);
				}
			}
		});

		Label label_3 = new Label(composite, SWT.NONE);
		label_3.setBounds(339, 55, 34, 17);
		label_3.setText("班级");

		comboViewerClass = new ComboViewer(composite, SWT.NONE);
		Combo comboClass = comboViewerClass.getCombo();
		comboClass.setBounds(379, 52, 142, 25);
		comboViewerClass.setContentProvider(new ComboArrayContentProvider(MyComboType.CLASS));
		comboViewerClass.setLabelProvider(new ComboILabelProvider());
		comboViewerClass.setInput(new ClassModel[0]);

		Button btnQuery = new Button(composite, SWT.NONE);
		btnQuery.setBounds(543, 29, 80, 43);
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
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("kinderId", kinderIdText.getText());
				param.put("kinderName", kinderNameText.getText());
				Grade grade = CommonUtil.<Grade> getSelectedItem(comboViewerGrade);
				if (grade != null)
				{
					param.put("gradeId", String.valueOf(grade.getGradeId()));
				}
				ClassModel classModel = CommonUtil.<ClassModel> getSelectedItem(comboViewerClass);
				if (classModel != null)
				{
					param.put("classId", String.valueOf(classModel.getClassId()));
				}
				List<FeeExpireKinder> feeExpireKinderList = kinderService.queryExpireAtNextMonth(param);
				if (feeExpireKinderList != null)
				{
					checkboxTableViewer.setInput(feeExpireKinderList.toArray(new FeeExpireKinder[0]));
				}
			}
		});

		checkboxTableViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER | SWT.FULL_SELECTION);

		Label label_4 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_4.setBounds(10, 78, 769, 11);

		Table expireKindersTable = checkboxTableViewer.getTable();
		expireKindersTable.setLinesVisible(true);
		expireKindersTable.setHeaderVisible(true);
		expireKindersTable.setBounds(10, 95, 769, 355);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn = tableViewerColumn.getColumn();
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText("姓名");
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				FeeExpireKinder obj = (FeeExpireKinder) element;
				return obj.getKinderName();
			}
		});

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_1 = tableViewerColumn_1.getColumn();
		tblclmnNewColumn_1.setWidth(120);
		tblclmnNewColumn_1.setText("学号");
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				FeeExpireKinder obj = (FeeExpireKinder) element;
				return obj.getKinderId();
			}
		});

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_2 = tableViewerColumn_2.getColumn();
		tblclmnNewColumn_2.setWidth(100);
		tblclmnNewColumn_2.setText("年级");
		tableViewerColumn_2.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				FeeExpireKinder obj = (FeeExpireKinder) element;
				return obj.getKinderGradeName();
			}
		});

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_3 = tableViewerColumn_3.getColumn();
		tblclmnNewColumn_3.setWidth(89);
		tblclmnNewColumn_3.setText("班级");
		tableViewerColumn_3.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				FeeExpireKinder obj = (FeeExpireKinder) element;
				return obj.getKinderClassName();
			}
		});

		TableViewerColumn tableViewerColumn_6 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_6 = tableViewerColumn_6.getColumn();
		tblclmnNewColumn_6.setWidth(100);
		tblclmnNewColumn_6.setText("缴费时间");
		tableViewerColumn_6.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				FeeExpireKinder obj = (FeeExpireKinder) element;
				return FormatData.dateToStringWithoutTime(obj.getFeeTime());// TODO
			}
		});

		TableViewerColumn tableViewerColumn_7 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_7 = tableViewerColumn_7.getColumn();
		tblclmnNewColumn_7.setWidth(80);
		tblclmnNewColumn_7.setText("缴费天数");
		tableViewerColumn_7.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				FeeExpireKinder obj = (FeeExpireKinder) element;
				return String.valueOf(obj.getFeeDays());// TODO
			}
		});

		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_4 = tableViewerColumn_4.getColumn();
		tblclmnNewColumn_4.setWidth(100);
		tblclmnNewColumn_4.setText("费用到期时间");
		tableViewerColumn_4.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				FeeExpireKinder obj = (FeeExpireKinder) element;
				return FormatData.dateToStringWithoutTime(obj.getFeeExpireTime());// TODO
			}
		});
		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(checkboxTableViewer, SWT.BUTTON1);
		TableColumn tblclmnNewColumn_5 = tableViewerColumn_5.getColumn();
		tblclmnNewColumn_5.setWidth(86);
		tblclmnNewColumn_5.setText("操作");
		tableViewerColumn_5.setLabelProvider(new ColumnLabelProvider()
		{
			Map<Object, Button> buttons = new HashMap<Object, Button>();

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
					button.setText("续费");
					buttons.put(cell.getElement(), button);
				}
				final FeeExpireKinder tempObj = (FeeExpireKinder) item.getData();
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
						new KinderFeeRenewGroup(parent, SWT.NONE, userId, tempObj);
					}
				});
				TableEditor editor = new TableEditor(item.getParent());
				editor.grabHorizontal = true;
				editor.grabVertical = true;
				editor.setEditor(button, item, cell.getColumnIndex());
				editor.layout();
			}
		});

		Button btnPrint = new Button(composite, SWT.NONE);
		btnPrint.setBounds(699, 29, 80, 43);
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
				PrinterUtil.printTable(table, getShell(), title);
				parent.getChildren()[0].dispose();
				new ExpireFeeKindersGroup(parent, SWT.NONE, userId);
			}
		});

		checkboxTableViewer.setContentProvider(new ArrayContentProvider());
		// checkboxTableViewer.setLabelProvider(new MyITableLabelProvider());
		Map<String, Object> param = new HashMap<String, Object>();
		List<FeeExpireKinder> feeExpireKinderList = kinderService.queryExpireAtNextMonth(param);
		if (!feeExpireKinderList.isEmpty())
		{
			checkboxTableViewer.setInput(feeExpireKinderList.toArray(new FeeExpireKinder[0]));
		}
	}
}
