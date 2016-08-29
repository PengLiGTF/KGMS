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
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
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
import com.kindergarten.data.Grade;
import com.kindergarten.data.KinderInfoModel;
import com.kindergarten.data.KinderStatus;
import com.kindergarten.groups.AbstractGroup;
import com.kindergarten.groups.classes.ClassesAddGroup;
import com.kindergarten.service.ClassManageService;
import com.kindergarten.service.KinderService;
import com.kindergarten.util.ButtonNameConstant;
import com.kindergarten.util.CommonUtil;
import com.kindergarten.util.print.PrinterUtil;

public class KinderInfoListGroup extends AbstractGroup
{
	private Table tableKinderList;
	private Text kinderIdText;
	private Text kinderNameText;
	private ComboViewer comboViewerStatus;
	private ComboViewer comboViewerGrade;
	private ComboViewer comboViewerClass;
	private Button btnToClassM;

	private CheckboxTableViewer checkboxTableViewer;
	private ClassModel classModel;
	private Grade grade;

	public KinderInfoListGroup(final Composite parent, int style, final String userId, final ClassModel classModel, final Grade grade)
	{
		this(parent, style, userId);
		this.classModel = classModel;
		this.grade = grade;
		btnToClassM = new Button(composite, SWT.NONE);
		btnToClassM.setBounds(605, 52, 80, 27);
		btnToClassM.setText(" 返回班级管理");
		// btnToClassM.setVisible(false);
		btnToClassM.addMouseListener(new MouseListener()
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
				new ClassesAddGroup(parent, SWT.NONE, userId);
			}
		});

		Map<String, String> queryParam = new HashMap<String, String>();// TODO
		List<KinderInfoModel> kinderInfoList = new ArrayList<KinderInfoModel>();
		StructuredSelection seleced = new StructuredSelection(new Grade[]
		{ grade });
		comboViewerGrade.setSelection(seleced);
		StructuredSelection selecedC = new StructuredSelection(new ClassModel[]
		{ classModel });
		comboViewerClass.setSelection(selecedC);
		queryParam.put("gradeId", String.valueOf(grade.getGradeId()));
		queryParam.put("classId", String.valueOf(classModel.getClassId()));
		kinderInfoList = new KinderService().queryKinderInfoByCondition(queryParam);
		checkboxTableViewer.setInput(kinderInfoList.toArray(new KinderInfoModel[0]));
	}

	/**
	 * @wbp.parser.constructor
	 */
	public KinderInfoListGroup(final Composite parent, int style, final String userId)
	{
		super(parent, style, userId);
		this.setText("儿童信息管理");

		final Map<Object, Button> buttons = new HashMap<Object, Button>();
		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 85, 675, 2);

		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setBounds(10, 10, 61, 17);
		label_1.setText("学号");

		kinderIdText = new Text(composite, SWT.BORDER);
		kinderIdText.setBounds(68, 7, 111, 23);

		Label label_2 = new Label(composite, SWT.NONE);
		label_2.setBounds(221, 10, 61, 17);
		label_2.setText("姓名");

		kinderNameText = new Text(composite, SWT.BORDER);
		kinderNameText.setBounds(279, 7, 111, 23);

		Label label_3 = new Label(composite, SWT.NONE);
		label_3.setBounds(472, 10, 61, 17);
		label_3.setText("入园状态");

		comboViewerStatus = new ComboViewer(composite, SWT.NONE);
		Combo comboStatus = comboViewerStatus.getCombo();
		comboStatus.setBounds(556, 7, 129, 25);
		comboViewerStatus.setContentProvider(new ArrayContentProvider()
		{
			@Override
			public Object[] getElements(Object inputElement)
			{
				return CommonUtil.getAllStatus();
			}
		});
		comboViewerStatus.setLabelProvider(new MyLabelProvider());
		comboViewerStatus.setInput(new KinderStatus[0]);

		comboViewerGrade = new ComboViewer(composite, SWT.NONE);
		Combo comboGrade = comboViewerGrade.getCombo();
		comboGrade.setBounds(68, 42, 111, 25);
		comboViewerGrade.setContentProvider(new ArrayContentProvider()
		{
			@Override
			public Object[] getElements(Object inputElement)
			{
				return new ClassManageService().queryAllGrade().toArray(new Grade[0]);
			}
		});
		comboViewerGrade.setLabelProvider(new MyLabelProvider());
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

		Label label_4 = new Label(composite, SWT.NONE);
		label_4.setBounds(10, 45, 61, 17);
		label_4.setText("年级");

		Button btnQuery = new Button(composite, SWT.NONE);
		btnQuery.setBounds(519, 52, 80, 27);
		btnQuery.setText("查询");
		btnQuery.setData(ButtonNameConstant.BTN_QUERY);
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
				StructuredSelection statusSelected = (StructuredSelection) comboViewerStatus.getSelection();
				String statusCode = "";
				KinderStatus sm = (KinderStatus) statusSelected.getFirstElement();
				if (sm != null)
				{
					statusCode = sm.getStatusCode();
				}
				StructuredSelection gradeSelected = (StructuredSelection) comboViewerGrade.getSelection();
				String gradeId = "";
				Grade grade = (Grade) gradeSelected.getFirstElement();
				if (grade != null)
				{
					gradeId = String.valueOf(grade.getGradeId());
				}
				StructuredSelection classSelected = (StructuredSelection) comboViewerClass.getSelection();
				String classId = "";
				ClassModel cModel = (ClassModel) classSelected.getFirstElement();
				if (cModel != null)
				{
					classId = String.valueOf(cModel.getClassId());
				}

				Map<String, String> param = new HashMap<String, String>();
				param.put("kinderId", kinderId);
				param.put("kinderName", kinderName);
				param.put("gradeId", gradeId);
				param.put("classId", classId);
				param.put("kinderStatus", statusCode);

				List<KinderInfoModel> kmList = new KinderService().queryKinderInfoByCondition(param);

				if (kmList != null)
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
					checkboxTableViewer.setInput(kmList.toArray(new KinderInfoModel[0]));
				}
			}
		});

		Label label_5 = new Label(composite, SWT.NONE);
		label_5.setBounds(220, 45, 43, 17);
		label_5.setText("班级");

		comboViewerClass = new ComboViewer(composite, SWT.NONE);
		Combo comboClass = comboViewerClass.getCombo();
		comboClass.setBounds(279, 42, 111, 25);
		comboViewerClass.setContentProvider(new ArrayContentProvider());
		comboViewerClass.setLabelProvider(new MyLabelProvider());

		checkboxTableViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER | SWT.FULL_SELECTION);
		tableKinderList = checkboxTableViewer.getTable();
		tableKinderList.setLinesVisible(true);
		tableKinderList.setHeaderVisible(true);
		tableKinderList.setBounds(10, 93, 675, 349);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn = tableViewerColumn.getColumn();
		tblclmnNewColumn.setWidth(90);
		tblclmnNewColumn.setText("学号");
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				KinderInfoModel model = (KinderInfoModel) element;
				return model.getKinderId();
			}
		});

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumn = tableViewerColumn_1.getColumn();
		tableColumn.setWidth(82);
		tableColumn.setText("姓名");
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				KinderInfoModel model = (KinderInfoModel) element;
				return model.getKinderName();
			}
		});

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn_1 = tableViewerColumn_2.getColumn();
		tblclmnNewColumn_1.setWidth(63);
		tblclmnNewColumn_1.setText("性别");
		tableViewerColumn_2.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				KinderInfoModel model = (KinderInfoModel) element;
				String sex = model.getKinderSex();
				return CommonUtil.getSexNameByCode(sex.charAt(0));
			}
		});

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumn_1 = tableViewerColumn_3.getColumn();
		tableColumn_1.setWidth(81);
		tableColumn_1.setText("年级");
		tableViewerColumn_3.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				KinderInfoModel model = (KinderInfoModel) element;
				return model.getKinderGradeName();
			}
		});

		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumn_2 = tableViewerColumn_4.getColumn();
		tableColumn_2.setWidth(100);
		tableColumn_2.setText("班级");
		tableViewerColumn_4.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				KinderInfoModel model = (KinderInfoModel) element;
				return model.getKinderClassName();
			}
		});

		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumn_3 = tableViewerColumn_5.getColumn();
		tableColumn_3.setWidth(100);
		tableColumn_3.setText("入园状态");
		tableViewerColumn_5.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				KinderInfoModel model = (KinderInfoModel) element;
				return CommonUtil.getStatusNameByCode(model.getKinderStatus());
			}
		});

		TableViewerColumn tableViewerColumn_6 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumn_4 = tableViewerColumn_6.getColumn();
		tableColumn_4.setWidth(100);
		tableColumn_4.setText("缴费历史");
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
					button.setText("缴费历史");
					buttons.put(cell.getElement(), button);
				}
				final KinderInfoModel tempObj = (KinderInfoModel) item.getData();
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
						new KinderFeeHistoryGroup(parent, SWT.NONE, userId, tempObj.getKinderId());
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
		btnPrint.setBounds(699, 94, 80, 27);
		btnPrint.setData(ButtonNameConstant.BTN_PRINT);
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
					if (tableColumn.getText().equals("缴费历史"))
					{
						disposedColumn = tableColumn;
						break;
					}
				}
				disposedColumn.dispose();
				PrinterUtil.printTable(table, getShell(), "学生信息打印");
				parent.getChildren()[0].dispose();
				if (classModel != null && grade != null)
				{
					new KinderInfoListGroup(parent, SWT.NONE, userId, classModel, grade);
				} else
				{
					new KinderInfoListGroup(parent, SWT.NONE, userId);
				}
			}
		});

		Label label_6 = new Label(composite, SWT.SEPARATOR | SWT.VERTICAL);
		label_6.setBounds(691, 93, 2, 349);

		checkboxTableViewer.setContentProvider(new ArrayContentProvider());
	}

	static class MyLabelProvider implements ILabelProvider
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
		public Image getImage(Object element)
		{
			return null;
		}

		@Override
		public String getText(Object element)
		{
			if (Grade.class.isInstance(element))
			{
				Grade grade = (Grade) element;
				return grade.getGradeName();
			} else if (ClassModel.class.isInstance(element))
			{
				ClassModel cModel = (ClassModel) element;
				return cModel.getClassName();
			} else
			{
				KinderStatus kinderStatus = (KinderStatus) element;
				return kinderStatus.getStatusName();
			}
		}
	}
}
