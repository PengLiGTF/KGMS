package com.kindergarten.util.print.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SimplePrint {
	Display display = new Display();
	Shell shell = new Shell(display);
	
	
	public SimplePrint() {
		// shell.pack();
		// shell.open();
		PrintDialog dialog = new PrintDialog(shell, SWT.NULL);
		PrinterData printerData = dialog.open();
		if (printerData != null) {
			Printer printer = new Printer(printerData);
			if (printer.startJob("Text")) {
				GC gc = new GC(printer);
				if (printer.startPage()) {
					gc.drawString("Eclipse", 200, 200);
					printer.endPage();
				}
				gc.dispose();
				printer.endJob();
			}
			printer.dispose();
			System.out.println("Print job done.");
		}
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	public static void main(String[] args) {
		new SimplePrint();
	}
}