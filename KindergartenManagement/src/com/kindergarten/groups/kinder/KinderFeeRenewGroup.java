package com.kindergarten.groups.kinder;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.paperclips.ImagePrint;
import net.sf.paperclips.StyledTextPrint;
import net.sf.paperclips.TextStyle;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.util.Calendar;
import com.kindergarten.data.ClassModel;
import com.kindergarten.data.FeeExpireKinder;
import com.kindergarten.data.FeeTemplate;
import com.kindergarten.data.Grade;
import com.kindergarten.data.Kinder;
import com.kindergarten.data.KinderFeeInfo;
import com.kindergarten.data.SexModel;
import com.kindergarten.data.User;
import com.kindergarten.groups.AbstractGroup;
import com.kindergarten.service.ClassManageService;
import com.kindergarten.service.KinderService;
import com.kindergarten.service.UserService;
import com.kindergarten.util.ComboArrayContentProvider;
import com.kindergarten.util.ComboILabelProvider;
import com.kindergarten.util.CommonUtil;
import com.kindergarten.util.FeeTypeConstant;
import com.kindergarten.util.MessageBoxUtil;
import com.kindergarten.util.MyComboType;
import com.kindergarten.util.NumberToCN;
import com.kindergarten.util.print.KinderFeePrintDialog;
import com.kindergarten.util.print.KinderPrintTool;
import com.kindergarten.util.print.KinderPrinterModel;
import com.kindergarten.util.print.StudentFeePrintUtil;

public class KinderFeeRenewGroup extends AbstractGroup
{
	private Text kinderIdText;
	private Text kinderNameText;
	private Text renewDays;
	private DateTime renewExpireTime;
	private DateTime renewStartTime;
	private List<KinderFeeInfo> feeInfoList;
	private DateTime latestFeeStartTime;
	private DateTime latestFeeEndTime;

	private FeeExpireKinder feeExpireKinder;
	private Text previlegeMoneyText;
	private Text otherFeeText;
	private Text actualFee;

	private ComboViewer comboViewerFeeTemplate;
	private ComboViewer comboViewerGrade;
	private ComboViewer comboViewerClass;
	private ComboViewer comboViewerSex;
	private Text operatorUserId;
	private Button btnPrint;

	private DateTime operTime;

	public KinderFeeRenewGroup(Composite parent, int style, String userId, FeeExpireKinder feeExpireKinder)
	{
		this(parent, style, userId);
		this.feeExpireKinder = feeExpireKinder;
		kinderIdText.setText(feeExpireKinder.getKinderId());
		kinderNameText.setText(feeExpireKinder.getKinderName());
		List<User> userList = new UserService().queryUserByCondition(userId, "");
		if (userList != null && userList.size() > 0)
		{
			operatorUserId.setText(userList.get(0).getUserName());
		}
	}

	/**
	 * @wbp.parser.constructor
	 */
	public KinderFeeRenewGroup(final Composite parent, int style, final String userId)
	{
		super(parent, style, userId);
		setText("续费管理");
		composite.setLocation(10, 22);

		Label label = new Label(composite, SWT.NONE);
		label.setBounds(21, 29, 38, 17);
		label.setText("学号");

		kinderIdText = new Text(composite, SWT.BORDER);
		kinderIdText.setBounds(65, 26, 149, 23);
		kinderIdText.addModifyListener(new ModifyListener()
		{
			@Override
			public void modifyText(ModifyEvent e)
			{
				feeInfoList = new KinderService().queryFeeHistory(kinderIdText.getText());
				if (!feeInfoList.isEmpty())
				{
					KinderFeeInfo temp = feeInfoList.get(0);
					kinderNameText.setText(temp.getKinderName());
					Date date = temp.getFeeExpireTime();
					Calendar cl = Calendar.getInstance();
					cl.setTime(date);
					latestFeeEndTime.setDate(cl.get(Calendar.YEAR), cl.get(Calendar.MONTH), cl.get(Calendar.DATE));
					Date startTime = temp.getFeeTime();
					cl.setTime(startTime);
					latestFeeStartTime.setDate(cl.get(Calendar.YEAR), cl.get(Calendar.MONTH), cl.get(Calendar.DATE));

					ClassManageService classMService = new ClassManageService();
					Grade grade = classMService.getGradeByGradeId(temp.getKinderGradeId());
					ClassModel cm = classMService.getClassModelById(temp.getKinderClassId());
					StructuredSelection ssGrade = new StructuredSelection(new Grade[]
					{ grade });
					comboViewerGrade.setSelection(ssGrade);
					StructuredSelection ssClass = new StructuredSelection(new ClassModel[]
					{ cm });
					comboViewerClass.setSelection(ssClass);
					SexModel sexModel = CommonUtil.getSexModelByCode(temp.getSex());
					StructuredSelection sexSelection = new StructuredSelection(new SexModel[]
					{ sexModel });
					comboViewerSex.setSelection(sexSelection);

				} else
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "学号不存在");
					return;
				}
			}
		});

		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setBounds(236, 32, 38, 17);
		label_1.setText("姓名");

		kinderNameText = new Text(composite, SWT.BORDER);
		kinderNameText.setEnabled(false);
		kinderNameText.setEditable(false);
		kinderNameText.setBounds(280, 29, 100, 23);

		Group grpSss = new Group(composite, SWT.NONE);
		grpSss.setText("最近缴费情况");
		grpSss.setBounds(22, 68, 588, 81);

		Label label_2 = new Label(grpSss, SWT.NONE);
		label_2.setBounds(10, 37, 61, 17);
		label_2.setText("缴费开始时间");

		latestFeeStartTime = new DateTime(grpSss, SWT.BORDER);
		latestFeeStartTime.setBounds(88, 30, 88, 24);

		Label label_3 = new Label(grpSss, SWT.NONE);
		label_3.setBounds(313, 37, 61, 17);
		label_3.setText("费用到期时间");

		latestFeeEndTime = new DateTime(grpSss, SWT.BORDER);
		latestFeeEndTime.setBounds(415, 30, 88, 24);

		Label label_4 = new Label(composite, SWT.NONE);
		label_4.setBounds(21, 248, 61, 17);
		label_4.setText("续费天数");

		renewDays = new Text(composite, SWT.BORDER);
		renewDays.setBounds(88, 245, 162, 23);
		renewDays.addModifyListener(new ModifyListener()
		{
			@Override
			public void modifyText(ModifyEvent e)
			{
				String renewValue = renewDays.getText();
				if (StringUtils.isBlank(renewValue))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "续费天数不能为空");
					return;
				} else if (!CommonUtil.isDigital(renewValue))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "续费天数只能为数字");
					return;
				}
				if (StringUtils.isBlank(kinderIdText.getText()))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "请输入学号");
					renewDays.setText("");
					return;
				}
				if (feeInfoList == null || feeInfoList.isEmpty())
				{
					feeInfoList = new KinderService().queryFeeHistory(kinderIdText.getText());
				}
				if (feeInfoList != null && !feeInfoList.isEmpty())
				{
					KinderFeeInfo temp = feeInfoList.get(0);
					Date date = temp.getFeeExpireTime();
					Calendar cl = Calendar.getInstance();
					cl.setTime(date);
					cl.add(Calendar.DATE, Integer.parseInt(renewValue));
					renewExpireTime.setDate(cl.get(Calendar.YEAR), cl.get(Calendar.MONTH), cl.get(Calendar.DATE));
				}
			}
		});

		Label label_5 = new Label(composite, SWT.NONE);
		label_5.setBounds(307, 297, 90, 17);
		label_5.setText("续费后到期日期");

		renewExpireTime = new DateTime(composite, SWT.BORDER);
		renewExpireTime.setEnabled(false);
		renewExpireTime.setBounds(409, 290, 201, 24);

		final Button btnSave = new Button(composite, SWT.NONE);
		btnSave.setBounds(404, 404, 80, 27);
		btnSave.setText("保存");
		final String feeType = FeeTypeConstant.RENEW_FEE;
		btnSave.addMouseListener(new MouseListener()
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
				String kinderIdValue = kinderIdText.getText();
				if (StringUtils.isBlank(kinderIdValue))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "学号不能为空");
					return;
				}

				/* ----------------------------------- */
				if (StringUtils.isBlank(renewDays.getText()))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "缴费天数不能为空");
					return;
				} else if (!CommonUtil.isDigital(renewDays.getText()))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "缴费天数只能是数字");
					return;
				}
				int feeDayValue = Integer.parseInt(renewDays.getText());

				String pMoneyStr = previlegeMoneyText.getText();
				double privilegeMoney = 0.00D;
				if (!StringUtils.isBlank(pMoneyStr))
				{
					if (!CommonUtil.isDigital(pMoneyStr))
					{
						MessageBoxUtil.showWarnMessageBox(getShell(), "优惠金额只能是数字");
						return;
					}
					privilegeMoney = Double.parseDouble(pMoneyStr);
				}

				String actualFeeStr = actualFee.getText();
				if (StringUtils.isBlank(actualFeeStr))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "请计算实收费用");
					return;
				}
				double actualMoney = Double.parseDouble(actualFeeStr.substring(0, actualFeeStr.indexOf("(")));
				String operatorId = userId;
				StructuredSelection sexViewer = (StructuredSelection) comboViewerSex.getSelection();
				Object objSex = sexViewer.getFirstElement();
				if (objSex == null)
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "请为学生设置性别");
					return;
				}
				SexModel sex = (SexModel) objSex;
				String kinderSexValue = String.valueOf(sex.getCode());

				StructuredSelection gradeViewer = (StructuredSelection) comboViewerGrade.getSelection();
				Object objGrade = gradeViewer.getFirstElement();
				if (objGrade == null)
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "请为学生设置年级");
					return;
				}
				Grade grade = (Grade) objGrade;
				int kinderGradeId = grade.getGradeId();

				StructuredSelection classViewer = (StructuredSelection) comboViewerClass.getSelection();
				Object objClass = classViewer.getFirstElement();
				if (objClass == null)
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "请为学生分配班级");
					return;
				}
				ClassModel classModel = (ClassModel) objClass;
				int kinderClassId = classModel.getClassId();

				Calendar cal = Calendar.getInstance();
				cal.set(renewExpireTime.getYear(), renewExpireTime.getMonth(), renewExpireTime.getDay());
				Date feeExpire = cal.getTime();
				cal.set(renewStartTime.getYear(), renewStartTime.getMonth(), renewStartTime.getDay());
				Date feeTime = cal.getTime();

				StructuredSelection feeTemplateViewer = (StructuredSelection) comboViewerFeeTemplate.getSelection();
				Object objFeeTemp = feeTemplateViewer.getFirstElement();
				if (objFeeTemp == null)
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "请选择费用收取标准");
					return;
				}
				FeeTemplate feeTemplate = (FeeTemplate) objFeeTemp;
				int feeTemplateId = feeTemplate.getId();

				cal.set(operTime.getYear(), operTime.getMonth(), operTime.getDay());
				Date operTimeValue = cal.getTime();

				String otherFeeStr = otherFeeText.getText();
				double otherMoney = 0.00D;
				if (!StringUtils.isBlank(otherFeeStr))
				{
					if (!CommonUtil.isDigital(otherFeeStr))
					{
						MessageBoxUtil.showWarnMessageBox(getShell(), "其他费用只能是数字");
						otherFeeText.forceFocus();
						return;
					}
					otherMoney = Double.valueOf(otherFeeStr);
				}
				Kinder kinder = new Kinder();
				kinder.setKinderClassId(kinderClassId);
				kinder.setKinderGradeId(kinderGradeId);
				kinder.setKinderId(kinderIdValue);
				kinder.setKinderName(kinderNameText.getText());
				kinder.setKinderSex(kinderSexValue.charAt(0));
				kinder.setKinderStatusCode(CommonUtil.STATUS_IN);

				List<KinderFeeInfo> feeInfoList = new ArrayList<KinderFeeInfo>();
				KinderFeeInfo feeInfo = new KinderFeeInfo();
				feeInfo.setKinderId(kinderIdValue);
				feeInfo.setFeeDays(feeDayValue);
				feeInfo.setFeeExpireTime(feeExpire);
				feeInfo.setFeeTemplateId(feeTemplateId);
				feeInfo.setFeeTime(feeTime);
				feeInfo.setFeeVoucherStatus(KinderFeeInfo.AUDITED);
				User user = new User();
				user.setUserId(operatorId);
				feeInfo.setOperator(user);
				feeInfo.setOperTime(operTimeValue);
				feeInfo.setOtherMoney(otherMoney);
				feeInfo.setPrivilegeMoney(privilegeMoney);
				feeInfo.setActualMoney(actualMoney);
				feeInfo.setFeeType(feeType);
				feeInfo.setFeeReason("到期续费");
				feeInfoList.add(feeInfo);
				kinder.setKinderFeeInfoList(feeInfoList);
				try
				{
					new KinderService().addKinder(kinder);
					MessageBoxUtil.showWarnMessageBox(getShell(), "学生续费成功");
					btnSave.setEnabled(false);
					btnPrint.setEnabled(true);
				} catch (SQLException e1)
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "学生续费成功");
					e1.printStackTrace();
				}
//				/* ----------------------------------- */
//				Control[] controls = parent.getChildren();
//				if (controls != null && controls.length > 0)
//				{
//					controls[0].dispose();
//				}
//				new ExpireFeeKindersGroup(parent, SWT.NONE, userId);
			}
		});

		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.setBounds(530, 404, 80, 27);
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
					controls[0].dispose();
				}
				new ExpireFeeKindersGroup(parent, SWT.NONE, userId);
			}
		});

		Label label_6 = new Label(composite, SWT.NONE);
		label_6.setBounds(21, 209, 48, 17);
		label_6.setText("缴费标准");

		comboViewerFeeTemplate = new ComboViewer(composite, SWT.NONE);
		Combo comboFeeTemplate = comboViewerFeeTemplate.getCombo();
		comboFeeTemplate.setBounds(92, 206, 162, 25);
		comboViewerFeeTemplate.setContentProvider(new ComboArrayContentProvider(MyComboType.FEE_TEMPLATE));
		comboViewerFeeTemplate.setLabelProvider(new ComboILabelProvider());
		comboViewerFeeTemplate.setInput(new Object[0]);

		Label label_7 = new Label(composite, SWT.NONE);
		label_7.setBounds(337, 209, 48, 17);
		label_7.setText("优惠金额");

		previlegeMoneyText = new Text(composite, SWT.BORDER);
		previlegeMoneyText.setBounds(408, 206, 202, 23);

		Label label_8 = new Label(composite, SWT.NONE);
		label_8.setBounds(337, 251, 53, 17);
		label_8.setText("其他费用");

		otherFeeText = new Text(composite, SWT.BORDER);
		otherFeeText.setBounds(404, 248, 206, 23);

		Label label_9 = new Label(composite, SWT.NONE);
		label_9.setBounds(21, 329, 48, 17);
		label_9.setText("续费金额");

		actualFee = new Text(composite, SWT.BORDER);
		actualFee.setBounds(92, 326, 259, 23);

		Label label_10 = new Label(composite, SWT.NONE);
		label_10.setBounds(31, 167, 38, 17);
		label_10.setText("年级");

		comboViewerGrade = new ComboViewer(composite, SWT.NONE);
		Combo comboGrade = comboViewerGrade.getCombo();
		comboGrade.setBounds(92, 164, 162, 25);
		comboViewerGrade.setContentProvider(new ComboArrayContentProvider(MyComboType.GRADE));
		comboViewerGrade.setLabelProvider(new ComboILabelProvider());
		comboViewerGrade.setInput(new Object[0]);
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

		Label label_11 = new Label(composite, SWT.NONE);
		label_11.setBounds(337, 167, 38, 17);
		label_11.setText("班级");

		comboViewerClass = new ComboViewer(composite, SWT.NONE);
		Combo comboClass = comboViewerClass.getCombo();
		comboClass.setBounds(404, 164, 206, 25);
		comboViewerClass.setContentProvider(new ComboArrayContentProvider(MyComboType.CLASS));
		comboViewerClass.setLabelProvider(new ComboILabelProvider());

		Label label_12 = new Label(composite, SWT.NONE);
		label_12.setBounds(408, 29, 38, 17);
		label_12.setText("性别");

		comboViewerSex = new ComboViewer(composite, SWT.NONE);
		Combo combo = comboViewerSex.getCombo();
		combo.setBounds(452, 27, 158, 25);

		Label label_13 = new Label(composite, SWT.NONE);
		label_13.setBounds(21, 297, 80, 17);
		label_13.setText("续费开始日期");

		renewStartTime = new DateTime(composite, SWT.BORDER);
		renewStartTime.setBounds(107, 293, 147, 24);

		Button button = new Button(composite, SWT.NONE);
		button.setBounds(378, 322, 90, 27);
		button.setText("计算续费金额");
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
				StructuredSelection SelectedFeeTemplate = (StructuredSelection) comboViewerFeeTemplate.getSelection();
				Object obj = SelectedFeeTemplate.getFirstElement();
				if (obj == null)
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "请选择缴费标准");
					return;
				}
				FeeTemplate feeTemplate = (FeeTemplate) SelectedFeeTemplate.getFirstElement();
				double mount = feeTemplate.getFeeAmount();
				String renewDaysStr = renewDays.getText();
				if(StringUtils.isBlank(renewDaysStr))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "请输入续费天数");
					return;
				}
				if(!CommonUtil.isDigital(renewDaysStr))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "续费天数只能是数字");
					return;
				}
				double amount = mount / 30d * (Integer.parseInt(renewDays.getText()));
				double privilegeValue = 0.00D;
				String privelegeMoneyValueStr = previlegeMoneyText.getText();
				if (!StringUtils.isBlank(privelegeMoneyValueStr))
				{
					if (!CommonUtil.isDigital(privelegeMoneyValueStr))
					{
						MessageBoxUtil.showWarnMessageBox(getShell(), "优惠金额必须为数字");
						return;
					}
					privilegeValue = Double.parseDouble(privelegeMoneyValueStr);
				}
				double otherFeeMoney = 0.00D;
				String otherFeeMoneyValueStr = otherFeeText.getText();
				if (!StringUtils.isBlank(otherFeeMoneyValueStr))
				{
					if (!CommonUtil.isDigital(otherFeeMoneyValueStr))
					{
						MessageBoxUtil.showWarnMessageBox(getShell(), "其他费用金额必须为数字");
						return;
					}
					otherFeeMoney = Double.parseDouble(otherFeeMoneyValueStr);
				}

				amount -= privilegeValue;
				amount += otherFeeMoney;
				// if (kinderFeeInfo != null)
				// {
				// amount -= Double.valueOf(kinderFeeInfo.getPreFeeMoney());
				// }
				DecimalFormat df = new DecimalFormat("#.00");
				actualFee.setText(String.valueOf(df.format(amount)) + "(" + NumberToCN.number2CNMontrayUnit(new BigDecimal(amount)) + ")");
			}
		});

		Label label_14 = new Label(composite, SWT.NONE);
		label_14.setBounds(21, 358, 48, 17);
		label_14.setText("操作人");

		operatorUserId = new Text(composite, SWT.BORDER);
		operatorUserId.setBounds(88, 355, 162, 23);

		Label label_15 = new Label(composite, SWT.NONE);
		label_15.setBounds(307, 365, 61, 17);
		label_15.setText("操作时间");

		operTime = new DateTime(composite, SWT.BORDER);
		operTime.setBounds(378, 355, 232, 24);

		btnPrint = new Button(composite, SWT.NONE);
		btnPrint.setBounds(280, 404, 80, 27);
		btnPrint.setText("打印");
		btnPrint.setEnabled(false);
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
//				StyledTextPrint doc = new StyledTextPrint();
//				TextStyle normal = new TextStyle().font("Arial", 20, SWT.NORMAL);
//				TextStyle bold = normal.fontStyle(SWT.BOLD);
				Calendar cal = Calendar.getInstance();
				cal.set(operTime.getYear(), operTime.getMonth(), operTime.getDay());
				String checkId = CommonUtil.generateRenewFeeCheckId();
				KinderPrinterModel printerModel = new KinderPrinterModel();
				printerModel.setCheckId(checkId);
				printerModel.setKinderId(kinderIdText.getText());
				printerModel.setKinderName(kinderNameText.getText());
				printerModel.setGradeName(comboViewerGrade.getCombo().getText());
				printerModel.setClassName(comboViewerClass.getCombo().getText());
				printerModel.setFeeTemplate(comboViewerFeeTemplate.getCombo().getText());
				printerModel.setFeeDays(renewDays.getText() + "天");
				printerModel.setPrivelegeMoney(previlegeMoneyText.getText());
				printerModel.setOtherMoney(otherFeeText.getText());
				//printerModel.setPreFeeMoney("");
//				printerModel.setDeductionPreFeeMoney(deductionPreFeeText.getText());
				printerModel.setAmountMoney(actualFee.getText());
				printerModel.setOperatorName(operatorUserId.getText());
				printerModel.setOperDate(cal.getTime());
				KinderPrintTool.print(printerModel);
//				
//				doc.setStyle(normal).append(createSampleImage()).append("                       学生续费证明", bold).newline().newline()
//						.append("-------------------------------------------------------------").newline().append("单据号：", bold)
//						.append(checkId, normal.underline()).newline().append("学号：").append(kinderIdText.getText(), normal.underline()).newline().append("姓名：")
//						.append(kinderNameText.getText(), normal.underline()).newline().append("年级：")
//						.append(comboViewerGrade.getCombo().getText(), normal.underline()).newline().append("班级：")
//						.append(comboViewerClass.getCombo().getText(), normal.underline()).newline().append("收费标准：")
//						.append(comboViewerFeeTemplate.getCombo().getText(), normal.underline()).newline().append("收费时间段：")
//						.append(renewDays.getText() + "/天", normal.underline()).newline().append("优惠额：")
//						.append(previlegeMoneyText.getText(), normal.underline()).newline().append("其他费用(学杂和园备服费用)：")
//						.append(otherFeeText.getText(), normal.underline()).newline().append("实收金额：").append(actualFee.getText(), normal.underline()).newline()
//						.append("经办人：").append(operatorUserId.getText(), normal.underline()).newline().append("经办时间：")
//						.append(CommonUtil.formatDateToString(cal.getTime(), CommonUtil.TIME_FORMAT_PATTERN), normal.underline());
//				new KinderFeePrintDialog(getShell(), doc).open();
			}

			private ImagePrint createSampleImage()
			{
				return new ImagePrint(new ImageData(StudentFeePrintUtil.class.getClassLoader().getResourceAsStream("images/sp.png")), new Point(600, 600));
			}
		});

		comboViewerSex.setContentProvider(new ComboArrayContentProvider(MyComboType.SEX));
		comboViewerSex.setLabelProvider(new ComboILabelProvider());
		comboViewerSex.setInput(new Object[0]);
	}
}
