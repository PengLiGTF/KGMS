package com.kindergarten.util;

/**
 * 增删改查、导出、打印按钮名称常亮定义
 * */
public class ButtonNameConstant
{
	public static final String BTN_ADD = "btnAdd";
	public static final String BTN_DELETE = "btnDelete";
	public static final String BTN_EDIT = "btnEdit";
	public static final String BTN_QUERY = "btnQuery";
	public static final String BTN_EXPORT = "btnExport";
	public static final String BTN_PRINT = "btnPrint";
	public static final String BTN_SAVE = "btnSave";
	public static final String BTN_RECYCLE = "btnRecycle";
	public static final String BTN_GRANT_ALL = "btnGrantAll";
	public static final String BTN_GRADE_ADD = "btnGradeAdd";
	public static final String BTN_AUDIT = "btnAudit";
	public static final String BTN_UNAUDIT = "btnUnAudit";

	public static class BtnIdentity
	{
		private String btnId;
		private String btnName;

		public BtnIdentity(String btnId, String btnName)
		{
			super();
			this.btnId = btnId;
			this.btnName = btnName;
		}

		public String getBtnId()
		{
			return btnId;
		}

		public void setBtnId(String btnId)
		{
			this.btnId = btnId;
		}

		public String getBtnName()
		{
			return btnName;
		}

		public void setBtnName(String btnName)
		{
			this.btnName = btnName;
		}
	}
}
