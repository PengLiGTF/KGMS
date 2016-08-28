package com.kindergarten.util.print;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

public class PrinterUtil
{

	public static void printTable(Table table, Shell shell, String title)
	{
		// create a document with default settings from PageSetup
		final PDocument doc = new PDocument(title);
		// put some header text on it
		PTextBox t = new PTextBox(doc);
		t.setText(title);
		new PVSpace(doc, 0.1);
		new PHLine(doc, 0.02, SWT.COLOR_BLACK);
		new PVSpace(doc, 0.5);
		// create the table
		SWTPTable pTable = new SWTPTable(doc);
		pTable.setTable(table);
		pTable.setBoxProvider(new PTableBoxProvider());
		PrintPreview pr = new PrintPreview(shell, title, IconSource.getImage("print"), doc);
		pr.open();
	}

}
