package com.kindergarten.util;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;

import com.kindergarten.data.FeeTemplate;
import com.kindergarten.data.Grade;
import com.kindergarten.data.SexModel;
import com.kindergarten.service.ClassManageService;
import com.kindergarten.service.FeeTemplateService;

public class ComboArrayContentProvider extends ArrayContentProvider
{
	private MyComboType cType;

	public ComboArrayContentProvider(MyComboType cType)
	{
		this.cType = cType;
	}

	@Override
	public Object[] getElements(Object inputElement)
	{
		if (cType == MyComboType.SEX)
		{
			SexModel[] sexs = CommonUtil.getSexModel();
			return (Object[]) sexs;
		} else if (cType == MyComboType.GRADE)
		{
			ClassManageService service = new ClassManageService();
			return service.queryAllGrade().toArray(new Grade[0]);
		} else if (cType == MyComboType.CLASS)
		{
			// 班级由年级联动而来
			return (Object[]) inputElement;
		} else if (cType == MyComboType.FEE_TEMPLATE)
		{
			List<FeeTemplate> templateList = new FeeTemplateService().queryAllFeeTemplateList();
			return templateList.toArray(new FeeTemplate[0]);
		} else if (cType == MyComboType.STATUS)
		{
			return (Object[]) CommonUtil.getAllStatus();
		}
		return null;
	}
}