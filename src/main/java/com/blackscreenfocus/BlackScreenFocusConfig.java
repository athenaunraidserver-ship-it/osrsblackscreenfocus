package com.blackscreenfocus;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.Range;

@ConfigGroup(BlackScreenFocusConfig.GROUP)
public interface BlackScreenFocusConfig extends Config
{
	String GROUP = "blackscreenfocus";

	enum Horizontal
	{
		LEFT(0), CENTER(0.5), RIGHT(1);

		final double fraction;

		Horizontal(double fraction)
		{
			this.fraction = fraction;
		}
	}

	enum Vertical
	{
		TOP(0), CENTER(0.5), BOTTOM(1);

		final double fraction;

		Vertical(double fraction)
		{
			this.fraction = fraction;
		}
	}

	@Range(min = ViewportMath.MIN_PERCENT, max = ViewportMath.MAX_PERCENT)
	@ConfigItem(keyName = "widthPercent", name = "Width %", description = "Game width as a percent of the client window.", position = 1)
	default int widthPercent()
	{
		return 60;
	}

	@ConfigItem(keyName = "linkHeight", name = "Height follows width", description = "When ticked, Height % below is ignored and the width % is used for the height too.", position = 2)
	default boolean linkHeight()
	{
		return false;
	}

	@Range(min = ViewportMath.MIN_PERCENT, max = ViewportMath.MAX_PERCENT)
	@ConfigItem(keyName = "heightPercent", name = "Height %", description = "Game height as a percent of the client window. Only used when 'Height follows width' is off.", position = 3)
	default int heightPercent()
	{
		return 60;
	}

	@ConfigItem(keyName = "horizontal", name = "Horizontal position", description = "Where the game sits left to right.", position = 4)
	default Horizontal horizontal()
	{
		return Horizontal.CENTER;
	}

	@ConfigItem(keyName = "vertical", name = "Vertical position", description = "Where the game sits top to bottom.", position = 5)
	default Vertical vertical()
	{
		return Vertical.CENTER;
	}

	@ConfigItem(keyName = "background", name = "Empty space colour", description = "Colour of the area around the game.", position = 6)
	default Color background()
	{
		return Color.BLACK;
	}

	@ConfigItem(keyName = "toggleHotkey", name = "Toggle hotkey", description = "Switch between the small and full-size game. Works while the plugin is enabled.", position = 7)
	default Keybind toggleHotkey()
	{
		return Keybind.NOT_SET;
	}
}
