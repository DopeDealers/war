package com.tommytony.war.utility;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerRequests {
	public static boolean hasBullet(Player player, String bullet) {
		for(ItemStack it : player.getInventory().getContents()) {
			if(it != null && it.getType().name().toUpperCase().contains(bullet.toUpperCase())) {
				return true;
			}
		}
		return false;
	}
	
	public static void removeBullet(Player player, String bullet) {
		for(ItemStack it : player.getInventory().getContents()) {
			if(it != null && it.getType().name().toUpperCase().contains(bullet.toUpperCase())) {
				it.setAmount(it.getAmount()-1);
				return;
			}
		}
	}
}
