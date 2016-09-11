package com.kindergarten.groups;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public abstract class AbstractGroup extends Group
{
	protected String userId;
	protected Composite composite;

	public AbstractGroup(Composite parent, int style, String userId)
	{
		super(parent, style);
		parent.setBounds(190, 0, 1024, 600);
		this.setBounds(0, 0, 1024, 600);
		this.userId = userId;
		composite = new Composite(this, SWT.NONE);
		composite.setBounds(10, 20, 800, 600);
	}

	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}

}
