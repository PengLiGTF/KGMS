package com.kindergarten.util.print.test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PrintTest implements Printable
{
	private String roomID;
	private Date printDate;
	private Integer rentType;
	private Float waterFee;
	private Float electricityFee;
	private Float rent;
	private String remark;
	private static int RECEIPT_NO = 0;
	// public PrintTest()
	// {
	//
	// }

	public PrintTest printTest;

	public PrintTest()
	{
		this.roomID = "ffffff" + "房间";
		this.printDate = new Date();
		this.rentType = 111;// /prentRecord.getRentType();
		this.waterFee = 1.1f;// prentDetail.getWaterBill();
		this.electricityFee = 2.2f;// prentDetail.getElectricityBill();
		this.rent = 333.3f;// prentDetail.getRent();
		this.remark = "dfee";// prentDetail.getRemark();
	}

	/** */
	/**
	 * @param Graphic指明打印的图形环境
	 * @param PageFormat指明打印页格式
	 *            （页面大小以点为计量单位，1点为1英才的1/72，1英寸为25.4毫米。A4纸大致为595×842点）
	 * @param pageIndex指明页号
	 **/
	public int print(Graphics graphics, PageFormat pf, int pageIndex) throws PrinterException
	{
		String receiptTitle = "收款收据";
		String checkId = "1000000001";
		// 转换成Graphics2D
		Graphics2D graphics2D = (Graphics2D) graphics;
		// 设置打印颜色为红色
		graphics2D.setColor(Color.RED);
		// 打印起点坐标x=10
		double xPoint = pf.getImageableX();
		double yPoint = pf.getImageableY();
		System.out.println("打印起点坐标值是{" + xPoint + "," + yPoint + "}。");
		Font font = new Font("新宋体", Font.TRUETYPE_FONT, 6);
		graphics2D.setFont(font);// 设置字体
		BasicStroke basicStroke = new BasicStroke(0.5f);
		graphics2D.setStroke(basicStroke);// 设置线宽
		float height = font.getSize2D();// 字体高度
		System.out.println("字体高度是" + height);
		// 绘制收据标题
		graphics2D.drawString(receiptTitle, 120, (float) yPoint + 1 * height);
		// 绘制收据编号
		graphics2D.drawString("No.:" + checkId, (float) xPoint * 21, (float) yPoint + 1 * height);
		// 绘制打印时间
		SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		graphics2D.drawString(myDateFormat.format(printDate), (float) xPoint * 21, (float) yPoint + 2 * height);
		// 绘制收据标题下面画二条横线
		graphics2D.drawLine(115, (int) (yPoint + 1 * height + 5), 160, (int) (yPoint + 1 * height + 5));
		graphics2D.drawLine(115, (int) (yPoint + 1 * height + 7), 160, (int) (yPoint + 1 * height + 7));

		float rowY = (float) yPoint + 4 * height;
		float rowX1 = (float) xPoint * 2;// label
		float rowX2 = (float) xPoint * 5;// text
		float rowX3 = (float) xPoint * 15;// label
		float rowX4 = (float) xPoint * 18;// text
		int lineY = (int) (rowY + 2);
		int lineX1 = (int) rowX2;
		int lineX2 = (int) rowX3;
		int lineX3 = (int) rowX4;
		int lineX4 = (int) xPoint + 260;
		// 正文1—学号 姓名
		graphics2D.drawString("学号", rowX1, rowY);
		graphics2D.drawString("20160821000001", rowX2, rowY);
		graphics2D.drawLine(lineX1, lineY, lineX2, lineY);
		graphics2D.drawString("姓名", rowX3, rowY);
		graphics2D.drawString("20160821000001", rowX4, rowY);
		graphics2D.drawLine(lineX3, lineY, lineX4, lineY);
		// 班级 年级
		rowY = rowY + 1.5f * height;
		lineY = (int) (rowY + 2);
		graphics2D.drawString("年级", rowX1, rowY);
		graphics2D.drawString("20160821000001", rowX2, rowY);
		graphics2D.drawLine(lineX1, lineY, lineX2, lineY);
		graphics2D.drawString("班级", rowX3, rowY);
		graphics2D.drawString("20160821000001", rowX4, rowY);
		graphics2D.drawLine(lineX3, lineY, lineX4, lineY);
		// 缴费标准
		rowY = rowY + 1.5f * height;
		lineY = (int) (rowY + 2);
		graphics2D.drawString("缴费标准", rowX1, rowY);
		graphics2D.drawString("20160821000001", rowX2, rowY);
		graphics2D.drawLine(lineX1, lineY, lineX2, lineY);
		graphics2D.drawString("缴费天数", rowX3, rowY);
		graphics2D.drawString("20160821000001", rowX4, rowY);
		graphics2D.drawLine(lineX3, lineY, lineX4, lineY);
		// 优惠额度
		rowY = rowY + 1.5f * height;
		lineY = (int) (rowY + 2);
		graphics2D.drawString("优惠金额", rowX1, rowY);
		graphics2D.drawString("20160821000001", rowX2, rowY);
		graphics2D.drawLine(lineX1, lineY, lineX2, lineY);
		graphics2D.drawString("其他费用", rowX3, rowY);
		graphics2D.drawString("20160821000001", rowX4, rowY);
		graphics2D.drawLine(lineX3, lineY, lineX4, lineY);
		// 抵扣预交费用
		rowY = rowY + 1.5f * height;
		lineY = (int) (rowY + 2);
		graphics2D.drawString("抵扣预缴费用", rowX1, rowY);
		graphics2D.drawString("20160821000001", rowX2 + 20, rowY);
		graphics2D.drawLine(lineX1, lineY, lineX2, lineY);
		// 抵扣预交费用
		rowY = rowY + 1.5f * height;
		lineY = (int) (rowY + 2);
		graphics2D.drawString("实收金额", rowX1, rowY);
		graphics2D.drawString("20160821000001", rowX2, rowY);
		graphics2D.drawLine(lineX1, lineY, lineX4, lineY);

		// 签字(收款人)
		rowY = rowY + 4f * height;
		graphics2D.drawString("签字(收款人)", (float) xPoint * 17 + 3, rowY);
		graphics2D.drawString("小明", (float) xPoint * 23, rowY);
		// 签字（收款人)下划线
		rowY += 2;
		graphics2D.drawLine((int) xPoint * 20, (int) rowY, (int) xPoint + 260, (int) rowY);
		return 0;
	}

	public int print2(Graphics graphics, PageFormat pf, int pageIndex) throws PrinterException
	{
		// print string
		String receiptTitle = "收款收据";
		String content1 = "今收到";
		String feeItem = null;
		Float rentTotal = 0f;
		String content2 = "总计人民币(大写)";
		// 收款的内容（根据租赁类型，只有"房租","房租，水费，电费"两种值).

		switch (this.rentType)
		{
		case 1:
			feeItem = "房租，押金";
			rentTotal = this.rent;
			break;
		case 2:
			feeItem = "房租，押金，水费，电费";
			rentTotal = this.rent + this.electricityFee + this.waterFee;
			break;
		default:
			feeItem = "未知收费项目";
		}
		// 转换成Graphics2D
		Graphics2D graphics2D = (Graphics2D) graphics;
		// 设置打印颜色为黑色
		graphics2D.setColor(Color.RED);

		// 打印起点坐标x=10
		double x = pf.getImageableX();
		double y = pf.getImageableY();
		System.out.println("打印起点坐标值是{" + x + "," + y + "}。");
		switch (pageIndex)
		{
		case 0:
			// 设置打印字体（字体名称、样式和点大小）（字体名称可以是物理或者逻辑名称）
			// Java平台所定义的五种字体系列：Serif、SansSerif、Monospaced、Dialog 和 DialogInput
			Font font = new Font("新宋体", Font.TRUETYPE_FONT, 9);
			graphics2D.setFont(font);// 设置字体
			BasicStroke basicStroke = new BasicStroke(0.5f);
			float[] dash1 =
			{ 2.0f };
			// 设置打印线的属性。
			// 1.线宽; 2.;3.;4空白的宽度;5.虚线的宽度;6.偏移量
			// graphics2D.setStroke(new BasicStroke(0.5f,
			// BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
			// 2.0f, dash1, 0.0f));
			// graphics2D.setStroke(new BasicStroke(0.5f));
			graphics2D.setStroke(basicStroke);// 设置线宽
			float height = font.getSize2D();// 字体高度
			System.out.println("字体高度是" + height);
			// 绘制收据标题
			graphics2D.drawString(receiptTitle, 120, (float) y + 1 * height);
			// 绘制收据编号
			String receiptNo = String.valueOf(RECEIPT_NO);
			graphics2D.drawString("No.:" + "11111111111111111", (float) x * 21, (float) y + 1 * height);
			// 绘制打印时间
			SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
			graphics2D.drawString(myDateFormat.format(printDate), (float) x * 21, (float) y + 2 * height);

			// 绘制收据标题下面的第一条横线
			graphics2D.drawLine(115, (int) (y + 1 * height + 5), 160, (int) (y + 1 * height + 5));
			// 绘制收据标题下面的第二条横线
			graphics2D.drawLine(115, (int) (y + 1 * height + 7), 160, (int) (y + 1 * height + 7));
			// 正文1——今收到
			graphics2D.drawString(content1, (float) x * 2, (float) y + 4 * height);
			// 收费项目名称
			graphics2D.drawString(this.roomID + feeItem, (float) x * 5, (float) y + 4 * height);
			// 绘制正文1下划线
			graphics2D.drawLine((int) x * 5, (int) (y + 4 * height + 4), (int) x + 260, (int) (y + 4 * height + 4));
			// 人民币(大写)
			graphics2D.drawString(content2 + "ddddd", (float) x * 2, (float) y + 6 * height);
			// 绘制人民币(大写)下划线
			graphics2D.drawLine((int) x * 12, (int) (y + 6 * height + 4), (int) x + 200, (int) (y + 6 * height + 4));
			// (小写)
			graphics2D.drawString("(小写)" + rentTotal + "元", (float) x * 20, (float) y + 6 * height);
			// 绘制(小写)下划线
			graphics2D.drawLine((int) x * 23, (int) (y + 6 * height + 4), (int) x + 260, (int) (y + 6 * height + 4));

			// 附注
			graphics2D.drawString("附注:" + this.remark, (float) x * 2, (float) y + 8 * height);
			// 绘制附注下划线
			graphics2D.drawLine((int) x * 4 + 4, (int) (y + 8 * height + 4), (int) x + 260, (int) (y + 8 * height + 4));

			// 签字(收款人)
			graphics2D.drawString("签字(收款人)", (float) x * 17 + 3, (float) y + 11 * height);
			// 签字（收款人)下划线
			graphics2D.drawLine((int) x * 23, (int) (y + 11 * height + 2), (int) x + 260, (int) (y + 11 * height + 2));

			return PAGE_EXISTS;

		default:
			RECEIPT_NO = RECEIPT_NO - 1;
			return NO_SUCH_PAGE;
		}

	}

	// 方法调用
	public void printReceipt22(PrintTest printTest)
	{
		// 通俗理解就是书、文档
		Book book = new Book();
		// 设置成竖打
		PageFormat pf = new PageFormat();
		pf.setOrientation(PageFormat.PORTRAIT);
		// 通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。
		Paper p = new Paper();
		p.setSize(280, 120);// 纸张大小
		p.setImageableArea(10, 10, 280, 120);// A4(595 X
												// 842)设置打印区域，其实0，0应该是72，72，因为A4纸的默认X,Y边距是72
		pf.setPaper(p);
		// 把 PageFormat 和 Printable 添加到书中，组成一个页面
		book.append(printTest, pf);
		// 获取打印服务对象
		PrinterJob job = PrinterJob.getPrinterJob();
		// 设置打印类
		job.setPageable(book);

		try
		{
			// 可以用printDialog显示打印对话框，在用户确认后打印；也可以直接打印
			// boolean isShowing=job.printDialog();
			// if(isShowing)
			// {
			// System.out.println(this.printDate+"*************");
			RECEIPT_NO = RECEIPT_NO + 1;
			job.print();
			// }
		} catch (PrinterException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		// 通俗理解就是书、文档
		Book book = new Book();
		// 设置成竖打
		PageFormat pf = new PageFormat();
		pf.setOrientation(PageFormat.PORTRAIT);
		// 通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。
		Paper p = new Paper();
		p.setSize(280, 120);// 纸张大小
		// A4(595 X 842)设置打印区域，其实0，0应该是72，72，因为A4纸的默认X,Y边距是72
		p.setImageableArea(10, 10, 280, 120);
		pf.setPaper(p);
		// 把 PageFormat 和 Printable 添加到书中，组成一个页面
		book.append(new PrintTest(), pf);
		// 获取打印服务对象
		PrinterJob job = PrinterJob.getPrinterJob();
		// 设置打印类
		job.setPageable(book);
		try
		{
			// 可以用printDialog显示打印对话框，在用户确认后打印；也可以直接打印
			boolean a = job.printDialog();
			if (a)
			{
				job.print();
			}
		} catch (PrinterException e)
		{
			e.printStackTrace();
		}
	}

}