package com.ihsdev;

import com.ihsdev.IHSDevPlugin;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class IHSDevPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(IHSDevPlugin.class);
		RuneLite.main(args);
	}
}

