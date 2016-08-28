package com.kindergarten.util;

import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class TipBox extends MessageBox
{

	public TipBox(Shell parent)
	{
		super(parent);
	}

	public static void Message(Shell shell, String text, String message)
	{
		TipBox tipBox = new TipBox(shell);
		tipBox.setText(text);
		tipBox.setMessage(message);
		tipBox.open();
	}

	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
}
