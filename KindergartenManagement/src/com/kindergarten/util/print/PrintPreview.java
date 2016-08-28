package com.kindergarten.util.print;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolItem;

/**
 * A GUI dialog that layouts and displays a PDocument. It also allows to print
 * the document.
 * 
 * @author Friederich Kupzog
 */
public class PrintPreview extends KDialog
{
	protected PDocument document;

	protected Label guiImageLabel;

	protected CLabel guiPageLabel;

	protected Combo guiZoom;

	protected ScrolledComposite guiScrollArea;

	protected boolean layoutNeccessary;

	protected int percent;

	protected int page;

	/**
	 * @param parent
	 * @param title
	 * @param icon
	 */
	public PrintPreview(Shell parent, String title, Image icon, PDocument doc)
	{
		super(parent, title, icon);
		createContents();
		document = doc;
		page = 1;
		percent = 100;
		layoutNeccessary = true;

		addToolItem("print", "打印", IconSource.getImage("print"));
		addToolItem("first", "第一页", IconSource.getImage("first"));
		addToolItem("prev", "前一页", IconSource.getImage("prev"));
		addToolItem("next", "下一页", IconSource.getImage("next"));
		addToolItem("last", "最末页", IconSource.getImage("last"));

//		Button close = addButtonRight("&SchlieBen", "");
//		// addButtonRight("Seite &einrichten","");
//		close.setFocus();

		guiShell.addShellListener(new ShellAdapter()
		{
			public void shellClosed(ShellEvent arg0)
			{
				onClose();
			}
		});

		Composite comp = new Composite(guiToolBarArea, SWT.BORDER);
		comp.setLayout(new FillLayout());
		guiPageLabel = new CLabel(comp, SWT.NONE);
		guiPageLabel.setText(guiPageLabel.getText() + "        ");
		guiPageLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		adjustToToolBar(comp);

		guiZoom = new Combo(guiToolBarArea, SWT.BORDER | SWT.READ_ONLY);
		guiZoom.add("500%");
		guiZoom.add("200%");
		guiZoom.add("100%");
		guiZoom.add("80%");
		guiZoom.add("50%");
		guiZoom.add("20%");
		guiZoom.add("passend");
		adjustToToolBar(guiZoom);
		guiZoom.setToolTipText("VorschaugroBe");
		guiZoom.select(2);
		guiZoom.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent arg0)
			{
				onCombo(((Combo) arg0.widget).getText());
			}
		});
		guiMainArea.setLayout(new FillLayout());
		guiScrollArea = new ScrolledComposite(guiMainArea, SWT.H_SCROLL | SWT.V_SCROLL);
		guiImageLabel = new Label(guiScrollArea, SWT.NONE);
		guiScrollArea.setContent(guiImageLabel);
		if (guiImageLabel.getImage() != null)
			guiImageLabel.getImage().dispose();
		guiImageLabel.setImage(getPageImage(page));
		guiPageLabel.setText(" Seite " + page + " von " + document.getNumOfPages() + "       ");
		guiImageLabel.setSize(guiImageLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}

	public int getShellStyle()
	{
		return super.getShellStyle() | SWT.MAX | SWT.MIN;
	}

	protected void doLayout()
	{
		int x = Display.getCurrent().getBounds().width - 100;
		int y = Display.getCurrent().getBounds().height - 10;
		guiShell.setSize(x, y);
		guiShell.setMaximized(true);
	}

	public Image getPageImage(int page)
	{
		Point dpi = Display.getCurrent().getDPI();

		try
		{
			int h = (int) Math.round(document.getPageHeight() / 2.54 * dpi.y * percent / 100 + 5);
			int w = (int) Math.round(document.getPageWidth() / 2.54 * dpi.x * percent / 100 + 5);
			Image newImage = new Image(Display.getCurrent(), w, h);
			GC gc = new GC(newImage);

			PBox.setParameters(gc, Display.getCurrent(), dpi, percent);
			if (layoutNeccessary)
				document.layout();
			layoutNeccessary = false;
			document.draw(page);

			// Schatten

			gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY));
			gc.fillRectangle(0, newImage.getBounds().height - 5, newImage.getBounds().width, newImage.getBounds().height);
			gc.fillRectangle(newImage.getBounds().width - 5, 0, newImage.getBounds().width - 5, newImage.getBounds().height);

			gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
			gc.fillRectangle(0, newImage.getBounds().height - 5, 5, newImage.getBounds().height);
			gc.fillRectangle(newImage.getBounds().width - 5, 0, newImage.getBounds().width, 5);

			gc.dispose();
			return newImage;
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
			return null;
		}

	}

	protected void onCombo(String text)
	{
		if (text.startsWith("passend"))
		{
			long ypixel = Math.round(document.getPageHeight() / 2.54 * Display.getCurrent().getDPI().y);
			long xpixel = Math.round(document.getPageWidth() / 2.54 * Display.getCurrent().getDPI().x);

			int yscale = (int) (100 * (guiScrollArea.getBounds().height) / ypixel);
			int xscale = (int) (100 * (guiScrollArea.getBounds().width) / xpixel);

			percent = Math.min(yscale, xscale);
		} else
		{
			text = text.substring(0, text.length() - 1);
			percent = 100;
			try
			{
				percent = Integer.parseInt(text);
			} catch (Exception e1)
			{
				MsgBox.show("'" + text + "' ist keine gultige Zahl.");
				guiZoom.select(3);
			}
		}
		layoutNeccessary = true;
		if (guiImageLabel.getImage() != null)
			guiImageLabel.getImage().dispose();
		guiImageLabel.setImage(getPageImage(page));
		guiImageLabel.setSize(guiImageLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}

	protected void onToolItem(ToolItem toolitem, String name)
	{
		if (name.equals("next"))
		{
			if (page < document.getNumOfPages())
			{
				page++;
				if (guiImageLabel.getImage() != null)
					guiImageLabel.getImage().dispose();
				guiImageLabel.setImage(getPageImage(page));
				guiPageLabel.setText(" Seite " + page + " von " + document.getNumOfPages());
			}
		} else if (name.equals("prev"))
		{
			if (page > 1)
			{
				page--;
				if (guiImageLabel.getImage() != null)
					guiImageLabel.getImage().dispose();
				guiImageLabel.setImage(getPageImage(page));
				guiPageLabel.setText(" Seite " + page + " von " + document.getNumOfPages());
			}
		} else if (name.equals("first"))
		{
			page = 1;
			if (guiImageLabel.getImage() != null)
				guiImageLabel.getImage().dispose();
			guiImageLabel.setImage(getPageImage(page));
			guiPageLabel.setText(" Seite " + page + " von " + document.getNumOfPages());
		} else if (name.equals("last"))
		{
			page = document.getNumOfPages();
			if (guiImageLabel.getImage() != null)
				guiImageLabel.getImage().dispose();
			guiImageLabel.setImage(getPageImage(page));
			guiPageLabel.setText(" Seite " + page + " von " + document.getNumOfPages());
		} else if (name.equals("print"))
		{
			onPrint();
		}
	}

	protected void onButton(Button button, String buttonText)
	{
		if (buttonText.startsWith("&Schlie"))
			onClose();
		else if (buttonText.startsWith("Seite"))
			onPageSetup();
	}

	protected void onClose()
	{
		if (guiImageLabel.getImage() != null)
			guiImageLabel.getImage().dispose();
		PTextStyle.disposeFonts();
		close();
	}

	protected void onPageSetup()
	{
		/*
		 * funktioniert nicht: - Abgeschnittene Tabellen bleiben abgeschnitten -
		 * Skalierung geht nicht
		 */
		PageSetup ps = new PageSetup(guiShell);
		ps.open();
		document.readMeasuresFromPageSetup();
		layoutNeccessary = true;
		if (guiImageLabel.getImage() != null)
			guiImageLabel.getImage().dispose();
		guiImageLabel.setImage(getPageImage(page));
		guiImageLabel.setSize(guiImageLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}

	protected void onPrint()
	{
		PrintDialog dialog = new PrintDialog(guiShell, SWT.BORDER);
		PrinterData data = dialog.open();
		if (data == null)
			return;
		if (data.printToFile)
		{
			FileDialog d = new FileDialog(guiShell, SWT.SAVE);
			d.setFilterNames(new String[]
			{ "All Files (*.*)" });
			d.setFilterExtensions(new String[]
			{ "*.*" }); // Windows wild
			// cards
			d.setFilterPath("c:\\"); // Windows path
			d.setFileName("");
			d.open();
			data.fileName = d.getFilterPath() + "\\" + d.getFileName();

		}

		Printer printer = new Printer(data);
		GC gc = new GC(printer);

		PBox.setParameters(gc, printer, printer.getDPI(), (int) (document.getScale() * 100));
		document.layout();

		if (printer.startJob(document.getTitle()))
		{
			if (data.scope == PrinterData.ALL_PAGES)
			{
				data.startPage = 1;
				data.endPage = document.getNumOfPages();
			} else if (data.scope == PrinterData.SELECTION)
			{
				data.startPage = page;
				data.endPage = page;
			} else if (data.scope == PrinterData.PAGE_RANGE)
			{
				if (data.startPage > document.getNumOfPages())
					data.startPage = document.getNumOfPages();
				if (data.endPage > document.getNumOfPages())
					data.endPage = document.getNumOfPages();
			}

			for (int page = data.startPage; page <= data.endPage; page++)
			{
				printer.startPage();
				document.draw(page);
				printer.endPage();
			}
			printer.endJob();
		}
		gc.dispose();
		printer.dispose();
		layoutNeccessary = true;
	}
}