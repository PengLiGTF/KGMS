package com.kindergarten.groups.kinder;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import net.sf.paperclips.ImagePrint;
import net.sf.paperclips.StyledTextPrint;
import net.sf.paperclips.TextStyle;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.util.Calendar;
import com.kindergarten.data.Kinder;
import com.kindergarten.data.KinderFeeInfo;
import com.kindergarten.data.SexModel;
import com.kindergarten.data.User;
import com.kindergarten.groups.AbstractGroup;
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

public class PreFeeKindersRecordGroup extends AbstractGroup
{
	private Text kinderNameText;
	private Text kinderIdText;
	private Text privilegeFeeText;
	private Text otherFeeText;
	private Text actualFeeText;
	private Text operatorText;
	private DateTime feeDataTime;
	private DateTime operTime;

	private ComboViewer comboViewerSex;
	private Button btnPrint;

	public PreFeeKindersRecordGroup(final Composite parent, int style, final String userId)
	{
		super(parent, style, userId);
		composite.setLocation(10, 20);
		setText("学生预缴费");

		Label label = new Label(composite, SWT.NONE);
		label.setBounds(10, 70, 34, 17);
		label.setText("姓名");

		kinderNameText = new Text(composite, SWT.BORDER);
		kinderNameText.setBounds(77, 67, 147, 23);

		Label label_2 = new Label(composite, SWT.NONE);
		label_2.setBounds(10, 26, 34, 17);
		label_2.setText("学号");

		kinderIdText = new Text(composite, SWT.BORDER);
		kinderIdText.setBounds(77, 23, 403, 23);

		kinderIdText.addModifyListener(new ModifyListener()
		{
			@Override
			public void modifyText(ModifyEvent e)
			{
				String tempId = kinderIdText.getText();
				if (!StringUtils.isBlank(tempId))
				{
					Kinder kinder = new KinderService().getKinderByKinderId(kinderIdText.getText());
					if (kinder != null)
					{
						kinderNameText.setText(kinder.getKinderName());
					}
				}
			}
		});

		Button button = new Button(composite, SWT.NONE);
		button.setBounds(486, 21, 80, 27);
		button.setText("生成学号");
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
				String kinderIdValue = CommonUtil.generateKinderId();
				kinderIdText.setText(kinderIdValue);
			}
		});

		Label label_3 = new Label(composite, SWT.NONE);
		label_3.setBounds(10, 146, 61, 17);
		label_3.setText("优惠额度");

		Label label_4 = new Label(composite, SWT.NONE);
		label_4.setBounds(10, 192, 61, 17);
		label_4.setText("其他费用");

		privilegeFeeText = new Text(composite, SWT.BORDER);
		privilegeFeeText.setBounds(77, 140, 147, 23);

		otherFeeText = new Text(composite, SWT.BORDER);
		otherFeeText.setBounds(77, 186, 147, 23);

		Label label_5 = new Label(composite, SWT.NONE);
		label_5.setBounds(10, 230, 55, 17);
		label_5.setText("预缴费用");

		actualFeeText = new Text(composite, SWT.BORDER);
		actualFeeText.setBounds(77, 227, 378, 23);

		Label label_6 = new Label(composite, SWT.NONE);
		label_6.setBounds(10, 316, 49, 17);
		label_6.setText("操作人");

		operatorText = new Text(composite, SWT.BORDER);
		operatorText.setBounds(77, 310, 147, 23);
		final List<User> userList = new UserService().queryUserByCondition(userId, "");
		operatorText.setText(userList.get(0).getUserName());
		operatorText.setEditable(false);

		Label label_7 = new Label(composite, SWT.NONE);
		label_7.setBounds(293, 316, 61, 17);
		label_7.setText("操作时间");

		operTime = new DateTime(composite, SWT.BORDER);
		operTime.setBounds(365, 309, 201, 24);
		operTime.setEnabled(false);

		Label label_8 = new Label(composite, SWT.NONE);
		label_8.setBounds(10, 270, 55, 17);
		label_8.setText("缴费日期");

		feeDataTime = new DateTime(composite, SWT.BORDER);
		feeDataTime.setBounds(77, 270, 147, 24);
		feeDataTime.setEnabled(false);

		final Button btnSave = new Button(composite, SWT.NONE);
		btnSave.setBounds(379, 396, 80, 27);
		btnSave.setText("确定");
		final String feeType = FeeTypeConstant.PRE_FEE;
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

				String kinderName = kinderNameText.getText();
				if (StringUtils.isBlank(kinderName))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "学生姓名不能为空");
					return;
				}
				String kinderId = kinderIdText.getText();
				if (StringUtils.isBlank(kinderId))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "请生成学生学号");
					return;
				}
				String privilegeMoney = privilegeFeeText.getText();
				if(StringUtils.isBlank(privilegeMoney))
				{
					privilegeMoney = "0.00";
				}
				if (!CommonUtil.isDigital(privilegeMoney))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "优惠金额必须是数字");
					return;
				}
				String otherMoney = otherFeeText.getText();
				if(StringUtils.isBlank(otherMoney))
				{
					otherMoney = "0.00";
				}
				if (!CommonUtil.isDigital(otherMoney))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "其他费用必须是数字");
					return;
				}
				String preFeeMoney = actualFeeText.getText();
				if(StringUtils.isBlank(preFeeMoney))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "请输入预缴费金额");
					return;
				}
				if (!CommonUtil.isDigital(preFeeMoney))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "预缴费用必须是数字");
					return;
				}
				StructuredSelection ss = (StructuredSelection) comboViewerSex.getSelection();
				if (ss.getFirstElement() == null)
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "请为学生选择性别");
					return;
				}
				SexModel sexModel = (SexModel) ss.getFirstElement();
				char sex = sexModel.getCode();
				Kinder kinder = new Kinder();
				kinder.setKinderId(kinderId);
				kinder.setKinderName(kinderName);
				kinder.setKinderSex(sex);
				List<KinderFeeInfo> kinderFeeInfoList = new ArrayList<KinderFeeInfo>();
				KinderFeeInfo kfi = new KinderFeeInfo();
				kfi.setKinderId(kinderId);
				Calendar cal = Calendar.getInstance();
				cal.set(feeDataTime.getYear(), feeDataTime.getMonth(), feeDataTime.getDay());
				kfi.setFeeTime(cal.getTime());
				kfi.setFeeExpireTime(cal.getTime());
				kfi.setPrivilegeMoney(Double.parseDouble(privilegeMoney));
				kfi.setPreFeeMoney(Double.parseDouble(preFeeMoney));
				kfi.setOtherMoney(Double.parseDouble(otherMoney));
				cal.set(operTime.getYear(), operTime.getMonth(), operTime.getDay());
				kfi.setOperTime(cal.getTime());
				kfi.setOperator(userList.get(0));
				kfi.setFeeType(feeType);
				kinderFeeInfoList.add(kfi);
				kinder.setKinderFeeInfoList(kinderFeeInfoList);
				try
				{
					new KinderService().addPerFeeKinder(kinder);
					MessageBoxUtil.showWarnMessageBox(getShell(), "预缴费成功");
					btnSave.setEnabled(false);
					btnPrint.setEnabled(true);
				} catch (SQLException e1)
				{
					e1.printStackTrace();
				}
			}
		});

		Button btnClose = new Button(composite, SWT.NONE);
		btnClose.setBounds(486, 396, 80, 27);
		btnClose.setText("关闭");
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
				new PreFeeKinderListGroup(parent, SWT.NONE, userId);
			}
		});

		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setBounds(10, 113, 34, 17);
		label_1.setText("性别");
		comboViewerSex = new ComboViewer(composite, SWT.NONE);
		Combo combo = comboViewerSex.getCombo();
		combo.setBounds(77, 104, 147, 25);

		btnPrint = new Button(composite, SWT.NONE);
		btnPrint.setBounds(245, 396, 80, 27);
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
				DecimalFormat df = new DecimalFormat("#.00");
				String actualFee = actualFeeText.getText();
				if (StringUtils.isBlank(actualFee))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "请输入预交费用");
					return;
				}
				if (!CommonUtil.isDigital(actualFee))
				{
					MessageBoxUtil.showWarnMessageBox(getShell(), "预交费用必须为数字");
					return;
				}
				Double preFeeMoney = Double.valueOf(actualFee);
				StyledTextPrint doc = new StyledTextPrint();
				TextStyle normal = new TextStyle().font("Arial", 20, SWT.NORMAL);
				TextStyle bold = normal.fontStyle(SWT.BOLD);
				Calendar cal = Calendar.getInstance();
				cal.set(operTime.getYear(), operTime.getMonth(), operTime.getDay());
				String checkId = CommonUtil.generatePreFeeCheckId();
				
				KinderPrinterModel printerModel = new KinderPrinterModel();
				printerModel.setCheckId(checkId);
				printerModel.setKinderId(kinderIdText.getText());
				printerModel.setKinderName(kinderNameText.getText());
//				printerModel.setGradeName(comboViewerGrade.getCombo().getText());
//				printerModel.setClassName(comboViewerClass.getCombo().getText());
//				printerModel.setFeeTemplate(comboViewerFeeTemplate.getCombo().getText());
//				printerModel.setFeeDays(feeDays.getText() + "天");
				String priviMoney = privilegeFeeText.getText();
				priviMoney = (priviMoney == null || "".equals(priviMoney)) ? "0.00" : priviMoney;
				printerModel.setPrivelegeMoney(CommonUtil.formatMoneyInChinese(priviMoney));
				String otherMoney = otherFeeText.getText();
				otherMoney = (otherMoney == null || "".equals(otherMoney)) ? "0.00" : otherMoney;
				printerModel.setOtherMoney(CommonUtil.formatMoneyInChinese(otherMoney));
				printerModel.setPreFeeMoney(CommonUtil.formatMoneyInChinese(preFeeMoney));
//				printerModel.setDeductionPreFeeMoney(deductionPreFeeText.getText());
//				printerModel.setAmountMoney(actualFee.getText());
				printerModel.setOperatorName(operatorText.getText());
				printerModel.setOperDate(cal.getTime());
				KinderPrintTool.printPreFee(printerModel);
				
//				doc.setStyle(normal).append(createSampleImage()).append("                     学生预缴费证明", bold).newline().newline()
//						.append("-------------------------------------------------------------").newline().append("单据号：", bold)
//						.append(checkId, normal.underline()).newline().append("学号：").append(kinderIdText.getText(), normal.underline()).newline().append("姓名：")
//						.append(kinderNameText.getText(), normal.underline()).newline().append("性别：")
//						.append(comboViewerSex.getCombo().getText(), normal.underline()).newline().append("优惠额：")
//						.append(privilegeFeeText.getText(), normal.underline()).newline().append("其他费用(学杂和园备服费用)：")
//						.append(otherFeeText.getText(), normal.underline()).newline().append("预缴费用：")
//						.append(NumberToCN.number2CNMontrayUnit(new BigDecimal(preFeeMoney)), normal.underline()).newline().append("经办人：")
//						.append(operatorText.getText(), normal.underline()).newline().append("经办时间：")
//						.append(CommonUtil.formatDateToString(cal.getTime(), CommonUtil.TIME_FORMAT_PATTERN), normal.underline());
//				new KinderFeePrintDialog(getShell(), doc).open();
			}

//			private ImagePrint createSampleImage()
//			{
//				return new ImagePrint(new ImageData(StudentFeePrintUtil.class.getClassLoader().getResourceAsStream("images/sp.png")), new Point(600, 600));
//			}
		});

		comboViewerSex.setContentProvider(new ComboArrayContentProvider(MyComboType.SEX));
		comboViewerSex.setLabelProvider(new ComboILabelProvider());
		comboViewerSex.setInput(new SexModel[0]);
	}
}
