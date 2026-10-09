package com.blackscreenfocus;

import java.awt.Insets;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ViewportMathTest
{
	@Test
	public void centredSixtyPercent()
	{
		// 2000x1000 panel, 60% -> 1200x600 game, 800x400 spare split evenly
		assertEquals(new Insets(200, 400, 200, 400), ViewportMath.insets(2000, 1000, 60, 60, 0.5, 0.5));
	}

	@Test
	public void insetsAlwaysAddUpToPanel()
	{
		for (int w = 1; w < 40; w += 7)
		{
			for (int pct = 20; pct <= 100; pct += 13)
			{
				Insets i = ViewportMath.insets(1921 + w, 1081 + w, pct, pct, 0.5, 0.5);
				int gameW = 1921 + w - i.left - i.right;
				assertEquals(Math.round((1921 + w) * pct / 100.0), gameW);
			}
		}
	}

	@Test
	public void alignedToCorners()
	{
		assertEquals(new Insets(0, 0, 400, 800), ViewportMath.insets(2000, 1000, 60, 60, 0, 0));
		assertEquals(new Insets(400, 800, 0, 0), ViewportMath.insets(2000, 1000, 60, 60, 1, 1));
	}

	@Test
	public void hundredPercentIsNoPadding()
	{
		assertEquals(new Insets(0, 0, 0, 0), ViewportMath.insets(1920, 1080, 100, 100, 0.5, 0.5));
	}

	@Test
	public void clampsAbsurdValues()
	{
		assertEquals(new Insets(0, 0, 0, 0), ViewportMath.insets(1920, 1080, 500, 500, 0.5, 0.5));
		// 1% is clamped up to the 20% floor so the game can never vanish
		assertEquals(new Insets(432, 768, 432, 768), ViewportMath.insets(1920, 1080, 1, 1, 0.5, 0.5));
	}

	@Test
	public void zeroSizedPanelIsSafe()
	{
		assertEquals(new Insets(0, 0, 0, 0), ViewportMath.insets(0, 0, 60, 60, 0.5, 0.5));
	}
}
