//#region imports
package com.ihsdev;

import com.google.inject.Provides;

import java.awt.image.BufferedImage;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.*;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.WorldsFetch;
import net.runelite.client.game.WorldService;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.info.InfoPanel;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.WorldUtil;
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
//#endregion
public class IHSDevPlugin extends Plugin
{
	//#region startup, shutdown
	private LogLevel logLevel = LogLevel.INFO; // set as info as debug has too much spam
	private enum LogLevel {
		DEBUG,
		INFO
	}
	@Inject
	private Client client;

	@Inject
	private IHSDevConfig config;

	@Inject
    private WorldService worldService;


	@Inject
	private ClientToolbar clientToolbar;

	private IHSDevInfoPanel panel;
	private NavigationButton navButton;


	//@Inject
	//private OverlayManager overlayManager;


	@Override
	protected void startUp() throws Exception
	{
		//String info = STR."My name is \{name}"; // java 21
		LogWithColour("IHSDev started! ");
		loadPanel();
	}

	@Override
	protected void shutDown() throws Exception
	{
		LogWithColour("IHSDev stopped!");
		unloadPanel();
	}
	//#endregion
	
	//#region providers
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
	//#endregion

	//#region Subscriptions
	// GameState
	boolean hasWelcomeBeenSaid = false;
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
			if (hasWelcomeBeenSaid) return; // Only once per session
			hasWelcomeBeenSaid = true;
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "IHSDev says " + config.greeting(), null);
			//config.doWorldSet() 
		}
	}

	// Example of overhead message capture - https://www.youtube.com/watch?v=ZpDkEVxwhzo
	@Subscribe public void onOverheadTextChanged(OverheadTextChanged e) { OverheadTextChangeFromAhToAhh(e); }
	// World changed or config settings changed
	@Subscribe public void onConfigChanged(ConfigChanged e) { ConfigChangeThing(e); }

	// ?
	@Subscribe public void onWorldsFetch(WorldsFetch e) { LogWithColour("WorldsFetched");} //SetWorld();
	// ?
	@Subscribe public void onWorldResult(WorldResult e) { LogWithColour("WorldResult");}
	// Display message of item lost
	@Subscribe public void onItemDespawned(ItemDespawned e) { ItemDespawnedStuff(e); }
	//@Subscribe public void (e) { LogWithColour("");}
	
	// Working events
	//@Subscribe public void onWorldChanged(WorldChanged e) { LogWithColour("WorldChanged ");} //SetWorld();
	//@Subscribe public void onWorldListLoad(WorldListLoad e) { LogWithColour("WorldListLoad");} //SetWorld();
	//@Subscribe public void onWidgetLoaded(WidgetLoaded e) { LogWithColour("WidgetLoaded");} // After loaded in game
	//@Subscribe public void onVarbitChanged (VarbitChanged e) { LogWithColour("VarbitChanged");} // After loaded in game
	//@Subscribe public void onFocusChanged(FocusChanged e) { LogWithColour("FocusChanged");}
	//@Subscribe public void onBeforeRender(BeforeRender e) { LogWithColour("BeforeRender");} // constant
	//@Subscribe public void onClientTick(ClientTick e) { LogWithColour("ClientTick");}
	//@Subscribe public void onPostClientTick(PostClientTick e) { LogWithColour("PostClientTick");} // constant
	
	@Subscribe public void onGameTick(GameTick e) { GameTickStuff(e);}
	@Subscribe public void onBeforeRender(BeforeRender e) { BeforeRenderStuff(e); } // constant
	//#endregion
	



	
	//#region UI
	public void loadPanel()
	{
		//OSRS_Plugin_IHS\build\classes\java\test\com\ihsdev
		//https://github.com/runelite/runelite/blob/master/runelite-client/src/main/resources/net/runelite/client/plugins/info/info_icon.png
		//https://github.com/runelite/runelite/blob/master/runelite-client/src/main/java/net/runelite/client/plugins/info/InfoPanel.java
		panel = injector.getInstance(IHSDevInfoPanel.class);
		panel.init();

		final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "icon.png"); //info_icon.png icon
		
		navButton = NavigationButton.builder()
		.tooltip("IHSDev")
		.icon(icon)
		.priority(99) //10 //50
		.panel(panel)
		.build();
		
		clientToolbar.addNavigation(navButton);
		//clientToolbar.openPanel(navButton);
		LogWithColour("Panel set!");
	}

	public void unloadPanel()
	{
		panel.deinit();
		clientToolbar.removeNavigation(navButton);
		panel = null;
		navButton = null;
	}
	//#endregion
	







	//#region Custom code
	public void GameTickStuff(GameTick e)
	{
		if (config.isGameTickInfoSet() == false) return;
		LogWithColour("Idle timeout: " + client.getIdleTimeout() + 
		", mouse idle: " + client.getMouseIdleTicks() +
		", keyboard idle: " + client.getKeyboardIdleTicks() +
		", instance? " + client.isInInstancedRegion());
		//client.setIdleTimeout(0); // set to highest?
		// set int
		// set bool
		//config.defaultWorldInt() = 0;
		//config.defaultWorld = 0;
		//config.defaultWorldInt(client.getIdleTimeout());
	}

	public void ItemDespawnedStuff(ItemDespawned e)
	{
		// TODO Display item lost
		if (config.isGameTickInfoSet() == false) return; // TODO change to other bool?

		LogWithColour("ItemDespawned - you lost " + e.getItem().getClass().getName());
		LogWithColour("s " + e.toString());
		LogWithColour("id " + e.getItem().getId());
		LogWithColour("item " + e.getTile().getGroundItems());
	}

	public boolean hasMessageSent = false;
	// Client initialization - set world only once after rendering
	public void BeforeRenderStuff(BeforeRender e)
	{
		//LogWithColour("BeforeRenderStuff state: " + client.getGameState());
		if (hasMessageSent) return;
		if (client.getGameState() != GameState.LOGIN_SCREEN) return;
		
		hasMessageSent = true; // Do only once
		LogWithColour("Login screen reached"); 
		SetWorld();
	}

	// Happens on chat notifications too?
	public void OverheadTextChangeFromAhToAhh(OverheadTextChanged e)
	{
		if (e.getActor().equals(client.getLocalPlayer()) && e.getOverheadText().equals("Ah")) {
			LogWithColour("OverheadTextChangeFromAhToAhh");
			client.getLocalPlayer().setOverheadText("Ahh");
		}
	}

	// On Config change
	public void ConfigChangeThing(ConfigChanged e)
	{
		LogWithColour("ConfigChangeThing");
		
		// Int
		if (e.getKey().equals(IHSDevConfig.ConfigItemsKeys.DefaultWorld.name())) { //"DefaultWorld"
			SetWorld();
		}
		
		// Boolean
		if (e.getKey().equals(IHSDevConfig.ConfigItemsKeys.DoWorldSet.name())) {
			SetWorld();
		}

		// Boolean
		if (e.getKey().equals(IHSDevConfig.ConfigItemsKeys.IsGameTickInfoSet.name())) {
			LogWithColour("GameTick info set ;)");
		}

		// Int
		if (e.getKey().equals(IHSDevConfig.ConfigItemsKeys.TriggerSomething.name())) {
			LogWithColour("Done something ;)");
		}
		LogWithColour("Old: " + e.getOldValue() + ", New: " + e.getNewValue());
	}

	// Set world to load on
	public void SetWorld()
	{
		//LogWithColour("SetWorld");
		if (client.getGameState() != GameState.LOGIN_SCREEN) return;
		if (config.doWorldSet() == false) return;

		//https://github.com/runelite/runelite/blob/master/runelite-client/src/main/java/net/runelite/client/plugins/defaultworld/DefaultWorldPlugin.java
		// TODO create list of worlds?
		// World 326 = free
		// World 327 = members
		//log.info("Hello {}", client.getWorld());
		//event - WorldListLoad.getWorlds()	
		//https://github.com/runelite/runelite/blob/master/runelite-api/src/main/java/net/runelite/api/Client.java
		int worldId = config.defaultWorldInt(); // 326
        WorldResult worldResult = worldService.getWorlds();
        World world = worldResult.findWorld(worldId);
		if (world == null) return;
		final net.runelite.api.World rsWorld = client.createWorld();
		rsWorld.setActivity(world.getActivity());
		rsWorld.setAddress(world.getAddress());
		rsWorld.setId(world.getId());
		rsWorld.setPlayerCount(world.getPlayers());
		rsWorld.setLocation(world.getLocation());
		rsWorld.setTypes(WorldUtil.toWorldTypes(world.getTypes()));

		LogWithColour("World set to " + rsWorld.getId());
		client.changeWorld(rsWorld);
	}

	// Log to debug with coloured text
	public void LogWithColour(String message)
	{
		if (logLevel.equals(LogLevel.INFO)) log.info("\u001b[32m " + message + " \u001b[0m");
		else if (logLevel.equals(LogLevel.DEBUG)) log.debug("\u001b[32m " + message + " \u001b[0m");
	}
	//#endregion
}
