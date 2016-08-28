package com.kindergarten.util.print;

import net.sf.paperclips.ImagePrint;
import net.sf.paperclips.PaperClips;
import net.sf.paperclips.Print;
import net.sf.paperclips.PrintJob;
import net.sf.paperclips.StyledTextPrint;
import net.sf.paperclips.TextStyle;
import net.sf.paperclips.ui.PrintPreview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class StudentFeePrintUtil
{

	public void open()
	{
		final Display display = new Display();
		final Shell shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setText("学生缴费打印");
		shell.setLayout(new GridLayout());
		shell.setSize(600, 800);
		final PrintJob job = new PrintJob("学生缴费打印", createPrint());
		Composite buttonPanel = new Composite(shell, SWT.NONE);
		buttonPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		buttonPanel.setLayout(new RowLayout(SWT.HORIZONTAL));
		final PrintPreview preview = new PrintPreview(shell, SWT.BORDER);
		Button print = new Button(buttonPanel, SWT.PUSH);
		print.setText("Print");
		print.setImage(IconSource.getImage("print"));
		print.addListener(SWT.Selection, new Listener()
		{
			public void handleEvent(Event event)
			{
				PrintDialog dialog = new PrintDialog(shell);
				PrinterData printerData = dialog.open();
				if (printerData != null)
					PaperClips.print(job, printerData);
			}
		});

		preview.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		preview.setFitHorizontal(true);
		preview.setFitVertical(true);
		preview.setPrintJob(job);

		shell.open();

		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		display.dispose();
	}

	private Print createPrint()
	{
		StyledTextPrint doc = new StyledTextPrint();
		TextStyle normal = new TextStyle().font("Arial", 20, SWT.NORMAL);
		TextStyle bold = normal.fontStyle(SWT.BOLD);
		doc.setStyle(normal).append(createSampleImage()).append("缴费证明", normal.align(SWT.CENTER)).newline().newline().append("单据号：", bold)
				.append("100000001", normal.underline()).newline().append("学号：").append("20160800000001", normal.underline()).newline().append("姓名：")
				.append("张三", normal.underline()).newline().append("年级：").append("大班", normal.underline()).newline().append("班级：")
				.append("大一班", normal.underline()).newline().append("收费标准：").append("大班标准", normal.underline()).newline().append("收费时间段：")
				.append("20天", normal.underline()).newline().append("优惠额：").append("1000", normal.underline()).newline().append("其他费用(学杂和园备服费用)：")
				.append("100", normal.underline()).newline().append("实收金额：").append("10000", normal.underline()).newline().append("经办人：")
				.append("张三", normal.underline()).newline().append("经办时间：").append("2016-07-01", normal.underline());
		return doc;
	}

	private ImagePrint createSampleImage()
	{
		return new ImagePrint(new ImageData(StudentFeePrintUtil.class.getClassLoader().getResourceAsStream("images/sp.png")), new Point(600, 600));
	}
}
