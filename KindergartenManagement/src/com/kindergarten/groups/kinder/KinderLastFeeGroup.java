package com.kindergarten.groups.kinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.kindergarten.data.KinderFeeInfo;
import com.kindergarten.groups.AbstractGroup;
import com.kindergarten.service.KinderService;
import com.kindergarten.service.UserService;
import com.kindergarten.util.CommonUtil;
import com.kindergarten.util.MessageBoxUtil;

public class KinderLastFeeGroup extends AbstractGroup
{
	private Table table;
	private String kinderId;
	private Text kinderText;
	boolean isAdmin = false;

	/**
	 * @wbp.parser.constructor
	 * 
	 */
	public KinderLastFeeGroup(final Composite parent, int style, final String userId, String kinderId)
	{
		this(parent, style, userId);
		
		composite.setBounds(10, 20, 840, 600);
		setText("学生最近一次缴费记录");
		final Map<Object, List<Button>> buttons = new HashMap<Object, List<Button>>();
		this.kinderId = kinderId;
		final CheckboxTableViewer checkboxTableViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table = checkboxTableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(10, 56, 752, 359);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tblclmnId = tableViewerColumn.getColumn();
		tblclmnId.setWidth(100);
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
		tblclmnName.setWidth(100);
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
		tableColumnDays.setWidth(100);
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

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumnFeeStart = tableViewerColumn_3.getColumn();
		tableColumnFeeStart.setWidth(100);
		tableColumnFeeStart.setText("缴费开始日期");
		tableViewerColumn_3.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				KinderFeeInfo obj = (KinderFeeInfo) element;
				return CommonUtil.formatDateToString(obj.getFeeTime(), CommonUtil.TIME_FORMAT_PATTERN);
			}
		});

		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumnFeeExpire = tableViewerColumn_4.getColumn();
		tableColumnFeeExpire.setWidth(100);
		tableColumnFeeExpire.setText("费用到期日");
		tableViewerColumn_4.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public String getText(Object element)
			{
				KinderFeeInfo obj = (KinderFeeInfo) element;
				return CommonUtil.formatDateToString(obj.getFeeExpireTime(), CommonUtil.TIME_FORMAT_PATTERN);
			}
		});

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

		TableViewerColumn tableViewerColumn_6 = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumn_4 = tableViewerColumn_6.getColumn();
		tableColumn_4.setWidth(100);
		tableColumn_4.setText("操作");
		tableViewerColumn_6.setLabelProvider(new ColumnLabelProvider()
		{
			@Override
			public void update(ViewerCell cell)
			{
				TableItem item = (TableItem) cell.getItem();
				KinderFeeInfo kFeeInfo = (KinderFeeInfo) item.getData();
				int feeStatus = kFeeInfo.getFeeVoucherStatus();
				Button btnEdit = null;
				Button btnUnCheck = null;
				Button btnInvalid = null;
				if (buttons.containsKey(cell.getElement()))
				{
					List<Button> btnList = buttons.get(cell.getElement());
					for (Button btn : btnList)
					{
						if ("修改".equals(btn.getText()))
						{
							btnEdit = btn;
							continue;
						}
						if ("反审核".equals(btn.getText()))
						{
							btnUnCheck = btn;
							continue;
						}
						if ("作废".equals(btn.getText()))
						{
							btnInvalid = btn;
							continue;
						}
					}
				} else
				{
					btnEdit = new Button((Composite) cell.getViewerRow().getControl(), SWT.NONE);
					btnEdit.setText("修改");
					btnUnCheck = new Button((Composite) cell.getViewerRow().getControl(), SWT.NONE);
					btnUnCheck.setText("反审核");
					btnInvalid = new Button((Composite) cell.getViewerRow().getControl(), SWT.NONE);
					btnInvalid.setText("作废");
					List<Button> btnList = new ArrayList<Button>();
					btnList.add(btnEdit);
					btnList.add(btnUnCheck);
					btnList.add(btnInvalid);
					buttons.put(cell.getElement(), btnList);
				}
				if (!isAdmin)
				{
					btnUnCheck.setEnabled(false);
					if(feeStatus == 102)
					{
						btnEdit.setEnabled(true);
					}else 
					{
						btnEdit.setEnabled(false);
					}
				} else
				{
					btnInvalid.setVisible(false);
					btnInvalid.setEnabled(false);
				}
				final KinderFeeInfo tempObj = (KinderFeeInfo) item.getData();
				if (CommonUtil.UN_CHECKED == tempObj.getFeeVoucherStatus())
				{
					btnUnCheck.setEnabled(false);
				}
				btnEdit.addMouseListener(new OperMouseListener(tempObj));
				btnUnCheck.addMouseListener(new OperMouseListener(tempObj));
				btnInvalid.addMouseListener(new OperMouseListener(tempObj));

				TableEditor editorEdit = new TableEditor(item.getParent());
				editorEdit.grabHorizontal = false;
				editorEdit.minimumWidth = 30;
				editorEdit.grabVertical = true;
				editorEdit.horizontalAlignment = SWT.LEFT;
				editorEdit.setEditor(btnEdit, item, cell.getColumnIndex());
				editorEdit.layout();
				
				TableEditor editorInvalid = new TableEditor(item.getParent());
				editorInvalid.grabHorizontal = false;
				editorInvalid.minimumWidth = 30;
				editorInvalid.grabVertical = true;
				editorInvalid.horizontalAlignment = SWT.CENTER;
				editorInvalid.setEditor(btnInvalid, item, cell.getColumnIndex());
				editorInvalid.layout();
				if(isAdmin)
				{
					TableEditor editorUnCheck = new TableEditor(item.getParent());
					editorUnCheck.grabHorizontal = false;
					editorUnCheck.grabVertical = true;
					editorUnCheck.minimumWidth = 40;
					editorUnCheck.horizontalAlignment = SWT.RIGHT;
					editorUnCheck.setEditor(btnUnCheck, item, cell.getColumnIndex());
					editorUnCheck.layout();
				}
			}

			class OperMouseListener implements MouseListener
			{
				private KinderFeeInfo tempObj;

				public OperMouseListener(KinderFeeInfo tempObj)
				{
					super();
					this.tempObj = tempObj;
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
					if ("修改".equals(button.getText()))
					{
						Control[] controls = parent.getChildren();
						if (controls != null && controls.length > 0)
						{
							controls[0].dispose();
						}
						new KinderRecordGroup(parent, SWT.NONE, userId, tempObj, true);
					} else if ("反审核".equals(button.getText()))
					{
						KinderService kinderService = new KinderService();
						try
						{
							kinderService.updateFeeStatusToUnCheck(tempObj.getKinderFeeInfoId());
							MessageBoxUtil.showWarnMessageBox(getShell(), "反审核成功");
							button.setEnabled(false);
						} catch (Exception ex)
						{
							button.setEnabled(true);
							MessageBoxUtil.showWarnMessageBox(getShell(), "反审核失败");
						}
					} else if ("作废".equals(button.getText()))
					{
						KinderService kinderService = new KinderService();
						try
						{
							kinderService.updateFeeStatusToInValid(tempObj.getKinderFeeInfoId());
							MessageBoxUtil.showWarnMessageBox(getShell(), "作废成功");
							button.setEnabled(false);
						} catch (Exception ex)
						{
							button.setEnabled(true);
							MessageBoxUtil.showWarnMessageBox(getShell(), "作废失败");
						}
					}
				}
			}
		});

		Label label = new Label(composite, SWT.NONE);
		label.setBounds(10, 22, 61, 17);
		label.setText("学号");

		kinderText = new Text(composite, SWT.BORDER);
		kinderText.setBounds(77, 16, 318, 23);

		Button btnQuery = new Button(composite, SWT.NONE);
		btnQuery.setBounds(649, 17, 113, 27);
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
				String kinderIdValue = kinderText.getText();
				KinderService feeService = new KinderService();
				List<KinderFeeInfo> feeHisList = feeService.queryLastedFeeRecord(kinderIdValue, isAdmin);
//				if (feeHisList == null || feeHisList.isEmpty())
//				{
//					// 查询没有数据时，需要把表格中的按钮给disposed掉
//					feeHisList = new ArrayList<KinderFeeInfo>();
					Collection<List<Button>> btnList = buttons.values();
					for (List<Button> btns : btnList)
					{
						for (Button bu : btns)
						{
							bu.dispose();
						}
					}
//				}
				checkboxTableViewer.setInput(feeHisList.toArray(new KinderFeeInfo[0]));
			}
		});

		checkboxTableViewer.setContentProvider(new ArrayContentProvider());
		KinderService feeService = new KinderService();
		List<KinderFeeInfo> feeHisList = feeService.queryLastedFeeRecord(kinderId, isAdmin);
		checkboxTableViewer.setInput(feeHisList.toArray(new KinderFeeInfo[0]));
	}

	public KinderLastFeeGroup(Composite parent, int none, String userId)
	{
		super(parent, none, userId);
		final String roleId = new UserService().queryUserRoleByUserId(userId);
		if (roleId.equals("r_0001"))// 管理员
		{
			isAdmin = true;
		}
	}
}
