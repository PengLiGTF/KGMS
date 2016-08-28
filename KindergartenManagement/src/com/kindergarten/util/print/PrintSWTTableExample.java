package com.kindergarten.util.print;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * This example shows how to print data from a KTableModel. More information
 * about this can be found in the text that is produced by KPrintExample.java.
 * 
 * @author Friederich Kupzog
 * 
 */
public class PrintSWTTableExample
{
	private Display display;

	public PrintSWTTableExample()
	{
		// create some gui with a SWT table
		display = new Display();
		final Shell shell = new Shell(display);
		final org.eclipse.swt.layout.GridLayout gridLayout = new org.eclipse.swt.layout.GridLayout();
		gridLayout.numColumns = 2;
		shell.setLayout(gridLayout);
		final Color red = display.getSystemColor(SWT.COLOR_RED);

		final Table table_swt = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		final GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
		table_swt.setLayoutData(gridData);
		table_swt.setLinesVisible(true);
		TableColumn column1 = new TableColumn(table_swt, SWT.NONE);
		TableColumn column2 = new TableColumn(table_swt, SWT.NONE);
		TableColumn column3 = new TableColumn(table_swt, SWT.NONE);
		column1.setText("Column1");
		column2.setText("Column2");
		column3.setText("Column3");

		for (int i = 0; i < 100; i++)
		{
			TableItem item = new TableItem(table_swt, SWT.NONE);
			item.setText(new String[]
			{ "cell " + i + " 0", "cell " + i + " 1", "cell " + i + "2" });
		}
		column1.pack();
		column2.pack();
		column3.pack();

		final Button butPrint = new Button(shell, SWT.NONE);
		butPrint.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				// create a document with default settings from PageSetup
				final PDocument doc = new PDocument("SWT Table Printing Example");
				// put some header text on it
				PTextBox t;
				t = new PTextBox(doc);
				t.setText("SWT Table Printing Example");
				new PVSpace(doc, 0.1);
				new PHLine(doc, 0.02, SWT.COLOR_BLACK);
				new PVSpace(doc, 0.5);
				// create the table
				SWTPTable table = new SWTPTable(doc);
				table.setTable(table_swt);
				table.setBoxProvider(new PTableBoxProvider());
				PrintPreview pr = new PrintPreview(null, "Test", IconSource.getImage("print"), doc);
				pr.open();
			}
		});
		butPrint.setText("打印预览");

		final Button butClose = new Button(shell, SWT.NONE);
		butClose.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				shell.dispose();
			}
		});
		butClose.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		butClose.setText("Close");

		// Show the shell
		shell.setSize(494, 478);
		shell.setText("SWT Table Printing Example");
		shell.setVisible(true);

		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	/**
	 * This function would print the document witout the print preview.
	 * 
	 * @param doc
	 */
	public void print(PDocument doc)
	{
		PrintDialog dialog = new PrintDialog(null, SWT.BORDER);
		PrinterData data = dialog.open();
		if (data == null)
			return;
		if (data.printToFile)
		{
			data.fileName = "print.out"; // you probably want to ask the user
			// for a filename
		}

		Printer printer = new Printer(data);
		GC gc = new GC(printer);
		PBox.setParameters(gc, printer, printer.getDPI(), 100);
		if (printer.startJob("DoSys Druckauftrag"))
		{
			printer.startPage();
			doc.layout();
			doc.draw(1);
			printer.endJob();
		}
		gc.dispose();
	}

	public static void main(String[] args)
	{
		new PrintSWTTableExample();
	}
}
