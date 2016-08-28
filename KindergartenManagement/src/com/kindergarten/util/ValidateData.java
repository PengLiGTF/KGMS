package com.kindergarten.util;

import org.eclipse.swt.widgets.Shell;

public class ValidateData
{

	// check login message
	public boolean validLogin(Shell shell, String admin_name, String admin_pwd)
	{
		if (null == admin_name || "".equals(admin_name))
		{
			TipBox.Message(shell, "登陆验证", "用户名不能为空");
			return false;
		} else if (null == admin_pwd || "".equals(admin_pwd))
		{
			TipBox.Message(shell, "登陆验证", "密码不能为空");
			return false;
		}

		return true;
	}

	// check insert book message
	public boolean validBook(Shell shell, String b_id, String b_name)
	{
		if (!(b_id.trim().matches("^((?i)b)[\\d]{4,}$")))
		{
			TipBox.Message(shell, "��ʾ��Ϣ", "������b��ͷ���>=4λ���֣�");
			return false;
		} else if (null == b_name || b_name.trim().equals(""))
		{
			TipBox.Message(shell, "��ʾ��Ϣ", "����дͼ������");
			return false;
		}
		return true;
	}

	// check insert reader message
	public boolean validReader(Shell shell, String r_name, String r_email, String r_addr)
	{
		if (null == r_name || "".equals(r_name))
		{
			TipBox.Message(shell, "��ʾ��Ϣ", "����д��������");
			return false;
		} else if (!r_email.trim().matches("^[\\w]+@[\\w]+\\.(com|cn|(com.cn)|net|(net.cn))+$"))
		{
			TipBox.Message(shell, "��ʾ��Ϣ", "����д��ȷEmail��(��:example@email.com)");
			return false;
		} else if (null == r_addr || "".equals(r_addr))
		{
			TipBox.Message(shell, "��ʾ��Ϣ", "����дסַ��");
			return false;
		}
		return true;
	}
}
