import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.ComboContentAdapter;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.SimpleContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class LaunchApp
{
	protected Shell shell;

	private Text nameT;
	private Combo cityC;
	private Text remarksT;

	/**
	 * Launch the application
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			LaunchApp window = new LaunchApp();
			window.open();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Open the window
	 */
	public void open()
	{
		final Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	/**
	 * Create contents of the window
	 */
	protected void createContents()
	{
		shell = new Shell();
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		shell.setLayout(gridLayout);
		shell.setSize(226, 122);
		shell.setText("Field Assist");

		final Label nameL = new Label(shell, SWT.NONE);
		nameL.setText("姓名");

		nameT = new Text(shell, SWT.BORDER);
		nameT.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Label cityL = new Label(shell, SWT.NONE);
		cityL.setText("城市");

		cityC = new Combo(shell, SWT.NONE);
		cityC.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Label remarksL = new Label(shell, SWT.NONE);
		remarksL.setText("备注");

		remarksT = new Text(shell, SWT.BORDER);
		remarksT.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		//
		Dialog.applyDialogFont(this.shell);

		//
		this.addNameTextFieldAssist();
		this.addCityComboFieldAssist();
		this.addRemarksTextFieldAssist();
	}

	/**
	 * 给名称Text添加自动完成功能
	 */
	private void addNameTextFieldAssist()
	{
		// 让text可以进行代码提示. 提示内容为: "aa", "BB", "无敌".
		// 注意: 不区分大小写. [如: 输入'b', 内容中会出现"BB"]
		new AutoCompleteField(nameT, new TextContentAdapter(), new String[]
		{ "aa", "BB", "无敌" });
	}

	/**
	 * 给城市Combo添加自动完成功能
	 */
	private void addCityComboFieldAssist()
	{
		// 让combo可以代码提示. 提示内容为: "BeiJing", "南京", "北京"
		new AutoCompleteField(cityC, new ComboContentAdapter(), new String[]
		{ "BeiJing", "南京", "北京" });
	}

	/**
	 * 给备注Text添加自动完成功能
	 */
	private void addRemarksTextFieldAssist()
	{
		// 下面使用ContentProposalAdapter,而没有继续使用AutoCompleteField.
		// [去查看代码你会发现:AutoCompleteFiled实现和下面的代码几乎一样. ]
		// AutoCompleteFiled使用的同样就将传入的String[]去构造一个SimpleContentProposalProvider.
		// 但是,AutoCompleteFiled内部的ContentProposalAdapter是无法从外部得到的.
		// 所以,为了能够自定义ContentProposalAdapter,
		// 还必须将AutoCompleteField内部实现的代码在外部再写一遍.
		KeyStroke keyStroke = null; // null 表示不接受快捷键
		try
		{
			keyStroke = KeyStroke.getInstance("Ctrl+1"); // 在text上按Ctrl+1弹出popup的shell.
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		ContentProposalAdapter adapter = new ContentProposalAdapter(remarksT, new TextContentAdapter(), new SimpleContentProposalProvider(new String[]
		{ "one", "two", "three" }), keyStroke, new char[]
		{ '.', ' ' });
		adapter.setAutoActivationDelay(200); // 延时200ms
		adapter.setPropagateKeys(true); // 如果用户的输入值在内容列表中[比如输入'o',而内容中有'one'],则弹出popup的shell
		adapter.setFilterStyle(ContentProposalAdapter.FILTER_CUMULATIVE); // 用户同步输入的内容也过滤列表[如:用户输入'o',则弹出popup的shell中的内容列表被过滤,其中都是'o'开头的,
																			// 再输入一个'n',
																			// 则内容列表中被过滤,只有以'on'开头的]
		adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_INSERT); // 回写插入
		// adapter.setLabelProvider(new LabelProvider() { //
		// 可以不用指定LabelProvider. 如果指定,则不仅仅可以显示Text, 还可以显示Image.
		// @Override
		// public String getText(Object element) {
		// IContentProposal proposal = (IContentProposal) element;
		// return "XX" + proposal.getContent();
		// }
		// @Override
		// public Image getImage(Object element) {
		// return super.getImage(element);
		// }
		// });

		// 上面的代码中使用的是SimpleContentProposalProvider,
		// 则会用每个String去构造默认的一个IContentProposal,
		// 具体逻辑见: SimpleContentProposalProvider.makeContentProposal

		// 请注意: 可以不用设置setLabelProvider的,
		// 那么将会直接从IContentProposal中取label或content显示.
		// 有labelProvider则从labelProvider得到在内容list中显示的值.
		// 具体逻辑见: ContentProposalAdapter.getString()方法
		// if (labelProvider == null) {
		// return proposal.getLabel() == null ? proposal.getContent() :
		// proposal.getLabel();
		// }
		// return labelProvider.getText(proposal);

		// 同样的, 如果你添加了labelProvider, 那么也可以给每个IContentProposal返回Image.
		// 具体逻辑见: ContentProposalAdapter.getImage()方法

	}

	// ContentProposalAdapter.setAutoActivationDelay 弹出popup的延迟时间

	// ContentProposalAdapter.setPropagateKeys(true);
	// 说明: 如果用户敲入的字母在内容列表内时,是否弹出popup内容列表.
	// true 弹出. 用户输入'o'也会弹出popup的shell. 输入'.'也会弹出.
	// false 不弹出. 用户只有输入'.'才弹出popup的shell. 输入'o'等,不弹出.

	// ContentProposalAdapter.setFilterStyle(ContentProposalAdapter.FILTER_*);
	// 作用: 在用户敲入字母的时候是否过滤popup弹出的shell里面的内容.
	// ContentProposalAdapter.FILTER_NONE 不过滤. 说明: 下面的内容列表永远不变.
	// ContentProposalAdapter.FILTER_CHARACTER 只用一个输入字符为条件过滤下面的内容列表.
	// 说明:在输入多个字符后,下面的内容列表会被清空.
	// ContentProposalAdapter.FILTER_CUMULATIVE 随着用户输入不停的过滤下面的内容列表.
	// 注意在3.4后被@deprecated了. 说明: 随着用户的输入,下面的内容一直在过滤

	// ContentProposalAdapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_*);
	// 说明: 用户从popup的shell中得到的内容怎么回写到控件上.
	// ContentProposalAdapter.PROPOSAL_INSERT 插入.
	// ContentProposalAdapter.PROPOSAL_REPLACE 覆盖.
	// ContentProposalAdapter.PROPOSAL_IGNORE 忽略. 应该叫追加比较合适.

	// TextContentAdapter只可以用于Text.
	// ComboContentAdapter只可以用于Combo.
	// 所以, 对于StyledText或Snipper等都需要自定义ContentAdapter.

}