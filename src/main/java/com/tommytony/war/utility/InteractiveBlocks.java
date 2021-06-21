package com.tommytony.war.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

public class InteractiveBlocks {
    private static List<Material> InteractiveBlocks = new ArrayList<>(
    		Arrays.asList(
    				Material.CRAFTING_TABLE,Material.ENCHANTING_TABLE,Material.ANVIL,Material.BREWING_STAND,Material.TRAPPED_CHEST,Material.CHEST,Material.DISPENSER,Material.DROPPER,Material.JUKEBOX,Material.ENDER_CHEST,Material.STONE_BUTTON,Material.BEACON,Material.TRIPWIRE_HOOK,Material.HOPPER,Material.DAYLIGHT_DETECTOR,Material.ITEM_FRAME,Material.REPEATER,Material.COMPARATOR,Material.ACACIA_DOOR,Material.BIRCH_DOOR,Material.DARK_OAK_DOOR,Material.JUNGLE_DOOR,Material.SPRUCE_DOOR,Material.LEVER,Material.SPRUCE_FENCE_GATE,Material.BIRCH_FENCE_GATE,Material.JUNGLE_FENCE_GATE,Material.DARK_OAK_FENCE_GATE,Material.ACACIA_FENCE_GATE,Material.WHITE_SHULKER_BOX,Material.ORANGE_SHULKER_BOX,Material.MAGENTA_SHULKER_BOX,Material.LIGHT_BLUE_SHULKER_BOX,Material.YELLOW_SHULKER_BOX,Material.LIME_SHULKER_BOX,Material.PINK_SHULKER_BOX,Material.GRAY_SHULKER_BOX,Material.WHITE_SHULKER_BOX,Material.CYAN_SHULKER_BOX,Material.PURPLE_SHULKER_BOX,Material.BLUE_SHULKER_BOX,Material.BROWN_SHULKER_BOX,Material.GREEN_SHULKER_BOX,Material.RED_SHULKER_BOX,Material.BLACK_SHULKER_BOX,Material.BARREL,
    				Material.OAK_BUTTON,Material.SPRUCE_BUTTON,Material.BIRCH_BUTTON,Material.JUNGLE_BUTTON,Material.ACACIA_BUTTON,Material.DARK_OAK_BUTTON,Material.CRIMSON_BUTTON,Material.WARPED_BUTTON,Material.POLISHED_BLACKSTONE_BUTTON,
    				Material.OAK_TRAPDOOR,Material.SPRUCE_TRAPDOOR,Material.BIRCH_TRAPDOOR,Material.JUNGLE_TRAPDOOR,Material.ACACIA_TRAPDOOR,Material.DARK_OAK_TRAPDOOR,Material.CRIMSON_TRAPDOOR,Material.WARPED_TRAPDOOR,
    				Material.OAK_BOAT,Material.SPRUCE_BOAT,Material.BIRCH_BOAT,Material.JUNGLE_BOAT,Material.ACACIA_BOAT,Material.DARK_OAK_BOAT,
    				Material.CRIMSON_DOOR,Material.WARPED_DOOR,Material.CRIMSON_FENCE_GATE,Material.WARPED_FENCE_GATE
    				));
    
    public static List<Material> getList() {
    	return InteractiveBlocks;
    }
}
