package com.tommytony.war.ui;

import com.tommytony.war.War;
import com.tommytony.war.Warzone;
import com.tommytony.war.config.WarzoneConfigBag;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Connor on 7/27/2017.
 */
public class EditZoneTeamConfigUI extends ChestUI {
	private final Warzone zone;

	public EditZoneTeamConfigUI(Warzone zone) {
		super();
		this.zone = zone;
	}

	@Override
	public void build(final Player player, Inventory inv) {
		ItemStack item;
		ItemMeta meta;
		
		UIConfigHelper.addTeamConfigOptions(this, player, inv, zone.getTeamDefaultConfig(), null, zone, 0);
		item = new ItemStack(Material.SNOWBALL);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "Restore Defaults");
		item.setItemMeta(meta);
		this.addItem(inv, getSize() - 1, item, new Runnable() {
			@Override
			public void run() {
				zone.getWarzoneConfig().reset();
				zone.getTeamDefaultConfig().reset();
				WarzoneConfigBag.afterUpdate(zone, player, "All options set to defaults in warzone " + zone.getName() + " by " + player.getName(), false);
				War.war.getUIManager().assignUI(player, new EditZoneTeamConfigUI(zone));
			}
		});
	}

	@Override
	public String getTitle() {
		return ChatColor.RED + "Warzone \"" + zone.getName() + "\": Team-Default-Config";
	}

	@Override
	public int getSize() {
		return 9*6;
	}
}
