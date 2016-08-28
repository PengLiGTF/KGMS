package com.kindergarten.groups;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.kindergarten.MainFrame;
import com.kindergarten.util.SWTResourceManager;
import com.kindergarten.util.TipBox;
import com.kindergarten.util.ValidateData;

public class AddBookGroup extends AbstractGroup
{

	private Text b_author;
	private Text b_intro;
	private Text b_out;
	private Text b_id;
	private Text b_name;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public AddBookGroup(final Composite parent, int style, String userId)
	{
		super(parent, style, userId);
		this.setText("增加书籍");

		final Composite composite = new Composite(this, SWT.NONE);
		composite.setBounds(10, 20, 565, 448);
		composite.setBackgroundImage(SWTResourceManager.getImage(IndexGroup.class, "/images/111.jpg"));

		Label label_3 = new Label(composite, SWT.NONE);
		label_3.setBounds(104, 138, 52, 14);
		label_3.setText("�����ߣ�");

		Label label_4 = new Label(composite, SWT.NONE);
		label_4.setBounds(104, 234, 52, 14);
		label_4.setText("�򡡽飺");

		Label label_5 = new Label(composite, SWT.NONE);
		label_5.setBounds(96, 191, 60, 14);
		label_5.setText("����ʱ�䣺");

		b_author = new Text(composite, SWT.NONE);
		b_author.setBounds(177, 138, 140, 14);

		b_intro = new Text(composite, SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		b_intro.setBounds(177, 234, 140, 56);

		b_out = new Text(composite, SWT.NONE);
		b_out.setBounds(177, 191, 140, 14);

		Button addbtn = new Button(composite, SWT.NONE);
		addbtn.addMouseListener(new MouseAdapter()
		{
			public void mouseUp(MouseEvent e)
			{
				ValidateData vd = new ValidateData();
				if (vd.validBook(parent.getShell(), b_id.getText(), b_name.getText()))
				{
					TipBox.Message(parent.getShell(), "��ʾ��Ϣ", "��ϲ������ӳɹ���");
					b_id.setText("");
					b_name.setText("");
					b_author.setText("");
					b_intro.setText("");
					b_out.setText("");
				}
			}
		});
		addbtn.setBounds(135, 305, 69, 24);
		addbtn.setText("���");

		Button returnbtn = new Button(composite, SWT.NONE);
		returnbtn.addMouseListener(new MouseAdapter()
		{
			public void mouseUp(MouseEvent e)
			{
				parent.getShell().dispose();
				new MainFrame(AddBookGroup.this.userId).open();
			}
		});
		returnbtn.setBounds(227, 305, 69, 24);
		returnbtn.setText("����");

		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setBounds(104, 44, 52, 14);
		label_1.setText("�顡�ţ�");

		b_id = new Text(composite, SWT.NONE);
		b_id.setBounds(177, 44, 140, 14);

		Label label_2 = new Label(composite, SWT.NONE);
		label_2.setBounds(104, 86, 52, 14);
		label_2.setText("�顡����");

		b_name = new Text(composite, SWT.NONE);
		b_name.setBounds(177, 86, 140, 14);

	}

	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}
}
