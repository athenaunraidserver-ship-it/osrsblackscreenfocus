package com.blackscreenfocus;

import com.google.inject.Provides;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.inject.Inject;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.HotkeyListener;

@Slf4j
@PluginDescriptor(
	name = "Black Screen Focus",
	description = "Shrink the game to a % of the window and leave the rest black, for less mouse travel",
	tags = {"resize", "shrink", "stretched", "focus", "black", "toa", "pvp", "screen"}
)
public class BlackScreenFocusPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private BlackScreenFocusConfig config;

	@Inject
	private KeyManager keyManager;

	/** The black panel RuneLite wraps around the game canvas. Touch on the EDT only. */
	private JPanel gamePanel;
	private Border originalBorder;
	private Color originalBackground;

	/** Hotkey flips this; plugin enabled + shrunk is the default. */
	private boolean shrunk = true;

	private final ComponentAdapter resizeListener = new ComponentAdapter()
	{
		@Override
		public void componentResized(ComponentEvent e)
		{
			applyLayout();
		}
	};

	private final HotkeyListener hotkeyListener = new HotkeyListener(() -> config.toggleHotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			SwingUtilities.invokeLater(() ->
			{
				shrunk = !shrunk;
				applyLayout();
			});
		}
	};

	@Provides
	BlackScreenFocusConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(BlackScreenFocusConfig.class);
	}

	@Override
	protected void startUp()
	{
		keyManager.registerKeyListener(hotkeyListener);
		shrunk = true;
		SwingUtilities.invokeLater(() ->
		{
			Canvas canvas = client.getCanvas();
			gamePanel = canvas == null ? null
				: (JPanel) SwingUtilities.getAncestorOfClass(JPanel.class, canvas);
			if (gamePanel == null)
			{
				log.warn("Black Screen Focus: could not find the game panel, doing nothing");
				return;
			}

			originalBorder = gamePanel.getBorder();
			originalBackground = gamePanel.getBackground();
			gamePanel.addComponentListener(resizeListener);
			applyLayout();
		});
	}

	@Override
	protected void shutDown()
	{
		keyManager.unregisterKeyListener(hotkeyListener);
		SwingUtilities.invokeLater(() ->
		{
			if (gamePanel == null)
			{
				return;
			}
			gamePanel.removeComponentListener(resizeListener);
			gamePanel.setBorder(originalBorder);
			gamePanel.setBackground(originalBackground);
			gamePanel.revalidate();
			gamePanel.repaint();
			gamePanel = null;
		});
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (BlackScreenFocusConfig.GROUP.equals(event.getGroup()))
		{
			SwingUtilities.invokeLater(this::applyLayout);
		}
	}

	/** EDT only. */
	private void applyLayout()
	{
		if (gamePanel == null)
		{
			return;
		}

		Insets insets = shrunk
			? ViewportMath.insets(
				gamePanel.getWidth(),
				gamePanel.getHeight(),
				config.widthPercent(),
				config.linkHeight() ? config.widthPercent() : config.heightPercent(),
				config.horizontal().fraction,
				config.vertical().fraction)
			: new Insets(0, 0, 0, 0);

		gamePanel.setBackground(config.background());

		// Skip no-op updates: avoids a relayout on every resize event while dragging.
		Insets current = gamePanel.getInsets();
		if (!current.equals(insets))
		{
			gamePanel.setBorder(new EmptyBorder(insets));
			gamePanel.revalidate();
			gamePanel.repaint();
		}
	}
}
