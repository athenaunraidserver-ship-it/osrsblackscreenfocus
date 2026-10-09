package com.blackscreenfocus;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

/** Launches a RuneLite dev client with this plugin preloaded. Run with: gradlew run */
public class BlackScreenFocusPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(BlackScreenFocusPlugin.class);
		RuneLite.main(args);
	}
}
