package com.ihsdev;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.OverheadTextChanged;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.WorldsFetch;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import net.runelite.http.api.worlds.World;
import net.runelite.http.api.worlds.WorldRegion;
import net.runelite.http.api.worlds.WorldResult;

@Slf4j
@PluginDescriptor(
	name = "1IHSDev",
	description = "IHS Dev plugin",
	tags = {"dev", "test"},
	loadWhenOutdated = true,
	enabledByDefault = true
)
public class IHSDevPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private IHSDevConfig config;

	@Override
	protected void startUp() throws Exception
	{
		LogWithColour("IHSDev started!");
		//SetWorld();
		/*
		 panel = new LevelsPanel(this, config);
		 navButton = NavigationButton.builder()
		 .tooltip("Time to Level")
		 .icon(ImageUtil.getResourceStreamFromClass(getClass(), "/icon.png"))
		 .priority(50)
		 .panel(panel)
		 .build();
		 
		 clientToolbar.addNavigation(navButton);
		 */
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("IHSDev stopped!");
	}
	
	@Provides
	IHSDevConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(IHSDevConfig.class);
	}
	// Used for myCheckbox
	/*
		* 
		@Provides
		IHSDevConfig getConfig(ConfigManager configManager)
		{
			return configManager.getConfig(IHSDevConfig.class);
		}
	*/
	
	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		log.info("GameStateChanged");

		if (gameStateChanged.getGameState() == GameState.LOGIN_SCREEN)
		{
			SetWorld();
		}
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "IHSDev says " + config.myCheckbox() + " - "+ config.greeting(), null);
		}
	}


	// Example of overhead message capture - https://www.youtube.com/watch?v=ZpDkEVxwhzo
	@Subscribe
	public void onOverheadTextChanged(OverheadTextChanged e)
	{
		if (e.getActor().equals(client.getLocalPlayer()) && e.getOverheadText().equals("Ah")) {
			client.getLocalPlayer().setOverheadText("Ah no");
		}
	}
	
	@Subscribe
	public void onConfigChanged(ConfigChanged e)
	{
		//e.g. world changed or config...
		if (e.getKey().equals("DoWorldSet")) {
			SetWorld();
		}
		System.out.println("Old: " + e.getOldValue() + ", New: " + e.getNewValue());
	}
	@Subscribe
	//public void onWorldsFetch(WorldResult e)
	public void onWorldResult(WorldResult e)
	{
		System.out.println("WorldsFetched");
		//SetWorld();
	}





	
	
	//#region Custom code
	public void LogWithColour(String message)
	{
		log.info("\u001b[32m " + message + " \u001b[0m");
	}
	
	public void SetWorld()
	{
		//https://github.com/runelite/runelite/blob/master/runelite-client/src/main/java/net/runelite/client/plugins/defaultworld/DefaultWorldPlugin.java
		
		//client.getWorld();
		//var world = client.getWorld();
		//world.setId(233);
		//client.changeWorld(world);
		net.runelite.api.World[] worlds = client.getWorldList();
		net.runelite.api.World world = null;
		for(int i = 0; i< worlds.length; i++){
			//System.out.println("World checked: " + worlds[i]);
			if (worlds[i].getId() == 326) {
				world = worlds[i];
			}
		}
		if (world != null) {
			System.out.println("World set");
			client.changeWorld(world); //326
		}
	}
	//#endregion
}
