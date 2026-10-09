package com.blackscreenfocus;

import java.awt.Insets;

/**
 * Pure geometry: given the full size of the game panel and a target size in percent,
 * work out the black padding (insets) that leaves the game area in the requested spot.
 */
final class ViewportMath
{
	static final int MIN_PERCENT = 20;
	static final int MAX_PERCENT = 100;

	private ViewportMath()
	{
	}

	/**
	 * @param panelWidth  full panel width in px
	 * @param panelHeight full panel height in px
	 * @param widthPct    game area width, percent of panel (clamped to 20-100)
	 * @param heightPct   game area height, percent of panel (clamped to 20-100)
	 * @param alignX      0 = left, 0.5 = centre, 1 = right
	 * @param alignY      0 = top, 0.5 = centre, 1 = bottom
	 */
	static Insets insets(int panelWidth, int panelHeight, int widthPct, int heightPct, double alignX, double alignY)
	{
		if (panelWidth <= 0 || panelHeight <= 0)
		{
			return new Insets(0, 0, 0, 0);
		}

		int gameW = scaled(panelWidth, widthPct);
		int gameH = scaled(panelHeight, heightPct);

		int spareX = panelWidth - gameW;
		int spareY = panelHeight - gameH;

		int left = (int) Math.round(spareX * alignX);
		int top = (int) Math.round(spareY * alignY);

		return new Insets(top, left, spareY - top, spareX - left);
	}

	private static int scaled(int full, int pct)
	{
		int clamped = Math.max(MIN_PERCENT, Math.min(MAX_PERCENT, pct));
		return Math.max(1, (int) Math.round(full * clamped / 100.0));
	}
}
