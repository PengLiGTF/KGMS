package com.kindergarten.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class MessageBoxUtil
{

	public static int showWarnMessageBox(Shell shell, String message)
	{
		MessageBox warnMb = new MessageBox(shell, SWT.OK | SWT.ICON_INFORMATION | SWT.APPLICATION_MODAL);
		warnMb.setMessage(message);
		return warnMb.open();
	}

	public static int showConfirmMessageBox(Shell shell, String message)
	{
		MessageBox warnMb = new MessageBox(shell, SWT.OK | SWT.CANCEL | SWT.ICON_INFORMATION | SWT.APPLICATION_MODAL);
		warnMb.setMessage(message);
		return warnMb.open();
	}
}
