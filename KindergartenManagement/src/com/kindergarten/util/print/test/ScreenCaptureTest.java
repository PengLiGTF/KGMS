package com.kindergarten.util.print.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class ScreenCaptureTest {
	public static void main(String[] args) {
		String[] names = { "Harry", "Sally", "Jhon", "Tim", "Scott" };
		String[] msg = { "Hello World!", "Today is a nice day to walk",
				"I am bored", "This is how you take widget capture",
				"Ha Ha Ha!! That's funny !" };

		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Screen Capture");
		shell.setLayout(new GridLayout(1, true));

		final Composite composite = new Composite(shell, SWT.BORDER);
		composite.setLayout(new GridLayout(2, true));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		/*
		 * Create Table Widget
		 */

		Table table = new Table(composite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		TableColumn columnName = new TableColumn(table, SWT.LEFT);
		columnName.setText("Name");
		columnName.setWidth(100);

		TableColumn columnMsg = new TableColumn(table, SWT.LEFT);
		columnMsg.setText("Message");
		columnMsg.setWidth(200);
		for (int i = 0; i < 5; i++) {
			TableItem item = new TableItem(table, SWT.NONE, 0);
			item.setText(0, names[i]);
			item.setText(1, msg[i]);
		}
		/*
		 * Create StyledText Widget
		 */

		StyledText text = new StyledText(composite, SWT.BORDER);
		text.setText("0123456789 ABCDEFGHIJKLM NOPQRSTUVWXYZ");
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		StyleRange style1 = new StyleRange();
		style1.start = 0;
		style1.length = 10;
		style1.fontStyle = SWT.BOLD;
		text.setStyleRange(style1);

		StyleRange style2 = new StyleRange();
		style2.start = 11;
		style2.length = 13;
		style2.foreground = display.getSystemColor(SWT.COLOR_RED);
		text.setStyleRange(style2);

		StyleRange style3 = new StyleRange();
		style3.start = 25;
		style3.length = 13;
		style3.background = display.getSystemColor(SWT.COLOR_BLUE);
		text.setStyleRange(style3);

		/*
		 * Create Button
		 */

		Button button = new Button(shell, SWT.PUSH);
		button.setText("Capture");
		button.pack();

		button.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				/*
				 * Widget capturing logic
				 */
				GC gc = new GC(display);
				final Image image = new Image(display, shell.getBounds());
				gc.copyArea(image, shell.getBounds().x, shell.getBounds().y);
				gc.dispose();

				Shell popup = new Shell(shell);
				popup.setText("Captured Image");
				popup.addListener(SWT.Close, new Listener() {
					public void handleEvent(Event e) {
						image.dispose();
					}
				});

				Canvas canvas = new Canvas(popup, SWT.NONE);
				canvas.setBounds(0, 0, image.getImageData().width,
						image.getImageData().height);
				canvas.addPaintListener(new PaintListener() {
					public void paintControl(PaintEvent e) {
						e.gc.drawImage(image, 0, 0);
					}
				});
				popup.pack();
				popup.open();
			}
		});

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();
	}
}