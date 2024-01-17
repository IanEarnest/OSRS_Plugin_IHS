package com.ihsdev;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("ihsdev")
public interface IHSDevConfig extends Config
{
	/*
	 * String - Welcome Greeting
	 * Int - Default World Set
	 * Boolean - Enable/Disable Default World Setting
	 * Boolean - Enable/Disable GameTick Info
	 * Boolean - Trigger something
	 * Combobox - combo box of things
	*/
	public int defaultWorld = 326;

	public static enum ConfigItemsKeys { // TODO Update list per ConfigItem
		WelcomeGreeting,
		DefaultWorld,
		DoWorldSet,
		IsGameTickInfoSet,
		TriggerSomething,
		Combobox1
	}

	//https://github.com/runelite/runelite/wiki/Creating-plugin-config-panels
	//#region String - Welcome Greeting
	@ConfigItem(
		position = 1,
		keyName = "WelcomeGreeting",
		name = "Welcome Greeting",
		description = "The message to show to the user when they login"
		)
		default String greeting()
		{
			return "Hello World! From IHS";
		}
	//#endregion
		

	//#region Int - Default World Set
	@ConfigItem(
		position = 2,
		keyName = "DefaultWorld",
		name = "Default World on load",
		description = "Default World set"
	)
	default int defaultWorldInt()
	{
		return defaultWorld;
	}
	/*
	default int defaultWorldInt(int e)
	{
		return e;
	}
	*/
	//#endregion

	//#region Boolean - Enable/Disable Default World Setting
	@ConfigItem(
		position = 3,
		keyName = "DoWorldSet",
		name = "Set world on load?",
		description = "Set the world"
	)
	default boolean doWorldSet()
	{
		return true;
	}
	//#endregion

	//#region Boolean - Enable/Disable GameTick Info
	@ConfigItem(
		position = 4,
		keyName = "GameTickInfo",
		name = "GameTick Info?",
		description = "Messages for each game tick"
	)
	default boolean isGameTickInfoSet()
	{
		return true;
	}
	//#endregion

	//#region Boolean - Trigger something
	@ConfigItem(
		position = 4,
		keyName = "TriggerSomething",
		name = "Trigger Something?",
		description = "Trigger something"
	)
	default boolean triggerSomething()
	{
		return true;
	}
	//#endregion
	
	//#region Combobox - combo box of things
	// TODO fix combobox
	@ConfigItem(
		position = 5,
		keyName = "Combobox1",
		name = "Combobox",
		description = "myCombobox things"
	)
	default Enum myCombobox()
	{
		return Level.LOW;
	}
	enum Level {
		LOW,
		MEDIUM,
		HIGH
	}
	//#endregion
}
