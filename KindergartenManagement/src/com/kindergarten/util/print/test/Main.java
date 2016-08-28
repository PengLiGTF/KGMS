package com.kindergarten.util.print.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.window.WindowManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.StyledTextPrintOptions;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Main {

	ApplicationWindow applicationWindow;
	WindowManager manager;

	StyledTextPrintOptions styledTextPrintOptions;
	StyledText styledText;

	Display display;
	Shell shell;
	Text text;
	Font font;
	Color foregroundColor, backgroundColor;

	Printer printer;
	GC gc;
	Font printerFont;
	Color printerForegroundColor, printerBackgroundColor;
	int lineHeight = 0;
	int tabWidth = 0;
	int leftMargin, rightMargin, topMargin, bottomMargin;
	int x, y;
	int index, end;
	String textToPrint;
	String tabs;
	StringBuffer wordBuffer;

	public static void main(String[] args) {
		new Main().open();
	}

	void open() {
		display = new Display();
		font = new Font(display, "Courier", 10, SWT.NORMAL);
		foregroundColor = display.getSystemColor(SWT.COLOR_BLACK);
		backgroundColor = display.getSystemColor(SWT.COLOR_WHITE);
		shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setText("Print Text");
		shell.setMaximized(true);
		text = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL
				| SWT.H_SCROLL);
		text.setFont(font);
		text.setForeground(foregroundColor);
		text.setBackground(backgroundColor);

		Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
		MenuItem item = new MenuItem(menuBar, SWT.CASCADE);
		item.setText("&File");
		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		item.setMenu(fileMenu);
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText("&Open...");
		item.setAccelerator(SWT.CTRL + 'O');
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuOpen();
			}
		});
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText("Font...");
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuFont();
			}
		});
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText("Foreground Color...");
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuForegroundColor();
			}
		});
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText("Background Color...");
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuBackgroundColor();
			}
		});
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText("&Print...");
		item.setAccelerator(SWT.CTRL + 'P');
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuPrint();
			}
		});
		new MenuItem(fileMenu, SWT.SEPARATOR);
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText("E&xit");
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				System.exit(0);
			}
		});

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		if (font != null)
			font.dispose();
		if (foregroundColor != null)
			foregroundColor.dispose();
		if (backgroundColor != null)
			backgroundColor.dispose();
	}

	void menuOpen() {
		final String textString;
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		dialog.setFilterExtensions(new String[] { "*.java", "*.*" });
		String name = dialog.open();
		if ((name == null) || (name.length() == 0))
			return;

		try {
			File file = new File(name);
			FileInputStream stream = new FileInputStream(file.getPath());
			try {
				Reader in = new BufferedReader(new InputStreamReader(stream));
				char[] readBuffer = new char[2048];
				StringBuffer buffer = new StringBuffer((int) file.length());
				int n;
				while ((n = in.read(readBuffer)) > 0) {
					buffer.append(readBuffer, 0, n);
				}
				textString = buffer.toString();
				stream.close();
			} catch (IOException e) {
				MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
				box.setMessage("Error reading file:\n" + name);
				box.open();
				return;
			}
		} catch (FileNotFoundException e) {
			MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
			box.setMessage("File not found:\n" + name);
			box.open();
			return;
		}
		text.setText(textString);
	}

	void menuFont() {
		FontDialog fontDialog = new FontDialog(shell);
		fontDialog.setFontList(font.getFontData());
		FontData fontData = fontDialog.open();
		if (fontData != null) {
			if (font != null)
				font.dispose();
			font = new Font(display, fontData);
			text.setFont(font);
		}
	}

	void menuForegroundColor() {
		ColorDialog colorDialog = new ColorDialog(shell);
		colorDialog.setRGB(foregroundColor.getRGB());
		RGB rgb = colorDialog.open();
		if (rgb != null) {
			if (foregroundColor != null)
				foregroundColor.dispose();
			foregroundColor = new Color(display, rgb);
			text.setForeground(foregroundColor);
		}
	}

	void menuBackgroundColor() {
		ColorDialog colorDialog = new ColorDialog(shell);
		colorDialog.setRGB(backgroundColor.getRGB());
		RGB rgb = colorDialog.open();
		if (rgb != null) {
			if (backgroundColor != null)
				backgroundColor.dispose();
			backgroundColor = new Color(display, rgb);
			text.setBackground(backgroundColor);
		}
	}

	void menuPrint() {
		PrintDialog dialog = new PrintDialog(shell, SWT.NULL);
		PrinterData data = dialog.open();
		if (data == null)
			return;
		if (data.printToFile) {
			data.fileName = "print.out";
		}

		textToPrint = text.getText();
		printer = new Printer(data);
		Thread printingThread = new Thread("Printing") {
			public void run() {
				print(printer);
				printer.dispose();
			}
		};
		printingThread.start();
	}

	void print(Printer printer) {
		if (printer.startJob("Text")) {
			Rectangle clientArea = printer.getClientArea();
			Rectangle trim = printer.computeTrim(0, 0, 0, 0);
			Point dpi = printer.getDPI();
			leftMargin = dpi.x + trim.x;
			rightMargin = clientArea.width - dpi.x + trim.x + trim.width;
			topMargin = dpi.y + trim.y;
			bottomMargin = clientArea.height - dpi.y + trim.y + trim.height;

			int tabSize = 4;
			StringBuffer tabBuffer = new StringBuffer(tabSize);
			for (int i = 0; i < tabSize; i++)
				tabBuffer.append(' ');
			tabs = tabBuffer.toString();
			gc = new GC(printer);

			FontData fontData = font.getFontData()[0];
			printerFont = new Font(printer, fontData.getName(),
					fontData.getHeight(), fontData.getStyle());
			gc.setFont(printerFont);
			tabWidth = gc.stringExtent(tabs).x;
			lineHeight = gc.getFontMetrics().getHeight();

			RGB rgb = foregroundColor.getRGB();
			printerForegroundColor = new Color(printer, rgb);
			gc.setForeground(printerForegroundColor);

			rgb = backgroundColor.getRGB();
			printerBackgroundColor = new Color(printer, rgb);
			gc.setBackground(printerBackgroundColor);

			printText();
			printer.endJob();
			printerFont.dispose();
			printerForegroundColor.dispose();
			printerBackgroundColor.dispose();
			gc.dispose();
		}
	}

	void printText() {
		printer.startPage();
		wordBuffer = new StringBuffer();
		x = leftMargin;
		y = topMargin;
		index = 0;
		end = textToPrint.length();
		while (index < end) {
			char c = textToPrint.charAt(index);
			index++;
			if (c != 0) {
				if (c == 0x0a || c == 0x0d) {
					if (c == 0x0d && index < end
							&& textToPrint.charAt(index) == 0x0a) {
						index++;
					}
					printWordBuffer();
					newline();
				} else {
					if (c != '\t') {
						wordBuffer.append(c);
					}
					if (Character.isWhitespace(c)) {
						printWordBuffer();
						if (c == '\t') {
							x += tabWidth;
						}
					}
				}
			}
		}
		if (y + lineHeight <= bottomMargin) {
			printer.endPage();
		}
	}

	void printWordBuffer() {
		if (wordBuffer.length() > 0) {
			String word = wordBuffer.toString();
			int wordWidth = gc.stringExtent(word).x;
			if (x + wordWidth > rightMargin) {
				newline();
			}
			gc.drawString(word, x, y, false);
			x += wordWidth;
			wordBuffer = new StringBuffer();
		}
	}

	void newline() {
		x = leftMargin;
		y += lineHeight;
		if (y + lineHeight > bottomMargin) {
			printer.endPage();
			if (index + 1 < end) {
				y = topMargin;
				printer.startPage();
			}
		}
	}
}