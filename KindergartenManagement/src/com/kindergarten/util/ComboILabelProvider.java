package com.kindergarten.util;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.kindergarten.data.ClassModel;
import com.kindergarten.data.FeeStaticTypeModel;
import com.kindergarten.data.FeeTemplate;
import com.kindergarten.data.Grade;
import com.kindergarten.data.KinderStatus;
import com.kindergarten.data.SexModel;

/**
 * 共用的下拉框 labelProvider
 * */
public class ComboILabelProvider implements ILabelProvider, IColorProvider
{
	private int index = 1;

	@Override
	public void addListener(ILabelProviderListener listener)
	{
	}

	@Override
	public void dispose()
	{

	}

	@Override
	public boolean isLabelProperty(Object element, String property)
	{
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener)
	{

	}

	@Override
	public Image getImage(Object element)
	{
		return null;
	}

	@Override
	public String getText(Object element)
	{
		if (SexModel.class.isInstance(element))
		{
			SexModel sex = (SexModel) element;
			return sex.getName();
		} else if (Grade.class.isInstance(element))
		{
			Grade grade = (Grade) element;
			return grade.getGradeName();
		} else if (ClassModel.class.isInstance(element))
		{
			ClassModel cModel = (ClassModel) element;
			return cModel.getClassName();
		} else if (FeeTemplate.class.isInstance(element))
		{
			FeeTemplate feeTemplate = (FeeTemplate) element;
			return feeTemplate.getFeeTemplateName();
		} else if (KinderStatus.class.isInstance(element))
		{
			KinderStatus status = (KinderStatus) element;
			return status.getStatusName();
		} else if (FeeStaticTypeModel.class.isInstance(element))
		{
			FeeStaticTypeModel temp = (FeeStaticTypeModel) element;
			return temp.getName();
		}
		return null;
	}

	@Override
	public Color getForeground(Object element)
	{
		if ((++index) % 2 == 0)
		{
			Color color = Display.getDefault().getSystemColor(SWT.COLOR_RED);
			return color;
		}
		return null;
	}

	@Override
	public Color getBackground(Object element)
	{
		return null;
	}
}
