package com.kindergarten.util.print;

import net.sf.paperclips.ImagePrint;
import net.sf.paperclips.PaperClips;
import net.sf.paperclips.Print;
import net.sf.paperclips.PrintJob;
import net.sf.paperclips.StyledTextPrint;
import net.sf.paperclips.TextStyle;
import net.sf.paperclips.ui.PrintPreview;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class KinderFeePrintDialog extends Dialog
{

	private Print print;
	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public KinderFeePrintDialog(Shell parentShell, Print print)
	{
		super(parentShell);
		this.print = print;
	}

	private Print createPrint()
	{
		return this.print;
	}


	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(null);
		final Shell shell = getShell();
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
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent)
	{
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize()
	{
		return new Point(450, 300);
	}

}
