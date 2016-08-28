package com.kindergarten.util.print;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * This example shows how to print data from a KTableModel. More information
 * about this can be found in the text that is produced by KPrintExample.java.
 * 
 * @author Friederich Kupzog
 * 
 */
public class PrintKTableExample
{
	private Display d;

	public PrintKTableExample()
	{

		d = new Display();

		// create a document with default settings from PageSetup
		PDocument doc = new PDocument("KTable printing example");

		// put some header text on it
		PTextBox t;

		t = new PTextBox(doc);
		t.setText("KTABLE PRINTING EXAMPLE");

		new PVSpace(doc, 0.1);
		new PHLine(doc, 0.02, SWT.COLOR_BLACK);
		new PVSpace(doc, 0.5);

		// create the table
		PTable table = new PTable(doc);
		table.setModel(new ExampleTableModel());
		table.setBoxProvider(new PTableBoxProvider());

		PrintPreview pr = new PrintPreview(null, "Test", IconSource.getImage("print"), doc);
		pr.open();
		d.dispose();
	}

	/**
	 * This function would print the document witout the print preview.
	 * 
	 * @param doc
	 */
	public void print(PDocument doc)
	{
		PrintDialog dialog = new PrintDialog(null, SWT.BORDER);
		PrinterData data = dialog.open();
		if (data == null)
			return;
		if (data.printToFile)
		{
			data.fileName = "print.out"; // you probably want to ask the user
			// for a filename
		}

		Printer printer = new Printer(data);
		GC gc = new GC(printer);
		PBox.setParameters(gc, printer, printer.getDPI(), 100);
		if (printer.startJob("DoSys Druckauftrag"))
		{
			printer.startPage();
			doc.layout();
			doc.draw(1);
			printer.endJob();
		}
		gc.dispose();

	}

	public static void main(String[] args)
	{
		new PrintKTableExample();
	}
}

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/**
 * A style for printable objects that that contain text.
 * 
 * @author Friederich Kupzog
 */
class PTextStyle
{

	public static final int ALIGN_LEFT = 1;

	public static final int ALIGN_RIGHT = 2;

	public static final int ALIGN_CENTER = 3;

	protected static HashMap fonts = new HashMap();

	public int fontSize;

	public String fontName;

	public int fontStyle;

	public int fontColor;

	public int textAlign;

	protected double marginLeft;

	protected double marginRight;

	protected double marginTop;

	protected double marginBottom;

	public PTextStyle()
	{
		fontName = "Arial";
		fontStyle = SWT.NORMAL;
		fontSize = 10;
		fontColor = SWT.COLOR_BLACK;
		textAlign = ALIGN_LEFT;

		marginLeft = 0.0;
		marginRight = 0.0;

		marginTop = 0.0;
		marginBottom = 0.0;

	}

	public static void disposeFonts()
	{
		for (Iterator iter = fonts.values().iterator(); iter.hasNext();)
		{
			Font element = (Font) iter.next();
			element.dispose();
		}
		fonts.clear();
	}

	public static PTextStyle getDefaultStyle()
	{
		return new PTextStyle();
	}

	public Font getFont()
	{
		int height = Math.abs(fontSize * PBox.scalingPercent / 100);
		String key = PBox.device.getDPI().x + "|" + PBox.device.getDPI().y + "|" + fontName + "|" + height + "|" + fontStyle;
		Font font = (Font) fonts.get(key);
		if (font != null)
			return font;
		font = new Font(PBox.device, fontName, Math.abs(fontSize * PBox.scalingPercent / 100), fontStyle);
		fonts.put(key, font);
		return font;
	}

	public Color getFontColor()
	{
		return PBox.device.getSystemColor(fontColor);
	}

	/**
	 * @return double
	 */
	public double getMarginLeft()
	{
		return marginLeft;
	}

	/**
	 * @return double
	 */
	public double getMarginRight()
	{
		return marginRight;
	}

	/**
	 * Sets the marginLeft.
	 * 
	 * @param marginLeft
	 *            The marginLeft to set
	 */
	public void setMarginLeft(double marginLeft)
	{
		this.marginLeft = marginLeft;
	}

	/**
	 * Sets the marginRight.
	 * 
	 * @param marginRight
	 *            The marginRight to set
	 */
	public void setMarginRight(double marginRight)
	{
		this.marginRight = marginRight;
	}

	/**
	 * @return double
	 */
	public double getMarginBottom()
	{
		return marginBottom;
	}

	/**
	 * @return double
	 */
	public double getMarginTop()
	{
		return marginTop;
	}

	/**
	 * Sets the marginBottom.
	 * 
	 * @param marginBottom
	 *            The marginBottom to set
	 */
	public void setMarginBottom(double marginBottom)
	{
		this.marginBottom = marginBottom;
	}

	/**
	 * Sets the marginTop.
	 * 
	 * @param marginTop
	 *            The marginTop to set
	 */
	public void setMarginTop(double marginTop)
	{
		this.marginTop = marginTop;
	}

}

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

class PTextPart
{
	public Point origin;

	public int startLine;

	public int numOfLines;
}

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/**
 * Allows to print KTable objects.
 * 
 * You have to specify a KTableModel and a PTableBoxProvider.
 * 
 * @author Friederich Kupzog
 */
class PTable
{

	protected KTableModel model;

	protected PTableBoxProvider boxProvider;

	protected PContainer parent;

	public PTable(PContainer parent)
	{
		this.parent = parent;
	}

	protected void fillDocument()
	{
		boolean abgeschnitten = false;
		// Zeilen
		for (int i = 0; i < model.getRowCount(); i++)
		{
			// System.out.println("Spalte "+i);
			int height = model.getRowHeight();
			if (i == 0)
				height = model.getFirstRowHeight();

			double width = parent.getPossibleWidth();

			// Spalten
			for (int j = 0; j < model.getColumnCount(); j++)
			{
				// System.out.println(" Zeile "+j);
				int style = PBox.POS_RIGHT | PBox.ROW_ALIGN;
				if (j == 0)
					style = PBox.POS_BELOW | PBox.ROW_ALIGN;

				PBox box = boxProvider.createBox(parent, style, j, i, model.getColumnWidth(j), height,
						(model.getFixedColumnCount() > j || model.getFixedRowCount() > i), model.getContentAt(j, i));
				double boxWidth = Math.max(box.minCm, parent.getPossibleWidth() * box.hWeight);
				width -= boxWidth;
				if (width < 0)
				{
					box.dispose();
					abgeschnitten = true;
					break;
				}
			}
		}
		if (abgeschnitten)
			MsgBox.show("Tabelle ist zu breit fur die Seite\n" + "und wird deshalb abgeschnitten.");

	}

	/**
	 * @return PTableBoxProvider
	 */
	public PTableBoxProvider getBoxProvider()
	{
		return boxProvider;
	}

	/**
	 * @return KTableModel
	 */
	public KTableModel getModel()
	{
		return model;
	}

	/**
	 * Sets the boxProvider.
	 * 
	 * @param boxProvider
	 *            The boxProvider to set
	 */
	public void setBoxProvider(PTableBoxProvider boxProvider)
	{
		this.boxProvider = boxProvider;
		if (this.boxProvider != null && this.model != null)
		{
			fillDocument();
		}
	}

	/**
	 * Sets the model.
	 * 
	 * @param model
	 *            The model to set
	 */
	public void setModel(KTableModel model)
	{
		this.model = model;
		if (this.boxProvider != null && this.model != null)
		{
			fillDocument();
		}
	}

}

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/**
 * A style for printable objects.
 * 
 * @author Friederich Kupzog
 */
class PStyle
{

	public double[] lines;

	public int lineColor;

	public int backColor;

	public PStyle()
	{
		lines = new double[4];
		lines[0] = 0.0;
		lines[1] = 0.0;
		lines[2] = 0.0;
		lines[3] = 0.0;
		lineColor = SWT.COLOR_BLACK;
		backColor = SWT.COLOR_WHITE;

		// setDebugStyle();
	}

	private void setDebugStyle()
	{
		lines[0] = 0.01;
		lines[1] = 0.01;
		lines[2] = 0.01;
		lines[3] = 0.01;
		lineColor = SWT.COLOR_GRAY;
	}

	public static PStyle getDefaultStyle()
	{
		return new PStyle();
	}

	public int getLineWidth(int num)
	{
		int pixel = 0;
		if (num == 0 || num == 2)
			pixel = PBox.pixelY(lines[num]);
		if (num == 1 || num == 3)
			pixel = PBox.pixelX(lines[num]);
		if (pixel < 0)
			return 0;
		if (pixel == 0)
			return 1;
		return pixel;
	}

	public boolean hasLine(int num)
	{
		return lines[num] > 0;
	}

	public Color getLineColor()
	{
		return PBox.device.getSystemColor(lineColor);
	}

	public Color getBackColor()
	{
		return PBox.device.getSystemColor(backColor);
	}

}

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/**
 * @author Friederich Kupzog A PTextStyle that is easy to create.
 */
class PSimpleTextStyle extends PTextStyle
{
	public PSimpleTextStyle(String fontname, int size, boolean bold)
	{
		super();
		this.fontName = fontname;
		this.fontSize = size;
		if (bold)
			this.fontStyle = SWT.BOLD;
	}

}

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/*
 * This feature was contributed by Onsel Armagan, Istanbul, Turkey Thanks a lot!
 */

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/**
 * A text box that shows the current page number.
 * 
 * @author Friederich Kupzog
 */
class PPageNumber extends PTextBox
{
	protected static int pageNumber = 0;

	/**
	 * @param parent
	 * @param style
	 */
	public PPageNumber(PContainer parent, int style)
	{
		super(parent, style, -1, 0);
		setText("    ");
	}

	public void draw(int page, Point originOffset)
	{

		if (layoutIsOnPage(page))
		{
			super.draw(page, originOffset);
			gc.setFont(textStyle.getFont());
			gc.setForeground(textStyle.getFontColor());
			gc.drawText("" + pageNumber, origin.x + originOffset.x + pixelX(textStyle.getMarginLeft()), origin.y + originOffset.y, true);
		}
	}

}

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/**
 * forces a page break at the current position in the tree of printable elements
 * 
 * @author Friederich Kupzog
 */
class PPageBreak extends PBox
{

	public PPageBreak(PContainer parent)
	{
		super(parent);
	}

	/*
	 * overridden from superclass
	 */
	protected int getWidth()
	{
		return 0;
	}

	/*
	 * overridden from superclass
	 */
	protected int layoutHowMuchWouldYouOccupyOf(Point spaceLeft, int page)
	{
		return -1;
	}

	/*
	 * overridden from superclass
	 */
	public void draw(int page, Point originOffset)
	{
	}

}

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/**
 * A printable text label. If you need page breaks within the text of a box, use
 * PTextBox instead. CAUTION: A PLittleTextBox with too much text for one entire
 * page causes the layout process to hang in an endless loop.
 * 
 * @author Friederich Kupzog For more details
 * @see PDocument and
 * @see PBox
 */
class PLittleTextBox extends PBox
{

	protected String text;

	protected PTextStyle textStyle;

	protected ArrayList textLines;

	/**
	 * Creates a non-wrapping text box with a fixed size according to its text.
	 * 
	 * @param parent
	 * @param style
	 */
	public PLittleTextBox(PContainer parent)
	{
		super(parent);
		init();
	}

	/**
	 * Creates a non-wrapping text box with a fixed size according to its text.
	 * 
	 * @param parent
	 * @param style
	 */
	public PLittleTextBox(PContainer parent, int style)
	{
		super(parent, style);
		init();
	}

	/**
	 * Creates a text box with wrapping capabilities if hWeight is > 0.
	 * 
	 * @param parent
	 * @param style
	 * @param hWeight
	 *            Specify -1 for a non-wrapping text box (If the text has
	 *            newlines it will be a multi-line textbox). Spezify a number
	 *            between 0 and 1 for a multiline textbox that consumes the
	 *            given fraction of the available document width.
	 * @param minWidth
	 *            This allows you to specify a minimum width for the text. The
	 *            text box will consume some space depending to hWeight or its
	 *            text if hWeight is -1, but at least the given amount of
	 *            centimeters. For a box with a fixed width for example set
	 *            hWeigth = 0 and specify a non-zero minWidth.
	 */
	public PLittleTextBox(PContainer parent, int style, double hWeight, double minWidth)
	{
		super(parent, style, hWeight, minWidth);
		init();
	}

	private void init()
	{
		text = "";
		textStyle = PTextStyle.getDefaultStyle();
		textLines = new ArrayList();
	}

	public void setText(String text)
	{
		if (text == null)
			text = "";
		this.text = text;
	}

	/*
	 * overridden from superclass
	 */
	protected int getWidth()
	{
		if (grabbing)
			return grabWidth;
		if (hWeight >= 0 && hWeight <= 1 && minCm >= 0)
		{
			PDocument myDoc = getDocument();
			double maxWidthCm = (myDoc.pageWidth - myDoc.margins[1] - myDoc.margins[3]) * hWeight;
			return Math.max(pixelX(maxWidthCm), pixelX(minCm));
		}

		gc.setFont(textStyle.getFont());
		if (textLines.size() == 0)
			splitIntoLines();
		int erg = 0;
		for (Iterator iter = textLines.iterator(); iter.hasNext();)
		{
			String element = (String) iter.next();
			int w = gc.stringExtent(element).x;
			if (w > erg)
				erg = w;
		}
		erg += pixelX(textStyle.getMarginLeft());
		erg += pixelX(textStyle.getMarginRight());
		erg = Math.max(erg, pixelX(minCm));
		return erg;
	}

	protected void splitIntoLines()
	{
		textLines.clear();
		gc.setFont(textStyle.getFont());

		if ((grabWidth > 0) || (hWeight >= 0 && hWeight <= 1))
		{
			PDocument myDoc = getDocument();
			int maxWidth;

			if (grabWidth > 0)
				maxWidth = grabWidth;
			else
			{
				double maxWidthCm = (myDoc.pageWidth - myDoc.margins[1] - myDoc.margins[3]) * hWeight;
				maxWidth = Math.max(pixelX(maxWidthCm), pixelX(minCm));
			}
			maxWidth -= pixelX(textStyle.getMarginLeft());
			maxWidth -= pixelX(textStyle.getMarginRight());

			boolean fertig = false;
			int start = 0;
			int pos = 0;
			int lastPossibility = start;

			if (text.length() > 0)
			{
				while (!fertig)
				{
					int textLength = 0;
					while (!fertig && textLength < maxWidth)
					{
						if (text.charAt(pos) == ' ')
							lastPossibility = pos;
						if (text.charAt(pos) == '-')
							lastPossibility = pos;
						if (text.charAt(pos) == '\n')
						{
							textLines.add(text.substring(start, pos));
							start = pos + 1;
							pos = start;
						}
						int testPos = pos + 1;
						if (testPos > text.length())
							testPos = text.length();
						textLength = gc.stringExtent(text.substring(start, testPos)).x;
						if (textLength < maxWidth)
							pos++;
						if (pos >= text.length())
						{
							fertig = true;
						}
					}

					int umbruchPos = pos;
					if (lastPossibility > start && !fertig)
						umbruchPos = lastPossibility + 1;

					textLines.add(text.substring(start, umbruchPos));

					if (!fertig)
					{
						start = umbruchPos;
						if (start >= text.length())
						{
							fertig = true;
						} else
						{
							pos = start;
							lastPossibility = start;
						}
					}
				}
			}

		} else
		{
			textLines.add(text);
		}
	}

	/*
	 * overridden from superclass
	 */
	protected void layoutResetTuning()
	{
		super.layoutResetTuning();
		textLines.clear();
	}

	/*
	 * overridden from superclass
	 */
	protected int getHeight()
	{
		if (forcedHeight > 0)
			return forcedHeight;
		if (textLines.size() == 0)
			splitIntoLines();
		gc.setFont(textStyle.getFont());
		int lineHeight = gc.stringExtent("A").y;

		return (textLines.size() * lineHeight) + pixelY(textStyle.getMarginTop() + textStyle.getMarginBottom());
	}

	public void draw(int page, Point originOffset)
	{

		if (layoutIsOnPage(page))
		{
			super.draw(page, originOffset);
			Font font = textStyle.getFont();
			gc.setFont(font);
			gc.setForeground(textStyle.getFontColor());

			int lineHeight = gc.stringExtent("A").y;

			for (int i = 0; i < textLines.size(); i++)
			{

				int alignPixel = 0;
				if (textStyle.textAlign == PTextStyle.ALIGN_CENTER)
				{
					int textWidth = gc.stringExtent((String) textLines.get(i)).x;
					alignPixel = (getWidth() - pixelX(textStyle.getMarginLeft()) - pixelX(textStyle.getMarginRight()) - textWidth) / 2;
				} else if (textStyle.textAlign == PTextStyle.ALIGN_RIGHT)
				{
					int textWidth = gc.stringExtent((String) textLines.get(i)).x;
					alignPixel = (getWidth() - pixelX(textStyle.getMarginLeft()) - pixelX(textStyle.getMarginRight()) - textWidth);
				}

				gc.drawText((String) textLines.get(i), origin.x + alignPixel + originOffset.x + pixelX(textStyle.getMarginLeft()), origin.y + originOffset.y
						+ pixelY(textStyle.getMarginTop()) + (i * lineHeight), true);

			}

		}
	}

	/**
	 * @return PTextStyle
	 */
	public PTextStyle getTextStyle()
	{
		return textStyle;
	}

	/**
	 * Sets the textStyle.
	 * 
	 * @param textStyle
	 *            The textStyle to set
	 */
	public void setTextStyle(PTextStyle textStyle)
	{
		this.textStyle = textStyle;
	}

}

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/**
 * A printable Image label.
 * 
 * @author Friederich Kupzog For more details
 * @see PDocument
 */
class PImageBox extends PBox
{

	protected String imgName;

	protected int imgDPI;

	protected Image image;

	protected Point imgOriginalSize;

	protected Point imgTargetSize;

	/**
	 * @param parent
	 * @param style
	 */
	public PImageBox(PContainer parent, int style)
	{
		super(parent, style);
	}

	/**
	 * Sets the Image. The size of the image will be calculated by using the dpi
	 * parameter, in which you can specify which resolution is the "native"
	 * image resulotion. If you e.g. specify 600 dpi and print on a 600 dpi
	 * printer, the image will not be resized. If you print it on an 300 dpi
	 * printer, it will be resized.
	 * 
	 * @param name
	 * @param dpi
	 */
	public void setImage(String name, int dpi)
	{
		this.imgName = name;
		this.imgDPI = dpi;
		image = null;
		imgOriginalSize = null;
		imgTargetSize = null;
	}

	/*
	 * overridden from superclass
	 */
	protected void layoutResetTuning()
	{
		super.layoutResetTuning();
		image = null;
		imgOriginalSize = null;
		imgTargetSize = null;
	}

	protected Point calcSize()
	{
		try
		{
			Class clazz = new Object().getClass();
			InputStream is = clazz.getResourceAsStream(imgName);
			image = new Image(device, is);

			imgOriginalSize = new Point(0, 0);
			imgOriginalSize.x = image.getImageData().width;
			imgOriginalSize.y = image.getImageData().height;

			imgTargetSize = new Point(0, 0);
			imgTargetSize.x = (int) (imgOriginalSize.x * scalingPercent / 100 * (pixelPerInch.x / (double) imgDPI));
			imgTargetSize.y = (int) (imgOriginalSize.y * scalingPercent / 100 * (pixelPerInch.y / (double) imgDPI));

			sizeCalculatedfor = scalingPercent;

			image.getImageData().transparentPixel = -1;
			image.getImageData().maskData = null;

			return imgTargetSize;

		} catch (Exception e1)
		{
			System.out.println("could not open ressource " + imgName);
			imgOriginalSize = new Point(10, 10);
			imgTargetSize = new Point(10, 10);
			return imgTargetSize;
		}

	}

	/*
	 * overridden from superclass
	 */
	protected int layoutHowMuchWouldYouOccupyOf(Point spaceLeft, int page)
	{
		if (layoutAlreadyFinished())
			return 0;
		if (sizeCalculatedfor != scalingPercent || image == null)
		{
			calcSize();
		}
		// System.out.println("Size: "+imgTargetSize.y+" Space: "+spaceLeft.y);
		if (imgTargetSize.y > spaceLeft.y)
			return -1;
		return imgTargetSize.y;
	}

	/*
	 * overridden from superclass
	 */
	protected int getWidth()
	{
		if (sizeCalculatedfor != scalingPercent || image == null)
		{
			calcSize();
		}
		return imgTargetSize.x;
	}

	/*
	 * overridden from superclass
	 */
	protected int getHeight()
	{
		if (rowAlign)
			return forcedHeight;
		if (sizeCalculatedfor != scalingPercent || image == null)
		{
			calcSize();
		}
		return imgTargetSize.y;
	}

	/*
	 * overridden from superclass
	 */
	protected int layoutOccupy(Point origin, Point spaceLeft, int page)
	{
		if (layoutAlreadyFinished())
			return 0;
		if (sizeCalculatedfor != scalingPercent || image == null)
		{
			calcSize();
		}
		this.origin = new PagePoint(origin, page);
		return imgTargetSize.y;
	}

	public void draw(int page, Point originOffset)
	{
		if (layoutIsOnPage(page))
		{
			super.draw(page, originOffset);
			if (image != null)
			{
				gc.drawImage(image, 0, 0, imgOriginalSize.x, imgOriginalSize.y, origin.x + originOffset.x, origin.y + originOffset.y, imgTargetSize.x,
						imgTargetSize.y);
			}
		}
	}

}

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/**
 * Horizontal white space
 * 
 * @author Friederich Kupzog
 */
class PHSpace extends PBox
{

	private double cm;

	/**
	 * Creates a new Space
	 */
	public PHSpace(PContainer parent, int style, double cm)
	{
		super(parent, style);
		this.cm = cm;
		// getBoxStyle().backColor = SWT.COLOR_GREEN;
	}

	/*
	 * overridden from superclass
	 */
	protected int getWidth()
	{
		if (grabbing)
			return grabWidth;
		return PBox.pixelX(cm);
	}

	protected int getHeight()
	{
		if (forcedHeight > 0)
			return forcedHeight;
		// return 2;
		return 0;
	}

}

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/**
 * Class that represents a document to print.
 * 
 * To print a document an a GC, the folowing steps are neccessary:
 * 
 * (1) create the document's content by creating a PDocument object and adding
 * children.
 * 
 * (2) setting the GC and its resulution by using the static method
 * PDocument.setParameters
 * 
 * (3) layoutouting the children by a call to PDocument.layout()
 * 
 * (4) drawing the documents pages on a GC by calling PDocument.draw(int page)
 * 
 * (5) disposing the GC and all Font objects by calling PTextStyle.disposeAll.
 * 
 * Note that all this (except (1)) is done by the PrintPreview class.
 * 
 * Example code: <code>
 *     Image newImage = new Image(
 Display.getCurrent(),
 9*40,13*40);
 GC gc = new GC(newImage);
 PBox.setParameters(gc, Display.getCurrent(), d.getDPI());
 PDocument c = new PDocument(9,13);
 
 PTextBox t = new PTextBox(c, PBox.BEGINNING);
 t.setText("This is text.");
 
 c.layout();    
 c.draw(1);
 
 gc.dispose();
 
 PTextStyle.disposeAll();
 

 * </code>
 * 
 * @author Friederich Kupzog
 */

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/**
 * A Container class for PBoxes. Used for PDocument, Headers and Footers.
 * 
 * @author Friederich Kupzog
 */
class PContainer
{

	protected ArrayList children;

	protected PDocument doc;

	private int calculatedHeight;

	/**
   * 
   */
	public PContainer(PDocument doc)
	{
		this.doc = doc;
		children = new ArrayList(100);
		calculatedHeight = -1;
	}

	protected double getPossibleWidth()
	{
		return (doc.pageWidth - doc.margins[1] - doc.margins[3]);
	}

	protected void addChild(PBox child)
	{
		children.add(child);
	}

	/**
	 * Returns the height of this Container on the first pages it occupies.
	 * Usually only used with headers and footers. Does only work if the
	 * container is already layouted.
	 * 
	 * @return int
	 */
	protected int getHeight()
	{
		if (calculatedHeight == -1)
			return 0;
		return calculatedHeight;
	}

	protected void layoutResetTuning()
	{
		calculatedHeight = -1;
		for (Iterator iter = children.iterator(); iter.hasNext();)
		{
			PBox element = (PBox) iter.next();
			element.layoutResetTuning();
		}
	}

	/*
	 * Gibt die Anzahl der gefullten Seiten zuruck.
	 */
	protected int layout(int maxHeightForFirstPage, int maxHeightForOtherPages)
	{
		int currentPage = 1;
		Point currentOrgin = new Point(0, 0);
		Point spaceLeft = new Point(PBox.pixelX(getPossibleWidth()), maxHeightForFirstPage);

		resetElementIndex();
		List currentRow = getNextRow();

		// Schleife uber alle Elemente in "Zeilen"
		while (currentRow != null)
		{
			boolean pageBreakNeccessary;
			rowHorizontalLayout(currentRow, spaceLeft);

			do
			{
				pageBreakNeccessary = rowVerticalLayout(currentRow, currentOrgin, spaceLeft, currentPage);
				if (currentPage == 1 && calculatedHeight < currentOrgin.y)
					calculatedHeight = currentOrgin.y;
				if (pageBreakNeccessary)
				{
					currentPage++;
					PPageNumber.pageNumber++;
					currentOrgin = new Point(0, 0);
					spaceLeft = new Point(PBox.pixelX(getPossibleWidth()), maxHeightForOtherPages);
				}
			} while (pageBreakNeccessary);

			currentRow = getNextRow();
		}
		return currentPage;

	}

	/*
	 * Gibt true zuruck, wenn ein Seitenumbruch notig ist. Wird in diesem Falle
	 * von layout() nochmal fur die gleiche Zeile aber die nachste Seite
	 * aufgerufen.
	 */
	private boolean rowVerticalLayout(List row, Point origin, Point spaceLeft, int page)
	{
		int max = 0;
		boolean pageBreakNeccessary = false;
		boolean allOnNextPage = false;

		origin.x = 0;

		// Hohen verarbeiten
		for (Iterator iter = row.iterator(); iter.hasNext();)
		{
			PBox element = (PBox) iter.next();
			if (element instanceof PPageBreak)
				break;

			int height = element.layoutHowMuchWouldYouOccupyOf(spaceLeft, page);
			if (!element.layoutWouldYouFinishWithin(spaceLeft, page))
				pageBreakNeccessary = true;
			if (height < 0)
			{
				allOnNextPage = true;
				max = 0;
				break;
			} else if (height > max)
				max = height;
		}

		if (!allOnNextPage)
		{
			for (Iterator iter = row.iterator(); iter.hasNext();)
			{
				PBox element = (PBox) iter.next();
				if (element instanceof PPageBreak)
				{
					max = spaceLeft.y;
					break;
				}
				element.layoutOccupy(origin, spaceLeft, page);
				if (element.rowAlign)
					element.setForcedHeight(max);
				origin.x += element.getWidth();
			}
			origin.y += max;
			spaceLeft.y -= max;
			origin.x = 0;
			return pageBreakNeccessary;
		}
		return true;
	}

	// Breiten berechnen und setzen
	private void rowHorizontalLayout(List row, Point spaceLeft)
	{
		int numOfGrabbingElements = 0;
		int widthLeft = spaceLeft.x;

		for (Iterator iter = row.iterator(); iter.hasNext();)
		{
			PBox element = (PBox) iter.next();
			if (element.grabbing)
				numOfGrabbingElements++;
			else
				widthLeft -= element.getWidth();
		}

		if (widthLeft < 0)
			widthLeft = 0; // bad practice, but easy...
		if (numOfGrabbingElements > 0)
		{
			int grabWidth = widthLeft / numOfGrabbingElements;

			for (Iterator iter = row.iterator(); iter.hasNext();)
			{
				PBox element = (PBox) iter.next();
				if (element.grabbing)
					element.grabWidth = grabWidth;
			}
		}

	}

	private int elementIndex;

	private List getNextRow()
	{
		if (elementIndex == children.size())
			return null;
		int last = elementIndex;
		int save = elementIndex;

		boolean firstRun = true;

		for (; last < children.size(); last++)
		{
			PBox element = (PBox) children.get(last);
			if (!firstRun && element.below)
				break;
			firstRun = false;
		}
		elementIndex = last;
		return children.subList(save, last);
	}

	private void resetElementIndex()
	{
		elementIndex = 0;
	}

	public void draw(int page, Point origin)
	{
		for (Iterator iter = children.iterator(); iter.hasNext();)
		{
			PBox element = (PBox) iter.next();
			if (element.layoutIsOnPage(page))
			{
				element.draw(page, origin);
			}
		}
	}

}

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/**
 * Abstract superclass for all printable Objects.
 * 
 * For more details
 * 
 * @see PDocument
 * @author Friederich Kupzog
 */
class PBox
{
	// Styles
	/**
	 * Style flag that forces the PBox to be in a new line below the previous
	 * one Cannot be used simultaniously to POS_RIGHT.
	 */
	public static final int POS_BELOW = 1;

	/**
	 * Style flag that forces the PBox to be in line with the previous one.
	 * Cannot be used simultaniously to POS_BELOW.
	 */
	public static final int POS_RIGHT = 2;

	/**
	 * Style flag that can be used additionally to POS_BELOW/POS_RIGHT to tell
	 * the PBox that it shoul consume all available horizontal space on the
	 * page.
	 */
	public static final int GRAB = 4;

	/**
	 * Style flag that is mainly used by tables and forces all PBoxes in a line
	 * to have the same height. All PBoxes in the line should be constructed
	 * with this flag
	 */
	public static final int ROW_ALIGN = 8;

	// static parameters (set by setParameters())
	protected static GC gc = null; // GC used thoughout the drawing system

	protected static Point pixelPerCm = new Point(0, 0);

	protected static Point pixelPerInch = new Point(0, 0);

	protected static Device device = null; // the Device on which the GC works.

	protected static int scalingPercent = 100; // The scaling factor (used for

	// previews in different sizes)

	// member variables
	// Misc:
	protected PContainer parent;

	protected PStyle boxStyle;

	protected boolean below;

	protected boolean rowAlign;

	protected int forcedHeight; // this variable is set to a value != 0 by

	// the layout method and determines a fixed pixel height

	// Positioning:
	protected PagePoint origin;

	protected int sizeCalculatedfor;

	protected double hWeight; // -1 = fixed width, see minCm.

	protected double minCm; // -1 = occupy existing space

	protected boolean grabbing;

	protected int grabWidth; // set by the layout function

	// which is able to calculate the width
	// of grabbing poxes.

	// Constructors
	/**
	 * Constructs a Box with default size below the previous one.
	 */
	public PBox(PContainer parent)
	{
		this.parent = parent;
		if (parent != null)
			parent.addChild(this);
		boxStyle = PStyle.getDefaultStyle();

		below = true;
		rowAlign = false;
		origin = new PagePoint();
		sizeCalculatedfor = 0;
		grabbing = false;
		grabWidth = 0;

		hWeight = -1;
		minCm = 0.0;
		forcedHeight = 0;
	}

	/**
	 * Constructs a Box with default size.
	 * 
	 * @param parent
	 * @param style
	 *            Poition: POS_BELOW or POS_RIGHT.
	 */
	public PBox(PContainer parent, int style)
	{
		this(parent);
		below = true;
		if ((style & POS_RIGHT) > 0)
			below = false;
		if ((style & POS_BELOW) > 0)
			below = true;
		if ((style & GRAB) > 0)
			grabbing = true;
		if ((style & ROW_ALIGN) > 0)
			rowAlign = true;
	}

	/**
	 * Constructs a Box with default size.
	 * 
	 * @param parent
	 * @param style
	 *            Poition: POS_BELOW or POS_RIGHT. Also GRAB and/or ROW_ALIGN.
	 * @param hWeight
	 *            Determines, how much of the existing page width should be
	 *            occupied. 1.0 = full page. -1 = fiexed width, see minCm.
	 * @param minCm
	 *            Minimum width in cm. If > 0.0, the box will at least have this
	 *            width.
	 */
	public PBox(PContainer parent, int style, double hWeight, double minCm)
	{
		this(parent, style);
		this.hWeight = hWeight;
		this.minCm = minCm;
	}

	public void dispose()
	{
		parent.children.remove(this);
	}

	/*
	 * Sets the forced height. This means that the PBox will have this height
	 * (in pixel) regardless of how high it wants to be.
	 */
	protected void setForcedHeight(int height)
	{
		forcedHeight = height;
	}

	public void draw(int page, Point originOffset)
	{
		Point originForDrawing = new Point(this.origin.x + originOffset.x, this.origin.y + originOffset.y);

		if (layoutIsOnPage(page))
		{
			int width = getWidth();
			int height = getHeight(page);

			gc.setBackground(boxStyle.getBackColor());
			gc.fillRectangle(originForDrawing.x, originForDrawing.y, width, height);

			gc.setBackground(boxStyle.getLineColor());

			if (boxStyle.hasLine(0))
			{
				gc.fillRectangle(originForDrawing.x, originForDrawing.y, width, boxStyle.getLineWidth(0));
			}
			if (boxStyle.hasLine(1))
			{
				gc.fillRectangle(originForDrawing.x + width - boxStyle.getLineWidth(1), originForDrawing.y, boxStyle.getLineWidth(1), height);
			}
			if (boxStyle.hasLine(2))
			{
				gc.fillRectangle(originForDrawing.x, originForDrawing.y + height - boxStyle.getLineWidth(2), width, boxStyle.getLineWidth(2));
			}
			if (boxStyle.hasLine(3))
			{
				gc.fillRectangle(originForDrawing.x, originForDrawing.y, boxStyle.getLineWidth(3), height);
			}

			gc.setBackground(boxStyle.getBackColor());
		}
	}

	/**
	 * Returns the elements PDocument.
	 * 
	 * @return PDocument
	 */
	public PDocument getDocument()
	{
		return parent.doc;
	}

	// /////////////////////////////////////////////////////////////////
	// LAYOUT API
	// /////////////////////////////////////////////////////////////////

	/*
	 * Some elements can occupy more than one Page. Therefore this function
	 * tests if the element has a part on the given page. @param page @return
	 * boolean
	 */
	protected boolean layoutIsOnPage(int page)
	{
		return (page == origin.page);
	}

	/*
	 * Returns the space in y-direction the Element would occupy of the rest of
	 * the page if told so. Convention: this method can be called several times
	 * for one page, but only until layoutOccupy is called once for this page.
	 * 
	 * @param spaceLeft @return int -1, if the element deciedes not to have any
	 * part on the given page
	 */
	protected int layoutHowMuchWouldYouOccupyOf(Point spaceLeft, int page)
	{
		if (layoutAlreadyFinished())
			return 0;
		if (getHeight() > spaceLeft.y)
			return -1;
		return getHeight();
	}

	protected boolean layoutAlreadyFinished()
	{
		return origin.page > 0;
	}

	/*
	 * Returns true if the box would fit or at least finish into/within the
	 * given space in y-direction. Convention: this method can be called several
	 * times for one page, but only until layoutOccupy is called once for this
	 * page. @param spaceLeft
	 */
	protected boolean layoutWouldYouFinishWithin(Point spaceLeft, int page)
	{
		if (getHeight() > spaceLeft.y)
			return false;
		return true;
	}

	/*
	 * Tells the element to occupy the given space on the page. Returns the
	 * space in y-direction the Element occupys of the rest of the page.
	 * Convention: this method is only called once for one page, and after this
	 * call there will be no further layoutHowMuchWouldYouOccpy-calls for this
	 * page. @param spaceLeft @return int
	 */
	protected int layoutOccupy(Point origin, Point spaceLeft, int page)
	{
		if (!layoutAlreadyFinished())
		{
			this.origin.page = page;
			this.origin.x = origin.x;
			this.origin.y = origin.y;
		}
		return getHeight();
	}

	/*
	 * use this method to make all tuning variables unvalid and so force a
	 * recalculation of these variables.
	 */
	protected void layoutResetTuning()
	{
		sizeCalculatedfor = 0;
		origin.page = 0;
		forcedHeight = 0;
	}

	/*
	 * Gives the horizontal size of the element. (has only to work AFTER the
	 * layout process, is used by draw().)
	 */
	protected int getWidth()
	{
		if (grabbing)
			return grabWidth;
		if (hWeight < 0)
			return pixelX(minCm);
		return Math.max(pixelX(minCm), pixelX(parent.getPossibleWidth() * hWeight));
	}

	/*
	 * Gives the vertical size of the element. Used by all layout* functions. If
	 * multipage functionallity is needed, this mechanism does no longer work.
	 * Use/overwrite getHeight(int page) instead.
	 */
	protected int getHeight()
	{
		if (forcedHeight > 0)
			return forcedHeight;
		return 0;
	}

	protected int getHeight(int page)
	{
		if (origin.page == page)
		{
			if (rowAlign)
				return forcedHeight;
			return getHeight();
		}
		return 0;
	}

	// /////////////////////////////////////////////////////////////////
	// STATIC API
	// /////////////////////////////////////////////////////////////////

	protected static int pixelX(double cm)
	{
		long tmp = Math.round(cm * pixelPerCm.x * scalingPercent / 100);
		return (int) tmp;
	}

	protected static int pixelY(double cm)
	{
		long tmp = Math.round(cm * pixelPerCm.y * scalingPercent / 100);
		return (int) tmp;
	}

	/**
	 * Sets the main parameters for a document to print.
	 * 
	 * @param theGC
	 * @param theDevice
	 * @param dpi
	 */
	public static void setParameters(GC theGC, Device theDevice, Point dpi, int percent)
	{
		gc = theGC;
		device = theDevice;
		scalingPercent = percent;
		pixelPerInch = dpi;
		pixelPerCm = new Point((int) Math.round(dpi.x / 2.54), (int) Math.round(dpi.y / 2.54));
	}

	/**
	 * @return PStyle
	 */
	public PStyle getBoxStyle()
	{
		return boxStyle;
	}

	/**
	 * Sets the boxStyle.
	 * 
	 * @param boxStyle
	 *            The boxStyle to set
	 */
	public void setBoxStyle(PStyle boxStyle)
	{
		this.boxStyle = boxStyle;
	}

}

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/**
 * A static data storage and a GUI dialog to change the data. PDocument uses the
 * settings of the static variables in PageSetup. You can open a Dialog to
 * changes these values by creating a PageSetup and call the open() function.
 * 
 * @author Friederich Kupzog
 */
class PageSetup extends KDialog
{
	protected Composite root;

	private Combo combFormat, cmbScalierung, cmbMargin;

	private Button butPortrait, butLandscape;

	public final static int MARGIN_SMALL = 0;

	public final static int MARGIN_MEDIUM = 1;

	public final static int MARGIN_HUGE = 2;

	public final static String[] formatNames =
	{ "A3", "A4", "A5" };

	public final static String[] scalings =
	{ "100%", "90%", "80%", "70%", "60%", "50%" };

	public static double paperHeight = 29.6;

	public static double paperWidth = 20.6;

	public static String format = "A4";

	public static boolean portrait = true;

	public static int scaling = 100;

	public static int marginStyle = MARGIN_MEDIUM;

	public PageSetup(Shell parent)
	{
		super(parent, "Seite einrichten", IconSource.getImage("print"));
		createContents();

		setDialogImage(IconSource.getImage("SeiteEinrichten"));
		addButtonRight("OK", "", true);
		addButtonRight("Abbrechen", "");
		combFormat.setText(format);
	}

	public int getShellStyle()
	{
		return SWT.CLOSE | SWT.APPLICATION_MODAL;
	}

	protected void createContents()
	{
		guiMainArea.setLayout(new FillLayout());
		root = new Composite(guiMainArea, SWT.NONE);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 10;
		gridLayout.numColumns = 2;
		root.setLayout(gridLayout);

		{
			final Label l = new Label(root, SWT.NONE);
			l.setText("Papierformat:");
			final GridData gridData_2 = new GridData();
			gridData_2.widthHint = 80;
			l.setLayoutData(gridData_2);
		}
		{
			combFormat = new Combo(root, SWT.BORDER | SWT.READ_ONLY);
			combFormat.setToolTipText("Bestimmt die PapiergroBe. Diese muss mit der Druckereinstellung ubereinstimmen.");
			for (int i = 0; i < formatNames.length; i++)
			{
				combFormat.add(formatNames[i]);
			}
			combFormat.setText(format);

			final GridData gridData_1 = new GridData(GridData.FILL_HORIZONTAL);
			gridData_1.widthHint = 180;
			combFormat.setLayoutData(gridData_1);
		}
		{
			final Label label = new Label(root, SWT.NONE);
			label.setText("Seitenrander:");
			label.setLayoutData(new GridData(GridData.FILL_BOTH));
		}
		{
			cmbMargin = new Combo(root, SWT.READ_ONLY);
			cmbMargin.setToolTipText("Bestimmt die Breite der Rander.");
			cmbMargin.add("Schmale Rander");
			cmbMargin.add("Normale Rander");
			cmbMargin.add("Breite Rander");
			cmbMargin.select(marginStyle);
			cmbMargin.setLayoutData(new GridData(GridData.FILL_BOTH));
		}
		{
			final Label label = new Label(root, SWT.NONE);
			final GridData gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
			gridData.horizontalSpan = 1;
			label.setLayoutData(gridData);
			label.setText("Ausrichtung:");
		}
		{
			butPortrait = new Button(root, SWT.RADIO);
			butPortrait
					.setToolTipText("Bestimmt, ob das Papier hochkant oder Breit bedruckt werden soll. \nDiese Einstellung muss mit der des Druckers ubereinstimmen");
			butPortrait.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
			butPortrait.setText("Hochformat");
			butPortrait.setSelection(portrait);
		}
		{
			final Label label = new Label(root, SWT.NONE);
		}
		{
			butLandscape = new Button(root, SWT.RADIO);
			butLandscape.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
			butLandscape.setText("Breitformat");
			butLandscape.setSelection(!portrait);
			butLandscape
					.setToolTipText("Bestimmt, ob das Papier hochkant oder quer bedruckt werden soll. \nDiese Einstellung muss mit der des Druckers ubereinstimmen");
		}
		{
			final Label label = new Label(root, SWT.NONE);
			label.setText("Skalierung:");
			label.setLayoutData(new GridData(GridData.FILL_BOTH));
		}
		{
			cmbScalierung = new Combo(root, SWT.READ_ONLY);
			cmbScalierung.setItems(scalings);
			cmbScalierung.select(10 - (scaling / 10));
			cmbScalierung.setLayoutData(new GridData(GridData.FILL_BOTH));
			cmbScalierung.setToolTipText("Hiermit konnen Sie dir GroBe des Ausdrucks veringern, so daB mehr auf eine Seite passt.");
		}

	}

	/*
	 * overridden from superclass
	 */
	protected void onButton(Button button, String buttonText)
	{
		if (buttonText.equals("OK"))
		{
			saveSettings();
		}
		close();
	}

	protected void saveSettings()
	{
		format = combFormat.getText();
		scaling = 100 - 10 * (cmbScalierung.getSelectionIndex());
		marginStyle = cmbMargin.getSelectionIndex();

		portrait = butPortrait.getSelection();

		if (portrait)
		{
			paperHeight = getPaperHeightInCm(format);
			paperWidth = getPaperWidthInCm(format);
		} else
		{
			paperWidth = getPaperHeightInCm(format);
			paperHeight = getPaperWidthInCm(format);
		}

	}

	public static double getPaperHeightInCm(String formatName)
	{
		if (formatName.equals("A5"))
		{
			return 20.8;
		} else if (formatName.equals("A4"))
		{
			return 29.6;
		} else if (formatName.equals("A3"))
		{
			return 41.6;
		}
		return 1.0;
	}

	public static double getPaperWidthInCm(String formatName)
	{
		if (formatName.equals("A5"))
		{
			return 14.8;
		} else if (formatName.equals("A4"))
		{
			return 20.6;
		} else if (formatName.equals("A3"))
		{
			return 29.6;
		}
		return 1.0;
	}

}

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/**
 * Used inside the KPrint implementation
 * 
 * @author Friederich Kupzog
 */
class PagePoint
{

	/**
   * 
   */
	public int page;

	public int x, y;

	public PagePoint()
	{
		x = 0;
		y = 0;
		page = 0;
	}

	public PagePoint(Point p, int page)
	{
		x = p.x;
		y = p.y;
		this.page = page;
	}

}

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */
/**
 * A Message Box class used to display messages.
 * 
 * @author Friederich Kupzog
 */
class MsgBox
{

	/**
   * 
   */
	private Display d;

	private Shell s;

	private Label bild, meldung;

	private Control additionalControl;

	private boolean ende;

	/**
	 * Der Text des Buttons, der vom Benutzer betatigt wurde
	 */
	public String pressedButton;

	public MsgBox(Display d, String title, String message, String buttons)
	{
		this.d = d;
		this.s = new Shell(d, SWT.TITLE | SWT.APPLICATION_MODAL);
		this.s.setText(title);
		additionalControl = null;
		ende = false;

		FormLayout fl = new FormLayout();
		this.s.setLayout(fl);

		bild = new Label(this.s, SWT.LEFT);
		bild.setImage(IconSource.getImage("MsgBox"));
		bild.setBackground(d.getSystemColor(SWT.COLOR_WHITE));

		FormData f = new FormData();
		f.top = new FormAttachment(0, 0);
		f.left = new FormAttachment(0, 0);
		f.bottom = new FormAttachment(100, 0);
		bild.setLayoutData(f);

		Label separator = new Label(this.s, SWT.SEPARATOR);

		f = new FormData();
		f.top = new FormAttachment(0, 0);
		f.left = new FormAttachment(bild, 0);
		f.bottom = new FormAttachment(100, 0);
		separator.setLayoutData(f);

		meldung = new Label(s, SWT.LEFT | SWT.WRAP);
		meldung.setText(message);

		f = new FormData();
		f.top = new FormAttachment(0, 25);
		f.left = new FormAttachment(bild, 25);
		f.right = new FormAttachment(100, -25);
		f.bottom = new FormAttachment(100, -55);
		meldung.setLayoutData(f);

		ButtonBar butBar = new ButtonBar(s, 80);
		StringTokenizer t = new StringTokenizer(buttons, ",");
		boolean first = true;
		while (t.hasMoreTokens())
		{
			Button but = butBar.addButton(t.nextToken(), "", new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent e)
				{
					pressedButton = ((Button) e.getSource()).getText();
					ende = true;
				}
			});
			if (first)
			{
				first = false;
				s.setDefaultButton(but);
			}
		}
		f = new FormData();
		f.bottom = new FormAttachment(100, -4);
		f.left = new FormAttachment(bild, 15);
		f.right = new FormAttachment(100, -15);
		butBar.setLayoutData(f);

	}

	/**
	 * Erlaubt das Hinzufugen weiterer Steuerelemente zur MsgBox. Diese werden
	 * unter dem Text und uber der Buttonleiste engezeigt.
	 * 
	 * Benutzung: MsgBox box = new MsgBox(display,"Box","Beispiel","OK"); Text
	 * feld = new Text(box.getShell(),SWT.BORDER); box.addControl(feld);
	 * box.open(); (hier Zugriff auf feld) box.dispose();
	 * 
	 * @param c
	 *            das anzuzeigende Control.
	 */
	public void addControl(Control c)
	{
		// Meldung neu abstutzen
		FormData f = new FormData();
		f.top = new FormAttachment(0, 25);
		f.left = new FormAttachment(bild, 25);
		f.right = new FormAttachment(100, -25);
		// f.bottom = new FormAttachment(100,-55);
		meldung.setLayoutData(f);

		// Neues Control layouten
		f = new FormData();
		f.top = new FormAttachment(meldung, 5);
		f.left = new FormAttachment(bild, 25);
		f.right = new FormAttachment(100, -25);
		f.bottom = new FormAttachment(100, -55);
		c.setLayoutData(f);
		additionalControl = c;

	}

	public void setImage(Image newImg)
	{
		bild.setImage(newImg);
	}

	/**
	 * Gibt die Shell der MsgBox zuruck.
	 * 
	 * @return Shell
	 */
	public Shell getShell()
	{
		return s;
	}

	/**
	 * Zeigt die MsgBox an.
	 */
	public void open()
	{
		s.pack();
		s.setLocation((d.getBounds().width - s.getBounds().width) / 2, (d.getBounds().height - s.getBounds().height) / 2);

		s.open();

		if (additionalControl != null)
			additionalControl.setFocus();

		while (!ende)
		{
			if (!d.readAndDispatch())
				d.sleep();
		}
	}

	/**
	 * Muss nach box.open() aufgerufen werden!
	 */
	public void dispose()
	{
		s.close();
		s.dispose();
	}

	/**
	 * Baut eine fertige MsgBox auf und zeigt diese an.
	 * 
	 * @param d
	 * @param title
	 * @param message
	 * @param buttons
	 * @return String
	 */

	public static String show(Display d, String title, String message, String buttons)
	{
		MsgBox box = new MsgBox(d, title, message, buttons);
		box.open();
		box.dispose();
		return box.pressedButton;

	}

	/**
	 * Baut eine fertige MsgBox auf und zeigt diese an
	 * 
	 * @param d
	 * @param message
	 * @return String
	 */
	public static String show(Display d, String message)
	{
		return show(d, "Meldung", message, "OK");
	}

	public static String show(String title, String message, String buttons)
	{
		MsgBox box = new MsgBox(Display.getCurrent(), title, message, buttons);
		box.open();
		box.dispose();
		return box.pressedButton;

	}

	public static String show(String message)
	{
		return show("Meldung", message, "OK");
	}

}

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */
class KPrintExample
{

	/**
	 * This example shows how to create well-layouted text and graphics using
	 * KPrint classes. The layout is completely paper-format independent and
	 * works with different paper sizes. (Try it by selecting A3 / A5 in the
	 * page setup dialog)
	 */

	private Display d;

	public KPrintExample()
	{

		d = new Display();

		// we call the page setup dialog so that the user can
		// do adjustments if he likes.
		// In an application with a main window and a menu bar
		// this should not be neccessary.
		PageSetup p = new PageSetup(null);
		p.open();

		// Now we create an example document using the page setup settings
		PDocument doc = new PDocument("KPrint example");

		// the following functions create the text
		generateHeader(doc);
		generateFooter(doc);
		generateTitle(doc);
		generateSubtitle(doc, "Introduction");

		generateParagraph(doc, "KPrint is a collection of Java classes that allows " + "the user to generate print layouts within the SWT framework. The way "
				+ "a layout is created is comparable to the " + "technique used to generate GUI layouts in SWT. You have some "
				+ "global container (Shell in SWT, PDocument in KPrint), on which " + "you can place other elements (PBoxes).");

		generateSubtitle(doc, "The KPrint layout concept");

		generateParagraph(doc, "In SWT, you use Layouts " + "to determine how the widgets are arranged on the window. KPrint "
				+ "has just one simple layout concept.");
		generateParagraph(doc, "The elements you can place on the paper are text " + "boxes, images, lines and whitespace. "
				+ "One element can either be on the right of the previous element or below " + "the previous line of elements.");
		generateImage(doc, "/gfx/fig1.gif", "Figure 1: The position of an element is " + "relative to the position of the previous element and determined by "
				+ "the style flag which can be PBox.POS_RIGHT or PBox.POS_BELOW.");
		generateParagraph(doc, "This layout concept is both simple but powerful. " + "There are some layouts that cannot be generated by this concept, "
				+ "but in most cases one can find a simpler solution that is possible " + "to describe with the KPrint layout concept.");

		new PPageBreak(doc);

		generateSubtitle(doc, "This Text as a KPrint example");

		generateParagraph(doc, "This text is created using the KPrint framework. " + "It shows that KPrint can be used to layout long text passages, e.g. "
				+ "to print help system contents. ");

		generateImage(doc, "/gfx/fig2.gif", "Figure 2: For right adjusted elements "
				+ "you need to put a grabbing PPox, e.g. a PHSpace or a PTextBox in front " + "of them. For an example see the header of this document.");

		generateSubtitle(doc, "Printing KTables");

		generateParagraph(doc, "KPrint offers the PTable class that allows to print KTables. "
				+ "(for more information about KTable see de.kupzog.ktable, www.kupzog.de/fkmk_uk) "
				+ "All you need to print a table is a KTableModel and a PTableBoxProvider. "
				+ "The KTableModel offers the data and the column size information. The "
				+ "box provider is comparable to a cell renderer. It creates a PBox for "
				+ "each table cell. You can use a default box provider which creates " + "a PLittleTextBox or you can implement your own box provider with "
				+ "custom font, colors, borders or that provides PImageBoxes. See the " + "PrintKTableExample class for an example how to print data from a "
				+ "KTableModel. You need the KTable.jar from www.kupzog.de/fkmk_uk " + "on your classpath to be able to compile this example. ");

		generateParagraph(doc, "Printing a Table works like that: you just add " + "a PTable object to your document and set its table model and box "
				+ "provider. When the document is layouted, the layout function replaces " + "the PTable object by PBoxes that are fetched from the box "
				+ "provider for each table cell.");

		generateSubtitle(doc, "Printing SWT Tables");

		generateParagraph(doc, "KPrint offers also the possibility to print " + "the PTable SWT tables. It works pretty much like printing KTables, but "
				+ "you will use thw SWTPTable class instead of PTable. Thanks to Onsel Armagan in " + "Istanbul, Turkey for his feature.");

		// at last we can open a print preview
		PrintPreview pr = new PrintPreview(null, "Test", IconSource.getImage("print"), doc);
		pr.open();
		d.dispose();
	}

	private void generateHeader(PDocument doc)
	{
		PTextBox t;
		// We want the companie's logo right-adjusted on the first page

		// - to right-adjust the logo and the text,
		// we need a flexible (grabbing) filler.
		// This can be done with an empty text box.
		new PHSpace(doc.getFirstHeader(), PBox.GRAB, 0);

		// - the logo itself
		PImageBox i = new PImageBox(doc.getFirstHeader(), PBox.POS_RIGHT);
		i.setImage("/gfx/fkmk.gif", 96);

		// - some horizontal space between logo and text
		new PHSpace(doc.getFirstHeader(), PBox.POS_RIGHT, 0.2);

		// - a little Text
		t = new PTextBox(doc.getFirstHeader(), PBox.POS_RIGHT, 0, 3.4);
		t.setText("Friederich Kupzog\nElectronics & Software\nfkmk@kupzog.de\nwww.kupzog.de/fkmk");
		t.getTextStyle().fontSize = 9;

		// - some vertical space below
		new PVSpace(doc.getFirstHeader(), 1);

	}

	private void generateFooter(PDocument doc)
	{
		PTextBox box;

		// a line
		new PVSpace(doc.getFirstFooter(), 0.4);
		new PHLine(doc.getFirstFooter());
		new PVSpace(doc.getFirstFooter(), 0.4);

		// a flexible filler for right-adjustment
		box = new PTextBox(doc.getFirstFooter(), PBox.POS_BELOW | PBox.GRAB);
		box.setText("Generated by KPrintExample.java");

		// the page number
		box = new PPageNumber(doc.getFirstFooter(), PBox.POS_RIGHT);

		// this shall be on all pages:
		doc.setAllFootersLikeFirst();
	}

	private void generateSubtitle(PDocument doc, String text)
	{
		PTextBox t;

		// some vertical space
		new PVSpace(doc, 0.2);

		// the subtitle's text
		t = new PTextBox(doc, PBox.POS_BELOW);
		t.setText(text);
		t.getTextStyle().fontSize = 12;
		t.getTextStyle().fontStyle = SWT.BOLD;

		// some vertical space
		new PVSpace(doc, 0.2);
	}

	private void generateParagraph(PDocument doc, String text)
	{
		PTextBox t;

		// some margin for the text
		new PHSpace(doc, PBox.POS_BELOW, 2);

		// the text grabs the rest of the page width
		t = new PTextBox(doc, PBox.POS_RIGHT | PBox.GRAB);
		t.setText(text);
		t.getTextStyle().fontSize = 10;

		// some vertical space
		new PVSpace(doc, 0.2);

	}

	private void generateImage(PDocument doc, String imgName, String text)
	{
		PTextBox t;

		// some vertical space
		new PVSpace(doc, 0.3);

		// some margin for the image
		new PHSpace(doc, PBox.POS_BELOW, 2);

		PImageBox i = new PImageBox(doc, PBox.POS_RIGHT);
		i.setImage(imgName, 96);

		// some margin for the text
		new PVSpace(doc, 0.3);
		new PVSpace(doc, 0.0);
		new PHSpace(doc, PBox.POS_BELOW, 2);

		// the text grabs the rest of the page width
		t = new PTextBox(doc, PBox.POS_RIGHT | PBox.GRAB);
		t.setText(text);
		t.getTextStyle().fontSize = 8;
		t.getTextStyle().fontStyle = SWT.BOLD;
		t.getTextStyle().fontColor = SWT.COLOR_DARK_GRAY;

		new PVSpace(doc, 0.3);
	}

	private void generateTitle(PDocument doc)
	{
		PTextBox t;
		t = new PTextBox(doc, PBox.POS_BELOW);
		t.setText("Creating print layouts using KPrint");
		t.getTextStyle().fontSize = 15;

		// a line below the text
		new PVSpace(doc, 0.2);
		new PHLine(doc, 0.02, SWT.COLOR_BLACK);
		new PVSpace(doc, 1);
	}

	public static void main(String[] args)
	{
		new KPrintExample();
	}
}

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/**
 * This class is intended for subclassing.
 * 
 * It offers functionality for displaying a picture in the dialog's header and
 * for adding buttons (right- and left-adjusted) in the dialogs footer.
 * Additionally, it offers an easy tool bar creating mechanism.
 * 
 * The events generated by buttons and toolitems can be handled in the method
 * onButton and onToolItem.
 * 
 * If not changed by the customer, the main Layout guiMainLayout has a
 * gridLayout(). Overwrite createMainAreaLayout() to change that.
 * 
 * @author Friederich Kupzog
 */
class KDialog
{
	protected Shell guiShell;

	protected Display guiDisplay;

	protected Composite guiPictureArea;

	protected Composite guiToolBarArea;

	protected Composite guiMainArea;

	protected Composite guiButtonArea;

	protected Button guiLastLeftBut, guiLastRightBut;

	protected Control guiLastToolControl;

	protected Layout guiMainAreaLayout;

	protected Label guiPictureLabel;

	protected ToolBar guiToolBar;

	protected GridData guiPictureGridData;

	protected GridData guiToolBarGridData;

	/**
	 * Cretaes a new, top level dialog.
	 */
	public KDialog()
	{
		createShell(null);
	}

	/**
	 * Creates a new Dialog.
	 * 
	 * @param parent
	 *            The parent shell for this dialog.
	 */
	public KDialog(Shell parent)
	{
		createShell(parent);
	}

	/**
	 * Creates a new Dialog.
	 * 
	 * @param parent
	 *            The parent shell for this dialog.
	 * @param title
	 *            The Dialog's title
	 */
	public KDialog(Shell parent, String title)
	{
		this(parent);
		setTitle(title);
	}

	/**
	 * Creates a new Dialog.
	 * 
	 * @param parent
	 *            The parent shell for this dialog.
	 * @param title
	 *            The Dialog's title
	 * @param icon
	 *            The dialog's window icon.
	 */
	public KDialog(Shell parent, String title, Image icon)
	{
		this(parent);
		setTitle(title);
		setShellImage(icon);
	}

	/*
	 * Baut das Shell-Objekt auf und die Composits der 1. Ebene
	 */
	protected void createShell(Shell parent)
	{
		guiDisplay = Display.getCurrent();
		// Shell
		if (parent != null)
			guiShell = new Shell(parent, getShellStyle());
		else
			guiShell = new Shell(Display.getCurrent(), getShellStyle());
		guiShell.setSize(800, 600);
		createShellLayout();
		createShellComposits();

	}

	protected void createShellComposits()
	{
		// picture area
		guiPictureArea = new Composite(guiShell, SWT.NONE);
		guiPictureGridData = new GridData();
		guiPictureGridData.grabExcessHorizontalSpace = true;
		guiPictureGridData.horizontalAlignment = GridData.FILL;
		guiPictureGridData.heightHint = 0;
		guiPictureArea.setLayoutData(guiPictureGridData);

		// ToolBar area
		guiToolBarArea = new Composite(guiShell, SWT.NONE);
		guiToolBarGridData = new GridData();
		guiToolBarGridData.grabExcessHorizontalSpace = true;
		guiToolBarGridData.horizontalAlignment = GridData.FILL;
		guiToolBarGridData.heightHint = 0;
		guiToolBarArea.setLayoutData(guiToolBarGridData);

		// main area
		guiMainArea = new Composite(guiShell, SWT.NONE);
		createMainAreaLayout();
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = GridData.FILL;
		guiMainArea.setLayoutData(gd);

		// button area
		createButtonBar();
	}

	protected void createButtonBar()
	{
		// AuBeres Composite
		guiButtonArea = new Composite(guiShell, SWT.NONE);
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		guiButtonArea.setLayoutData(gd);

		FormLayout butLayout = new FormLayout();
		guiButtonArea.setLayout(butLayout);

		// Trennlinie
		Label sep = new Label(guiButtonArea, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd = new FormData();
		fd.bottom = new FormAttachment(100, -32);
		fd.left = new FormAttachment(0, 0);
		fd.right = new FormAttachment(100, 0);
		sep.setLayoutData(fd);

	}

	protected void createMainAreaLayout()
	{
		guiMainAreaLayout = new GridLayout();
		((GridLayout) guiMainAreaLayout).makeColumnsEqualWidth = false;
		((GridLayout) guiMainAreaLayout).numColumns = 1;
		guiMainArea.setLayout(guiMainAreaLayout);
	}

	/**
	 * Factorymethod for pre-configured GridData objects
	 * 
	 * Configurates: grabExcessHorizontalSpace = true horizontalAlignment =
	 * GridData.BEGINNING
	 * 
	 * @return GridData
	 */
	public static GridData createGridData()
	{
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.BEGINNING;
		return gd;
	}

	/**
	 * Factorymethod for pre-configured GridData objects
	 * 
	 * Configurates: grabExcessHorizontalSpace = true horizontalAlignment =
	 * GridData.BEGINNING heightHint = hint
	 * 
	 * @return GridData
	 */

	public static GridData createGridDataHHint(int hint)
	{
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.heightHint = hint;
		gd.horizontalAlignment = GridData.BEGINNING;
		return gd;
	}

	/**
	 * Factorymethod for pre-configured GridData objects
	 * 
	 * Configurates: grabExcessHorizontalSpace = true horizontalAlignment =
	 * GridData.FILL horizontalSpan = columns verticalAlignment = GridData.FILL
	 * 
	 * @return GridData
	 */
	public static GridData createGridDataFill(int columns)
	{
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.FILL;
		gd.horizontalSpan = columns;
		return gd;
	}

	/**
	 * Factorymethod for pre-configured GridData objects
	 * 
	 * Configurates: grabExcessHorizontalSpace = true horizontalAlignment =
	 * GridData.FILL horizontalSpan = columns verticalAlignment = GridData.FILL
	 * horizontalIndent = hIndent
	 * 
	 * @return GridData
	 */
	public static GridData createGridDataFill(int columns, int hIndent)
	{
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.FILL;
		gd.horizontalSpan = columns;
		gd.horizontalIndent = hIndent;
		return gd;
	}

	protected void createShellLayout()
	{
		GridLayout layout = new GridLayout(1, true);
		guiShell.setLayout(layout);
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		guiShell.setLayout(layout);
	}

	/**
	 * Returns the style of the dialog. Is also used during shell creation.
	 * Overwrite this method to give your dialog another style.
	 * 
	 * @return int
	 */
	public int getShellStyle()
	{
		return SWT.CLOSE | SWT.RESIZE;
	}

	/**
	 * This method should create the dialogs GUI and is NOT called by the
	 * KDialog constructor. Overwrite this method to build add your own widgets
	 * to the dialog. Do not forget to call it in your own constructor.
	 */
	protected void createContents()
	{
		/*
		 * new Text(guiMainArea,SWT.BORDER); addButton("Ha!",""); addButton("Du
		 * langer Button",""); addButtonRight("Close","");
		 * addButtonRight("Komfort","",true);
		 */
	}

	/**
	 * Overwrite this method, if you whish your dioalog having a specific size
	 * (use guiShell.setSize())
	 */
	protected void doLayout()
	{
		guiShell.pack();
	}

	/**
	 * This method opens the dialogs and processes events until the shell is
	 * closed. You do not need to overwrite this method. Use close() to close
	 * the dialog programmatically.
	 */
	public void open()
	{
		doLayout();
		doPositioning();
		guiShell.open();
		while (!guiShell.isDisposed())
		{
			if (!guiDisplay.readAndDispatch())
				guiDisplay.sleep();
		}
		guiShell.dispose();
	}

	public void close()
	{
		// guiShell.close();
		// guiShell.dispose();
	}

	/**
	 * This method centers the dialog on the screen. Overwrite this method if
	 * you whish another position.
	 */
	protected void doPositioning()
	{
		guiShell.setLocation(0,0);
	}

	/**
	 * Sets the icon of the shell.
	 * 
	 * @param image
	 */
	public void setShellImage(Image image)
	{
		guiShell.setImage(image);
	}

	/**
	 * Sets the window title.
	 * 
	 * @param title
	 */
	public void setTitle(String title)
	{
		guiShell.setText(title);
	}

	/**
	 * Sets the image displayed in the dialogs header.
	 * 
	 * @param image
	 */
	public void setDialogImage(Image image)
	{
		guiPictureArea.setBackground(guiDisplay.getSystemColor(SWT.COLOR_WHITE));
		guiPictureGridData.heightHint = image.getBounds().height + 2;
		GridLayout layout = new GridLayout(1, true);
		guiPictureArea.setLayout(layout);
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;

		guiPictureLabel = new Label(guiPictureArea, SWT.NONE);
		guiPictureLabel.setImage(image);
		// guiPictureLabel.setBackground(guiDisplay.getSystemColor(SWT.COLOR_WHITE));
		GridData gd = new GridData();
		// gd.grabExcessHorizontalSpace = true;
		// gd.horizontalAlignment = GridData.FILL;
		guiPictureLabel.setLayoutData(gd);

		Label line = new Label(guiPictureArea, SWT.SEPARATOR | SWT.HORIZONTAL);
		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		line.setLayoutData(gd);
	}

	/**
	 * Adds a ToolItem to the dialog's toolbar. Creates the toolbar if not
	 * already done.
	 * 
	 * @param name
	 *            The name if the ToolItem. Although this name is never
	 *            displayed to the user you can use it in onToolItem to identify
	 *            the activated ToolItem.
	 * @param tooltip
	 *            The item's tooltip
	 * @param icon
	 *            The icon to show.
	 * @return ToolItem The ToolItem created by this method.
	 */
	public ToolItem addToolItem(String name, String tooltip, Image icon)
	{
		if (guiToolBar == null)
		{
			FormLayout layout = new FormLayout();
			guiToolBarArea.setLayout(layout);
			// layout.horizontalSpacing = 0;
			// layout.verticalSpacing = 0;
			layout.marginHeight = 0;
			layout.marginWidth = 0;

			guiToolBar = new ToolBar(guiToolBarArea, SWT.FLAT);
			FormData fd = new FormData();
			fd.left = new FormAttachment(0, 0);
			fd.top = new FormAttachment(0, 0);
			guiToolBar.setLayoutData(fd);

			Label line = new Label(guiToolBarArea, SWT.SEPARATOR | SWT.HORIZONTAL);
			fd = new FormData();
			fd.left = new FormAttachment(0, 0);
			fd.top = new FormAttachment(guiToolBar, 1);
			fd.right = new FormAttachment(100, 0);
			line.setLayoutData(fd);

			guiLastToolControl = guiToolBar;
		}
		ToolItem ti = new ToolItem(guiToolBar, SWT.PUSH);
		ti.setImage(icon);
		ti.setToolTipText(tooltip);
		ti.setEnabled(true);
		ti.setData(name);
		ti.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				onToolItem((ToolItem) e.widget, (String) e.widget.getData());
			}
		});

		guiToolBarGridData.heightHint = guiToolBarArea.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		return ti;
	}

	public void adjustToToolBar(Control c)
	{
		FormData fd = new FormData();
		fd.left = new FormAttachment(guiLastToolControl, 2);
		fd.top = new FormAttachment(0, 1);
		fd.bottom = new FormAttachment(0, 22);
		c.setLayoutData(fd);
		guiLastToolControl = c;
	}

	protected Button addButton(boolean rightAdjusted, String text, String tip)
	{
		Button erg = new Button(guiButtonArea, SWT.PUSH);
		erg.setText(text);
		erg.setToolTipText(tip);

		Point butPrefferedSize = erg.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		FormData fd = new FormData();
		fd.bottom = new FormAttachment(100, -3);

		if (butPrefferedSize.x > 70)
			fd.width = butPrefferedSize.x + 4;
		else
			fd.width = 70;
		fd.height = 24;
		erg.setLayoutData(fd);

		if (rightAdjusted)
		{
			if (guiLastRightBut == null)
				fd.right = new FormAttachment(100, -3);
			else
				fd.right = new FormAttachment(guiLastRightBut, -3);
			guiLastRightBut = erg;
		} else
		{
			if (guiLastLeftBut == null)
				fd.left = new FormAttachment(0, 3);
			else
				fd.left = new FormAttachment(guiLastLeftBut, 3);
			guiLastLeftBut = erg;
		}

		erg.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent arg0)
			{
				onButton((Button) arg0.widget, ((Button) arg0.widget).getText());
			}
		});
		return erg;
	}

	/**
	 * Puts a Button into the button bar in the dialog's footer (right
	 * adjusted). To handle the event produced by this button see onButton().
	 * 
	 * @param text
	 * @param toolTip
	 * @return Button The Button produced by this method.
	 */
	public Button addButtonRight(String text, String toolTip)
	{
		return addButton(true, text, toolTip);
	}

	/**
	 * Puts a Button into the button bar in the dialog's footer (left adjusted).
	 * To handle the event produced by this button see onButton().
	 * 
	 * @param text
	 * @param toolTip
	 * @return Button The Button produced by this method.
	 */
	public Button addButton(String text, String toolTip)
	{
		return addButton(false, text, toolTip);
	}

	/**
	 * Puts a Button into the button bar in the dialog's footer (right
	 * adjusted). To handle the event produced by this button see onButton().
	 * 
	 * @param text
	 * @param toolTip
	 * @param isDefault
	 *            if true, this button will become the default button
	 * @return Button The Button produced by this method.
	 */
	public Button addButtonRight(String text, String toolTip, boolean isDefault)
	{
		Button erg = addButton(true, text, toolTip);
		if (isDefault)
			guiShell.setDefaultButton(erg);
		return erg;
	}

	/**
	 * Puts a Button into the button bar in the dialog's footer (left adjusted).
	 * To handle the event produced by this button see onButton().
	 * 
	 * @param text
	 * @param toolTip
	 * @param isDefault
	 *            if true, this button will become the default button
	 * @return Button The Button produced by this method.
	 */
	public Button addButton(String text, String toolTip, boolean isDefault)
	{
		Button erg = addButton(false, text, toolTip);
		if (isDefault)
			guiShell.setDefaultButton(erg);
		return erg;
	}

	/*
	 * Button-clicks call this method. Overwrite it to react on button clicks.
	 */
	protected void onButton(Button button, String buttonText)
	{
	}

	/*
	 * clicked ToolItems call this method. Overwrite it to react on button
	 * clicks.
	 */
	protected void onToolItem(ToolItem toolitem, String name)
	{
	}

}

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/**
 * This example shows the basic use of KPrint classes by creating a little 9 x
 * 13 cm greeting card.
 * 
 * @author Friederich Kupzog
 * 
 */
class GreetingCardExample
{
	private Display d;

	public GreetingCardExample()
	{

		d = new Display();

		// create an example document 13 cm x 9 cm with 1 cm borders
		PDocument doc = new PDocument(13, 9, 1, 1, 1, 1, 1.0, "Greeting Card");

		// put some text on it
		PTextBox t;

		t = new PTextBox(doc);
		t.setText("MANY GREETINGS FROM KPRINT");

		new PVSpace(doc, 0.1);
		new PHLine(doc, 0.2, SWT.COLOR_DARK_GREEN);
		new PVSpace(doc, 0.5);

		t = new PTextBox(doc, PBox.POS_BELOW, 0, 5);
		t.setText("We spent a nice time in this 5cm wide left adjusted Textbox.");

		new PHSpace(doc, PBox.POS_RIGHT, 0.3);

		t = new PTextBox(doc, PBox.POS_RIGHT, 0, 5);
		t.setText("The climate was fine and we had a lot of bright views. The class hierachie is unforgetable. Bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla.");

		new PVSpace(doc, 1);

		t = new PTextBox(doc, PBox.POS_BELOW, 1.0, 0);
		t.setText("See you soon: your personal swt programmer on holiday.");
		// Border lines are counted clockwise starting at the top.
		t.getBoxStyle().lines[0] = 0.05; // top
		t.getBoxStyle().lines[2] = 0.05; // bottom
		t.getBoxStyle().lineColor = SWT.COLOR_DARK_GREEN;

		// start a new Page
		new PPageBreak(doc);

		// some more text
		new PHSpace(doc, PBox.POS_BELOW | PBox.GRAB, 0);
		t = new PTextBox(doc, PBox.POS_RIGHT, 0, 1.3);
		t.setText("Affix stamp here");
		t.getBoxStyle().lines[0] = 0.01;
		t.getBoxStyle().lines[1] = 0.01;
		t.getBoxStyle().lines[2] = 0.01;
		t.getBoxStyle().lines[3] = 0.01;
		t.getTextStyle().setMarginBottom(0.1);
		t.getTextStyle().setMarginTop(0.1);
		t.getTextStyle().setMarginLeft(0.1);
		t.getTextStyle().setMarginRight(0.1);

		// open the print preview on this document
		PrintPreview pr = new PrintPreview(null, "Test", IconSource.getImage("print"), doc);
		pr.open();

		// dispose the document in the end
		d.dispose();
	}

	/**
	 * This function would print the document witout the print preview.
	 * 
	 * @param doc
	 */
	public void print(PDocument doc)
	{
		PrintDialog dialog = new PrintDialog(null, SWT.BORDER);
		PrinterData data = dialog.open();
		if (data == null)
			return;
		if (data.printToFile)
		{
			data.fileName = "print.out"; // you probably want to ask the user
			// for a filename
		}

		Printer printer = new Printer(data);
		GC gc = new GC(printer);
		PBox.setParameters(gc, printer, printer.getDPI(), 100);
		if (printer.startJob("DoSys Druckauftrag"))
		{
			printer.startPage();
			doc.layout();
			doc.draw(1);
			printer.endJob();
		}
		gc.dispose();

	}

	public static void main(String[] args)
	{
		new GreetingCardExample();
	}
}

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/**
 * This is an example KTableModel that can be printed using the
 * PrintKTableExample.
 * 
 * @author Kupzog
 */

class ExampleTableModel implements KTableModel
{
	private final static String[][] content =
	{
	{ "Name", "Kupzog", "Hansson", "Walter", "Hutton" },
	{ "Town", "Cologne", "Ystadt", "London", "Brighton" },
	{ "Interest", "programming", "hunting", "rafting", "painting" } };

	public Object getContentAt(int col, int row)
	{
		return content[col][row];
	}

	public KTableCellEditor getCellEditor(int col, int row)
	{
		return null;
	}

	public void setContentAt(int col, int row, Object value)
	{
	}

	public int getRowCount()
	{
		return 5;
	}

	public int getFixedRowCount()
	{
		return 1;
	}

	public int getColumnCount()
	{
		return 3;
	}

	public int getFixedColumnCount()
	{
		return 0;
	}

	public int getColumnWidth(int col)
	{
		return 130;
	}

	public boolean isColumnResizable(int col)
	{
		return false;
	}

	public void setColumnWidth(int col, int value)
	{
	}

	public int getRowHeight()
	{
		return 20;
	}

	public int getFirstRowHeight()
	{
		return 20;
	}

	public boolean isRowResizable()
	{
		return false;
	}

	public int getRowHeightMinimum()
	{
		return 20;
	}

	public void setRowHeight(int value)
	{
	}

	public KTableCellRenderer getCellRenderer(int col, int row)
	{
		return null;
	}

}

abstract class KTableCellEditor
{

	protected KTableModel m_Model;

	protected KTable m_Table;

	protected Rectangle m_Rect;

	protected int m_Row;

	protected int m_Col;

	protected Control m_Control;

	protected String toolTip;

	/**
	 * disposes the editor and its components
	 */
	public void dispose()
	{
		if (m_Control != null)
		{
			m_Control.dispose();
			m_Control = null;
		}
	}

	/**
	 * Activates the editor at the given position.
	 * 
	 * @param row
	 * @param col
	 * @param rect
	 */
	public void open(KTable table, int col, int row, Rectangle rect)
	{
		m_Table = table;
		m_Model = table.getModel();
		m_Rect = rect;
		m_Row = row;
		m_Col = col;
		if (m_Control == null)
		{
			m_Control = createControl();
			m_Control.setToolTipText(toolTip);
			m_Control.addFocusListener(new FocusAdapter()
			{
				public void focusLost(FocusEvent arg0)
				{
					close(true);
				}
			});
		}
		setBounds(m_Rect);
		GC gc = new GC(m_Table);
		m_Table.drawCell(gc, m_Col, m_Row);
		gc.dispose();
	}

	/**
	 * Deactivates the editor.
	 * 
	 * @param save
	 *            If true, the content is saved to the underlying table.
	 */
	public void close(boolean save)
	{
		m_Table.m_CellEditor = null;
		// m_Control.setVisible(false);
		GC gc = new GC(m_Table);
		m_Table.drawCell(gc, m_Col, m_Row);
		gc.dispose();
		this.dispose();
	}

	/**
	 * Returns true if the editor has the focus.
	 * 
	 * @return boolean
	 */
	public boolean isFocused()
	{
		if (m_Control == null)
			return false;
		return m_Control.isFocusControl();
	}

	/**
	 * Sets the editor's position and size
	 * 
	 * @param rect
	 */
	public void setBounds(Rectangle rect)
	{
		if (m_Control != null)
			m_Control.setBounds(rect);
	}

	/*
	 * Creates the editor's control. Has to be overwritten by useful editor
	 * implementations.
	 */
	protected abstract Control createControl();

	protected void onKeyPressed(KeyEvent e)
	{
		if ((e.character == '\r') && ((e.stateMask & SWT.SHIFT) == 0))
		{
			close(true);
		} else if (e.character == SWT.ESC)
		{
			close(false);
		} else
		{
			m_Table.scrollToFocus();
		}
	}

	protected void onTraverse(TraverseEvent e)
	{
		close(true);
		// m_Table.tryToOpenEditorAt(m_Col+1, m_Row);
	}

	/**
	 * @param toolTip
	 */
	public void setToolTipText(String toolTip)
	{
		this.toolTip = toolTip;
	}

}

/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author: Friederich Kupzog fkmk@kupzog.de www.kupzog.de/fkmk
 */

/**
 * Used by MsgBox.
 * 
 * @author Friederich Kupzog
 */

class ButtonBar extends Composite
{

	private RowLayout myLayout;

	private ArrayList myButtons;

	private int myButtonWidth;

	/** Erzeugt neuen ButtonBar */
	public ButtonBar(Composite owner, int buttonWidth)
	{
		super(owner, SWT.NONE);
		myButtonWidth = buttonWidth;
		myLayout = new RowLayout();
		myLayout.justify = true;
		myLayout.type = SWT.HORIZONTAL;
		myLayout.wrap = true;
		myLayout.spacing = 4;
		this.setLayout(myLayout);
		myButtons = new ArrayList();
	}

	/**
	 * Fugt einen Button zur Leiste hinzu. Gibt eine Referenz auf den angelegten
	 * Button zuruck.
	 */
	public Button addButton(String name, String toolTip, SelectionListener selListener)
	{
		Button b = new Button(this, SWT.PUSH);
		b.setText(name);
		b.setToolTipText(toolTip);
		b.setLayoutData(new RowData(myButtonWidth, 25));
		if (selListener != null)
			b.addSelectionListener(selListener);
		myButtons.add(b);
		return b;
	}

	/**
	 * Fugt einen Button zur Leiste hinzu, und registriert ihn bei der in
	 * myShell ubergebenen Shell als DefaultButton.
	 */
	public Button addButton(String name, String toolTip, Shell myShell, SelectionListener selListener)
	{
		Button b = addButton(name, toolTip, selListener);
		myShell.setDefaultButton(b);
		return b;
	}

}

/*******************************************************************************
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Friederich Kupzog - initial API and implementation
 * fkmk@kupzog.de www.kupzog.de/fkmk
 ******************************************************************************/

/**
 * Custom drawn tabel widget for SWT GUIs.
 * 
 * 
 * @see de.kupzog.ktable.KTableModel
 * @see de.kupzog.ktable.KTableCellRenderer
 * @see de.kupzog.ktable.KTableCellEditor
 * @see de.kupzog.ktable.KTableCellSelectionListener
 * 
 *      The idea of KTable is to have a flexible grid of cells to display data
 *      in it. The class focuses on displaying data and not on collecting the
 *      data to display. The latter is done by the KTableModel which has to be
 *      implemented for each specific case. The table asks the table model for
 *      the amount of columns and rows, the sizes of columns and rows and for
 *      the content of the cells which are currently drawn. Even if the table
 *      has a million rows, it won't get slower because it only requests those
 *      cells it currently draws. Only a bad table model can influence the
 *      drawing speed negatively.
 * 
 *      When drawing a cell, the table calls a KTableCellRenderer to do this
 *      work. The table model determines which cell renderer is used for which
 *      cell. A default renderer is available
 *      (KTableCellRenderer.defaultRenderer), but the creation of self-written
 *      renderers for specific purposes is assumed.
 * 
 *      KTable allows to resize columns and rows. Each column can have an
 *      individual size while the rows are all of the same height except the
 *      first row. Multiple column and row headers are possible. These "fixed"
 *      cells will not be scrolled out of sight. The column and row count always
 *      starts in the upper left corner with 0, independent of the number of
 *      column headers or row headers.
 * 
 * @author Friederich Kupzog
 * 
 */
class KTable extends Canvas
{

	// Daten und Datendarstellung
	protected KTableModel m_Model;

	protected KTableCellEditor m_CellEditor;

	// aktuelle Ansicht
	protected int m_TopRow;

	protected int m_LeftColumn;

	// Selection
	protected boolean m_RowSelectionMode;

	protected boolean m_MultiSelectMode;

	protected HashMap m_Selection;

	protected int m_FocusRow;

	protected int m_FocusCol;

	protected int m_ClickColumnIndex;

	protected int m_ClickRowIndex;

	// wichtige MaBe
	protected int m_RowsVisible;

	protected int m_RowsFullyVisible;

	protected int m_ColumnsVisible;

	protected int m_ColumnsFullyVisible;

	// SpaltengroBe
	protected int m_ResizeColumnIndex;

	protected int m_ResizeColumnLeft;

	protected int m_ResizeRowIndex;

	protected int m_ResizeRowTop;

	protected int m_NewRowSize;

	protected boolean m_Capture;

	protected Image m_LineRestore;

	protected int m_LineX;

	protected int m_LineY;

	// sonstige
	protected GC m_GC;

	protected Display m_Display;

	protected ArrayList cellSelectionListeners;

	protected ArrayList cellResizeListeners;

	protected boolean flatStyleSpecified;

	// ////////////////////////////////////////////////////////////////////////////
	// KONSTRUKTOR
	// ////////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a new KTable.
	 * 
	 * possible styles: SWT.V_SCROLL - show vertical scrollbar and allow
	 * vertical scrolling by arrow keys SWT.H_SCROLL - show horizontal scrollbar
	 * and allow horizontal scrolling by arrow keys SWT.FLAT - no border
	 * drawing.
	 * 
	 * After creation a table model should be added using setModel().
	 */
	public KTable(Composite parent, int style)
	{
		// Oberklasse initialisieren
		super(parent, SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE | style);

		// inits
		m_GC = new GC(this);
		m_Display = Display.getCurrent();
		m_Selection = new HashMap();
		m_CellEditor = null;

		flatStyleSpecified = ((style | SWT.FLAT) == style);

		m_RowSelectionMode = false;
		m_MultiSelectMode = false;
		m_TopRow = 0;
		m_LeftColumn = 0;
		m_FocusRow = 0;
		m_FocusCol = 0;
		m_RowsVisible = 0;
		m_RowsFullyVisible = 0;
		m_ColumnsVisible = 0;
		m_ColumnsFullyVisible = 0;
		m_ResizeColumnIndex = -1;
		m_ResizeRowIndex = -1;
		m_ResizeRowTop = -1;
		m_NewRowSize = -1;
		m_ResizeColumnLeft = -1;
		m_Capture = false;
		m_ClickColumnIndex = -1;
		m_ClickRowIndex = -1;

		m_LineRestore = null;
		m_LineX = 0;
		m_LineY = 0;

		cellSelectionListeners = new ArrayList(10);
		cellResizeListeners = new ArrayList(10);

		// Listener
		createListeners();

	}

	protected void createListeners()
	{

		addPaintListener(new PaintListener()
		{
			public void paintControl(PaintEvent event)
			{
				onPaint(event);
			}
		});

		addControlListener(new ControlAdapter()
		{
			public void controlResized(ControlEvent e)
			{
				redraw();
			}
		});

		addMouseListener(new MouseAdapter()
		{
			public void mouseDown(MouseEvent e)
			{
				onMouseDown(e);
			}

			public void mouseUp(MouseEvent e)
			{
				onMouseUp(e);
			}

			public void mouseDoubleClick(MouseEvent e)
			{
				onMouseDoubleClick(e);
			}
		});

		addMouseMoveListener(new MouseMoveListener()
		{
			public void mouseMove(MouseEvent e)
			{
				onMouseMove(e);
			}
		});

		if (getVerticalBar() != null)
		{
			getVerticalBar().addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent e)
				{
					m_TopRow = getVerticalBar().getSelection();
					redraw();
				}

			});
		}

		if (getHorizontalBar() != null)
		{
			getHorizontalBar().addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent e)
				{
					m_LeftColumn = getHorizontalBar().getSelection();
					redraw();
				}
			});
		}
		addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				onKeyDown(e);
			}
		});
	}

	// ////////////////////////////////////////////////////////////////////////////
	// Berechnungen
	// ////////////////////////////////////////////////////////////////////////////

	protected int getFixedWidth()
	{
		int width = 0;
		for (int i = 0; i < m_Model.getFixedColumnCount(); i++)
			width += m_Model.getColumnWidth(i);
		return width;
	}

	protected int getColumnLeft(int index)
	{
		if (index < m_Model.getFixedColumnCount())
		{
			int x = 0;
			for (int i = 0; i < index; i++)
			{
				x += m_Model.getColumnWidth(i);
			}
			return x;
		}
		if (index < m_LeftColumn)
			return -1;
		int x = getFixedWidth();
		for (int i = m_LeftColumn; i < index; i++)
		{
			x += m_Model.getColumnWidth(i);
		}
		return x;
	}

	protected int getColumnRight(int index)
	{
		if (index < 0)
			return 0;
		return getColumnLeft(index) + m_Model.getColumnWidth(index);
	}

	protected int getLastColumnRight()
	{
		return getColumnRight(m_Model.getColumnCount() - 1);
	}

	protected void doCalculations()
	{
		if (m_Model == null)
		{
			ScrollBar sb = getHorizontalBar();
			if (sb != null)
			{
				sb.setMinimum(0);
				sb.setMaximum(1);
				sb.setPageIncrement(1);
				sb.setThumb(1);
				sb.setSelection(1);
			}
			sb = getVerticalBar();
			if (sb != null)
			{
				sb.setMinimum(0);
				sb.setMaximum(1);
				sb.setPageIncrement(1);
				sb.setThumb(1);
				sb.setSelection(1);
			}
			return;
		}

		int m_HeaderHeight = m_Model.getFirstRowHeight();
		int m_RowHeight = m_Model.getRowHeight();

		Rectangle rect = getClientArea();
		if (m_LeftColumn < m_Model.getFixedColumnCount())
		{
			m_LeftColumn = m_Model.getFixedColumnCount();
		}

		if (m_TopRow < m_Model.getFixedRowCount())
		{
			m_TopRow = m_Model.getFixedRowCount();
		}

		int fixedWidth = getFixedWidth();
		int fixedHeight = m_HeaderHeight + (m_Model.getFixedRowCount() - 1) * m_Model.getRowHeight();
		m_ColumnsVisible = 0;
		m_ColumnsFullyVisible = 0;

		if (m_Model.getColumnCount() > m_Model.getFixedColumnCount())
		{
			int runningWidth = getColumnLeft(m_LeftColumn);
			for (int col = m_LeftColumn; col < m_Model.getColumnCount(); col++)
			{
				if (runningWidth < rect.width + rect.x)
					m_ColumnsVisible++;
				runningWidth += m_Model.getColumnWidth(col);
				if (runningWidth < rect.width + rect.x)
					m_ColumnsFullyVisible++;
				else
					break;
			}
		}

		ScrollBar sb = getHorizontalBar();
		if (sb != null)
		{
			if (m_Model.getColumnCount() <= m_Model.getFixedColumnCount())
			{
				sb.setMinimum(0);
				sb.setMaximum(1);
				sb.setPageIncrement(1);
				sb.setThumb(1);
				sb.setSelection(1);
			} else
			{
				sb.setMinimum(m_Model.getFixedColumnCount());
				sb.setMaximum(m_Model.getColumnCount());
				sb.setIncrement(1);
				sb.setPageIncrement(2);
				sb.setThumb(m_ColumnsFullyVisible);
				sb.setSelection(m_LeftColumn);
			}
		}

		m_RowsFullyVisible = Math.max(0, (rect.height - fixedHeight) / m_RowHeight);
		m_RowsFullyVisible = Math.min(m_RowsFullyVisible, m_Model.getRowCount() - m_Model.getFixedRowCount());
		m_RowsFullyVisible = Math.max(0, m_RowsFullyVisible);

		m_RowsVisible = m_RowsFullyVisible + 1;

		if (m_TopRow + m_RowsFullyVisible > m_Model.getRowCount())
		{
			m_TopRow = Math.max(m_Model.getFixedRowCount(), m_Model.getRowCount() - m_RowsFullyVisible);
		}

		if (m_TopRow + m_RowsFullyVisible >= m_Model.getRowCount())
		{
			m_RowsVisible--;
		}

		sb = getVerticalBar();
		if (sb != null)
		{
			if (m_Model.getRowCount() <= m_Model.getFixedRowCount())
			{
				sb.setMinimum(0);
				sb.setMaximum(1);
				sb.setPageIncrement(1);
				sb.setThumb(1);
				sb.setSelection(1);
			} else
			{
				sb.setMinimum(m_Model.getFixedRowCount());
				sb.setMaximum(m_Model.getRowCount());
				sb.setPageIncrement(m_RowsVisible);
				sb.setIncrement(1);
				sb.setThumb(m_RowsFullyVisible);
				sb.setSelection(m_TopRow);
			}
		}
	}

	/**
	 * Returns the area that is occupied by the given cell
	 * 
	 * @param col
	 * @param row
	 * @return Rectangle
	 */
	public Rectangle getCellRect(int col, int row)
	{
		int m_HeaderHeight = m_Model.getFirstRowHeight();
		if ((col < 0) || (col >= m_Model.getColumnCount()))
			return new Rectangle(-1, -1, 0, 0);

		int x = getColumnLeft(col) + 1;
		int y;

		if (row == 0)
			y = 0;
		else if (row < m_Model.getFixedRowCount())
			y = m_HeaderHeight + ((row - 1) * m_Model.getRowHeight());
		else
			y = m_HeaderHeight + (m_Model.getFixedRowCount() - 1 + row - m_TopRow) * m_Model.getRowHeight();
		int width = m_Model.getColumnWidth(col) - 1;
		int height = m_Model.getRowHeight() - 1;
		if (row == 0)
			height = m_Model.getFirstRowHeight() - 1;

		return new Rectangle(x, y, width, height);
	}

	protected boolean canDrawCell(int col, int row, Rectangle clipRect)
	{
		Rectangle r = getCellRect(col, row);
		return canDrawCell(r, clipRect);
	}

	protected boolean canDrawCell(Rectangle r, Rectangle clipRect)
	{
		if (r.y + r.height < clipRect.y)
			return false;
		if (r.y > clipRect.y + clipRect.height)
			return false;
		if (r.x + r.width < clipRect.x)
			return false;
		if (r.x > clipRect.x + clipRect.width)
			return false;
		return true;
	}

	// ////////////////////////////////////////////////////////////////////////////
	// ZEICHNEN
	// ////////////////////////////////////////////////////////////////////////////

	// Paint-Ereignis

	protected void onPaint(PaintEvent event)
	{
		Rectangle rect = getClientArea();
		GC gc = event.gc;

		doCalculations();

		if (m_Model != null)
		{

			drawBottomSpace(gc);
			drawCells(gc, gc.getClipping(), 0, m_Model.getFixedColumnCount(), 0, m_Model.getFixedRowCount());
			drawCells(gc, gc.getClipping(), m_LeftColumn, m_Model.getColumnCount(), 0, m_Model.getFixedRowCount());
			drawCells(gc, gc.getClipping(), 0, m_Model.getFixedColumnCount(), m_TopRow, m_TopRow + m_RowsVisible);
			drawCells(gc, gc.getClipping(), m_LeftColumn, m_Model.getColumnCount(), m_TopRow, m_TopRow + m_RowsVisible);
		} else
		{
			gc.fillRectangle(rect);
		}
	}

	// Bottom-Space

	protected void drawBottomSpace(GC gc)
	{
		Rectangle r = getClientArea();
		if (m_Model.getRowCount() > 0)
		{
			r.y = m_Model.getFirstRowHeight() + (m_Model.getFixedRowCount() - 1 + m_RowsVisible) * m_Model.getRowHeight() + 1;
		}

		gc.setBackground(getBackground());
		gc.fillRectangle(r);
		gc.fillRectangle(getLastColumnRight() + 2, 0, r.width, r.height);

		if (m_Model.getRowCount() > 0)
		{
			if (flatStyleSpecified)
				// gc.setForeground(this.getBackground());
				gc.setForeground(m_Display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
			else
				gc.setForeground(m_Display.getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
			// Linke Schattenlinie
			gc.drawLine(0, 0, 0, r.y - 1);
		}

		if (!flatStyleSpecified)
			gc.setForeground(this.getBackground());
		else
			gc.setForeground(m_Display.getSystemColor(SWT.COLOR_WHITE));
		// Untere Abschlusslinie
		gc.drawLine(0, r.y - 1, getLastColumnRight() + 1, r.y - 1);

		// Rechte Abschlusslinie
		gc.drawLine(getLastColumnRight() + 1, 0, getLastColumnRight() + 1, r.y - 1);
	}

	// Cells

	/**
	 * Redraws the the cells only in the given area.
	 * 
	 * @param cellsToRedraw
	 *            Defines the area to redraw. The rectangles elements are not
	 *            pixels but cell numbers.
	 */
	public void redraw(Rectangle cellsToRedraw)
	{
		redraw(cellsToRedraw.x, cellsToRedraw.y, cellsToRedraw.width, cellsToRedraw.height);
	}

	/**
	 * Redraws the the cells only in the given area.
	 * 
	 * @param firstCol
	 * @param firstRow
	 * @param numOfCols
	 * @param numOfRows
	 */
	public void redraw(int firstCol, int firstRow, int numOfCols, int numOfRows)
	{
		Rectangle clipRect = getClientArea();
		drawCells(new GC(this), clipRect, firstCol, firstCol + numOfCols, firstRow, firstRow + numOfRows);
	}

	protected void drawCells(GC gc, Rectangle clipRect, int fromCol, int toCol, int fromRow, int toRow)
	{
		int cnt = 0;
		Rectangle r;

		if (m_CellEditor != null)
		{
			if (!isCellVisible(m_CellEditor.m_Col, m_CellEditor.m_Row))
			{
				Rectangle hide = new Rectangle(-101, -101, 100, 100);
				m_CellEditor.setBounds(hide);
			} else
			{
				m_CellEditor.setBounds(getCellRect(m_CellEditor.m_Col, m_CellEditor.m_Row));
			}
		}

		for (int row = fromRow; row < toRow; row++)
		{
			r = getCellRect(0, row);
			if (r.y + r.height < clipRect.y)
			{
				continue;
			}
			if (r.y > clipRect.y + clipRect.height)
			{
				break;
			}

			for (int col = fromCol; col < toCol; col++)
			{
				r = getCellRect(col, row);
				if (r.x > clipRect.x + clipRect.width)
				{
					break;
				}
				if (canDrawCell(col, row, clipRect))
				{
					drawCell(gc, col, row);
					cnt++;
				}
			}
		}
	}

	protected void drawCell(GC gc, int col, int row)
	{
		if ((row < 0) || (row >= m_Model.getRowCount()))
		{
			return;
		}

		Rectangle rect = getCellRect(col, row);

		m_Model.getCellRenderer(col, row).drawCell(gc, rect, col, row, m_Model.getContentAt(col, row), showAsSelected(col, row),
				col < m_Model.getFixedColumnCount() || row < m_Model.getFixedRowCount(), col == m_ClickColumnIndex && row == m_ClickRowIndex);

	}

	protected boolean showAsSelected(int col, int row)
	{
		// A cell with an open editor should be drawn without focus
		if (m_CellEditor != null)
		{
			if (col == m_CellEditor.m_Col && row == m_CellEditor.m_Row)
				return false;
		}
		return isCellSelected(col, row);
	}

	protected void drawRow(GC gc, int row)
	{
		Rectangle clipRect = getClientArea();
		drawCells(gc, getClientArea(), 0, m_Model.getFixedColumnCount(), row, row + 1);
		drawCells(gc, getClientArea(), m_LeftColumn, m_Model.getColumnCount(), row, row + 1);
	}

	protected void drawCol(GC gc, int col)
	{
		Rectangle clipRect = getClientArea();
		drawCells(gc, clipRect, col, col + 1, 0, m_Model.getFixedRowCount());
		drawCells(gc, clipRect, col, col + 1, m_TopRow, m_TopRow + m_RowsVisible);
	}

	// ////////////////////////////////////////////////////////////////////////////
	// REAKTION AUF BENUTZER
	// ////////////////////////////////////////////////////////////////////////////

	/* gibt die Nummer einer Modellspalte zuruck */
	protected int getColumnForResize(int x, int y)
	{
		if (m_Model == null)
			return -1;
		if ((y <= 0) || (y >= m_Model.getFirstRowHeight() + (m_Model.getFixedRowCount() - 1) * m_Model.getRowHeight()))
			return -1;

		if (x < getFixedWidth() + 3)
		{
			for (int i = 0; i < m_Model.getFixedColumnCount(); i++)
				if (Math.abs(x - getColumnRight(i)) < 3)
				{
					if (m_Model.isColumnResizable(i))
						return i;
					return -1;
				}
		}

		for (int i = m_LeftColumn; i < m_Model.getColumnCount(); i++)
		{
			int left = getColumnLeft(i);
			int right = left + m_Model.getColumnWidth(i);
			if (Math.abs(x - right) < 3)
			{
				if (m_Model.isColumnResizable(i))
					return i;
				return -1;
			}
			if ((x >= left + 3) && (x <= right - 3))
				break;
		}
		return -1;
	}

	/* gibt die Nummer einer Zeile der Ansicht(!) zuruck */
	protected int getRowForResize(int x, int y)
	{
		if (m_Model == null)
			return -1;
		if ((x <= 0) || (x >= getFixedWidth()))
			return -1;

		if (y < m_Model.getFirstRowHeight())
			return -1;

		int row = 1 + ((y - m_Model.getFirstRowHeight()) / m_Model.getRowHeight());
		int rowY = m_Model.getFirstRowHeight() + row * m_Model.getRowHeight();

		if (Math.abs(rowY - y) < 3 && m_Model.isRowResizable())
			return row;

		return -1;
	}

	/**
	 * Returns the number of the row that is present at position y or -1, if out
	 * of area.
	 * 
	 * @param y
	 * @return int
	 */
	public int calcRowNum(int y)
	{
		if (m_Model == null)
			return -1;
		if (y < m_Model.getFirstRowHeight())
			return (m_Model.getFixedRowCount() == 0 ? m_TopRow : 0);
		y -= m_Model.getFirstRowHeight();
		int row = 1 + (y / m_Model.getRowHeight());
		if ((row < 0) || (row >= m_Model.getRowCount()))
			return -1;
		if (row >= m_Model.getFixedRowCount())
			return m_TopRow + row - m_Model.getFixedRowCount();
		return row;
	}

	/**
	 * Returns the number of the column that is present at position x or -1, if
	 * out of area.
	 * 
	 * @param y
	 * @return int
	 */
	public int calcColumnNum(int x)
	{
		if (m_Model == null)
			return -1;
		int col = 0;

		int z = 0;
		for (int i = 0; i < m_Model.getFixedColumnCount(); i++)
		{
			if ((x >= z) && (x <= z + m_Model.getColumnWidth(i)))
			{
				return i;
			}
			z += m_Model.getColumnWidth(i);
		}

		col = -1;
		z = getFixedWidth();
		for (int i = m_LeftColumn; i < m_Model.getColumnCount(); i++)
		{
			if ((x >= z) && (x <= z + m_Model.getColumnWidth(i)))
			{
				col = i;
				break;
			}
			z += m_Model.getColumnWidth(i);
		}
		return col;
	}

	public boolean isCellVisible(int col, int row)
	{
		if (m_Model == null)
			return false;
		return ((col >= m_LeftColumn && col < m_LeftColumn + m_ColumnsVisible && row >= m_TopRow && row < m_TopRow + m_RowsVisible)

		|| (col < m_Model.getFixedColumnCount() && row < m_Model.getFixedRowCount()));
	}

	public boolean isCellFullyVisible(int col, int row)
	{
		if (m_Model == null)
			return false;
		return ((col >= m_LeftColumn && col < m_LeftColumn + m_ColumnsFullyVisible && row >= m_TopRow && row < m_TopRow + m_RowsFullyVisible)

		|| (col < m_Model.getFixedColumnCount() && row < m_Model.getFixedRowCount()));
	}

	public boolean isRowVisible(int row)
	{
		if (m_Model == null)
			return false;
		return ((row >= m_TopRow && row < m_TopRow + m_RowsVisible) || row < m_Model.getFixedRowCount());

	}

	public boolean isRowFullyVisible(int row)
	{
		if (m_Model == null)
			return false;
		return ((row >= m_TopRow && row < m_TopRow + m_RowsFullyVisible) || row < m_Model.getFixedRowCount());
	}

	/*
	 * Focusses the given Cell. Assumes that the given cell is in the viewable
	 * area. Does all neccessary redraws.
	 */
	protected void focusCell(int col, int row, int stateMask)
	{
		GC gc = new GC(this);

		// close cell editor if active
		if (m_CellEditor != null)
			m_CellEditor.close(true);

		/*
		 * Special rule: in row selection mode the selection if a fixed cell in
		 * a non-fixed row is allowed and handled as a selection of a non-fixed
		 * cell.
		 */

		if (row >= m_Model.getFixedRowCount() && (col >= m_Model.getFixedColumnCount() || m_RowSelectionMode))
		{

			if ((stateMask & SWT.CTRL) == 0 && (stateMask & SWT.SHIFT) == 0)
			{
				// case: no modifier key
				boolean redrawAll = (m_Selection.size() > 1);
				int oldFocusRow = m_FocusRow;
				int oldFocusCol = m_FocusCol;
				clearSelectionWithoutRedraw();
				addToSelection(col, row);
				m_FocusRow = row;
				m_FocusCol = col;

				if (redrawAll)
					redraw();
				else if (m_RowSelectionMode)
				{
					if (isRowVisible(oldFocusRow))
						drawRow(gc, oldFocusRow);
					if (isRowVisible(m_FocusRow))
						drawRow(gc, m_FocusRow);
				} else
				{
					if (isCellVisible(oldFocusCol, oldFocusRow))
						drawCell(gc, oldFocusCol, oldFocusRow);
					if (isCellVisible(m_FocusCol, m_FocusRow))
						drawCell(gc, m_FocusCol, m_FocusRow);
				}
			}

			else if ((stateMask & SWT.CTRL) != 0)
			{
				// case: CTRL key pressed
				if (toggleSelection(col, row))
				{
					m_FocusCol = col;
					m_FocusRow = row;
				}

				if (m_RowSelectionMode)
				{
					drawRow(gc, row);
				} else
				{
					drawCell(gc, col, row);
				}
			}

			else if ((stateMask & SWT.SHIFT) != 0)
			{
				// case: SHIFT key pressed

				if (m_RowSelectionMode)
				{
					if (row < m_FocusRow)
					{
						// backword selection
						while (row != m_FocusRow)
						{
							addToSelection(0, --m_FocusRow);
						}
					} else
					{
						// foreward selection
						while (row != m_FocusRow)
						{
							addToSelection(0, ++m_FocusRow);
						}
					}
				} else
				// cell selection mode
				{
					if (row < m_FocusRow || (row == m_FocusRow && col < m_FocusCol))
					{
						// backword selection
						while (row != m_FocusRow || col != m_FocusCol)
						{
							m_FocusCol--;
							if (m_FocusCol < m_Model.getFixedColumnCount())
							{
								m_FocusCol = m_Model.getColumnCount();
								m_FocusRow--;
							}
							addToSelection(m_FocusCol, m_FocusRow);
						}
					} else
					{
						// foreward selection
						while (row != m_FocusRow || col != m_FocusCol)
						{
							m_FocusCol++;
							if (m_FocusCol == m_Model.getColumnCount())
							{
								m_FocusCol = m_Model.getFixedColumnCount();
								m_FocusRow++;
							}
							addToSelection(m_FocusCol, m_FocusRow);
						}
					}

				}

				redraw();
			}

			// notify non-fixed cell listeners
			fireCellSelection(col, row, stateMask);
		} else
		{
			// a fixed cell was focused
			drawCell(gc, col, row);
			// notify fixed cell listeners
			fireFixedCellSelection(col, row, stateMask);
		}

		gc.dispose();
	}

	protected void onMouseDown(MouseEvent e)
	{
		if (e.button == 1)
		{
			// deactivateEditor(true);
			setCapture(true);
			m_Capture = true;

			// Resize column?
			int columnIndex = getColumnForResize(e.x, e.y);
			if (columnIndex >= 0)
			{
				m_ResizeColumnIndex = columnIndex;
				m_ResizeColumnLeft = getColumnLeft(columnIndex);
				return;
			}

			// Resize row?
			int rowIndex = getRowForResize(e.x, e.y);
			if (rowIndex >= 0)
			{
				m_ResizeRowIndex = rowIndex;
				m_ResizeRowTop = m_Model.getFirstRowHeight() + (rowIndex - 1) * m_Model.getRowHeight();
				m_NewRowSize = m_Model.getRowHeight();
				return;
			}
		}

		// focus change
		int col = calcColumnNum(e.x);
		int row = calcRowNum(e.y);

		if (col == -1 || row == -1)
			return;

		m_ClickColumnIndex = col;
		m_ClickRowIndex = row;

		focusCell(col, row, e.stateMask);

	}

	protected void onMouseMove(MouseEvent e)
	{
		if (m_Model == null)
			return;

		// show resize cursor?
		if ((m_ResizeColumnIndex != -1) || (getColumnForResize(e.x, e.y) >= 0))
			setCursor(new Cursor(m_Display, SWT.CURSOR_SIZEWE));
		else if ((m_ResizeRowIndex != -1) || (getRowForResize(e.x, e.y) >= 0))
			setCursor(new Cursor(m_Display, SWT.CURSOR_SIZENS));
		else
			setCursor(null);

		if (e.button == 1)
		{
			// extend selection?
			if (m_ClickColumnIndex != -1 && m_MultiSelectMode)
			{
				int row = calcRowNum(e.y);
				int col = calcColumnNum(e.x);

				if (row >= m_Model.getFixedRowCount() && col >= m_Model.getFixedColumnCount())
				{

					m_ClickColumnIndex = col;
					m_ClickRowIndex = row;

					focusCell(col, row, (e.stateMask | SWT.SHIFT));
				}
			}

		}
		// column resize?
		if (m_ResizeColumnIndex != -1)
		{
			Rectangle rect = getClientArea();
			int oldSize = m_Model.getColumnWidth(m_ResizeColumnIndex);
			if (e.x > rect.x + rect.width - 1)
				e.x = rect.x + rect.width - 1;
			int newSize = e.x - m_ResizeColumnLeft;
			if (newSize < 5)
				newSize = 5;

			int leftX = getColumnLeft(m_ResizeColumnIndex);
			int rightX = getColumnRight(m_ResizeColumnIndex);

			m_Model.setColumnWidth(m_ResizeColumnIndex, newSize);
			newSize = m_Model.getColumnWidth(m_ResizeColumnIndex);

			GC gc = new GC(this);
			gc.copyArea(rightX, 0, rect.width - rightX, rect.height, leftX + newSize, 0);
			drawCol(gc, m_ResizeColumnIndex);
			if (newSize < oldSize)
			{
				int delta = oldSize - newSize;
				redraw(rect.width - delta, 0, delta, rect.height, false);
			}
			gc.dispose();
		}

		// row resize?
		if (m_ResizeRowIndex != -1)
		{
			Rectangle rect = getClientArea();
			GC gc = new GC(this);

			// calculate new size
			if (e.y > rect.y + rect.height - 1)
				e.y = rect.y + rect.height - 1;
			m_NewRowSize = e.y - m_ResizeRowTop;
			if (m_NewRowSize < m_Model.getRowHeightMinimum())
				m_NewRowSize = m_Model.getRowHeightMinimum();

			// restore old line area
			if (m_LineRestore != null)
			{
				gc.drawImage(m_LineRestore, m_LineX, m_LineY);
			}

			// safe old picture and draw line
			gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
			int lineEnd = getColumnRight(m_LeftColumn + m_ColumnsVisible - 1);
			m_LineRestore = new Image(m_Display, lineEnd, 1);
			m_LineX = rect.x + 1;
			m_LineY = m_ResizeRowTop + m_NewRowSize - 1;
			gc.copyArea(m_LineRestore, m_LineX, m_LineY);
			gc.drawLine(m_LineX, m_LineY, rect.x + lineEnd, m_LineY);
			gc.dispose();

		}

	}

	protected void onMouseUp(MouseEvent e)
	{
		// if (e.button == 1)
		{
			if (m_Model == null)
				return;

			setCapture(false);
			m_Capture = false;

			if (m_ResizeColumnIndex != -1)
			{
				fireColumnResize(m_ResizeColumnIndex, m_Model.getColumnWidth(m_ResizeColumnIndex));
				m_ResizeColumnIndex = -1;
				redraw();

			}
			if (m_ResizeRowIndex != -1)
			{
				m_ResizeRowIndex = -1;
				m_Model.setRowHeight(m_NewRowSize);
				m_LineRestore = null;
				fireRowResize(m_NewRowSize);
				redraw();
			}
			if (m_ClickColumnIndex != -1)
			{
				int col = m_ClickColumnIndex;
				int row = m_ClickRowIndex;
				m_ClickColumnIndex = -1;
				m_ClickRowIndex = -1;
				if (m_CellEditor == null)
				{
					drawCell(new GC(this), col, row);
				}
			}

		}
	}

	protected void onKeyDown(KeyEvent e)
	{
		boolean focusChanged = false;
		int newFocusRow = m_FocusRow;
		int newFocusCol = m_FocusCol;

		if (m_Model == null)
			return;

		if ((e.character == ' ') || (e.character == '\r'))
		{
			openEditorInFocus();
			return;
		} else if (e.keyCode == SWT.HOME)
		{
			newFocusCol = m_Model.getFixedColumnCount();
			if (newFocusRow == -1)
				newFocusRow = m_Model.getFixedRowCount();
			focusChanged = true;
		} else if (e.keyCode == SWT.END)
		{
			newFocusCol = m_Model.getColumnCount() - 1;
			if (newFocusRow == -1)
				newFocusRow = m_Model.getFixedRowCount();
			focusChanged = true;
		} else if (e.keyCode == SWT.ARROW_LEFT)
		{
			if (!m_RowSelectionMode)
			{
				if (newFocusCol > m_Model.getFixedColumnCount())
					newFocusCol--;
			}
			focusChanged = true;
		} else if (e.keyCode == SWT.ARROW_RIGHT)
		{
			if (!m_RowSelectionMode)
			{
				if (newFocusCol == -1)
				{
					newFocusCol = m_Model.getFixedColumnCount();
					newFocusRow = m_Model.getFixedRowCount();
				} else if (newFocusCol < m_Model.getColumnCount() - 1)
					newFocusCol++;
			}
			focusChanged = true;
		} else if (e.keyCode == SWT.ARROW_DOWN)
		{
			if (newFocusRow == -1)
			{
				newFocusRow = m_Model.getFixedRowCount();
				newFocusCol = m_Model.getFixedColumnCount();
			} else if (newFocusRow < m_Model.getRowCount() - 1)
				newFocusRow++;
			focusChanged = true;
		} else if (e.keyCode == SWT.ARROW_UP)
		{
			if (newFocusRow > m_Model.getFixedRowCount())
				newFocusRow--;
			focusChanged = true;
		} else if (e.keyCode == SWT.PAGE_DOWN)
		{
			newFocusRow += m_RowsVisible - 1;
			if (newFocusRow >= m_Model.getRowCount())
				newFocusRow = m_Model.getRowCount() - 1;
			if (newFocusCol == -1)
				newFocusCol = m_Model.getFixedColumnCount();
			focusChanged = true;
		} else if (e.keyCode == SWT.PAGE_UP)
		{
			newFocusRow -= m_RowsVisible - 1;
			if (newFocusRow < m_Model.getFixedRowCount())
				newFocusRow = m_Model.getFixedRowCount();
			if (newFocusCol == -1)
				newFocusCol = m_Model.getFixedColumnCount();
			focusChanged = true;
		}

		if (focusChanged)
		{

			focusCell(newFocusCol, newFocusRow, e.stateMask);

			if (!isCellFullyVisible(m_FocusCol, m_FocusRow))
				scrollToFocus();
		}
	}

	protected void onMouseDoubleClick(MouseEvent e)
	{
		if (m_Model == null)
			return;
		if (e.button == 1)
		{

			if (e.y < m_Model.getFirstRowHeight() + ((m_Model.getFixedRowCount() - 1) * m_Model.getRowHeight()))
			{
				// double click in header area
				int columnIndex = getColumnForResize(e.x, e.y);
				resizeColumnOptimal(columnIndex);
				return;
			} else
				openEditorInFocus();
		}
	}

	/**
	 * Resizes the given column to its optimal width.
	 * 
	 * Is also called if user doubleclicks in the resize area of a resizable
	 * column.
	 * 
	 * The optimal width is determined by asking the CellRenderers for the
	 * visible cells of the column for the optimal with and taking the minimum
	 * of the results. Note that the optimal width is only determined for the
	 * visible area of the table because otherwise this could take very long
	 * time.
	 * 
	 * @param column
	 *            The column to resize
	 * @return int The optimal with that was determined or -1, if column out of
	 *         range.
	 */
	public int resizeColumnOptimal(int column)
	{
		if (column >= 0 && column < m_Model.getColumnCount())
		{
			int optWidth = 5;
			for (int i = 0; i < m_Model.getFixedRowCount(); i++)
			{
				int width = m_Model.getCellRenderer(column, i).getOptimalWidth(m_GC, column, i, m_Model.getContentAt(column, i), true);
				if (width > optWidth)
					optWidth = width;
			}
			for (int i = m_TopRow; i < m_TopRow + m_RowsVisible; i++)
			{
				int width = m_Model.getCellRenderer(column, i).getOptimalWidth(m_GC, column, i, m_Model.getContentAt(column, i), true);
				if (width > optWidth)
					optWidth = width;
			}
			m_Model.setColumnWidth(column, optWidth);
			redraw();
			return optWidth;
		}
		return -1;
	}

	/**
	 * This method activated the cell editor on the current focus cell, if the
	 * table model allows cell editing for this cell.
	 */
	public void openEditorInFocus()
	{
		m_CellEditor = m_Model.getCellEditor(m_FocusCol, m_FocusRow);
		if (m_CellEditor != null)
		{
			Rectangle r = getCellRect(m_FocusCol, m_FocusRow);
			m_CellEditor.open(this, m_FocusCol, m_FocusRow, r);
		}
	}

	/*
	 * Tries to open KTableCellEditor on the given cell. If the cell exists, the
	 * model is asked for an editor and if there is one, the table scrolls the
	 * cell into the view and openes the editor on the cell. @param col @param
	 * row
	 * 
	 * public void tryToOpenEditorAt(int col, int row) { if (col >= 0 && col <
	 * m_Model.getColumnCount() && row >= 0 && row < m_Model.getRowCount()) {
	 * m_CellEditor = m_Model.getCellEditor(col, row); if (m_CellEditor != null)
	 * { m_FocusCol = col; m_FocusRow = row; scrollToFocus(); Rectangle r =
	 * getCellRect(col, row); m_CellEditor.open(this, m_FocusCol, m_FocusRow,
	 * r); } } }
	 */

	protected void scrollToFocus()
	{
		boolean change = false;

		// vertical scroll allowed?
		if (getVerticalBar() != null)
		{
			if (m_FocusRow < m_TopRow)
			{
				m_TopRow = m_FocusRow;
				change = true;
			}

			if (m_FocusRow >= m_TopRow + m_RowsFullyVisible)
			{
				m_TopRow = m_FocusRow - m_RowsFullyVisible + 1;
				change = true;
			}

		}

		// horizontal scroll allowed?
		if (getHorizontalBar() != null)
		{
			if (m_FocusCol < m_LeftColumn)
			{
				m_LeftColumn = m_FocusCol;
				change = true;
			}

			if (m_FocusCol >= m_LeftColumn + m_ColumnsFullyVisible)
			{
				int oldLeftCol = m_LeftColumn;
				Rectangle rect = getClientArea();
				while (m_LeftColumn < m_FocusCol && getColumnRight(m_FocusCol) > rect.width + rect.x)
				{
					m_LeftColumn++;
				}
				change = (oldLeftCol != m_LeftColumn);
			}
		}

		if (change)
			redraw();
	}

	protected void fireCellSelection(int col, int row, int statemask)
	{
		for (int i = 0; i < cellSelectionListeners.size(); i++)
		{
			((KTableCellSelectionListener) cellSelectionListeners.get(i)).cellSelected(col, row, statemask);
		}
	}

	protected void fireFixedCellSelection(int col, int row, int statemask)
	{
		for (int i = 0; i < cellSelectionListeners.size(); i++)
		{
			((KTableCellSelectionListener) cellSelectionListeners.get(i)).fixedCellSelected(col, row, statemask);
		}
	}

	protected void fireColumnResize(int col, int newSize)
	{
		for (int i = 0; i < cellResizeListeners.size(); i++)
		{
			((KTableCellResizeListener) cellResizeListeners.get(i)).columnResized(col, newSize);
		}
	}

	protected void fireRowResize(int newSize)
	{
		for (int i = 0; i < cellResizeListeners.size(); i++)
		{
			((KTableCellResizeListener) cellResizeListeners.get(i)).rowResized(newSize);
		}
	}

	/**
	 * Adds a listener that is notified when a cell is selected.
	 * 
	 * This can happen either by a click on the cell or by arrow keys. Note that
	 * the listener is not called for each cell that the user selects in one
	 * action using Shift+Click. To get all these cells use the listener and
	 * getCellSelecion() or getRowSelection().
	 * 
	 * @param listener
	 */
	public void addCellSelectionListener(KTableCellSelectionListener listener)
	{
		cellSelectionListeners.add(listener);
	}

	/**
	 * Adds a listener that is notified when a cell is resized. This happens
	 * when the mouse button is released after a resizing.
	 * 
	 * @param listener
	 */
	public void addCellResizeListener(KTableCellResizeListener listener)
	{
		cellResizeListeners.add(listener);
	}

	/**
	 * Removes the listener if present. Returns true, if found and removed from
	 * the list of listeners.
	 */
	public boolean removeCellSelectionListener(KTableCellSelectionListener listener)
	{
		return cellSelectionListeners.remove(listener);
	}

	/**
	 * Removes the listener if present. Returns true, if found and removed from
	 * the list of listeners.
	 */
	public boolean removeCellResizeListener(KTableCellResizeListener listener)
	{
		return cellResizeListeners.remove(listener);
	}

	// ////////////////////////////////////////////////////////////////////////////
	// SELECTION
	// ////////////////////////////////////////////////////////////////////////////

	/**
	 * Sets the "Row Selection Mode". The current selection is cleared when this
	 * method is called.
	 * 
	 * @param rowSelectMode
	 *            In the "Row Selection Mode", the table always selects a
	 *            complete row. Otherwise, each individual cell can be selected.
	 * 
	 *            This mode can be combined with the "Multi Selection Mode".
	 * 
	 */
	public void setRowSelectionMode(boolean rowSelectMode)
	{
		m_RowSelectionMode = rowSelectMode;
		clearSelection();
	}

	/**
	 * Sets the "Multi Selection Mode". The current selection is cleared when
	 * this method is called.
	 * 
	 * @param multiSelectMode
	 *            In the "Multi Select Mode", more than one cell or row can be
	 *            selected. The user can achieve this by shift-click and
	 *            ctrl-click. The selected cells/rows can be scattored ofer the
	 *            complete table. If you pass false, only a single cell or row
	 *            can be selected.
	 * 
	 *            This mode can be combined with the "Row Selection Mode".
	 */
	public void setMultiSelectionMode(boolean multiSelectMode)
	{
		m_MultiSelectMode = multiSelectMode;
		clearSelection();
	}

	/**
	 * Returns true if in "Row Selection Mode".
	 * 
	 * @see setSelectionMode
	 * @return boolean
	 */
	public boolean isRowSelectMode()
	{
		return m_RowSelectionMode;
	}

	/**
	 * Returns true if in "Multi Selection Mode".
	 * 
	 * @see setSelectionMode
	 * @return boolean
	 */
	public boolean isMultiSelectMode()
	{
		return m_MultiSelectMode;
	}

	protected void clearSelectionWithoutRedraw()
	{
		m_Selection.clear();
	}

	/**
	 * Clears the current selection (in all selection modes).
	 */
	public void clearSelection()
	{
		/*
		 * if (m_MultiSelectMode) { if (m_Selection.size() < m_RowsFullyVisible
		 * * m_ColumnsVisible) { if (m_RowSelectionMode) { for (Iterator iter =
		 * m_Selection.values().iterator(); iter.hasNext();) { int row =
		 * ((Integer) iter.next()).intValue(); if (row >= m_TopRow && row <
		 * m_TopRow+m_RowsFullyVisible) { } } } else { for (Iterator iter =
		 * m_Selection.values().iterator(); iter.hasNext();) { Point element =
		 * (Point) iter.next(); } } } }
		 */
		clearSelectionWithoutRedraw();
		m_FocusCol = -1;
		m_FocusRow = -1;
		if (m_MultiSelectMode)
			redraw();
	}

	/*
	 * works in both modes: Cell and Row Selection. Has no redraw functionality!
	 * 
	 * Returns true, if added to selection.
	 */
	protected boolean toggleSelection(int col, int row)
	{

		if (m_MultiSelectMode)
		{
			Object o;
			if (m_RowSelectionMode)
			{
				o = new Integer(row);
			} else
			{
				o = new Point(col, row);
			}
			if (m_Selection.get(o) != null)
			{
				m_Selection.remove(o);
				return false;
			} else
			{
				m_Selection.put(o, o);
				return true;
			}
		}
		return false;
	}

	/*
	 * works in both modes: Cell and Row Selection. Has no redraw functionality!
	 */
	protected void addToSelection(int col, int row)
	{
		if (m_MultiSelectMode)
		{
			if (m_RowSelectionMode)
			{
				Integer o = new Integer(row);
				m_Selection.put(o, o);
			} else
			{
				Point o = new Point(col, row);
				m_Selection.put(o, o);
			}
		}
		// System.out.println(m_Selection.size()+" "+col+"/"+row);
	}

	/**
	 * Selects the given cell. If scroll is true, it scrolls to show this cell
	 * if neccessary. In Row Selection Mode, the given row is selected and a
	 * scroll to the given column is done. Does nothing if the cell does not
	 * exist.
	 * 
	 * @param col
	 * @param row
	 * @param scroll
	 */
	public void setSelection(int col, int row, boolean scroll)
	{
		if (col < m_Model.getColumnCount() && col >= m_Model.getFixedColumnCount() && row < m_Model.getRowCount() && row >= m_Model.getFixedRowCount())
		{
			focusCell(col, row, 0);
			if (scroll)
			{
				scrollToFocus();
			}
		}
	}

	/**
	 * Returns true, if the given cell is selected. Works also in Row Selection
	 * Mode.
	 * 
	 * @param col
	 * @param row
	 * @return boolean
	 */
	public boolean isCellSelected(int col, int row)
	{
		if (!m_MultiSelectMode)
		{
			if (m_RowSelectionMode)
				return (row == m_FocusRow);
			return (col == m_FocusCol && row == m_FocusRow);
		}

		if (m_RowSelectionMode)
			return (m_Selection.get(new Integer(row)) != null);
		else
			return (m_Selection.get(new Point(col, row)) != null);
	}

	/**
	 * Returns true, if the given row is selected. Returns always false if not
	 * in Row Selection Mode!
	 * 
	 * @param row
	 * @return boolean
	 */
	public boolean isRowSelected(int row)
	{
		return (m_Selection.get(new Integer(row)) != null);
	}

	/**
	 * Returns an array of the selected row numbers. Returns null if not in Row
	 * Selection Mode. Returns an array with one or none element if not in Multi
	 * Selection Mode.
	 * 
	 * @return int[]
	 */
	public int[] getRowSelection()
	{
		if (!m_RowSelectionMode)
			return null;
		if (!m_MultiSelectMode)
		{
			if (m_FocusRow < 0)
				return new int[0];
			int[] tmp = new int[1];
			tmp[0] = m_FocusRow;
			return tmp;
		}

		Object[] ints = m_Selection.values().toArray();
		int[] erg = new int[ints.length];

		for (int i = 0; i < erg.length; i++)
		{
			erg[i] = ((Integer) ints[i]).intValue();
		}
		return erg;
	}

	/**
	 * Returns an array of the selected cells as Point[]. The columns are stored
	 * in the x fields, rows in y fields. Returns null if in Row Selection Mode.
	 * Returns an array with one or none element if not in Multi Selection Mode.
	 * 
	 * @return int[]
	 */
	public Point[] getCellSelection()
	{
		if (m_RowSelectionMode)
			return null;
		if (!m_MultiSelectMode)
		{
			if (m_FocusRow < 0 || m_FocusCol < 0)
				return new Point[0];
			Point[] tmp = new Point[1];
			tmp[0] = new Point(m_FocusCol, m_FocusRow);
			return tmp;
		}

		return (Point[]) m_Selection.values().toArray(new Point[1]);
	}

	// ////////////////////////////////////////////////////////////////////////////
	// MODEL
	// ////////////////////////////////////////////////////////////////////////////

	/**
	 * Sets the table model. The table model provides data to the table.
	 * 
	 * @see de.kupzog.ktable.KTableModel for more information.
	 * @param model
	 */
	public void setModel(KTableModel model)
	{
		m_Model = model;
		m_FocusCol = -1;
		m_FocusRow = -1;
		clearSelectionWithoutRedraw();
		redraw();
	}

	/**
	 * returns the current table model
	 * 
	 * @return KTableModel
	 */
	public KTableModel getModel()
	{
		return m_Model;
	}

}

/*******************************************************************************
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Friederich Kupzog - initial API and implementation
 * fkmk@kupzog.de www.kupzog.de/fkmk
 ******************************************************************************/

/*******************************************************************************
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Friederich Kupzog - initial API and implementation
 * fkmk@kupzog.de www.kupzog.de/fkmk
 ******************************************************************************/

class KTableCellEditorCombo extends KTableCellEditor
{
	private CCombo m_Combo;

	private String m_Items[];

	public void open(KTable table, int row, int col, Rectangle rect)
	{
		super.open(table, row, col, rect);
		m_Combo.setFocus();
		m_Combo.setText((String) m_Model.getContentAt(m_Col, m_Row));
	}

	public void close(boolean save)
	{
		if (save)
			m_Model.setContentAt(m_Col, m_Row, m_Combo.getText());
		super.close(save);
		m_Combo = null;
	}

	protected Control createControl()
	{
		m_Combo = new CCombo(m_Table, SWT.READ_ONLY);
		m_Combo.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		if (m_Items != null)
			m_Combo.setItems(m_Items);
		m_Combo.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				try
				{
					onKeyPressed(e);
				} catch (Exception ex)
				{
				}
			}
		});
		/*
		 * m_Combo.addTraverseListener(new TraverseListener() { public void
		 * keyTraversed(TraverseEvent arg0) { onTraverse(arg0); } });
		 */
		return m_Combo;
	}

	public void setBounds(Rectangle rect)
	{
		super.setBounds(new Rectangle(rect.x, rect.y + 1, rect.width, rect.height - 2));
	}

	public void setItems(String items[])
	{
		m_Items = items;
	}

}

/*******************************************************************************
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Friederich Kupzog - initial API and implementation
 * fkmk@kupzog.de www.kupzog.de/fkmk
 ******************************************************************************/
class KTableCellEditorMultilineText extends KTableCellEditor
{
	private Text m_Text;

	public void open(KTable table, int col, int row, Rectangle rect)
	{
		super.open(table, col, row, rect);
		m_Text.setText(m_Model.getContentAt(m_Col, m_Row).toString());
		m_Text.selectAll();
		m_Text.setVisible(true);
		m_Text.setFocus();
	}

	public void close(boolean save)
	{
		if (save)
			m_Model.setContentAt(m_Col, m_Row, m_Text.getText());
		m_Text = null;
		super.close(save);
	}

	protected Control createControl()
	{
		m_Text = new Text(m_Table, SWT.MULTI | SWT.V_SCROLL);
		m_Text.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				try
				{
					onKeyPressed(e);
				} catch (Exception ex)
				{
				}
			}
		});
		m_Text.addTraverseListener(new TraverseListener()
		{
			public void keyTraversed(TraverseEvent arg0)
			{
				onTraverse(arg0);
			}
		});
		return m_Text;
	}

	/*
	 * overridden from superclass
	 */
	public void setBounds(Rectangle rect)
	{
		super.setBounds(new Rectangle(rect.x, rect.y, rect.width, rect.height));
	}

}

/*******************************************************************************
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Friederich Kupzog - initial API and implementation
 * fkmk@kupzog.de www.kupzog.de/fkmk
 ******************************************************************************/
class KTableCellEditorMultilineWrapText extends KTableCellEditor
{
	private Text m_Text;

	public void open(KTable table, int col, int row, Rectangle rect)
	{
		super.open(table, col, row, rect);
		m_Text.setText(m_Model.getContentAt(m_Col, m_Row).toString());
		m_Text.selectAll();
		m_Text.setVisible(true);
		m_Text.setFocus();
	}

	public void close(boolean save)
	{
		if (save)
			m_Model.setContentAt(m_Col, m_Row, m_Text.getText());
		m_Text = null;
		super.close(save);
	}

	protected Control createControl()
	{
		m_Text = new Text(m_Table, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		m_Text.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				try
				{
					onKeyPressed(e);
				} catch (Exception ex)
				{
				}
			}
		});
		m_Text.addTraverseListener(new TraverseListener()
		{
			public void keyTraversed(TraverseEvent arg0)
			{
				onTraverse(arg0);
			}
		});
		return m_Text;
	}

	/*
	 * overridden from superclass
	 */
	public void setBounds(Rectangle rect)
	{
		super.setBounds(new Rectangle(rect.x, rect.y, rect.width, rect.height));
	}

}

/*******************************************************************************
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Friederich Kupzog - initial API and implementation
 * fkmk@kupzog.de www.kupzog.de/fkmk
 ******************************************************************************/

class KTableCellEditorText extends KTableCellEditor
{
	private Text m_Text;

	public void open(KTable table, int col, int row, Rectangle rect)
	{
		super.open(table, col, row, rect);
		m_Text.setText(m_Model.getContentAt(m_Col, m_Row).toString());
		m_Text.selectAll();
		m_Text.setVisible(true);
		m_Text.setFocus();
	}

	public void close(boolean save)
	{
		if (save)
			m_Model.setContentAt(m_Col, m_Row, m_Text.getText());
		super.close(save);
		m_Text = null;
		// System.out.println("set to null.");
	}

	protected Control createControl()
	{
		// System.out.println("Created a new one.");
		m_Text = new Text(m_Table, SWT.NONE);
		m_Text.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				try
				{
					onKeyPressed(e);
				} catch (Exception ex)
				{
				}
			}
		});
		m_Text.addTraverseListener(new TraverseListener()
		{
			public void keyTraversed(TraverseEvent arg0)
			{
				onTraverse(arg0);
			}
		});
		return m_Text;
	}

	/*
	 * overridden from superclass
	 */
	public void setBounds(Rectangle rect)
	{
		super.setBounds(new Rectangle(rect.x, rect.y + (rect.height - 15) / 2 + 1, rect.width, 15));
	}

}

/*******************************************************************************
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Friederich Kupzog - initial API and implementation
 * fkmk@kupzog.de www.kupzog.de/fkmk
 ******************************************************************************/
class KTableCellRenderer
{

	public static KTableCellRenderer defaultRenderer = new KTableCellRenderer();

	/**
   * 
   */
	protected Display m_Display;

	public KTableCellRenderer()
	{
		m_Display = Display.getCurrent();
	}

	/**
	 * Returns the optimal width of the given cell (used by column resizing)
	 * 
	 * @param col
	 * @param row
	 * @param content
	 * @param fixed
	 * @return int
	 */
	public int getOptimalWidth(GC gc, int col, int row, Object content, boolean fixed)
	{
		return gc.stringExtent(content.toString()).x + 8;
	}

	/**
	 * Standard implementation for CellRenderer. Draws a cell at the given
	 * position. Uses the .getString() method of content to get a String
	 * representation to draw.
	 * 
	 * @param gc
	 *            The gc to draw on
	 * @param rect
	 *            The coordinates and size of the cell (add 1 to width and hight
	 *            to include the borders)
	 * @param col
	 *            The column
	 * @param row
	 *            The row
	 * @param content
	 *            The content of the cell (as given by the table model)
	 * @param focus
	 *            True if the cell is selected
	 * @param fixed
	 *            True if the cell is fixed (unscrollable header cell)
	 * @param clicked
	 *            True if the cell is currently clicked (useful e.g. to paint a
	 *            pressed button)
	 */
	public void drawCell(GC gc, Rectangle rect, int col, int row, Object content, boolean focus, boolean fixed, boolean clicked)
	{
		if (fixed)
		{
			rect.height += 1;
			rect.width += 1;
			gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_FOREGROUND));
			if (clicked)
			{
				SWTX.drawButtonDown(gc, content.toString(), SWTX.ALIGN_HORIZONTAL_LEFT | SWTX.ALIGN_VERTICAL_CENTER, null, SWTX.ALIGN_HORIZONTAL_RIGHT
						| SWTX.ALIGN_VERTICAL_CENTER, rect);
			} else
			{
				SWTX.drawButtonUp(gc, content.toString(), SWTX.ALIGN_HORIZONTAL_LEFT | SWTX.ALIGN_VERTICAL_CENTER, null, SWTX.ALIGN_HORIZONTAL_RIGHT
						| SWTX.ALIGN_VERTICAL_CENTER, rect);
			}

			return;
		}

		Color textColor;
		Color backColor;
		Color vBorderColor;
		Color hBorderColor;

		if (focus)
		{
			textColor = m_Display.getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT);
			backColor = (m_Display.getSystemColor(SWT.COLOR_LIST_SELECTION));
			vBorderColor = m_Display.getSystemColor(SWT.COLOR_LIST_SELECTION);
			hBorderColor = m_Display.getSystemColor(SWT.COLOR_LIST_SELECTION);
		} else
		{
			textColor = m_Display.getSystemColor(SWT.COLOR_LIST_FOREGROUND);
			backColor = m_Display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
			vBorderColor = m_Display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
			hBorderColor = m_Display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
		}

		gc.setForeground(hBorderColor);
		gc.drawLine(rect.x, rect.y + rect.height, rect.x + rect.width, rect.y + rect.height);

		gc.setForeground(vBorderColor);
		gc.drawLine(rect.x + rect.width, rect.y, rect.x + rect.width, rect.y + rect.height);

		gc.setBackground(backColor);
		gc.setForeground(textColor);

		gc.fillRectangle(rect);

		SWTX.drawTextImage(gc, content.toString(), SWTX.ALIGN_HORIZONTAL_CENTER | SWTX.ALIGN_VERTICAL_CENTER, null, SWTX.ALIGN_HORIZONTAL_CENTER
				| SWTX.ALIGN_VERTICAL_CENTER, rect.x + 3, rect.y, rect.width - 3, rect.height);

	}

}

/*******************************************************************************
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Friederich Kupzog - initial API and implementation
 * fkmk@kupzog.de www.kupzog.de/fkmk
 ******************************************************************************/

class KTableCellResizeAdapter implements KTableCellResizeListener
{

	public void columnResized(int col, int newWidth)
	{
	}

	public void rowResized(int newHeight)
	{
	}

}

/*******************************************************************************
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Friederich Kupzog - initial API and implementation
 * fkmk@kupzog.de www.kupzog.de/fkmk
 ******************************************************************************/

interface KTableCellResizeListener
{

	/**
	 * Is called when a row is resized.
	 */
	public void rowResized(int newHeight);

	/**
	 * Is called when a column is resized.
	 */
	public void columnResized(int col, int newWidth);

}

/*******************************************************************************
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Friederich Kupzog - initial API and implementation
 * fkmk@kupzog.de www.kupzog.de/fkmk
 ******************************************************************************/

class KTableCellSelectionAdapter implements KTableCellSelectionListener
{
	/**
	 * Is called if a non-fixed cell is selected (gets the focus).
	 * 
	 * @see KTable for an explanation of the term "fixed cells".
	 * @param col
	 *            the column of the cell
	 * @param row
	 *            the row of the cell
	 * @param statemask
	 *            the modifier keys that where pressed when the selection
	 *            happened.
	 */
	public void cellSelected(int col, int row, int statemask)
	{
	}

	/**
	 * Is called if a fixed cell is selected (is clicked).
	 * 
	 * @see KTable for an explanation of the term "fixed cells".
	 * @param col
	 *            the column of the cell
	 * @param row
	 *            the row of the cell
	 * @param statemask
	 *            the modifier keys that where pressed when the selection
	 *            happened.
	 */
	public void fixedCellSelected(int col, int row, int statemask)
	{
	}

}

/*******************************************************************************
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Friederich Kupzog - initial API and implementation
 * fkmk@kupzog.de www.kupzog.de/fkmk
 ******************************************************************************/

interface KTableCellSelectionListener
{

	/**
	 * Is called if a non-fixed cell is selected (gets the focus).
	 * 
	 * @see KTable for an explanation of the term "fixed cells".
	 * @param col
	 *            the column of the cell
	 * @param row
	 *            the row of the cell
	 * @param statemask
	 *            the modifier keys that where pressed when the selection
	 *            happened.
	 */
	public void cellSelected(int col, int row, int statemask);

	/**
	 * Is called if a fixed cell is selected (is clicked).
	 * 
	 * @see KTable for an explanation of the term "fixed cells".
	 * @param col
	 *            the column of the cell
	 * @param row
	 *            the row of the cell
	 * @param statemask
	 *            the modifier keys that where pressed when the selection
	 *            happened.
	 */
	public void fixedCellSelected(int col, int row, int statemask);

}

/*******************************************************************************
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Friederich Kupzog - initial API and implementation
 * fkmk@kupzog.de www.kupzog.de/fkmk
 ******************************************************************************/

/**
 * @author kupzog (c) 2004 by Friederich Kupzog Elektronik & Software
 * 
 *         The table model is the most important part of KTable. It provides -
 *         content information - layout information - rendering information to
 *         the KTable.
 * 
 *         Generally speaking, all functions should return their results as
 *         quick as possible. If the table is slow, check it with
 *         KTableModelBasic. It is no longer slow, your model should be tuned.
 * 
 */

interface KTableModel
{

	/**
	 * This function should return the content at the given position. The
	 * content is an Object, that means it can be everything.
	 * 
	 * The returned Object is handed over to the KTableCellRenderer. You can
	 * deciede which renderer is used in getCellRenderer. Usually, the renderer
	 * expects the content being of a certain type.
	 */
	Object getContentAt(int col, int row);

	/**
	 * A table cell will be "in place editable" if this method returns a valid
	 * cell editor for the given cell. For no edit functionalitity return null.
	 * 
	 * @param col
	 * @param row
	 * @return KTableCellEditor
	 */
	KTableCellEditor getCellEditor(int col, int row);

	/**
	 * If getCellEditor() does return eny editors instead of null, the table
	 * will use this method to set the changed cell values.
	 * 
	 * @param col
	 * @param row
	 */
	void setContentAt(int col, int row, Object value);

	/**
	 * This function tells the KTable how many rows have to be displayed. KTable
	 * counts header rows as normal rows, so the number of header rows has to be
	 * added to the number of data rows. The function must at least return the
	 * number of fixed rows.
	 * 
	 * @return int
	 */
	int getRowCount();

	/**
	 * This function tells the KTable how many rows form the "column header".
	 * These rows are always displayed and not scrolled.
	 * 
	 * @return int
	 */
	int getFixedRowCount();

	/**
	 * This function tells the KTable how many columns have to be displayed. It
	 * must at least return the number of fixed Columns.
	 */
	int getColumnCount();

	/**
	 * This function tells the KTable how many columns form the "row header".
	 * These columns are always displayed and not scrolled.
	 * 
	 * @return int
	 */
	int getFixedColumnCount();

	/**
	 * Each column can have its individual width. The model has to manage these
	 * widths and return the values with this function.
	 * 
	 * @param col
	 * @return int
	 */
	int getColumnWidth(int col);

	/**
	 * This function should return true if the user should be allowed to resize
	 * the given column. (all rows have the same height except the first)
	 * 
	 * @param col
	 * @return boolean
	 */
	boolean isColumnResizable(int col);

	/**
	 * Each column can have its individual width. The model has to manage these
	 * widths. If the user resizes a column, the model has to keep track of
	 * these changes. The model is informed about such a resize by this method.
	 * (view updates are managed by the table)
	 * 
	 * @param col
	 * @param value
	 */
	void setColumnWidth(int col, int value);

	/**
	 * All rows except the first row have the same height.
	 * 
	 * @return int
	 */
	int getRowHeight();

	/**
	 * Returns the height of the first row, usually the header row. If no header
	 * is needed, this function should return the same value as getRowHeight.
	 * 
	 * @return int
	 */
	int getFirstRowHeight();

	/**
	 * This function should return true if the user should be allowed to resize
	 * the rows.
	 * 
	 * @param col
	 * @return boolean
	 */
	boolean isRowResizable();

	/**
	 * This function should return the minimum height of the rows. It is only
	 * needed if the rows are resizable.
	 * 
	 * @return int
	 */
	int getRowHeightMinimum();

	/**
	 * If the user resizes a row, the model has to keep track of these changes.
	 * The model is informed about such a resize by this method. (view updates
	 * are managed by the table)
	 */
	void setRowHeight(int value);

	/**
	 * Returns the cell renderer for the given cell. For a first approach,
	 * KTableCellRenderer.defaultRenderer can be returned. Derive
	 * KTableCellRenderer to change the tables appearance.
	 * 
	 * @param col
	 * @param row
	 * @return KTableCellRenderer
	 */
	KTableCellRenderer getCellRenderer(int col, int row);
}

/*******************************************************************************
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Friederich Kupzog - initial API and implementation
 * fkmk@kupzog.de www.kupzog.de/fkmk
 ******************************************************************************/

/**
 * @author Friederich Kupzog
 */
class KTableModelExample implements KTableModel
{

	private int[] colWidths;

	private int rowHeight;

	private HashMap content;

	/**
   * 
   */
	public KTableModelExample()
	{
		colWidths = new int[getColumnCount()];
		for (int i = 0; i < colWidths.length; i++)
		{
			colWidths[i] = 270;
		}
		rowHeight = 18;
		content = new HashMap();
	}

	// Inhalte

	public Object getContentAt(int col, int row)
	{
		// System.out.println("col "+col+" row "+row);
		String erg = (String) content.get(col + "/" + row);
		if (erg != null)
			return erg;
		return col + "/" + row;
	}

	/*
	 * overridden from superclass
	 */
	public KTableCellEditor getCellEditor(int col, int row)
	{
		if (col % 2 == 0)
		{
			KTableCellEditorCombo e = new KTableCellEditorCombo();
			e.setItems(new String[]
			{ "First text", "Second text", "third text" });
			return e;
		} else
			return new KTableCellEditorText();
	}

	/*
	 * overridden from superclass
	 */
	public void setContentAt(int col, int row, Object value)
	{
		content.put(col + "/" + row, value);
		//
	}

	// Umfang

	public int getRowCount()
	{
		return 100;
	}

	public int getFixedRowCount()
	{
		return 2;
	}

	public int getColumnCount()
	{
		return 100;
	}

	public int getFixedColumnCount()
	{
		return 1;
	}

	// GroBen

	public int getColumnWidth(int col)
	{
		return colWidths[col];
	}

	public int getRowHeight()
	{
		return rowHeight;
	}

	public boolean isColumnResizable(int col)
	{
		return true;
	}

	public int getFirstRowHeight()
	{
		return 22;
	}

	public boolean isRowResizable()
	{
		return true;
	}

	public int getRowHeightMinimum()
	{
		return 18;
	}

	public void setColumnWidth(int col, int value)
	{
		colWidths[col] = value;
	}

	public void setRowHeight(int value)
	{
		if (value < 2)
			value = 2;
		rowHeight = value;
	}

	// Rendering

	public KTableCellRenderer getCellRenderer(int col, int row)
	{
		return KTableCellRenderer.defaultRenderer;
	}

}

/*******************************************************************************
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Friederich Kupzog - initial API and implementation
 * fkmk@kupzog.de www.kupzog.de/fkmk
 ******************************************************************************/

class PaletteExampleModel implements KTableModel
{

	/*
	 * overridden from superclass
	 */
	public Object getContentAt(int col, int row)
	{
		return new RGB(col * 16, row * 16, (col + row) * 8);
	}

	/*
	 * overridden from superclass
	 */
	public KTableCellEditor getCellEditor(int col, int row)
	{
		return null;
	}

	/*
	 * overridden from superclass
	 */
	public void setContentAt(int col, int row, Object value)
	{
	}

	/*
	 * overridden from superclass
	 */
	public int getRowCount()
	{
		return 16;
	}

	/*
	 * overridden from superclass
	 */
	public int getFixedRowCount()
	{
		return 0;
	}

	/*
	 * overridden from superclass
	 */
	public int getColumnCount()
	{
		return 16;
	}

	/*
	 * overridden from superclass
	 */
	public int getFixedColumnCount()
	{
		return 0;
	}

	/*
	 * overridden from superclass
	 */
	public int getColumnWidth(int col)
	{
		return 10;
	}

	/*
	 * overridden from superclass
	 */
	public boolean isColumnResizable(int col)
	{
		return false;
	}

	/*
	 * overridden from superclass
	 */
	public void setColumnWidth(int col, int value)
	{
	}

	/*
	 * overridden from superclass
	 */
	public int getRowHeight()
	{
		return 10;
	}

	/*
	 * overridden from superclass
	 */
	public int getFirstRowHeight()
	{
		return 10;
	}

	/*
	 * overridden from superclass
	 */
	public boolean isRowResizable()
	{
		return false;
	}

	/*
	 * overridden from superclass
	 */
	public int getRowHeightMinimum()
	{
		return 10;
	}

	/*
	 * overridden from superclass
	 */
	public void setRowHeight(int value)
	{
	}

	private static KTableCellRenderer myRenderer = new PaletteExampleRenderer();

	/*
	 * overridden from superclass
	 */
	public KTableCellRenderer getCellRenderer(int col, int row)
	{
		return myRenderer;
	}

}

/*******************************************************************************
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Friederich Kupzog - initial API and implementation
 * fkmk@kupzog.de www.kupzog.de/fkmk
 ******************************************************************************/

class PaletteExampleRenderer extends KTableCellRenderer
{

	/**
   * 
   */
	public PaletteExampleRenderer()
	{
	}

	/*
	 * overridden from superclass
	 */
	public int getOptimalWidth(GC gc, int col, int row, Object content, boolean fixed)
	{
		return 16;
	}

	/*
	 * overridden from superclass
	 */
	public void drawCell(GC gc, Rectangle rect, int col, int row, Object content, boolean focus, boolean fixed, boolean clicked)
	{
		// Performance test:
		/*
		 * gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		 * gc.fillRectangle(rect);
		 * 
		 * int j=1; for (int i = 0; i < 10000000; i++) { j++; }
		 */
		Color color = new Color(m_Display, (RGB) content);
		gc.setBackground(m_Display.getSystemColor(SWT.COLOR_WHITE));
		rect.height++;
		rect.width++;
		gc.fillRectangle(rect);

		gc.setBackground(color);
		if (!focus)
		{
			rect.x += 1;
			rect.y += 1;
			rect.height -= 2;
			rect.width -= 2;
		}
		gc.fillRectangle(rect);
		color.dispose();
	}

}

/*******************************************************************************
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Friederich Kupzog - initial API and implementation
 * fkmk@kupzog.de www.kupzog.de/fkmk
 ******************************************************************************/

class TownExampleModel implements KTableModel
{

	private int[] colWidths;

	private TownExampleContent[] content;

	public TownExampleModel()
	{
		colWidths = new int[getColumnCount()];
		colWidths[0] = 120;
		colWidths[1] = 100;
		colWidths[2] = 180;

		content = new TownExampleContent[3];
		content[0] = new TownExampleContent("Aachen", "Germany");
		content[1] = new TownExampleContent("Cologne", "Germany");
		content[2] = new TownExampleContent("Edinburgh", "Scotland");

	}

	/*
	 * overridden from superclass
	 */
	public Object getContentAt(int col, int row)
	{
		if (row == 0) // Header
		{
			if (col == 0)
				return "Town";
			else if (col == 1)
				return "Country";
			else
				return "Notes";
		} else
		{
			return content[row - 1];
		}
	}

	/*
	 * overridden from superclass
	 */
	public KTableCellEditor getCellEditor(int col, int row)
	{
		if (row > 0 && col == 2)
			return new KTableCellEditorMultilineText();
		return null;
	}

	/*
	 * overridden from superclass
	 */
	public void setContentAt(int col, int row, Object value)
	{
		content[row - 1].notes = (String) value;
	}

	/*
	 * overridden from superclass
	 */
	public int getRowCount()
	{
		return 4;
	}

	/*
	 * overridden from superclass
	 */
	public int getFixedRowCount()
	{
		return 1;
	}

	/*
	 * overridden from superclass
	 */
	public int getColumnCount()
	{
		return 3;
	}

	/*
	 * overridden from superclass
	 */
	public int getFixedColumnCount()
	{
		return 0;
	}

	/*
	 * overridden from superclass
	 */
	public int getColumnWidth(int col)
	{
		return colWidths[col];
	}

	/*
	 * overridden from superclass
	 */
	public boolean isColumnResizable(int col)
	{
		return (col != 0);
	}

	/*
	 * overridden from superclass
	 */
	public void setColumnWidth(int col, int value)
	{
		if (value > 120)
			colWidths[col] = value;
	}

	/*
	 * overridden from superclass
	 */
	public int getRowHeight()
	{
		return 140;
	}

	/*
	 * overridden from superclass
	 */
	public int getFirstRowHeight()
	{
		return 20;
	}

	/*
	 * overridden from superclass
	 */
	public boolean isRowResizable()
	{
		return false;
	}

	/*
	 * overridden from superclass
	 */
	public int getRowHeightMinimum()
	{
		return 20;
	}

	/*
	 * overridden from superclass
	 */
	public void setRowHeight(int value)
	{
	}

	/*
	 * overridden from superclass
	 */
	public KTableCellRenderer getCellRenderer(int col, int row)
	{
		if (row > 0)
			return new TownExampleRenderer();
		return KTableCellRenderer.defaultRenderer;
	}

}

/*******************************************************************************
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Friederich Kupzog - initial API and implementation
 * fkmk@kupzog.de www.kupzog.de/fkmk
 ******************************************************************************/

class TownExampleRenderer extends KTableCellRenderer
{

	protected Display m_Display;

	public TownExampleRenderer()
	{
		m_Display = Display.getCurrent();
	}

	public int getOptimalWidth(GC gc, int col, int row, Object content, boolean fixed)
	{
		return Math.max(gc.stringExtent(content.toString()).x + 8, 120);
	}

	public void drawCell(GC gc, Rectangle rect, int col, int row, Object content, boolean focus, boolean fixed, boolean clicked)
	{
		Color textColor;
		Color backColor;
		Color borderColor;
		TownExampleContent myContent = (TownExampleContent) content;

		if (focus)
		{
			textColor = m_Display.getSystemColor(SWT.COLOR_BLUE);
		} else
		{
			textColor = m_Display.getSystemColor(SWT.COLOR_LIST_FOREGROUND);
		}
		backColor = (m_Display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		borderColor = m_Display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);

		gc.setForeground(borderColor);
		gc.drawLine(rect.x, rect.y + rect.height, rect.x + rect.width, rect.y + rect.height);

		gc.setForeground(borderColor);
		gc.drawLine(rect.x + rect.width, rect.y, rect.x + rect.width, rect.y + rect.height);

		if (col == 0)
		{
			gc.setBackground(m_Display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
			textColor = m_Display.getSystemColor(SWT.COLOR_LIST_FOREGROUND);
			gc.setForeground(textColor);

			gc.drawImage((myContent.image), rect.x, rect.y);

			rect.y += 120;
			rect.height -= 120;
			gc.fillRectangle(rect);
			gc.drawText((myContent.name), rect.x + 25, rect.y + 2);
		}

		else if (col == 1)
		{
			gc.setBackground(backColor);
			gc.setForeground(textColor);

			gc.fillRectangle(rect);

			SWTX.drawTextImage(gc, myContent.country, SWTX.ALIGN_HORIZONTAL_LEFT | SWTX.ALIGN_VERTICAL_TOP, null, SWTX.ALIGN_HORIZONTAL_LEFT
					| SWTX.ALIGN_VERTICAL_CENTER, rect.x + 3, rect.y, rect.width - 3, rect.height);

		}

		else if (col == 2)
		{
			gc.setBackground(backColor);
			gc.setForeground(textColor);

			gc.fillRectangle(rect);
			Rectangle save = gc.getClipping();
			gc.setClipping(rect);
			gc.drawText((myContent.notes), rect.x + 3, rect.y);
			gc.setClipping(save);

		}

	}

}

/*******************************************************************************
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software All rights
 * reserved. This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Friederich Kupzog - initial API and implementation
 * fkmk@kupzog.de www.kupzog.de/fkmk
 ******************************************************************************/

class TownExampleContent
{
	public String name;

	public Image image;

	public String country;

	public String notes;

	public TownExampleContent(String name, String country)
	{
		this.name = name;
		this.country = country;
		image = loadImageResource(Display.getCurrent(), "/gfx/" + name + ".gif");
		System.out.println(image);
		notes = "Double click to edit and use \n" + "Shift+Enter to start a new line...";
	}

	public Image loadImageResource(Display d, String name)
	{
		try
		{

			Image ret = null;
			Class clazz = this.getClass();
			InputStream is = clazz.getResourceAsStream(name);
			if (is != null)
			{
				ret = new Image(d, is);
				is.close();
			}
			return ret;
		} catch (Exception e1)
		{
			return null;
		}
	}

	/*
	 * overridden from superclass
	 */
	public String toString()
	{
		return notes;
	}

}

/**
 * @author Kosta, Friederich Kupzog
 */
class SWTX
{
	public static final int EVENT_SWTX_BASE = 1000;

	public static final int EVENT_TABLE_HEADER = EVENT_SWTX_BASE + 1;

	public static final int EVENT_TABLE_HEADER_CLICK = EVENT_SWTX_BASE + 2;

	public static final int EVENT_TABLE_HEADER_RESIZE = EVENT_SWTX_BASE + 3;

	//
	public static final int ALIGN_HORIZONTAL_MASK = 0x0F;

	public static final int ALIGN_HORIZONTAL_NONE = 0x00;

	public static final int ALIGN_HORIZONTAL_LEFT = 0x01;

	public static final int ALIGN_HORIZONTAL_LEFT_LEFT = ALIGN_HORIZONTAL_LEFT;

	public static final int ALIGN_HORIZONTAL_LEFT_RIGHT = 0x02;

	public static final int ALIGN_HORIZONTAL_LEFT_CENTER = 0x03;

	public static final int ALIGN_HORIZONTAL_RIGHT = 0x04;

	public static final int ALIGN_HORIZONTAL_RIGHT_RIGHT = ALIGN_HORIZONTAL_RIGHT;

	public static final int ALIGN_HORIZONTAL_RIGHT_LEFT = 0x05;

	public static final int ALIGN_HORIZONTAL_RIGHT_CENTER = 0x06;

	public static final int ALIGN_HORIZONTAL_CENTER = 0x07;

	public static final int ALIGN_VERTICAL_MASK = 0xF0;

	public static final int ALIGN_VERTICAL_TOP = 0x10;

	public static final int ALIGN_VERTICAL_BOTTOM = 0x20;

	public static final int ALIGN_VERTICAL_CENTER = 0x30;

	//
	private static GC m_LastGCFromExtend;

	private static Map m_StringExtentCache = new HashMap();

	private static synchronized Point getCachedStringExtent(GC gc, String text)
	{
		if (m_LastGCFromExtend != gc)
		{
			m_StringExtentCache.clear();
			m_LastGCFromExtend = gc;
		}
		Point p = (Point) m_StringExtentCache.get(text);
		if (p == null)
		{
			if (text == null)
				return new Point(0, 0);
			p = gc.stringExtent(text);
			m_StringExtentCache.put(text, p);
		}
		return new Point(p.x, p.y);
	}

	public static int drawTextVerticalAlign(GC gc, String text, int textAlign, int x, int y, int w, int h)
	{
		if (text == null)
			text = "";
		Point textSize = getCachedStringExtent(gc, text);
		{
			boolean addPoint = false;
			while ((text.length() > 0) && (textSize.x >= w))
			{
				text = text.substring(0, text.length() - 1);
				textSize = getCachedStringExtent(gc, text + "...");
				addPoint = true;
			}
			if (addPoint)
				text = text + "...";
			textSize = getCachedStringExtent(gc, text);
			if (textSize.x >= w)
			{
				text = "";
				textSize = getCachedStringExtent(gc, text);
			}
		}
		//
		if ((textAlign & ALIGN_VERTICAL_MASK) == ALIGN_VERTICAL_TOP)
		{
			gc.drawText(text, x, y);
			gc.fillRectangle(x, y + textSize.y, textSize.x, h - textSize.y);
			return textSize.x;
		}
		if ((textAlign & ALIGN_VERTICAL_MASK) == ALIGN_VERTICAL_BOTTOM)
		{
			gc.drawText(text, x, y + h - textSize.y);
			gc.fillRectangle(x, y, textSize.x, h - textSize.y);
			return textSize.x;
		}
		if ((textAlign & ALIGN_VERTICAL_MASK) == ALIGN_VERTICAL_CENTER)
		{
			int yOffset = (h - textSize.y) / 2;
			gc.drawText(text, x, y + yOffset);
			gc.fillRectangle(x, y, textSize.x, yOffset);
			gc.fillRectangle(x, y + yOffset + textSize.y, textSize.x, h - (yOffset + textSize.y));
			return textSize.x;
		}
		throw new SWTException("H: " + (textAlign & ALIGN_VERTICAL_MASK));
	}

	public static void drawTransparentImage(GC gc, Image image, int x, int y)
	{
		if (image == null)
			return;
		Point imageSize = new Point(image.getBounds().width, image.getBounds().height);
		Image img = new Image(Display.getCurrent(), imageSize.x, imageSize.y);
		GC gc2 = new GC(img);
		gc2.setBackground(gc.getBackground());
		gc2.fillRectangle(0, 0, imageSize.x, imageSize.y);
		gc2.drawImage(image, 0, 0);
		gc.drawImage(img, x, y);
		gc2.dispose();
		img.dispose();
	}

	public static void drawImageVerticalAlign(GC gc, Image image, int imageAlign, int x, int y, int h)
	{
		if (image == null)
			return;
		Point imageSize = new Point(image.getBounds().width, image.getBounds().height);
		//
		if ((imageAlign & ALIGN_VERTICAL_MASK) == ALIGN_VERTICAL_TOP)
		{
			drawTransparentImage(gc, image, x, y);
			gc.fillRectangle(x, y + imageSize.y, imageSize.x, h - imageSize.y);
			return;
		}
		if ((imageAlign & ALIGN_VERTICAL_MASK) == ALIGN_VERTICAL_BOTTOM)
		{
			drawTransparentImage(gc, image, x, y + h - imageSize.y);
			gc.fillRectangle(x, y, imageSize.x, h - imageSize.y);
			return;
		}
		if ((imageAlign & ALIGN_VERTICAL_MASK) == ALIGN_VERTICAL_CENTER)
		{
			int yOffset = (h - imageSize.y) / 2;
			drawTransparentImage(gc, image, x, y + yOffset);
			gc.fillRectangle(x, y, imageSize.x, yOffset);
			gc.fillRectangle(x, y + yOffset + imageSize.y, imageSize.x, h - (yOffset + imageSize.y));
			return;
		}
		throw new SWTException("H: " + (imageAlign & ALIGN_VERTICAL_MASK));
	}

	public static void drawTextImage(GC gc, String text, int textAlign, Image image, int imageAlign, int x, int y, int w, int h)
	{
		Point textSize = getCachedStringExtent(gc, text);
		Point imageSize;
		if (image != null)
			imageSize = new Point(image.getBounds().width, image.getBounds().height);
		else
			imageSize = new Point(0, 0);
		//
		/*
		 * Rectangle oldClipping = gc.getClipping(); gc.setClipping(x, y, w, h);
		 */
		try
		{
			if ((image == null) && ((textAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_CENTER))
			{
				Point p = getCachedStringExtent(gc, text);
				int offset = (w - p.x) / 2;
				if (offset > 0)
				{
					drawTextVerticalAlign(gc, text, textAlign, x + offset, y, w - offset, h);
					gc.fillRectangle(x, y, offset, h);
					gc.fillRectangle(x + offset + p.x, y, w - (offset + p.x), h);
				} else
				{
					p.x = drawTextVerticalAlign(gc, text, textAlign, x, y, w, h);
					// gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
					gc.fillRectangle(x + p.x, y, w - (p.x), h);
					// offset = (w - p.x) / 2;
					// gc.fillRectangle(x, y, offset, h);
					// gc.fillRectangle(x + offset + p.x, y, w - (offset + p.x),
					// h);
				}
				return;
			}
			if (((text == null) || (text.length() == 0)) && ((imageAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_CENTER))
			{
				int offset = (w - imageSize.x) / 2;
				// System.out.println("w: " + w + " imageSize" + imageSize + "
				// offset: " + offset);
				drawImageVerticalAlign(gc, image, imageAlign, x + offset, y, h);
				gc.fillRectangle(x, y, offset, h);
				gc.fillRectangle(x + offset + imageSize.x, y, w - (offset + imageSize.x), h);
				return;
			}
			if ((textAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_LEFT)
			{
				if ((imageAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_NONE)
				{
					textSize.x = drawTextVerticalAlign(gc, text, textAlign, x, y, w, h);
					gc.fillRectangle(x + textSize.x, y, w - textSize.x, h);
					return;
				}
				if ((imageAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_LEFT)
				{
					textSize.x = drawTextVerticalAlign(gc, text, textAlign, x + imageSize.x, y, w - imageSize.x, h);
					drawImageVerticalAlign(gc, image, imageAlign, x, y, h);
					gc.fillRectangle(x + textSize.x + imageSize.x, y, w - (textSize.x + imageSize.x), h);
					return;
				}
				if ((imageAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_RIGHT)
				{
					textSize.x = drawTextVerticalAlign(gc, text, textAlign, x, y, w - imageSize.x, h);
					drawImageVerticalAlign(gc, image, imageAlign, x + w - imageSize.x, y, h);
					gc.fillRectangle(x + textSize.x, y, w - (textSize.x + imageSize.x), h);
					return;
				}
				if ((imageAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_RIGHT_LEFT)
				{
					textSize.x = drawTextVerticalAlign(gc, text, textAlign, x, y, w - imageSize.x, h);
					drawImageVerticalAlign(gc, image, imageAlign, x + textSize.x, y, h);
					gc.fillRectangle(x + textSize.x + imageSize.x, y, w - (textSize.x + imageSize.x), h);
					return;
				}
				if ((imageAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_RIGHT_CENTER)
				{
					textSize.x = drawTextVerticalAlign(gc, text, textAlign, x, y, w - imageSize.x, h);
					int xOffset = (w - textSize.x - imageSize.x) / 2;
					drawImageVerticalAlign(gc, image, imageAlign, x + textSize.x + xOffset, y, h);
					gc.fillRectangle(x + textSize.x, y, xOffset, h);
					gc.fillRectangle(x + textSize.x + xOffset + imageSize.x, y, w - (textSize.x + xOffset + imageSize.x), h);
					return;
				}
				throw new SWTException("H: " + (imageAlign & ALIGN_HORIZONTAL_MASK));
			} // text align left
			if ((textAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_RIGHT)
			{
				if ((imageAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_NONE)
				{
					textSize.x = drawTextVerticalAlign(gc, text, textAlign, x, -1000, w, h);
					drawTextVerticalAlign(gc, text, textAlign, x + w - textSize.x, y, w, h);
					gc.fillRectangle(x, y, w - textSize.x, h);
					return;
				}
				if ((imageAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_LEFT)
				{
					textSize.x = drawTextVerticalAlign(gc, text, textAlign, x, -1000, w - imageSize.x, h);
					drawTextVerticalAlign(gc, text, textAlign, x + w - textSize.x, y, w - imageSize.x, h);
					drawImageVerticalAlign(gc, image, imageAlign, x, y, h);
					gc.fillRectangle(x + imageSize.x, y, w - (textSize.x + imageSize.x), h);
					return;
				}
				if ((imageAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_LEFT_RIGHT)
				{
					textSize.x = drawTextVerticalAlign(gc, text, textAlign, x, -1000, w - imageSize.x, h);
					drawTextVerticalAlign(gc, text, textAlign, x + w - textSize.x, y, w - imageSize.x, h);
					drawImageVerticalAlign(gc, image, imageAlign, x + w - (textSize.x + imageSize.x), y, h);
					gc.fillRectangle(x, y, w - (textSize.x + imageSize.x), h);
					return;
				}
				if ((imageAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_LEFT_CENTER)
				{
					textSize.x = drawTextVerticalAlign(gc, text, textAlign, x, -1000, w - imageSize.x, h);
					drawTextVerticalAlign(gc, text, textAlign, x + w - textSize.x, y, w - imageSize.x, h);
					int xOffset = (w - textSize.x - imageSize.x) / 2;
					drawImageVerticalAlign(gc, image, imageAlign, x + xOffset, y, h);
					gc.fillRectangle(x, y, xOffset, h);
					gc.fillRectangle(x + xOffset + imageSize.x, y, w - (xOffset + imageSize.x + textSize.x), h);
					return;
				}
				if ((imageAlign & ALIGN_HORIZONTAL_MASK) == ALIGN_HORIZONTAL_RIGHT)
				{
					textSize.x = drawTextVerticalAlign(gc, text, textAlign, x, -1000, w - imageSize.x, h);
					drawTextVerticalAlign(gc, text, textAlign, x + w - (textSize.x + imageSize.x), y, w - imageSize.x, h);
					drawImageVerticalAlign(gc, image, imageAlign, x + w - imageSize.x, y, h);
					gc.fillRectangle(x, y, w - (textSize.x + imageSize.x), h);
					return;
				}
				throw new SWTException("H: " + (imageAlign & ALIGN_HORIZONTAL_MASK));
			} // text align right
			throw new SWTException("H: " + (textAlign & ALIGN_HORIZONTAL_MASK));
		} // trye
		finally
		{
			// gc.setClipping(oldClipping);
		}
	}

	public static void drawTextImage(GC gc, String text, int textAlign, Image image, int imageAlign, Rectangle r)
	{
		drawTextImage(gc, text, textAlign, image, imageAlign, r.x, r.y, r.width, r.height);
	}

	public static void drawButtonUp(GC gc, String text, int textAlign, Image image, int imageAlign, int x, int y, int w, int h, Color face, Color shadowHigh,
			Color shadowNormal, Color shadowDark, int leftMargin, int topMargin)
	{
		Color prevForeground = gc.getForeground();
		Color prevBackground = gc.getBackground();
		try
		{
			gc.setBackground(face);
			gc.setForeground(shadowHigh);
			gc.drawLine(x, y, x, y + h - 1);
			gc.drawLine(x, y, x + w - 2, y);
			gc.setForeground(shadowDark);
			gc.drawLine(x + w - 1, y, x + w - 1, y + h - 1);
			gc.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
			gc.setForeground(shadowNormal);
			gc.drawLine(x + w - 2, y + 1, x + w - 2, y + h - 2);
			gc.drawLine(x + 1, y + h - 2, x + w - 2, y + h - 2);
			//
			gc.fillRectangle(x + 1, y + 1, leftMargin, h - 3);
			gc.fillRectangle(x + 1, y + 1, w - 3, topMargin);
			gc.setForeground(prevForeground);
			drawTextImage(gc, text, textAlign, image, imageAlign, x + 1 + leftMargin, y + 1 + topMargin, w - 3 - leftMargin, h - 3 - topMargin);
		} finally
		{
			gc.setForeground(prevForeground);
			gc.setBackground(prevBackground);
		}
	}

	public static void drawButtonUp(GC gc, String text, int textAlign, Image image, int imageAlign, int x, int y, int w, int h, Color face)
	{
		Display display = Display.getCurrent();
		drawButtonUp(gc, text, textAlign, image, imageAlign, x, y, w, h, face, display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW),
				display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW), display.getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW), 2, 2);
	}

	public static void drawButtonUp(GC gc, String text, int textAlign, Image image, int imageAlign, Rectangle r, int leftMargin, int topMargin)
	{
		Display display = Display.getCurrent();
		drawButtonUp(gc, text, textAlign, image, imageAlign, r.x, r.y, r.width, r.height, display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND),
				display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW), display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW),
				display.getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW), leftMargin, topMargin);
	}

	public static void drawButtonUp(GC gc, String text, int textAlign, Image image, int imageAlign, int x, int y, int w, int h)
	{
		Display display = Display.getCurrent();
		drawButtonUp(gc, text, textAlign, image, imageAlign, x, y, w, h, display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND),
				display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW), display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW),
				display.getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW), 2, 2);
	}

	public static void drawButtonUp(GC gc, String text, int textAlign, Image image, int imageAlign, Rectangle r)
	{
		Display display = Display.getCurrent();
		drawButtonUp(gc, text, textAlign, image, imageAlign, r.x, r.y, r.width, r.height);
	}

	public static void drawButtonDown(GC gc, String text, int textAlign, Image image, int imageAlign, int x, int y, int w, int h, Color face,
			Color shadowNormal, int leftMargin, int topMargin)
	{
		Color prevForeground = gc.getForeground();
		Color prevBackground = gc.getBackground();
		try
		{
			gc.setBackground(face);
			gc.setForeground(shadowNormal);
			gc.drawRectangle(x, y, w - 1, h - 1);
			gc.fillRectangle(x + 1, y + 1, 1 + leftMargin, h - 2);
			gc.fillRectangle(x + 1, y + 1, w - 2, topMargin + 1);
			gc.setForeground(prevForeground);
			drawTextImage(gc, text, textAlign, image, imageAlign, x + 2 + leftMargin, y + 2 + topMargin, w - 3 - leftMargin, h - 3 - topMargin);
		} finally
		{
			gc.setForeground(prevForeground);
			gc.setBackground(prevBackground);
		}
	}

	public static void drawButtonDown(GC gc, String text, int textAlign, Image image, int imageAlign, int x, int y, int w, int h)
	{
		Display display = Display.getCurrent();
		drawButtonDown(gc, text, textAlign, image, imageAlign, x, y, w, h, display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND),
				display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW), 2, 2);
	}

	public static void drawButtonDown(GC gc, String text, int textAlign, Image image, int imageAlign, Rectangle r)
	{
		drawButtonDown(gc, text, textAlign, image, imageAlign, r.x, r.y, r.width, r.height);
	}

	public static void drawButtonDown(GC gc, String text, int textAlign, Image image, int imageAlign, int x, int y, int w, int h, Color face)
	{
		Display display = Display.getCurrent();
		drawButtonDown(gc, text, textAlign, image, imageAlign, x, y, w, h, face, display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW), 2, 2);
	}

	public static void drawButtonDeepDown(GC gc, String text, int textAlign, Image image, int imageAlign, int x, int y, int w, int h)
	{
		Display display = Display.getCurrent();
		gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
		gc.drawLine(x, y, x + w - 2, y);
		gc.drawLine(x, y, x, y + h - 2);
		gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		gc.drawLine(x + w - 1, y, x + w - 1, y + h - 1);
		gc.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
		gc.setForeground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		gc.drawLine(x + 1, y + h - 2, x + w - 2, y + h - 2);
		gc.drawLine(x + w - 2, y + h - 2, x + w - 2, y + 1);
		//
		gc.setForeground(display.getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
		gc.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		gc.fillRectangle(x + 2, y + 2, w - 4, 1);
		gc.fillRectangle(x + 1, y + 2, 2, h - 4);
		//
		gc.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		drawTextImage(gc, text, textAlign, image, imageAlign, x + 2 + 1, y + 2 + 1, w - 4, h - 3 - 1);
	}

	public static void drawButtonDeepDown(GC gc, String text, int textAlign, Image image, int imageAlign, Rectangle r)
	{
		drawButtonDeepDown(gc, text, textAlign, image, imageAlign, r.x, r.y, r.width, r.height);
	}

	public static void drawFlatButtonUp(GC gc, String text, int textAlign, Image image, int imageAlign, int x, int y, int w, int h, Color face,
			Color shadowLight, Color shadowNormal, int leftMargin, int topMargin)
	{
		Color prevForeground = gc.getForeground();
		Color prevBackground = gc.getBackground();
		try
		{
			gc.setForeground(shadowLight);
			gc.drawLine(x, y, x + w - 1, y);
			gc.drawLine(x, y, x, y + h);
			gc.setForeground(shadowNormal);
			gc.drawLine(x + w, y, x + w, y + h);
			gc.drawLine(x + 1, y + h, x + w, y + h);
			//
			gc.setBackground(face);
			gc.fillRectangle(x + 1, y + 1, leftMargin, h - 1);
			gc.fillRectangle(x + 1, y + 1, w - 1, topMargin);
			//
			gc.setBackground(face);
			gc.setForeground(prevForeground);
			drawTextImage(gc, text, textAlign, image, imageAlign, x + 1 + leftMargin, y + 1 + topMargin, w - 1 - leftMargin, h - 1 - topMargin);
		} finally
		{
			gc.setForeground(prevForeground);
			gc.setBackground(prevBackground);
		}
	}

	public static void drawShadowImage(GC gc, Image image, int x, int y, int alpha)
	{
		Display display = Display.getCurrent();
		Point imageSize = new Point(image.getBounds().width, image.getBounds().height);
		//
		ImageData imgData = new ImageData(imageSize.x, imageSize.y, 24, new PaletteData(255, 255, 255));
		imgData.alpha = alpha;
		Image img = new Image(display, imgData);
		GC imgGC = new GC(img);
		imgGC.drawImage(image, 0, 0);
		gc.drawImage(img, x, y);
		imgGC.dispose();
		img.dispose();
	}
}