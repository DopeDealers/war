package com.tommytony.war.ui;

import com.tommytony.war.War;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DefaultZoneConfigUI extends ChestUI {
    @Override
    public void build(Player player, Inventory inv) {
        ItemStack item;
        ItemMeta meta;
        int i = 0;
        
		item = new ItemStack(Material.CHEST);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Default War Config");
		item.setItemMeta(meta);
		this.addItem(inv, 0, item, new Runnable() {
			@Override
			public void run() {
				War.war.getUIManager().assignUI(player, new EditDefaultWarConfigUI());
			}
		});
		item = new ItemStack(Material.CHEST);
		meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.YELLOW + "Default Team Config");
		item.setItemMeta(meta);
		this.addItem(inv, 1, item, new Runnable() {
			@Override
			public void run() {
				War.war.getUIManager().assignUI(player, new EditDefaultTeamConfigUI());
			}
		});
    }

    @Override
    public String getTitle() {
        return ChatColor.DARK_RED + "" + ChatColor.BOLD + "Default Configs";
    }

    @Override
    public int getSize() {
        return 9;
    }
}
