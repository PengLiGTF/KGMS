package com.kindergarten.groups.classes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.kindergarten.data.ClassModel;
import com.kindergarten.data.Grade;
import com.kindergarten.groups.AbstractGroup;
import com.kindergarten.groups.kinder.KinderInfoListGroup;
import com.kindergarten.service.ClassManageService;
import com.kindergarten.util.ButtonNameConstant;
import com.kindergarten.util.MessageBoxUtil;

public class ClassesAddGroup extends AbstractGroup
{
	final CTabFolder tabFolder;

	private ClassManageService classManageService;

	public ClassesAddGroup(final Composite parent, int style, final String userId)
	{
		super(parent, style, userId);
		this.setText("班级管理");
		composite.setBounds(10, 20, 756, 439);

		classManageService = new ClassManageService();

		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 64, 707, -6);

		Button btnGradeAdd = new Button(composite, SWT.NONE);
		btnGradeAdd.setBounds(10, 24, 90, 27);
		btnGradeAdd.setText("新增年级");
		btnGradeAdd.setData(ButtonNameConstant.BTN_GRADE_ADD);
		btnGradeAdd.addMouseListener(new ClassMouseListener(parent, getShell(), null, null));

		Label label_1 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setBounds(10, 56, 736, 2);

		tabFolder = new CTabFolder(composite, SWT.BORDER | SWT.CLOSE);
		tabFolder.setBounds(10, 64, 736, 365);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		tabFolder.setSimple(false);

		Button button = new Button(composite, SWT.NONE);
		button.setBounds(121, 24, 80, 27);
		button.setText("删除年级");
		button.setVisible(false);

		Button button_1 = new Button(composite, SWT.NONE);
		button_1.setBounds(225, 24, 80, 27);
		button_1.setText("修改年级");
		button_1.setVisible(false);
		Button button_2 = new Button(composite, SWT.NONE);
		button_2.setBounds(331, 24, 80, 27);
		button_2.setText("重新加载");
		button_2.setVisible(false);
		Map<Grade, List<ClassModel>> map = classManageService.queryAllGradeClasses();

		if (map != null && !map.isEmpty())
		{
			Set<Entry<Grade, List<ClassModel>>> set = map.entrySet();
			Iterator<Entry<Grade, List<ClassModel>>> it = set.iterator();
			boolean isFirst = true;
			while (it.hasNext())
			{
				Entry<Grade, List<ClassModel>> entry = it.next();
				final Grade grade = entry.getKey();
				CTabItem tbtmNewItem = new CTabItem(tabFolder, SWT.NONE);
				tbtmNewItem.setText(grade.getGradeName());
				tbtmNewItem.setData(grade.getGradeId());// 设置data以便获取时进行判断
				Composite composite = new Composite(tabFolder, SWT.NONE);
				tbtmNewItem.setControl(composite);

				if (isFirst)
				{
					tabFolder.setSelection(tbtmNewItem);
					isFirst = false;
				}

				CheckboxTableViewer checkboxTableViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER | SWT.FULL_SELECTION);
				Table classesTable = checkboxTableViewer.getTable();
				classesTable.setLinesVisible(true);
				classesTable.setHeaderVisible(true);
				classesTable.setBounds(10, 10, 425, 316);
				TableViewerColumn tableViewerColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
				TableColumn tblclmnSeq = tableViewerColumn.getColumn();
				tblclmnSeq.setWidth(100);
				tblclmnSeq.setText("序号");
				// 班级名称
				TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
				TableColumn tblclmnName = tableViewerColumn_1.getColumn();
				tblclmnName.setWidth(142);
				tblclmnName.setText("班级名称");
				// 班级人数
				TableViewerColumn tableViewerColumn_count = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
				TableColumn tblclmnCount = tableViewerColumn_count.getColumn();
				tblclmnCount.setWidth(142);
				tblclmnCount.setText("班级人数");

				Button btnAdd = new Button(composite, SWT.NONE);
				btnAdd.setBounds(475, 79, 90, 27);
				checkboxTableViewer.addDoubleClickListener(new IDoubleClickListener()
				{
					@Override
					public void doubleClick(DoubleClickEvent event)
					{
						StructuredSelection strucSelection = (StructuredSelection) event.getSelection();
						ClassModel model = (ClassModel) strucSelection.getFirstElement();
						parent.getChildren()[0].dispose();
						new KinderInfoListGroup(parent, SWT.NONE, userId, model, grade);
					}
				});

				btnAdd.setText("新增班级");
				btnAdd.setData(ButtonNameConstant.BTN_ADD);
				btnAdd.addMouseListener(new ClassMouseListener(parent, getShell(), tbtmNewItem, checkboxTableViewer));

				Button btnEdit = new Button(composite, SWT.NONE);
				btnEdit.setBounds(475, 162, 90, 27);
				btnEdit.setText("修改班级");
				btnEdit.setData(ButtonNameConstant.BTN_EDIT);
				btnEdit.addMouseListener(new ClassMouseListener(parent, getShell(), tbtmNewItem, checkboxTableViewer));

				Button btnDelete = new Button(composite, SWT.NONE);
				btnDelete.setBounds(475, 249, 90, 27);
				btnDelete.setText("删除班级");
				btnDelete.setData(ButtonNameConstant.BTN_DELETE);
				btnDelete.addMouseListener(new ClassMouseListener(parent, getShell(), tbtmNewItem, checkboxTableViewer));

				checkboxTableViewer.setContentProvider(new MyTableStructedProvider());// TODO
				checkboxTableViewer.setLabelProvider(new MyITableLabelProvider());
				checkboxTableViewer.setInput(entry.getValue().toArray(new ClassModel[0]));
			}
		}
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
				ClassModel cm = (ClassModel) element;
				switch (columnIndex)
				{
				case 0:
					return String.valueOf(cm.getClassId());
				case 1:
					return cm.getClassName();
				case 2:
					return String.valueOf(cm.getKinders()) + "(人)";
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
			ClassModel[] arr = (ClassModel[]) inputElement;
			return arr;
		}
	}

	class ClassMouseListener implements MouseListener
	{
		private Shell shell;
		private Composite parent;
		private CTabItem tbtmNewItem;
		private CheckboxTableViewer checkboxTableViewer;

		public ClassMouseListener(Composite parent, Shell shell, CTabItem tbtmNewItem, CheckboxTableViewer checkboxTableViewer)
		{
			this.parent = parent;
			this.shell = shell;
			this.tbtmNewItem = tbtmNewItem;
			this.checkboxTableViewer = checkboxTableViewer;
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
			String btnKey = (String) button.getData();
			if (btnKey.equals(ButtonNameConstant.BTN_ADD))
			{
				String gName = tbtmNewItem.getText();
				int gId = (int) tbtmNewItem.getData();
				Grade defaultGrade = new Grade();
				defaultGrade.setGradeId(gId);
				defaultGrade.setGradeName(gName);
				// 新增班级
				ClassAddDialog classesAdd = new ClassAddDialog(this.shell, defaultGrade);
				int result = classesAdd.open();
				if (result == 0)
				{
					checkboxTableViewer.setInput(classManageService.queryClassesByGradeId(gId).toArray(new ClassModel[0]));
				}
			} else if (btnKey.equals(ButtonNameConstant.BTN_EDIT))
			{
				// 修改班级
				Object[] objs = checkboxTableViewer.getCheckedElements();
				if (objs == null || objs.length <= 0)
				{
					MessageBoxUtil.showWarnMessageBox(shell, "请选择一个班级进行编辑");
					return;
				}
				ClassModel cModel = (ClassModel) objs[0];

				String gName = tbtmNewItem.getText();
				int gId = (int) tbtmNewItem.getData();
				Grade defaultGrade = new Grade();
				defaultGrade.setGradeId(gId);
				defaultGrade.setGradeName(gName);
				ClassAddDialog classesAdd = new ClassAddDialog(this.shell, defaultGrade);
				classesAdd.setSelectedClass(cModel);
				classesAdd.open();
				MessageBoxUtil.showWarnMessageBox(getShell(), "班级修改成功");
				checkboxTableViewer.setInput(classManageService.queryClassesByGradeId(gId).toArray(new ClassModel[0]));
				// TODO
			} else if (btnKey.equals(ButtonNameConstant.BTN_DELETE))
			{
				Object[] objs = checkboxTableViewer.getCheckedElements();
				if (objs == null || objs.length <= 0)
				{
					MessageBoxUtil.showWarnMessageBox(shell, "请至少选择一个班级进行删除");
					return;
				}
				List<ClassModel> modelList = new ArrayList<ClassModel>();
				for (Object tem : objs)
				{
					ClassModel cm = (ClassModel) tem;
					modelList.add(cm);
				}
				int gId = (int) tbtmNewItem.getData();
				ClassManageService classManageService = new ClassManageService();
				int confirm = MessageBoxUtil.showConfirmMessageBox(shell, "确定要进行删除操作么？");
				if (confirm == SWT.OK)
				{
					classManageService.deleteClass(modelList);
					checkboxTableViewer.setInput(classManageService.queryClassesByGradeId(gId).toArray(new ClassModel[0]));
				}
			} else if (btnKey.equals(ButtonNameConstant.BTN_GRADE_ADD))
			{
				GradeAddDialog cTypeDialog = new GradeAddDialog(this.shell);
				int result = cTypeDialog.open();
				if (0 == result)
				{
					final Grade grade = cTypeDialog.getGrade();
					if (grade != null)
					{
						CTabItem tbtmNewItem = new CTabItem(tabFolder, SWT.NONE);
						tabFolder.setSelection(tbtmNewItem);
						tbtmNewItem.setText(grade.getGradeName());
						tbtmNewItem.setData(grade.getGradeId());
						Composite composite = new Composite(tabFolder, SWT.NONE);
						tbtmNewItem.setControl(composite);
						CheckboxTableViewer checkboxTableViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER | SWT.FULL_SELECTION);
						Table classesTable = checkboxTableViewer.getTable();
						classesTable.setLinesVisible(true);
						classesTable.setHeaderVisible(true);
						classesTable.setBounds(10, 10, 425, 316);

						TableViewerColumn tableViewerColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
						TableColumn tblclmnSeq = tableViewerColumn.getColumn();
						tblclmnSeq.setWidth(100);
						tblclmnSeq.setText("序号");
						TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
						TableColumn tblclmnName = tableViewerColumn_1.getColumn();
						tblclmnName.setWidth(142);
						tblclmnName.setText("班级名称");
						
						// 班级人数
						TableViewerColumn tableViewerColumn_count = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
						TableColumn tblclmnCount = tableViewerColumn_count.getColumn();
						tblclmnCount.setWidth(142);
						tblclmnCount.setText("班级人数");

						checkboxTableViewer.addDoubleClickListener(new IDoubleClickListener()
						{
							@Override
							public void doubleClick(DoubleClickEvent event)
							{
								StructuredSelection strucSelection = (StructuredSelection) event.getSelection();
								ClassModel model = (ClassModel) strucSelection.getFirstElement();
								parent.getChildren()[0].dispose();
								new KinderInfoListGroup(parent, SWT.NONE, userId, model, grade);
							}
						});
						Button btnAdd = new Button(composite, SWT.NONE);
						btnAdd.setBounds(475, 79, 90, 27);
						btnAdd.setText("新增班级");
						btnAdd.setData(ButtonNameConstant.BTN_ADD);
						btnAdd.addMouseListener(new ClassMouseListener(parent, getShell(), tbtmNewItem, checkboxTableViewer));
						Button btnEdit = new Button(composite, SWT.NONE);
						btnEdit.setBounds(475, 162, 90, 27);
						btnEdit.setText("修改班级");
						btnEdit.setData(ButtonNameConstant.BTN_EDIT);
						btnEdit.addMouseListener(new ClassMouseListener(parent, getShell(), tbtmNewItem, checkboxTableViewer));
						Button btnDelete = new Button(composite, SWT.NONE);
						btnDelete.setBounds(475, 249, 90, 27);
						btnDelete.setText("删除班级");
						btnDelete.setData(ButtonNameConstant.BTN_DELETE);
						btnDelete.addMouseListener(new ClassMouseListener(parent, getShell(), tbtmNewItem, checkboxTableViewer));
						checkboxTableViewer.setContentProvider(new MyTableStructedProvider());// TODO
						checkboxTableViewer.setLabelProvider(new MyITableLabelProvider());
						checkboxTableViewer.setInput(new ClassModel[0]);
					}
				}
			}
		}
	}
}
