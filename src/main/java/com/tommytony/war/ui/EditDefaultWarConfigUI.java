package com.tommytony.war.ui;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.tommytony.war.War;

/**
 * Created by Connor on 7/27/2017.
 */
public class EditDefaultWarConfigUI extends ChestUI {
	public EditDefaultWarConfigUI() {
		super();
	}

	@Override
	public void build(final Player player, Inventory inv) {
		int i = 0;

		i = UIConfigHelper.addWarzoneConfigOptions(this, player, inv, War.war.getWarzoneDefaultConfig(), null, i);
	}

	@Override
	public String getTitle() {
		return ChatColor.RED + "War Default Zone Config";
	}

	@Override
	public int getSize() {
		return 9*6;
	}
}
