package com.kindergarten.groups;

import org.eclipse.swt.widgets.Composite;

import com.kindergarten.util.SWTResourceManager;

public class IndexGroup extends AbstractGroup
{

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public IndexGroup(Composite parent, int style, String userId)
	{
		super(parent, style, userId);
		this.setText("金鹰卡通幼儿园收费管理系统");
		//this.setBounds(0, 0, 591, 485);
//		Composite composite = new Composite(this, SWT.NONE);
//		composite.setBounds(10, 20, 571, 455);
		composite.setBackgroundImage(SWTResourceManager.getImage(IndexGroup.class, "/images/111.jpg"));
	}

	@Override
	protected void checkSubclass()
	{

	}
}
