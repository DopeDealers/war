package com.tommytony.war.ui;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.tommytony.war.War;

/**
 * Created by Connor on 7/27/2017.
 */
public class EditDefaultTeamConfigUI extends ChestUI {
	public EditDefaultTeamConfigUI() {
		super();
	}

	@Override
	public void build(final Player player, Inventory inv) {
		UIConfigHelper.addTeamConfigOptions(this, player, inv, War.war.getTeamDefaultConfig(), null, null, 0);
	}

	@Override
	public String getTitle() {
		return ChatColor.RED + "War Default Team Config";
	}

	@Override
	public int getSize() {
		return 9*6;
	}
}
