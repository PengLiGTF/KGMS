package com.kindergarten.groups.kinder;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.kindergarten.data.ClassModel;
import com.kindergarten.data.FeeTemplate;
import com.kindergarten.data.Grade;
import com.kindergarten.data.Kinder;
import com.kindergarten.data.KinderFeeInfo;
import com.kindergarten.data.KinderStatus;
import com.kindergarten.data.SexModel;
import com.kindergarten.data.User;
import com.kindergarten.groups.AbstractGroup;
import com.kindergarten.groups.IndexGroup;
import com.kindergarten.service.ClassManageService;
import com.kindergarten.service.FeeTemplateService;
import com.kindergarten.service.KinderService;
import com.kindergarten.service.UserService;
import com.kindergarten.util.ButtonNameConstant;
import com.kindergarten.util.ComboArrayContentProvider;
import com.kindergarten.util.ComboILabelProvider;
import com.kindergarten.util.CommonUtil;
import com.kindergarten.util.FeeTypeConstant;
import com.kindergarten.util.MessageBoxUtil;
import com.kindergarten.util.MyComboType;
import com.kindergarten.util.NumberToCN;
import com.kindergarten.util.print.KinderPrintTool;
import com.kindergarten.util.print.KinderPrinterModel;

/**
 * 入园学生管理
 * */
public class KinderRecordGroup extends AbstractGroup
{
	private Text kinderName;
	private Text feeDays;
	private Text privilegeAmount;
	private Text actualFee;
	private Text operatorUserId;
	private final Text kinderId;
	private final ComboViewer comboViewerSex;
	private final ComboViewer comboViewerGrade;
	private final ComboViewer comboViewerClass;
	private final DateTime feeExpireTime;
	private final DateTime feeStartTime;
	private final ComboViewer comboViewerStatus;
	private final ComboViewer comboViewerFeeTemplate;
	private final DateTime operTime;

	private KinderFeeInfo kinderFeeInfo;
	private Button btnGenerateId;
	private Text preFeeText;
	private Label labelPreFee;
	private Text otherFeeText;
	private boolean isEdit = false;

	private Button btnPrint;
	private Text deductionPreFeeText;

	/**
	 * 修改学生缴费信息
	 * */
	public KinderRecordGroup(Composite parent, int style, final String userId, KinderFeeInfo kinderFeeInfo, boolean isEdit)
	{
		this(parent, style, userId, kinderFeeInfo);
		this.isEdit = isEdit;
		StructuredSelection seleced = new StructuredSelection(new KinderStatus[]
		{ CommonUtil.getStatusByCode(kinderFeeInfo.getKinderStatus()) });
		comboViewerStatus.setSelection(seleced);

		ClassManageService cmService = new ClassManageService();
		Grade sGrade = cmService.getGradeByGradeId(kinderFeeInfo.getKinderGradeId());
		StructuredSelection selecedGrade = new StructuredSelection(new Grade[]
		{ sGrade });
		comboViewerGrade.setSelection(selecedGrade);
		ClassModel cm = cmService.getClassModelById(kinderFeeInfo.getKinderClassId());
		StructuredSelection selecedClassModel = new StructuredSelection(new ClassModel[]
		{ cm });
		comboViewerClass.setSelection(selecedClassModel);
		FeeTemplate feeTemplate = new FeeTemplateService().getFeeTemplateById(kinderFeeInfo.getFeeTemplateId());
		StructuredSelection selecedFeeTemplate = new StructuredSelection(new FeeTemplate[]
		{ feeTemplate });
		comboViewerFeeTemplate.setSelection(selecedFeeTemplate);
		Date feeStartTimeD = kinderFeeInfo.getFeeTime();
		Date feeExpireTimeD = kinderFeeInfo.getFeeExpireTime();
		Calendar cal = Calendar.getInstance();
		cal.setTime(feeStartTimeD);
		feeStartTime.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
		cal.setTime(feeExpireTimeD);
		feeExpireTime.setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
		feeDays.setText(String.valueOf(kinderFeeInfo.getFeeDays()));
		double actualFeeMoney = kinderFeeInfo.getActualMoney();
		privilegeAmount.setText(String.valueOf(kinderFeeInfo.getPrivilegeMoney()));
		otherFeeText.setText(String.valueOf(kinderFeeInfo.getOtherMoney()));
		DecimalFormat df = new DecimalFormat("#.00");
		preFeeText.setText(String.valueOf(kinderFeeInfo.getPreFeeMoney()));
		deductionPreFeeText.setText(String.valueOf(kinderFeeInfo.getDeductionPreFee()));
		actualFee.setText(String.valueOf(df.format(actualFeeMoney)) + "(" + NumberToCN.number2CNMontrayUnit(new BigDecimal(actualFeeMoney)) + ")");
	}

	/**
	 * 预交费学生办理入学手续
	 * */
	public KinderRecordGroup(Composite parent, int style, final String userId, KinderFeeInfo kinderFeeInfo)
	{
		this(parent, style, userId);
		this.kinderFeeInfo = kinderFeeInfo;
		kinderName.setText(kinderFeeInfo.getKinderName());
		kinderName.setEditable(false);
		kinderId.setText(kinderFeeInfo.getKinderId());
		kinderId.setEditable(false);
		btnGenerateId.setVisible(false);
		StructuredSelection seleced = new StructuredSelection(new SexModel[]
		{ CommonUtil.getSexModelByCode(kinderFeeInfo.getSex()) });
		comboViewerSex.setSelection(seleced);
		labelPreFee.setVisible(!isEdit);
		preFeeText.setVisible(!isEdit);
		preFeeText.setText(String.valueOf(kinderFeeInfo.getPreFeeMoney()));
		//deductionPreFeeText.setEditable(false);
		deductionPreFeeText.setEnabled(true);
		deductionPreFeeText.setText(String.valueOf(kinderFeeInfo.getPreFeeMoney()));
		privilegeAmount.setText(String.valueOf(kinderFeeInfo.getPrivilegeMoney()));
		otherFeeText.setText(String.valueOf(kinderFeeInfo.getOtherMoney()));
	}

	/**
	 * @wbp.parser.constructor
	 */
	public KinderRecordGroup(final Composite parent, int style, final String userId)
	{
		super(parent, style, userId);
		this.setText("学生入园录入");

		Label label = new Label(composite, SWT.NONE);
		label.setBounds(10, 29, 37, 17);
		label.setText("姓名");

		kinderName = new Text(composite, SWT.BORDER);
		kinderName.setBounds(85, 26, 147, 23);

		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setBounds(288, 29, 61, 17);
		label_1.setText("性别");

		comboViewerSex = new ComboViewer(composite, SWT.NONE);
		Combo kinderSex = comboViewerSex.getCombo();
		kinderSex.setBounds(359, 26, 174, 25);
		comboViewerSex.setContentProvider(new ComboArrayContentProvider(MyComboType.SEX));
		comboViewerSex.setLabelProvider(new ComboILabelProvider());
		Label label_2 = new Label(composite, SWT.NONE);
		label_2.setBounds(10, 133, 61, 17);
		label_2.setText("年级");

		comboViewerGrade = new ComboViewer(composite, SWT.NONE);
		Combo kinderGrade = comboViewerGrade.getCombo();
		kinderGrade.setBounds(85, 130, 147, 25);
		comboViewerGrade.setContentProvider(new ComboArrayContentProvider(MyComboType.GRADE));
		comboViewerGrade.setLabelProvider(new ComboILabelProvider());
		comboViewerGrade.setInput(null);

		Label label_3 = new Label(composite, SWT.NONE);
		label_3.setBounds(288, 133, 61, 17);
		label_3.setText("班级");

		comboViewerClass = new ComboViewer(composite, SWT.NONE);
		Combo kinderClass = comboViewerClass.getCombo();
		kinderClass.setBounds(359, 133, 174, 25);
		comboViewerClass.setContentProvider(new ComboArrayContentProvider(MyComboType.CLASS));
		comboViewerClass.setLabelProvider(new ComboILabelProvider());
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
		label_4.setBounds(10, 169, 61, 17);
		label_4.setText("收费标准");

		Label label_7 = new Label(composite, SWT.NONE);
		label_7.setBounds(277, 199, 61, 17);
		label_7.setText("费用到期时间");

		feeExpireTime = new DateTime(composite, SWT.BORDER);
		feeExpireTime.setBounds(359, 192, 174, 24);
		feeExpireTime.setEnabled(false);
		;

		Label label_5 = new Label(composite, SWT.NONE);
		label_5.setBounds(10, 199, 61, 17);
		label_5.setText("入园时间");

		feeStartTime = new DateTime(composite, SWT.BORDER);
		feeStartTime.setBounds(85, 192, 147, 24);
		feeStartTime.addSelectionListener(new SelectionListener()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				setFeeExpireTimeAccordingStartTime(feeExpireTime, feeStartTime);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{

			}
		});

		comboViewerFeeTemplate = new ComboViewer(composite, SWT.NONE);
		Combo feeTemplate = comboViewerFeeTemplate.getCombo();
		feeTemplate.setBounds(85, 161, 147, 25);
		comboViewerFeeTemplate.setContentProvider(new ComboArrayContentProvider(MyComboType.FEE_TEMPLATE));
		comboViewerFeeTemplate.setLabelProvider(new ComboILabelProvider());

		Label label_6 = new Label(composite, SWT.NONE);
		label_6.setBounds(277, 169, 61, 17);
		label_6.setText("收费天数");

		feeDays = new Text(composite, SWT.BORDER);
		feeDays.setBounds(359, 161, 174, 23);
		// 输入缴费天数，自动更新设置缴费到期日期
		feeDays.addModifyListener(new ModifyListener()
		{
			@Override
			public void modifyText(ModifyEvent e)
			{
				setFeeExpireTimeAccordingStartTime(feeExpireTime, feeStartTime);
			}
		});
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBounds(10, 230, 61, 17);
		lblNewLabel.setText("优惠金额");

		privilegeAmount = new Text(composite, SWT.BORDER);
		privilegeAmount.setBounds(85, 227, 147, 23);
		privilegeAmount.addModifyListener(new ModifyListener()
		{
			@Override
			public void modifyText(ModifyEvent e)
			{
				String value = privilegeAmount.getText();
				if (!StringUtils.isBlank(value))
				{
					if (!CommonUtil.isDigital(value))
					{
						MessageBoxUtil.showWarnMessageBox(getShell(), "优惠金额必须输入正确的数字形式");
						return;
					}
				}
			}
		});

		Label label_8 = new Label(composite, SWT.NONE);
		label_8.setBounds(10, 290, 61, 17);
		label_8.setText("实收金额");
		actualFee = new Text(composite, SWT.BORDER);
		actualFee.setBounds(85, 287, 333, 23);
		actualFee.setEditable(false);

		Label label_9 = new Label(composite, SWT.NONE);
		label_9.setBounds(277, 230, 61, 17);
		label_9.setText("其他费用");

		Label label_10 = new Label(composite, SWT.NONE);
		label_10.setBounds(10, 329, 61, 17);
		label_10.setText("经办人");

		operatorUserId = new Text(composite, SWT.BORDER);
		operatorUserId.setBounds(85, 329, 147, 23);
		List<User> userList = new UserService().queryUserByCondition(userId, "");
		operatorUserId.setText(userList.get(0).getUserName());
		operatorUserId.setEditable(false);

		Label label_11 = new Label(composite, SWT.NONE);
		label_11.setBounds(277, 332, 61, 17);
		label_11.setText("经办时间");

		operTime = new DateTime(composite, SWT.BORDER);
		operTime.setBounds(359, 329, 174, 24);

		final Button btnAdd = new Button(composite, SWT.NONE);
		btnAdd.setBounds(217, 382, 80, 27);
		btnAdd.setText("审核");
		btnAdd.setData(ButtonNameConstant.BTN_AUDIT);
		final String feeType = FeeTypeConstant.NORMAL_FEE;
		btnAdd.addMouseListener(new MouseListener()
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
				String kinderNameValue = kinderName.getText();
				if (StringUtils.isBlank(kinderNameValue))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "学生名称不能为空");
					return;
				}

				String kinderIdValue = kinderId.getText();
				if (StringUtils.isBlank(kinderIdValue))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "请为学生分配学号");
					return;
				}
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
				StructuredSelection kinderStatusViewer = (StructuredSelection) comboViewerStatus.getSelection();
				Object kinderStatusObj = kinderStatusViewer.getFirstElement();
				if (kinderStatusObj == null)
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "请设置学生的入园状态");
					return;
				}

				String deductionStr = deductionPreFeeText.getText();
				if (StringUtils.isBlank(deductionStr))
				{
					deductionStr = "0.00";
				}
				if (!CommonUtil.isDigital(deductionStr))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "抵扣预交费必须是数字");
					return;
				}

				ClassModel classModel = (ClassModel) objClass;
				int kinderClassId = classModel.getClassId();

				if (StringUtils.isBlank(feeDays.getText()))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "缴费天数不能为空");
					return;
				} else if (!CommonUtil.isDigital(feeDays.getText()))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "缴费天数只能是数字");
					return;
				}
				int feeDayValue = Integer.parseInt(feeDays.getText());
				if (feeDayValue <= 0 || feeDayValue % 30 != 0)
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "缴费天数只能是30的倍数");
					return;
				}
				String pMoneyStr = privilegeAmount.getText();
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

				Calendar cal = Calendar.getInstance();
				cal.set(feeExpireTime.getYear(), feeExpireTime.getMonth(), feeExpireTime.getDay());
				Date feeExpire = cal.getTime();
				cal.set(feeStartTime.getYear(), feeStartTime.getMonth(), feeStartTime.getDay());
				Date feeTime = cal.getTime();

				KinderStatus kinderStatus = (KinderStatus) kinderStatusObj;
				String kinderStatusCode = kinderStatus.getStatusCode();

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
						MessageBoxUtil.showWarnMessageBox(getShell(), "费用输入请保证是数字形式");
						otherFeeText.forceFocus();
						return;
					}
					otherMoney = Double.valueOf(otherFeeStr);
				}
				Kinder kinder = new Kinder();
				kinder.setKinderClassId(kinderClassId);
				kinder.setKinderGradeId(kinderGradeId);
				kinder.setKinderId(kinderIdValue);
				kinder.setKinderName(kinderNameValue);
				kinder.setKinderSex(kinderSexValue.charAt(0));
				kinder.setKinderStatusCode(kinderStatusCode);

				List<KinderFeeInfo> feeInfoList = new ArrayList<KinderFeeInfo>();
				KinderFeeInfo feeInfo = new KinderFeeInfo();
				feeInfo.setKinderId(kinderIdValue);
				feeInfo.setFeeDays(feeDayValue);
				feeInfo.setFeeExpireTime(feeExpire);
				feeInfo.setFeeTemplateId(feeTemplateId);
				feeInfo.setFeeTime(feeTime);
				feeInfo.setFeeVoucherStatus(CommonUtil.CHECKED);
				feeInfo.setDeductionPreFee(Double.parseDouble(deductionStr));
				User user = new User();
				user.setUserId(operatorId);
				feeInfo.setOperator(user);
				feeInfo.setOperTime(operTimeValue);
				feeInfo.setOtherMoney(otherMoney);
				feeInfo.setPrivilegeMoney(privilegeMoney);
				feeInfo.setActualMoney(actualMoney);
				feeInfo.setFeeType(feeType);
				if (isEdit)
				{
					//设置表主键，好去后台修改对应的记录
					feeInfo.setKinderFeeInfoId(kinderFeeInfo.getKinderFeeInfoId());
				}
				String feeReason = "入园缴费";
				if (kinderFeeInfo != null)
				{
					if (!isEdit)
					{
						feeReason = "预缴费办理入学缴费";
					} else
					{
						feeReason = "缴费修改";
					}
				}

				feeInfo.setFeeReason(feeReason);
				feeInfoList.add(feeInfo);
				kinder.setKinderFeeInfoList(feeInfoList);
				if (isEdit)
				{
					try
					{
						new KinderService().updateKinder(kinder);
						MessageBoxUtil.showWarnMessageBox(getShell(), "修改学生缴费信息成功");
						btnAdd.setEnabled(false);
						btnPrint.setEnabled(true);
					} catch (SQLException e1)
					{
						MessageBoxUtil.showWarnMessageBox(getShell(), "修改学生缴费信息失败");
						e1.printStackTrace();
					}
				} else
				{
					if (kinderFeeInfo != null)
					{
						try
						{
							new KinderService().addKinderWithPreFee(kinder, kinderFeeInfo);
							MessageBoxUtil.showWarnMessageBox(getShell(), "学生录入成功");
							btnAdd.setEnabled(false);
							btnPrint.setEnabled(true);
						} catch (SQLException e1)
						{
							e1.printStackTrace();
						}
					} else
					{
						// 入园学生缴费，且没有过预缴费用的
						try
						{
							new KinderService().addKinder(kinder);
							MessageBoxUtil.showWarnMessageBox(getShell(), "学生录入成功");
							btnAdd.setEnabled(false);
							btnPrint.setEnabled(true);
						} catch (SQLException e1)
						{
							MessageBoxUtil.showWarnMessageBox(getShell(), "学生录入失败");
							e1.printStackTrace();
						}
					}
				}
			}
		});

		btnPrint = new Button(composite, SWT.NONE);
		btnPrint.setBounds(338, 382, 80, 27);
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
				Calendar cal = Calendar.getInstance();
				cal.set(operTime.getYear(), operTime.getMonth(), operTime.getDay());
				String checkId = CommonUtil.generateFeeCheckId();
				KinderPrinterModel printerModel = new KinderPrinterModel();
				printerModel.setCheckId(checkId);
				printerModel.setKinderId(kinderId.getText());
				printerModel.setKinderName(kinderName.getText());
				printerModel.setGradeName(comboViewerGrade.getCombo().getText());
				printerModel.setClassName(comboViewerClass.getCombo().getText());
				printerModel.setFeeTemplate(comboViewerFeeTemplate.getCombo().getText());
				printerModel.setFeeDays(feeDays.getText() + "天");
				String priviMoney = privilegeAmount.getText();
				priviMoney = (priviMoney == null || "".equals(priviMoney)) ? "0.00" : priviMoney;
				printerModel.setPrivelegeMoney(CommonUtil.formatMoneyInChinese(priviMoney));
				String otherMoney = otherFeeText.getText();
				otherMoney = (otherMoney == null || "".equals(otherMoney)) ? "0.00" : otherMoney;
				printerModel.setOtherMoney(CommonUtil.formatMoneyInChinese(otherMoney));
				String preFeeMoney = preFeeText.getText();
				preFeeMoney = (preFeeMoney == null || "".equals(preFeeMoney)) ? "0.00" : preFeeMoney;
				printerModel.setPreFeeMoney(CommonUtil.formatMoneyInChinese(preFeeMoney));
				String deductionMoney = deductionPreFeeText.getText();
				deductionMoney = (deductionMoney == null || "".equals(deductionMoney)) ? "0.00" : deductionMoney;
				printerModel.setDeductionPreFeeMoney(CommonUtil.formatMoneyInChinese(deductionMoney));
				String actualMoney = actualFee.getText();
				actualMoney = (actualMoney == null || "".equals(actualMoney)) ? "0.00" : actualMoney;
				printerModel.setAmountMoney(actualMoney);
				printerModel.setOperatorName(operatorUserId.getText());
				printerModel.setOperDate(cal.getTime());
				KinderPrintTool.print(printerModel);
			}
		});

		Button btnReset = new Button(composite, SWT.NONE);
		btnReset.setBounds(451, 382, 80, 27);
		btnReset.setText("关闭");
		btnReset.addMouseListener(new MouseListener()
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
				if (controls.length > 0)
				{
					controls[0].dispose();
					new IndexGroup(parent, SWT.NONE, userId);
				}
			}
		});

		Label label_12 = new Label(composite, SWT.NONE);
		label_12.setBounds(10, 73, 61, 17);
		label_12.setText("学号");

		kinderId = new Text(composite, SWT.BORDER);
		kinderId.setBounds(85, 69, 333, 23);
		//kinderId.setEditable(false);

		btnGenerateId = new Button(composite, SWT.NONE);
		btnGenerateId.setBounds(453, 69, 80, 27);
		btnGenerateId.setText("生成学号");
		btnGenerateId.addMouseListener(new MouseListener()
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
				String kinderIdValue = CommonUtil.generateKinderId();
				kinderId.setText(kinderIdValue);
			}
		});

		Button btnCompute = new Button(composite, SWT.NONE);
		btnCompute.setBounds(424, 285, 107, 27);
		btnCompute.setText("计算实收金额");
		btnCompute.addMouseListener(new MouseListener()
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
				String feeDaysStr = feeDays.getText();
				if(StringUtils.isBlank(feeDaysStr))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "缴费天数不能为空");
					return;
				}
				if(!CommonUtil.isDigital(feeDaysStr))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "缴费天数必须为正确的数字格式");
					return;
				}
				int day = Integer.parseInt(feeDaysStr);
				if(day <= 0 || day % 30 != 0)
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "缴费天数必须为30的倍数");
					return;
				}
				double amount = mount / 30d * (Integer.parseInt(feeDaysStr));
				double privilegeValue = 0.00D;
				String privelegeMoneyValueStr = privilegeAmount.getText();
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
				DecimalFormat df = new DecimalFormat("#.00");
				if (StringUtils.isBlank(deductionPreFeeText.getText()))
				{
					deductionPreFeeText.setText("0.00");
				} else if (!CommonUtil.isDigital(deductionPreFeeText.getText()))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "费用输入必须为数字");
					return;
				}
				double deductionPreMoney = Double.valueOf(deductionPreFeeText.getText());
				double preFeeMoney = Double.valueOf(preFeeText.getText());
				if(deductionPreMoney > preFeeMoney)
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "抵扣预交费用大于预交费用了,请确认预缴费足够");
					return;
				}
				amount -= deductionPreMoney;
				actualFee.setText(String.valueOf(df.format(amount)) + "(" + NumberToCN.number2CNMontrayUnit(new BigDecimal(amount)) + ")");
			}
		});

		Label label_13 = new Label(composite, SWT.NONE);
		label_13.setBounds(10, 96, 61, 17);
		label_13.setText("学生状态");

		comboViewerStatus = new ComboViewer(composite, SWT.NONE);
		Combo comboStatus = comboViewerStatus.getCombo();
		comboStatus.setBounds(83, 99, 450, 25);

		labelPreFee = new Label(composite, SWT.NONE);
		labelPreFee.setBounds(10, 261, 44, 17);
		labelPreFee.setText("预交费");
		labelPreFee.setVisible(true);

		preFeeText = new Text(composite, SWT.BORDER);
		preFeeText.setText("0.00");// default money
		preFeeText.setEditable(false);
		preFeeText.setEnabled(false);
		preFeeText.setBounds(85, 258, 147, 23);
		preFeeText.setVisible(true);

		otherFeeText = new Text(composite, SWT.BORDER);
		otherFeeText.setBounds(359, 222, 174, 23);

		Label label_14 = new Label(composite, SWT.NONE);
		label_14.setBounds(277, 261, 67, 17);
		label_14.setText("预交费抵扣");

		deductionPreFeeText = new Text(composite, SWT.BORDER);
		deductionPreFeeText.setText("0.00");
		deductionPreFeeText.setEditable(true);
		//deductionPreFeeText.setEnabled(false);
		deductionPreFeeText.setBounds(359, 258, 174, 23);
		comboViewerStatus.setContentProvider(new ComboArrayContentProvider(MyComboType.STATUS));
		comboViewerStatus.setLabelProvider(new ComboILabelProvider());

		// 下来控件数据输入触发
		comboViewerStatus.setInput(new KinderStatus[0]);
		comboViewerSex.setInput(new SexModel[0]);
		comboViewerGrade.setInput(new Grade[0]);
		comboViewerFeeTemplate.setInput(new FeeTemplate[0]);
	}

	private void setFeeExpireTimeAccordingStartTime(final DateTime feeExpireTime, final DateTime feeStartTime)
	{
		String value = feeDays.getText();
		if (StringUtils.isBlank(value))
		{
			// MessageBoxUtil.showWarnMessageBox(getShell(), "缴费天数不能为空");
			return;
		} else if (!CommonUtil.isDigital(value))
		{
			MessageBoxUtil.showWarnMessageBox(getShell(), "缴费天数只能输入数字且不能以0开头");
			return;
		}
		int days = Integer.parseInt(value);
		Calendar calendar = Calendar.getInstance();
		calendar.set(feeStartTime.getYear(), feeStartTime.getMonth(), feeStartTime.getDay());
		int months = days / 30;
		calendar.add(Calendar.MONTH, months);
		feeExpireTime.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
	}
}
