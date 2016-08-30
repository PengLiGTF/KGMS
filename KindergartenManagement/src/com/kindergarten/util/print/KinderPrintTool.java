package com.kindergarten.util.print;

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

public class KinderPrintTool
{
	public static void main(String[] args)
	{
		KinderPrinterModel model = new KinderPrinterModel();
		model.setCheckId("100000000001");
		model.setKinderId("201608001");
		model.setKinderName("小王");
		model.setGradeName("小小班");
		model.setClassName("小一班");
		model.setFeeTemplate("小一班收费模板");
		model.setFeeDays("30");
		model.setPrivelegeMoney("100");
		model.setOtherMoney("100");
		model.setDeductionPreFeeMoney("100");
		model.setAmountMoney("8888888");
		model.setOperDate(new Date());
		model.setOperatorName("小王");
		print(model);
	}

	/**
	 * 打印预缴费清单
	 * */
	public static void printPreFee(KinderPrinterModel model)
	{
		// 通俗理解就是书、文档
		Book book = new Book();
		// 设置成竖打
		PageFormat pf = new PageFormat();
		pf.setOrientation(PageFormat.PORTRAIT);
		// 通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。
		Paper p = new Paper();
		p.setSize(560, 180);// 纸张大小
		// A4(595 X 842)设置打印区域，其实0，0应该是72，72，因为A4纸的默认X,Y边距是72
		p.setImageableArea(10, 10, 560, 180);
		pf.setPaper(p);
		// 把 PageFormat 和 Printable 添加到书中，组成一个页面
		book.append(new PreFeePrinter(model), pf);
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

	public static void print(KinderPrinterModel model)
	{
		// 通俗理解就是书、文档
		Book book = new Book();
		// 设置成竖打
		PageFormat pf = new PageFormat();
		pf.setOrientation(PageFormat.PORTRAIT);
		// 通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。
		Paper p = new Paper();
		p.setSize(560, 180);// 纸张大小
		// A4(595 X 842)设置打印区域，其实0，0应该是72，72，因为A4纸的默认X,Y边距是72
		p.setImageableArea(10, 10, 560, 180);
		pf.setPaper(p);
		// 把 PageFormat 和 Printable 添加到书中，组成一个页面
		book.append(new FeePrinter(model), pf);
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

	static class RenewFeePrinter implements Printable
	{
		@Override
		public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException
		{
			return 0;
		}
	}

	static class PreFeePrinter implements Printable
	{
		private KinderPrinterModel model;

		public PreFeePrinter(KinderPrinterModel model)
		{
			super();
			this.model = model;
		}

		@Override
		public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException
		{
			String receiptTitle = "预缴费收款收据";
			String checkId = model.getCheckId();
			// 转换成Graphics2D
			Graphics2D graphics2D = (Graphics2D) graphics;
			// 设置打印颜色为红色
			graphics2D.setColor(Color.RED);
			// 打印起点坐标x=10
			double xPoint = pageFormat.getImageableX();
			double yPoint = pageFormat.getImageableY();
			System.out.println("打印起点坐标值是{" + xPoint + "," + yPoint + "}。");
			Font font = new Font("新宋体", Font.TRUETYPE_FONT, 10);
			graphics2D.setFont(font);// 设置字体
			BasicStroke basicStroke = new BasicStroke(0.5f);
			graphics2D.setStroke(basicStroke);// 设置线宽
			float height = font.getSize2D();// 字体高度
			System.out.println("字体高度是" + height);
			// 绘制收据标题

			double pageWidth = pageFormat.getWidth();
			double centerIndex = pageWidth / 2;// 中间位置
			double titleStartIndex = centerIndex - 4 * xPoint;// 中间标题x坐标
			double titleEndIndex = centerIndex + 4 * xPoint;// 中间标题x坐标
			double checkNoIndex = centerIndex + centerIndex / 2;

			graphics2D.drawString(receiptTitle, (float) titleStartIndex, (float) yPoint + 1 * height);
			// 绘制收据编号
			graphics2D.drawString("No.:" + checkId, (float) checkNoIndex, (float) yPoint + 1 * height);
			// 绘制打印时间
			SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
			graphics2D.drawString(myDateFormat.format(model.getOperDate()), (float) checkNoIndex, (float) yPoint + 2 * height);
			// 绘制收据标题下面画二条横线
			graphics2D.drawLine((int) titleStartIndex, (int) (yPoint + 1 * height + 5), (int) titleEndIndex, (int) (yPoint + 1 * height + 5));
			graphics2D.drawLine((int) titleStartIndex, (int) (yPoint + 1 * height + 7), (int) titleEndIndex, (int) (yPoint + 1 * height + 7));

			// 边界四个点,从左上顺时针依次为1，2,3,4
			int border1X = (int) (xPoint * 1.5);
			int border1Y = (int) (yPoint + 3 * height);
			int border4X = border1X;
			int border4Y = (int) (border1Y + (int) 11 * height);
			int border2X = (int) (pageWidth - xPoint);
			int border2Y = border1Y;
			int border3X = border2X;
			int border3Y = border4Y;
			graphics2D.drawLine(border1X, border1Y, border2X, border2Y);
			graphics2D.drawLine(border1X, border1Y, border4X, border4Y);
			graphics2D.drawLine(border2X, border2Y, border3X, border3Y);
			graphics2D.drawLine(border4X, border4Y, border3X, border3Y);
			// 结束 画边框
			float rowY = (float) yPoint + 5 * height;
			float rowX1 = (float) xPoint * 3;// label
			float rowX2 = (float) (rowX1 + xPoint * 8);// text
			float rowX3 = (float) centerIndex;// label
			float rowX4 = (float) (rowX3 + 6 * xPoint);// text
			int lineY = (int) (rowY + 2);
			int lineX1 = (int) (rowX2 - xPoint * 3);// 第一行第一条线第一个点
			int lineX2 = (int) rowX3;// 第一行第一条线第二个点
			int lineX3 = (int) (rowX4 - xPoint);// 第一行第二条线第一个点
			int lineX4 = (int) (pageWidth - 2 * xPoint);// 第一行第二条线第二个点
			// 正文1—学号 姓名
			graphics2D.drawString("学号", rowX1, rowY);
			graphics2D.drawString(model.getKinderId(), rowX2, rowY);
			graphics2D.drawLine(lineX1, lineY, lineX2, lineY);
			graphics2D.drawString("姓名", rowX3, rowY);
			graphics2D.drawString(model.getKinderName(), rowX4, rowY);
			graphics2D.drawLine(lineX3, lineY, lineX4, lineY);
			// 班级 年级
			// rowY = rowY + 1.5f * height;
			// lineY = (int) (rowY + 2);
			// graphics2D.drawString("年级", rowX1, rowY);
			// graphics2D.drawString(model.getGradeName(), rowX2, rowY);
			// graphics2D.drawLine(lineX1, lineY, lineX2, lineY);
			// graphics2D.drawString("班级", rowX3, rowY);
			// graphics2D.drawString(model.getClassName(), rowX4, rowY);
			// graphics2D.drawLine(lineX3, lineY, lineX4, lineY);
			// 缴费标准
			// rowY = rowY + 1.5f * height;
			// lineY = (int) (rowY + 2);
			// graphics2D.drawString("缴费标准", rowX1, rowY);
			// graphics2D.drawString(model.getFeeTemplate(), rowX2, rowY);
			// graphics2D.drawLine(lineX1, lineY, lineX2, lineY);
			// graphics2D.drawString("缴费天数", rowX3, rowY);
			// graphics2D.drawString(String.valueOf(model.getFeeDays()), rowX4,
			// rowY);
			// graphics2D.drawLine(lineX3, lineY, lineX4, lineY);
			// 优惠额度
			rowY = rowY + 1.5f * height;
			lineY = (int) (rowY + 2);
			graphics2D.drawString("优惠金额", rowX1, rowY);
			graphics2D.drawString(String.valueOf(model.getPrivelegeMoney()), rowX2, rowY);
			graphics2D.drawLine(lineX1, lineY, lineX2, lineY);
			graphics2D.drawString("其他费用", rowX3, rowY);
			graphics2D.drawString(String.valueOf(model.getOtherMoney()), rowX4, rowY);
			graphics2D.drawLine(lineX3, lineY, lineX4, lineY);
			// 抵扣预交费用
			// rowY = rowY + 1.5f * height;
			// lineY = (int) (rowY + 2);
			// graphics2D.drawString("缴费用", rowX1, rowY);
			// graphics2D.drawString(String.valueOf(model.getPreFeeMoney()),
			// rowX2 + 20, rowY);
			// graphics2D.drawLine(lineX1, lineY, lineX2, lineY);
			// graphics2D.drawString("抵扣预缴费用", rowX3, rowY);
			// graphics2D.drawString(String.valueOf(model.getDeductionPreFeeMoney()),
			// rowX2 + 20, rowY);
			// graphics2D.drawLine(lineX3, lineY, lineX4, lineY);
			// 抵扣预交费用
			rowY = rowY + 1.5f * height;
			lineY = (int) (rowY + 2);
			graphics2D.drawString("实收金额", rowX1, rowY);
			graphics2D.drawString(model.getPreFeeMoney(), rowX2, rowY);
			graphics2D.drawLine(lineX1, lineY, lineX4, lineY);

			// 签字(收款人)
			rowY = rowY + 4f * height;
			graphics2D.drawString("签字(收款人)", (float) centerIndex + 3, rowY);
			graphics2D.drawString(model.getOperatorName(), (float) (centerIndex + xPoint * 13), rowY);
			// 签字（收款人)下划线
			rowY += 2;
			graphics2D.drawLine((int) (centerIndex + 8 * xPoint), (int) rowY, lineX4, (int) rowY);
			return 0;
		}
	}

	// 入园缴费打印
	static class FeePrinter implements Printable
	{
		private KinderPrinterModel model;

		public FeePrinter(KinderPrinterModel model)
		{
			super();
			this.model = model;
		}

		@Override
		public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException
		{
			String receiptTitle = "缴费收款收据";
			String checkId = model.getCheckId();
			// 转换成Graphics2D
			Graphics2D graphics2D = (Graphics2D) graphics;
			// 设置打印颜色为红色
			graphics2D.setColor(Color.RED);
			// 打印起点坐标x=10
			double xPoint = pageFormat.getImageableX();
			double yPoint = pageFormat.getImageableY();
			System.out.println("打印起点坐标值是{" + xPoint + "," + yPoint + "}。");
			Font font = new Font("新宋体", Font.TRUETYPE_FONT, 10);
			graphics2D.setFont(font);// 设置字体
			BasicStroke basicStroke = new BasicStroke(0.5f);
			graphics2D.setStroke(basicStroke);// 设置线宽
			float height = font.getSize2D();// 字体高度
			System.out.println("字体高度是" + height);
			// 绘制收据标题

			double pageWidth = pageFormat.getWidth();
			double centerIndex = pageWidth / 2;// 中间位置
			double titleStartIndex = centerIndex - 4 * xPoint;// 中间标题x坐标
			double titleEndIndex = centerIndex + 4 * xPoint;// 中间标题x坐标
			double checkNoIndex = centerIndex + centerIndex / 2;

			graphics2D.drawString(receiptTitle, (float) titleStartIndex, (float) yPoint + 1 * height);
			// 绘制收据编号
			graphics2D.drawString("No.:" + checkId, (float) checkNoIndex, (float) yPoint + 1 * height);
			// 绘制打印时间
			SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
			graphics2D.drawString(myDateFormat.format(model.getOperDate()), (float) checkNoIndex, (float) yPoint + 2 * height);
			// 绘制收据标题下面画二条横线
			graphics2D.drawLine((int) titleStartIndex, (int) (yPoint + 1 * height + 5), (int) titleEndIndex, (int) (yPoint + 1 * height + 5));
			graphics2D.drawLine((int) titleStartIndex, (int) (yPoint + 1 * height + 7), (int) titleEndIndex, (int) (yPoint + 1 * height + 7));

			// 边界四个点,从左上顺时针依次为1，2,3,4
			int border1X = (int) (xPoint * 1.5);
			int border1Y = (int) (yPoint + 3 * height);
			int border4X = border1X;
			int border4Y = (int) (border1Y + (int) 11 * height);
			int border2X = (int) (pageWidth - xPoint);
			int border2Y = border1Y;
			int border3X = border2X;
			int border3Y = border4Y;
			graphics2D.drawLine(border1X, border1Y, border2X, border2Y);
			graphics2D.drawLine(border1X, border1Y, border4X, border4Y);
			graphics2D.drawLine(border2X, border2Y, border3X, border3Y);
			graphics2D.drawLine(border4X, border4Y, border3X, border3Y);
			// 结束 画边框
			float rowY = (float) yPoint + 5 * height;
			float rowX1 = (float) xPoint * 3;// label
			float rowX2 = (float) (rowX1 + xPoint * 8);// text
			float rowX3 = (float) centerIndex;// label
			float rowX4 = (float) (rowX3 + 6 * xPoint);// text
			int lineY = (int) (rowY + 2);
			int lineX1 = (int) (rowX2 - xPoint * 3);// 第一行第一条线第一个点
			int lineX2 = (int) rowX3;// 第一行第一条线第二个点
			int lineX3 = (int) (rowX4 - xPoint);// 第一行第二条线第一个点
			int lineX4 = (int) (pageWidth - 2 * xPoint);// 第一行第二条线第二个点
			// 正文1—学号 姓名
			graphics2D.drawString("学号", rowX1, rowY);
			graphics2D.drawString(model.getKinderId(), rowX2, rowY);
			graphics2D.drawLine(lineX1, lineY, lineX2, lineY);
			graphics2D.drawString("姓名", rowX3, rowY);
			graphics2D.drawString(model.getKinderName(), rowX4, rowY);
			graphics2D.drawLine(lineX3, lineY, lineX4, lineY);
			// 班级 年级
			rowY = rowY + 1.5f * height;
			lineY = (int) (rowY + 2);
			graphics2D.drawString("年级", rowX1, rowY);
			graphics2D.drawString(model.getGradeName(), rowX2, rowY);
			graphics2D.drawLine(lineX1, lineY, lineX2, lineY);
			graphics2D.drawString("班级", rowX3, rowY);
			graphics2D.drawString(model.getClassName(), rowX4, rowY);
			graphics2D.drawLine(lineX3, lineY, lineX4, lineY);
			// 缴费标准
			rowY = rowY + 1.5f * height;
			lineY = (int) (rowY + 2);
			graphics2D.drawString("缴费标准", rowX1, rowY);
			graphics2D.drawString(model.getFeeTemplate(), rowX2, rowY);
			graphics2D.drawLine(lineX1, lineY, lineX2, lineY);
			graphics2D.drawString("缴费天数", rowX3, rowY);
			graphics2D.drawString(String.valueOf(model.getFeeDays()), rowX4, rowY);
			graphics2D.drawLine(lineX3, lineY, lineX4, lineY);
			// 优惠额度
			rowY = rowY + 1.5f * height;
			lineY = (int) (rowY + 2);
			graphics2D.drawString("优惠金额", rowX1, rowY);
			graphics2D.drawString(String.valueOf(model.getPrivelegeMoney()), rowX2, rowY);
			graphics2D.drawLine(lineX1, lineY, lineX2, lineY);
			graphics2D.drawString("其他费用", rowX3, rowY);
			graphics2D.drawString(String.valueOf(model.getOtherMoney()), rowX4, rowY);
			graphics2D.drawLine(lineX3, lineY, lineX4, lineY);
			// 抵扣预交费用
			rowY = rowY + 1.5f * height;
			lineY = (int) (rowY + 2);
			graphics2D.drawString("预缴费用", rowX1, rowY);
			graphics2D.drawString(String.valueOf(model.getPreFeeMoney()), rowX2 + 20, rowY);
			graphics2D.drawLine(lineX1, lineY, lineX2, lineY);
			graphics2D.drawString("抵扣预缴费用", rowX3, rowY);
			graphics2D.drawString(String.valueOf(model.getDeductionPreFeeMoney()), rowX4 + 20, rowY);
			graphics2D.drawLine(lineX3, lineY, lineX4, lineY);
			// 抵扣预交费用
			rowY = rowY + 1.5f * height;
			lineY = (int) (rowY + 2);
			graphics2D.drawString("实收金额", rowX1, rowY);
			graphics2D.drawString(model.getAmountMoney(), rowX2, rowY);
			graphics2D.drawLine(lineX1, lineY, lineX4, lineY);

			// 签字(收款人)
			rowY = rowY + 4f * height;
			graphics2D.drawString("签字(收款人)", (float) centerIndex + 3, rowY);
			graphics2D.drawString(model.getOperatorName(), (float) (centerIndex + xPoint * 13), rowY);
			// 签字（收款人)下划线
			rowY += 2;
			graphics2D.drawLine((int) (centerIndex + 8 * xPoint), (int) rowY, lineX4, (int) rowY);
			return 0;
		}
	}
}
