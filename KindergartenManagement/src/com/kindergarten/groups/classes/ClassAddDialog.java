package com.kindergarten.groups.classes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.kindergarten.data.ClassModel;
import com.kindergarten.data.Grade;
import com.kindergarten.service.ClassManageService;

public class ClassAddDialog extends Dialog
{
	private List<Grade> gradeList = new ArrayList<Grade>();
	private final ClassManageService classManageService;
	private ClassModel selectedClass;
	private ComboViewer comboViewer;
	private Grade defaultGrade;

	public Grade getDefaultGrade()
	{
		return defaultGrade;
	}

	public void setDefaultGrade(Grade defaultGrade)
	{
		this.defaultGrade = defaultGrade;
	}

	public ClassModel getSelectedClass()
	{
		return selectedClass;
	}

	public void setSelectedClass(ClassModel selectedClass)
	{
		this.selectedClass = selectedClass;
	}

	private Text className;

	public ClassAddDialog(Shell parentShell, Grade grade)
	{
		this(parentShell);
		this.defaultGrade = grade;
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public ClassAddDialog(Shell parentShell)
	{
		super(parentShell);
		classManageService = new ClassManageService();
		gradeList = classManageService.queryAllGrade();
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
		label.setBounds(10, 59, 61, 17);
		label.setText("班级名称：");

		className = new Text(container, SWT.BORDER);
		className.setBounds(77, 53, 270, 23);
		if (selectedClass != null)
		{
			className.setText(selectedClass.getClassName());
		}
		Label label_1 = new Label(container, SWT.NONE);
		label_1.setBounds(10, 114, 61, 17);
		label_1.setText("年级：");

		comboViewer = new ComboViewer(container, SWT.NONE);
		Combo comboGrade = comboViewer.getCombo();
		comboGrade.setBounds(77, 111, 270, 25);

		comboViewer.setContentProvider(new ArrayContentProvider()
		{
			@Override
			public Object[] getElements(Object inputElement)
			{
				return (Object[]) inputElement;
			}
		});
		comboViewer.setLabelProvider(new ILabelProvider()
		{
			@Override
			public Image getImage(Object element)
			{
				return null;
			}

			@Override
			public String getText(Object element)
			{
				Grade grade = (Grade) element;
				return grade.getGradeName();
			}

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
		});
		comboViewer.setInput(gradeList.toArray(new Grade[0]));
		StructuredSelection seleced = new StructuredSelection(new Grade[]
		{ defaultGrade });// TODO
		comboViewer.setSelection(seleced);
		return container;
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

	@Override
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		if (this.selectedClass != null)
		{
			newShell.setText("修改班级信息");
		} else
		{
			newShell.setText("新增班级");
		}
	}

	@Override
	protected void okPressed()
	{
		int gradeId = ((Grade) ((StructuredSelection) comboViewer.getSelection()).getFirstElement()).getGradeId();
		String cName = className.getText();
		if (this.selectedClass != null)
		{
			selectedClass.setClassName(cName);
			selectedClass.setGradeId(gradeId);
			classManageService.updateClass(selectedClass);
		} else
		{
			classManageService.addClass(cName, gradeId);
		}
		super.okPressed();
	}
}
