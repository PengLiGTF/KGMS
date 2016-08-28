package com.kindergarten.util.print.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class MainClass {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);

		Image image = new Image(display, "lipeng.jpg");
		ImageData imageData = image.getImageData();

		PrintDialog dialog = new PrintDialog(shell, SWT.NULL);
		PrinterData printerData = dialog.open();

		Printer printer = new Printer(printerData);

		Point screenDPI = display.getDPI();
		Point printerDPI = printer.getDPI();
		int scaleFactor = printerDPI.x / screenDPI.x;

		Rectangle trim = printer.computeTrim(0, 0, 0, 0);

		if (printer.startJob("fileName")) {
			if (printer.startPage()) {
				GC gc = new GC(printer);
				Image printerImage = new Image(printer, imageData);

				// Draw the image
				gc.drawImage(printerImage, 0, 0, imageData.width,
						imageData.height, -trim.x, -trim.y, scaleFactor
								* imageData.width, scaleFactor
								* imageData.height);

				// Clean up
				printerImage.dispose();
				gc.dispose();
				printer.endPage();
			}
		}
		// End the job and dispose the printer
		printer.endJob();
		printer.dispose();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}