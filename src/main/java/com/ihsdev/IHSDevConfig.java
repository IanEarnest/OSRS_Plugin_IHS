package com.ihsdev;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("ihsdev")
public interface IHSDevConfig extends Config
{
	//https://github.com/runelite/runelite/wiki/Creating-plugin-config-panels
	@ConfigItem(
		position = 1,
		keyName = "ihsdev",
		name = "Welcome Greeting",
		description = "The message to show to the user when they login"
	)
	default String greeting()
	{
		return "Hello World! From IHS";
	}

	@ConfigItem(
		keyName = "DoWorldSet",
		name = "Do World Set",
		description = "doWorldSet"
	)
	default boolean doWorldSet()
	{
		return true;
	}

	@ConfigItem(
		keyName = "ihsdev1",
		name = "Checkbox",
		description = "myCheckbox"
	)
	default boolean myCheckbox()
	{
		return true;
	}

	@ConfigItem(
		keyName = "ihsdev2",
		name = "Int",
		description = "myInt"
	)
	default int myInt()
	{
		return 0;
	}

	@ConfigItem(
		keyName = "ihsdev3",
		name = "Combobox",
		description = "myCombobox"
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
	
}
