package com.kindergarten.util.print;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;

public/**
 * A printable text label that can occupy more than one page. If you are shure
 * that the box will not be bigger than one page, you can use PLittleTextBox to
 * make the layout process quicker.
 * 
 * @author Friederich Kupzog For more details
 * @see PDocument and
 * @see PBox
 */
class PTextBox extends PBox
{

	protected String text;

	protected PTextStyle textStyle;

	// multi-page
	protected ArrayList pageList;

	protected ArrayList textLines;

	protected int unplacedLines;

	/**
	 * Creates a non-wrapping text box with a fixed size according to its text.
	 * 
	 * @param parent
	 * @param style
	 */
	public PTextBox(PContainer parent)
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
	public PTextBox(PContainer parent, int style)
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
	public PTextBox(PContainer parent, int style, double hWeight, double minWidth)
	{
		super(parent, style, hWeight, minWidth);
		init();
	}

	private void init()
	{
		text = "";
		textStyle = PTextStyle.getDefaultStyle();
		pageList = new ArrayList();
		textLines = new ArrayList();
		unplacedLines = 0;
	}

	public void setText(String text)
	{
		if (text == null)
			text = "";
		this.text = text;
	}

	protected int layoutHowMuchWouldYouOccupyOf(Point spaceLeft, int page)
	{
		if (textLines.size() == 0)
			splitIntoLines();
		if (unplacedLines == 0)
			return 0;

		gc.setFont(textStyle.getFont());
		int lineHeight = gc.stringExtent("A").y;
		// System.out.println("LineH: "+lineHeight+" Space: "+spaceLeft.y);
		int erg = 0;

		int ctr = 0;
		do
		{
			erg += lineHeight;
			ctr++;
			if (ctr == unplacedLines)
				break;
		} while (erg + lineHeight <= spaceLeft.y);

		if (erg > spaceLeft.y)
			return -1;
		return erg;
	}

	/*
	 * overridden from superclass
	 */
	protected boolean layoutWouldYouFinishWithin(Point spaceLeft, int page)
	{
		gc.setFont(textStyle.getFont());
		int lineHeight = gc.stringExtent("A").y;
		return ((unplacedLines * lineHeight) <= spaceLeft.y);
	}

	/*
	 * overridden from superclass
	 */
	protected int layoutOccupy(Point origin, Point spaceLeft, int page)
	{
		if (textLines.size() == 0)
			splitIntoLines();
		if (unplacedLines == 0)
			return 0;
		if (this.origin.page == 0)
		{
			this.origin.page = page;
			this.origin.x = origin.x;
			this.origin.y = origin.y;
		}

		gc.setFont(textStyle.getFont());
		int lineHeight = gc.stringExtent("A").y;
		int erg = 0;

		int ctr = 0;
		do
		{
			erg += lineHeight;
			ctr++;
			if (ctr == unplacedLines)
				break;
		} while (erg + lineHeight <= spaceLeft.y);

		if (erg > spaceLeft.y)
			return 0;

		PTextPart part = new PTextPart();
		part.numOfLines = ctr;
		part.origin = new Point(origin.x, origin.y);
		part.startLine = textLines.size() - unplacedLines;
		pageList.add(part);

		unplacedLines -= ctr;

		return erg;
	}

	/*
	 * overridden from superclass
	 */
	protected boolean layoutIsOnPage(int page)
	{
		if (page >= origin.page && page < origin.page + pageList.size())
			return true;
		return false;
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
							textLines.add(text.substring(start, pos).trim());
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
		unplacedLines = textLines.size();
	}

	/*
	 * overridden from superclass
	 */
	protected void layoutResetTuning()
	{
		super.layoutResetTuning();
		pageList.clear();
		textLines.clear();
	}

	/*
	 * overridden from superclass
	 */
	protected int getHeight(int page)
	{
		gc.setFont(textStyle.getFont());
		int lineHeight = gc.stringExtent("A").y;

		PTextPart part = (PTextPart) pageList.get(page - origin.page);
		return part.numOfLines * lineHeight;
	}

	public void draw(int page, Point originOffset)
	{

		if (layoutIsOnPage(page))
		{
			PTextPart part = (PTextPart) pageList.get(page - origin.page);
			this.origin = new PagePoint(part.origin, origin.page);
			super.draw(page, originOffset);
			Font font = textStyle.getFont();
			gc.setFont(font);
			gc.setForeground(textStyle.getFontColor());

			int lineHeight = gc.stringExtent("A").y;

			for (int i = 0; i < part.numOfLines; i++)
			{

				int alignPixel = 0;
				if (textStyle.textAlign == PTextStyle.ALIGN_CENTER)
				{
					int textWidth = gc.stringExtent((String) textLines.get(part.startLine + i)).x;
					alignPixel = (getWidth() - pixelX(textStyle.getMarginLeft()) - pixelX(textStyle.getMarginRight()) - textWidth) / 2;
				} else if (textStyle.textAlign == PTextStyle.ALIGN_RIGHT)
				{
					gc.setFont(font);
					int textWidth = gc.stringExtent((String) textLines.get(part.startLine + i)).x;
					alignPixel = (getWidth() - pixelX(textStyle.getMarginLeft()) - pixelX(textStyle.getMarginRight()) - textWidth);
					// System.out.println("'"+(String)textLines.get(part.startLine
					// + i)+"' I="+i+" width = "+textWidth+ "
					// align="+alignPixel);
				}

				gc.drawText((String) textLines.get(part.startLine + i), part.origin.x + alignPixel + originOffset.x + pixelX(textStyle.getMarginLeft()),
						part.origin.y + originOffset.y + (i * lineHeight), true);

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
