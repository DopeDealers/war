package com.tommytony.war.utility;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerRequests {
	public static boolean hasArrow(Player player) {
		for(ItemStack it : player.getInventory().getContents()) {
			if(it != null && it.getType().name().toLowerCase().contains("arrow")) {
				return true;
			}
		}
		return false;
	}
	
	public static void removeArrow(Player player) {
		for(ItemStack it : player.getInventory().getContents()) {
			if(it != null && it.getType().name().toLowerCase().contains("arrow")) {
				it.setAmount(it.getAmount()-1);
				return;
			}
		}
	}
}
