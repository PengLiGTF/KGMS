package com.kindergarten.util.print;

import org.eclipse.swt.widgets.Table;

/*
 * This feature was contributed by Onsel Armagan, Istanbul, Turkey Thanks a
 * lot!
 */
public class SWTPTable
{
	protected Table table;
	protected PTableBoxProvider boxProvider;
	protected PContainer parent;

	public SWTPTable(PContainer parent)
	{
		this.parent = parent;
	}

	protected void fillDocument()
	{
		boolean abgeschnitten = false;
		calculatePageLengths();
		// Zeilen
		/**
		 * TODO Print Table Header if (j == 0) style = PBox.POS_BELOW |
		 * PBox.ROW_ALIGN;
		 */
		double width = parent.getPossibleWidth();

		for (int j = 0; j < table.getColumnCount(); j++)
		{
			// System.out.println(" Zeile "+j);
			int height = table.getHeaderHeight();

			int style = PBox.POS_RIGHT | PBox.ROW_ALIGN;
			if (j == 0)
				style = PBox.POS_BELOW | PBox.ROW_ALIGN;
			PBox box = boxProvider.createBox(parent, style, j, 0, table.getColumn(j).getWidth(), height, true, table.getColumn(j).getText());
			double boxWidth = Math.max(box.minCm, parent.getPossibleWidth() * box.hWeight);
			width -= boxWidth;
			if (width < 0)
			{
				box.dispose();
				abgeschnitten = true;
				break;
			}
		}

		for (int i = 0; i < table.getItemCount(); i++)
		{
			// System.out.println("Spalte "+i);
			int height = table.getItemHeight();
			width = parent.getPossibleWidth();

			// Spalten
			for (int j = 0; j < table.getColumnCount(); j++)
			{
				// System.out.println(" Zeile "+j);
				int style = PBox.POS_RIGHT | PBox.ROW_ALIGN;
				if (j == 0)
					style = PBox.POS_BELOW | PBox.ROW_ALIGN;
				PBox box = boxProvider.createBox(parent, style, j, i, table.getColumn(j).getWidth(), height, false, table.getItem(i).getText(j));
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

	public void calculatePageLengths()
	{
		if (table != null)
		{
			PDocument doc = (PDocument) parent;

			double width = parent.getPossibleWidth();

			for (int j = 0; j < table.getColumnCount(); j++)
			{
				// System.out.println(" Zeile "+j);
				int height = table.getHeaderHeight();
				double boxWidth = Math.max(0, table.getColumn(j).getWidth() * 0.03);
				width -= boxWidth;
				if (width < 0)
				{
					break;
				}
			}
			if (width < 0)
			{
				doc.setPageHeight(PageSetup.paperWidth);
				doc.setPageWidth(PageSetup.paperHeight);
			}

		}

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
	public Table getTable()
	{
		return table;
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
		if (this.boxProvider != null && this.table != null)
		{
			fillDocument();
		}
	}

	/**
	 * Sets the table.
	 * 
	 * @param table
	 *            The table to set
	 */
	public void setTable(Table table)
	{
		this.table = table;
		if (this.boxProvider != null && this.table != null)
		{
			fillDocument();
		}
	}
}
