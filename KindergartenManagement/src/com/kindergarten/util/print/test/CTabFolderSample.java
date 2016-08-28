package com.kindergarten.util.print.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class CTabFolderSample {
	public static void main(String[] args) {
		CTabFolderSample inst = new CTabFolderSample();
		final Display display = Display.getDefault();
		final Shell shell = new Shell();
		shell.setSize(296, 255);
		shell.setText("CTabFolder ��ϰ");
		shell.setLayout(new GridLayout());
		//

		shell.open();

		final CTabFolder tabFolder = new CTabFolder(shell, SWT.NONE | SWT.CLOSE
				| SWT.BORDER);
		tabFolder.addCTabFolder2Listener(new CTabFolder2Adapter() {
			public void minimize(CTabFolderEvent event) {
				tabFolder.setMinimized(true);
				tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
						false));
				shell.layout(true);// ˢ�²���
			}

			public void maximize(CTabFolderEvent event) {
				tabFolder.setMaximized(true);
				tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
						true));
				shell.layout(true);
			}

			public void restore(CTabFolderEvent event) {
				tabFolder.setMinimized(false);
				tabFolder.setMaximized(false);
				tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
						false));
				shell.layout(true);
			}
		});
		// tabFolder.setBounds(0, 0, 283, 211);
		tabFolder.setTabHeight(20);
		tabFolder.marginHeight = 5;
		tabFolder.marginWidth = 5;
		tabFolder.setMaximizeVisible(true);
		tabFolder.setMinimizeVisible(true);
		GridData gd_tabFolder = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd_tabFolder.heightHint = 99;
		tabFolder.setLayoutData(gd_tabFolder);
		// �������������ù̶��ı���ɫ��ǰ��ɫ
		// tabFolder.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
		// tabFolder.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		// ���������ý���ɫ
		Color[] color = new Color[4];
		color[0] = display.getSystemColor(SWT.COLOR_DARK_BLUE);
		color[1] = display.getSystemColor(SWT.COLOR_BLUE);
		color[2] = display.getSystemColor(SWT.COLOR_DARK_GRAY);
		color[3] = display.getSystemColor(SWT.COLOR_WHITE);
		int[] intArray = new int[] { 25, 45, 100 };
		// tabFolder.setSelectionBackground(color, intArray);
		// ���������˱�����ɫ���������ͬʱ�����˱���ͼƬ�Ļ��Ա���ͼƬ����
		tabFolder.setSimple(false);// ����Բ��
		tabFolder.setUnselectedCloseVisible(true);

		CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE);
		tabItem.setText("New Item");

		Button btnNewButton = new Button(tabFolder, SWT.NONE);
		btnNewButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {

			}

			@Override
			public void mouseDown(MouseEvent arg0) {

			}

			@Override
			public void mouseUp(MouseEvent arg0) {
				CTabItem item = new CTabItem(tabFolder, SWT.None | SWT.MULTI
						| SWT.V_SCROLL);
				item.setText("ѡ�New2");
				Text t = new Text(tabFolder, SWT.None | SWT.MULTI
						| SWT.V_SCROLL | SWT.H_SCROLL | SWT.WRAP);
				t.setText("������ѡ�2");
				item.setControl(t);
			}

		});
		tabItem.setControl(btnNewButton);
		btnNewButton.setText("New Button");
		for (int i = 1; i < 4; i++) {
			CTabItem item = new CTabItem(tabFolder, SWT.None | SWT.MULTI
					| SWT.V_SCROLL);
			item.setText("ѡ�" + i);
			Text t = new Text(tabFolder, SWT.None | SWT.MULTI | SWT.V_SCROLL
					| SWT.H_SCROLL | SWT.WRAP);
			t.setText("����ѡ����Կ��Ƶ�����" + i + "\n\n�����һ��\n\nһ·˳��");
			item.setControl(t);

		}
		Image image = new Image(display, "E:\\flower.jpg");
		shell.setImage(image);
		shell.setSize(300, 200);
		shell.layout();
		if (!display.isDisposed()) {
			AddThread addThread = inst.new AddThread(tabFolder);
			display.asyncExec(addThread);
		}
		System.out.println("ddddddddddddddddddddd");
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	class AddThread implements Runnable {

		private CTabFolder tabFolder;

		public AddThread(CTabFolder tabFolder) {
			super();
			this.tabFolder = tabFolder;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			CTabItem item = new CTabItem(tabFolder, SWT.None | SWT.MULTI
					| SWT.V_SCROLL);
			item.setText("ѡ�New");
			Text t = new Text(tabFolder, SWT.None | SWT.MULTI | SWT.V_SCROLL
					| SWT.H_SCROLL | SWT.WRAP);
			t.setText("������ѡ�");
			item.setControl(t);
		}

	}

}