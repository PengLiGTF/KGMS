package com.kindergarten.groups.user;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

import com.kindergarten.data.FunOperation;
import com.kindergarten.data.Module;
import com.kindergarten.data.ModuleFun;
import com.kindergarten.data.Role;
import com.kindergarten.groups.AbstractGroup;
import com.kindergarten.groups.IndexGroup;
import com.kindergarten.service.ModuleService;
import com.kindergarten.service.RoleService;
import com.kindergarten.util.ButtonNameConstant;
import com.kindergarten.util.MessageBoxUtil;

public class AuthorityGrantGroup extends AbstractGroup
{
	final CheckboxTreeViewer checkboxTreeViewer;
	final ComboViewer roleComboViewer;
	List<Module> moduleList;
	List<Module> grantedModuleList;

	public AuthorityGrantGroup(final Composite parent, int style, final String userId)
	{
		super(parent, style, userId);
		this.setText("权限管理");
		composite.setBounds(10, 20, 821, 457);

		Label label = new Label(composite, SWT.NONE);
		label.setBounds(24, 23, 39, 17);
		label.setText("角色");

		roleComboViewer = new ComboViewer(composite, SWT.NONE);
		Combo roleId = roleComboViewer.getCombo();
		roleId.setBounds(84, 20, 160, 25);

		List<Role> roleList = new RoleService().queryAllRoles();

		roleComboViewer.setContentProvider(new ArrayContentProvider()
		{
			@Override
			public Object[] getElements(Object inputElement)
			{
				return (Object[]) inputElement;
			}
		});

		roleComboViewer.setLabelProvider(new ILabelProvider()
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
				Role role = (Role) element;
				return role.getRoleName();
			}
		});
		roleComboViewer.setInput(roleList.toArray(new Role[0]));
		// 选择角色进行权限设置
		roleComboViewer.addSelectionChangedListener(new ISelectionChangedListener()
		{
			@Override
			public void selectionChanged(SelectionChangedEvent event)
			{
				// 查询模块和模块下的功能
				StructuredSelection structured = (StructuredSelection) event.getSelection();
				Role role = (Role) structured.getFirstElement();
				String roleId = role.getRoleId();
				ModuleService moduleService = new ModuleService();

				moduleList = moduleService.queryAllModules();

				checkboxTreeViewer.setAutoExpandLevel(CheckboxTreeViewer.ALL_LEVELS);
				checkboxTreeViewer.setInput(moduleList.toArray(new Module[0]));
				// 查询已经赋予的权限
				grantedModuleList = moduleService.queryAllGrantedModuleFunsByRoleId(roleId);
				// 打开权限设置窗体时勾选已经赋予的权限
				checkboxTreeViewer.setCheckedElements(grantedModuleList.toArray());
				checkboxTreeViewer.getCheckedElements();
				for (Module module : grantedModuleList)
				{
					List<ModuleFun> funList = module.getModuleFuns();
					for (ModuleFun fun : funList)
					{
						checkboxTreeViewer.setChecked(fun, true);
						List<FunOperation> operList = fun.getFunOperationList();
						for (FunOperation oper : operList)
						{
							checkboxTreeViewer.setChecked(oper, true);
						}
					}
				}
			}
		});

		Label label_1 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setBounds(21, 67, 774, 2);

		checkboxTreeViewer = new CheckboxTreeViewer(composite, SWT.BORDER);
		Tree tree = checkboxTreeViewer.getTree();
		tree.setBounds(21, 76, 537, 371);
		tree.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label label_2 = new Label(composite, SWT.SEPARATOR | SWT.VERTICAL);
		label_2.setBounds(577, 75, 2, 359);

		Button btnSave = new Button(composite, SWT.NONE);
		btnSave.setData(ButtonNameConstant.BTN_SAVE);
		btnSave.setBounds(656, 314, 80, 27);
		btnSave.setText("保存");
		btnSave.addMouseListener(new AuthorityMouseListener(this.getShell()));

		Button btnRecycle = new Button(composite, SWT.NONE);
		btnRecycle.setData(ButtonNameConstant.BTN_RECYCLE);
		btnRecycle.setBounds(656, 222, 80, 27);
		btnRecycle.setText("收回权限");
		btnRecycle.addMouseListener(new AuthorityMouseListener(this.getShell()));

		Button btnGrantAll = new Button(composite, SWT.NONE);
		btnGrantAll.setData(ButtonNameConstant.BTN_GRANT_ALL);
		btnGrantAll.setBounds(656, 144, 80, 27);
		btnGrantAll.setText("赋予全部权限");

		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.setBounds(656, 371, 80, 27);
		btnCancel.setText("关闭");
		btnCancel.addMouseListener(new MouseListener()
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
					parent.getChildren()[0].dispose();
				}
				new IndexGroup(parent, SWT.NONE, userId);
			}
		});

		btnGrantAll.addMouseListener(new AuthorityMouseListener(this.getShell()));

		checkboxTreeViewer.setContentProvider(new FileTreeContentProvider());
		checkboxTreeViewer.setLabelProvider(new FileTreeLabelProvider());
		checkboxTreeViewer.addCheckStateListener(new ICheckStateListener()
		{
			public void checkStateChanged(CheckStateChangedEvent event)
			{
				checkboxTreeViewer.setSubtreeChecked(event.getElement(), event.getChecked());
			}
		});
	}

	class AuthorityMouseListener implements MouseListener
	{

		private Shell shell;

		public AuthorityMouseListener(Shell shell)
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
			Button btn = (Button) e.getSource();
			String data = (String) btn.getData();
			if (data != null)
			{
				StructuredSelection iSelection = (StructuredSelection) roleComboViewer.getSelection();
				if (iSelection.isEmpty())
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "请选择要进行权限操作的角色");
					return;
				}
				Object[] selectedNodes = checkboxTreeViewer.getCheckedElements();
				if (data.equals(ButtonNameConstant.BTN_SAVE))
				{
					Role role = (Role) iSelection.getFirstElement();
					String selectedRoleId = role.getRoleId();
					List<ModuleFun> funList = new ArrayList<ModuleFun>();
					List<FunOperation> funOperationList = new ArrayList<FunOperation>();

					for (Object selectedNode : selectedNodes)
					{
						if (ModuleFun.class.isInstance(selectedNode))
						{
							ModuleFun moduleFun = (ModuleFun) selectedNode;
							funList.add(moduleFun);
						} else if (FunOperation.class.isInstance(selectedNode))
						{
							FunOperation funOperation = (FunOperation) selectedNode;
							funOperationList.add(funOperation);
						}
					}
					int result = MessageBoxUtil.showConfirmMessageBox(shell, "确定要进行保存操作？");
					if (SWT.OK == result)
					{
						try
						{
							new RoleService().batchGrantToRole(selectedRoleId, funList, funOperationList);
							MessageBoxUtil.showWarnMessageBox(shell, "操作成功");
						} catch (SQLException e1)
						{
							e1.printStackTrace();
							MessageBoxUtil.showWarnMessageBox(shell, "操作失败，请检查数据库配置是否正常");
						}
					}
				} else if (data.equals(ButtonNameConstant.BTN_RECYCLE))
				{
					int result = MessageBoxUtil.showConfirmMessageBox(shell, "确定要回收当前角色的所有权限么？");
					if (SWT.OK == result)
					{
						if (selectedNodes == null || selectedNodes.length <= 0)
						{
							MessageBoxUtil.showWarnMessageBox(shell, "当前角色无权限可回收");
							return;
						} else
						{
							for (Object selectedNode : selectedNodes)
							{
								checkboxTreeViewer.setChecked(selectedNode, false);
							}
							MessageBoxUtil.showWarnMessageBox(shell, "操作成功，请按保存键进行保存");
						}
					}
				} else if (data.equals(ButtonNameConstant.BTN_GRANT_ALL))
				{
					int result = MessageBoxUtil.showConfirmMessageBox(shell, "当前操作会将所有权限赋给该角色，继续否？");
					if (SWT.OK == result)
					{
						checkboxTreeViewer.setAllChecked(true);
					}
					MessageBoxUtil.showWarnMessageBox(shell, "操作成功，请按保存键进行保存");
				}
			}
		}
	}

	static class FileTreeContentProvider implements ITreeContentProvider
	{
		/**
		 * Gets the children of the specified object
		 * 
		 * @param arg0
		 *            the parent object
		 * @return Object[]
		 */
		public Object[] getChildren(Object arg0)
		{
			if (Module.class.isInstance(arg0))
			{
				return ((Module) arg0).getModuleFuns().toArray(new ModuleFun[0]);
			} else if (ModuleFun.class.isInstance(arg0))
			{
				return ((ModuleFun) arg0).getFunOperationList().toArray(new FunOperation[0]);
			}
			return new Object[0];
		}

		/**
		 * Gets the parent of the specified object
		 * 
		 * @param arg0
		 *            the object
		 * @return Object
		 */
		public Object getParent(Object arg0)
		{
			return arg0;
		}

		/**
		 * Returns whether the passed object has children
		 * 
		 * @param arg0
		 *            the parent object
		 * @return boolean
		 */
		public boolean hasChildren(Object arg0)
		{
			// Get the children
			Object[] obj = getChildren(arg0);
			// Return whether the parent has children
			return obj == null ? false : obj.length > 0;
		}

		/**
		 * Gets the root element(s) of the tree
		 * 
		 * @param arg0
		 *            the input data
		 * @return Object[]
		 */
		public Object[] getElements(Object element)
		{
			return (Module[]) element;
		}

		/**
		 * Disposes any created resources
		 */
		public void dispose()
		{
			// Nothing to dispose
		}

		/**
		 * Called when the input changes
		 * 
		 * @param arg0
		 *            the viewer
		 * @param arg1
		 *            the old input
		 * @param arg2
		 *            the new input
		 */
		public void inputChanged(Viewer arg0, Object arg1, Object arg2)
		{
			// Nothing to change
		}
	}

	static class FileTreeLabelProvider implements ILabelProvider
	{
		// The listeners
		private List listeners;

		// Images for tree nodes
		private Image file;

		private Image dir;

		// Label provider state: preserve case of file names/directories
		boolean preserveCase;

		/**
		 * Constructs a FileTreeLabelProvider
		 */
		public FileTreeLabelProvider()
		{
			// Create the list to hold the listeners
			listeners = new ArrayList();
			// Create the images
			try
			{
				file = new Image(null, new FileInputStream("images/file.gif"));
				dir = new Image(null, new FileInputStream("images/directory.gif"));
			} catch (FileNotFoundException e)
			{
				// Swallow it; we'll do without images
			}
		}

		/**
		 * Sets the preserve case attribute
		 * 
		 * @param preserveCase
		 *            the preserve case attribute
		 */
		public void setPreserveCase(boolean preserveCase)
		{
			this.preserveCase = preserveCase;
			// Since this attribute affects how the labels are computed,
			// notify all the listeners of the change.
			LabelProviderChangedEvent event = new LabelProviderChangedEvent(this);
			for (int i = 0, n = listeners.size(); i < n; i++)
			{
				ILabelProviderListener ilpl = (ILabelProviderListener) listeners.get(i);
				ilpl.labelProviderChanged(event);
			}
		}

		/**
		 * Gets the image to display for a node in the tree
		 * 
		 * @param arg0
		 *            the node
		 * @return Image
		 */
		public Image getImage(Object arg0)
		{
			// If the node represents a directory, return the directory image.
			// Otherwise, return the file image.
			return Module.class.isInstance(arg0) ? dir : file;
		}

		/**
		 * Gets the text to display for a node in the tree
		 * 
		 * @param element
		 *            the node
		 * @return String
		 */
		public String getText(Object element)
		{
			String text = "";
			if (Module.class.isInstance(element))
			{
				text = ((Module) element).getModuleName();
			} else if (ModuleFun.class.isInstance(element))
			{
				text = ((ModuleFun) element).getFunName();
			} else if (FunOperation.class.isInstance(element))
			{
				text = ((FunOperation) element).getOperationName();
			}
			return text;
		}

		/**
		 * Adds a listener to this label provider
		 * 
		 * @param arg0
		 *            the listener
		 */
		public void addListener(ILabelProviderListener arg0)
		{
			listeners.add(arg0);
		}

		/**
		 * Called when this LabelProvider is being disposed
		 */
		public void dispose()
		{
			// Dispose the images
			if (dir != null)
				dir.dispose();
			if (file != null)
				file.dispose();
		}

		/**
		 * Returns whether changes to the specified property on the specified
		 * element would affect the label for the element
		 * 
		 * @param arg0
		 *            the element
		 * @param arg1
		 *            the property
		 * @return boolean
		 */
		public boolean isLabelProperty(Object arg0, String arg1)
		{
			return false;
		}

		/**
		 * Removes the listener
		 * 
		 * @param arg0
		 *            the listener to remove
		 */
		public void removeListener(ILabelProviderListener arg0)
		{
			listeners.remove(arg0);
		}
	}
}
