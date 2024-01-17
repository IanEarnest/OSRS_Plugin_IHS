package com.ihsdev;

import com.google.common.base.MoreObjects;
import com.google.inject.Inject;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ScheduledExecutorService;
import javax.annotation.Nullable;
import javax.inject.Named;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.runelite.api.Client;
import net.runelite.client.RuneLiteProperties;
import net.runelite.client.account.SessionManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.SessionClose;
import net.runelite.client.events.SessionOpen;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.LinkBrowser;

public class IHSDevInfoPanel extends PluginPanel
{
	private static final ImageIcon MY_ICON;
	private static final ImageIcon SKILLS_INTERFACE;
	private static final ImageIcon SKILLS_XP_TREE;

	private final JLabel label_icon = new JLabel();
	private final JLabel myLabel = new JLabel();
	private JPanel myContainer = new JPanel();
	private JLabel label_skills_interface = new JLabel();
	private JLabel label_skills_xp_tree = new JLabel();

	@Inject
	@Nullable
	private Client client;

	@Inject
	private EventBus eventBus;

	@Inject
	private SessionManager sessionManager;

	@Inject
	private ScheduledExecutorService executor;

	private String helloText = "Welcome to IHSDev Info Panel";

	static
	{
		MY_ICON = new ImageIcon(ImageUtil.loadImageResource(IHSDevInfoPanel.class, "icon.png"));
		SKILLS_INTERFACE = new ImageIcon(ImageUtil.loadImageResource(IHSDevInfoPanel.class, "skills_interface.png"));
		SKILLS_XP_TREE = new ImageIcon(ImageUtil.loadImageResource(IHSDevInfoPanel.class, "skills_xp_table.png"));
	}

	void init()
	{
		// Layout
		setLayout(new BorderLayout());
		setBackground(ColorScheme.DARK_GRAY_COLOR);
		setBorder(new EmptyBorder(10, 10, 10, 10));
		
		// Label
		final Font smallFont = FontManager.getRunescapeSmallFont();
		myLabel.setFont(smallFont);
		myLabel.setText(helloText);
		myLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);

		// Images
		label_icon.setIcon(MY_ICON);
		label_skills_interface.setIcon(SKILLS_INTERFACE);
		label_skills_xp_tree.setIcon(SKILLS_XP_TREE);

		// Containers
		myContainer.setBackground(ColorScheme.DARK_GRAY_COLOR);
		myContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
		myContainer.setLayout(new GridLayout(0, 1)); // 0 1
		myContainer.add(myLabel);
		//myContainer.add(Box.createGlue());
		myContainer.add(label_icon);
		//myContainer.add(Box.createGlue());
		//myContainer.add(myLabel);

		JPanel myContainer2 = new JPanel();
		myContainer2.setBackground(ColorScheme.LIGHT_GRAY_COLOR);
		myContainer2.setBorder(new EmptyBorder(10, 10, 10, 10));
		myContainer2.setLayout(new GridLayout(0, 1)); // 0 1
		myContainer2.add(label_skills_interface);

		JPanel myContainer3 = new JPanel();
		myContainer3.setBackground(ColorScheme.MEDIUM_GRAY_COLOR);
		myContainer3.setBorder(new EmptyBorder(10, 10, 10, 10));
		myContainer3.setLayout(new GridLayout(0, 1)); // 0 1
		myContainer3.add(label_skills_xp_tree);
		//actionsContainer.setBorder(new EmptyBorder(10, 0, 0, 0));
		//actionsContainer.setLayout(new GridLayout(0, 1, 0, 10));
		
		
		// Final add to layout
		//add(myContainer, BorderLayout.NORTH);
		//add(actionsContainer, BorderLayout.CENTER);
		add(myContainer, BorderLayout.NORTH);
		add(myContainer2, BorderLayout.CENTER);
		add(myContainer3, BorderLayout.SOUTH);

		eventBus.register(this);
	}

	void deinit()
	{
		eventBus.unregister(this);
	}

	/**
	 * Builds a link panel with a given icon, text and callable to call.
	 */
	/*
	private static JPanel buildLinkPanel(ImageIcon icon, String topText, String bottomText, Runnable callback)
	{
		JPanel container = new JPanel();
		container.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		container.setLayout(new BorderLayout());
		container.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		final Color hoverColor = ColorScheme.DARKER_GRAY_HOVER_COLOR;
		final Color pressedColor = ColorScheme.DARKER_GRAY_COLOR.brighter();
		
		JLabel iconLabel = new JLabel(icon);
		container.add(iconLabel, BorderLayout.WEST);
		
		JPanel textContainer = new JPanel();
		textContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		textContainer.setLayout(new GridLayout(2, 1));
		textContainer.setBorder(new EmptyBorder(5, 10, 5, 10));
		
		container.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				container.setBackground(pressedColor);
				textContainer.setBackground(pressedColor);
			}
			
			@Override
			public void mouseReleased(MouseEvent e)
			{
				callback.run();
				container.setBackground(hoverColor);
				textContainer.setBackground(hoverColor);
			}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				container.setBackground(hoverColor);
				textContainer.setBackground(hoverColor);
				container.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				container.setBackground(ColorScheme.DARKER_GRAY_COLOR);
				textContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);
				container.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
		
		JLabel topLine = new JLabel(topText);
		topLine.setForeground(Color.WHITE);
		topLine.setFont(FontManager.getRunescapeSmallFont());
		
		JLabel bottomLine = new JLabel(bottomText);
		bottomLine.setForeground(Color.WHITE);
		bottomLine.setFont(FontManager.getRunescapeSmallFont());
		
		textContainer.add(topLine);
		textContainer.add(bottomLine);

		container.add(textContainer, BorderLayout.CENTER);

		JLabel arrowLabel = new JLabel(ARROW_RIGHT_ICON);
		container.add(arrowLabel, BorderLayout.EAST);
		
		return container;
	}
	*/
	
	@Subscribe
	public void onSessionOpen(SessionOpen sessionOpen)
	{
	}
	
	@Subscribe
	public void onSessionClose(SessionClose e)
	{
	}
}