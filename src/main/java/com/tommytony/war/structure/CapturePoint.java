package com.tommytony.war.structure;

import java.util.Collection;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.tommytony.war.Warzone;
import com.tommytony.war.config.TeamKind;
import com.tommytony.war.config.WarzoneConfig;
import com.tommytony.war.volume.Volume;

import net.md_5.bungee.api.ChatColor;

/**
 * Capture points
 *
 * @author Connor Monahan
 */
public class CapturePoint {
	private static int[][][] structure = {
	};

	private final String name;
	private Volume volume;
	private Location location;
	private TeamKind controller, defaultController;
	private int strength, controlTime;
	private Warzone warzone;

	public CapturePoint(String name, Location location, TeamKind defaultController, int strength, Warzone warzone) {
		this.name = name;
		this.defaultController = defaultController;
		this.controller = defaultController;
		this.strength = strength;
		this.controlTime = 0;
		this.warzone = warzone;
		this.volume = new Volume("cp-" + name, warzone.getWorld());
		this.setLocation(location, warzone);
	}

	private Location getOrigin() {
		return location.clone().getBlock().getLocation();
	}

	private void updateBlocks() {
		Validate.notNull(location);
		// Set origin to middle
		Location origin = this.getOrigin();
		// Build structure - not necessary rn
		/*for (int y = 0; y < structure.length; y++) {
			for (int z = 0; z < structure[0].length; z++) {
				for (int x = 0; x < structure[0][0].length; x++) {
					BlockState state = origin.clone().add(x, y, z).getBlock().getState();
					switch (structure[y][z][x]) {
						case 0:
							state.setType(Material.AIR);
							break;
						default:
							throw new IllegalStateException("Invalid structure");
					}
					state.update(true);
				}
			}
		} */
		
		ArmorStand stand = null;
		
		Collection<Entity> entities = origin.getWorld().getNearbyEntities(origin, 1, 3, 1);
		for(Entity en : entities) {
			if(en.getType().name().toUpperCase().equals("ARMOR_STAND")) {
				stand = (ArmorStand) en;
			}
		}
				
		if(stand == null) {
			stand = (ArmorStand) origin.getWorld().spawnEntity(origin.clone().add(0.5, 2, 0.5), EntityType.ARMOR_STAND);
			stand.setPersistent(true);
			stand.setGravity(false);
			stand.setInvulnerable(true);
			stand.setCustomNameVisible(true);
			stand.setVisible(false);
			stand.setMarker(true);
		}
		
		// Add flag block
		if (strength > 0 && controller != null) {
			int step = (int) (strength / (getMaxStrength() / 4.0));
			
			switch(step) {
				case 4:
					stand.setCustomName(controller.getColor()+ "100% ||||||||||||||||||||||||||||||||||||||||");
					break;
				case 3:
					stand.setCustomName(controller.getColor()+ "80% ||||||||||||||||||||||||||||||||" + ChatColor.WHITE + ("||||||||"));
					break;
				case 2:
					stand.setCustomName(controller.getColor()+ "60% ||||||||||||||||||||||||" + ChatColor.WHITE + ("||||||||||||||||"));
					break;
				case 1:
					stand.setCustomName(controller.getColor()+ "40% ||||||||||||||||" + ChatColor.WHITE + ("||||||||||||||||||||||||"));
					break;
				case 0:
					stand.setCustomName(controller.getColor()+ "20% ||||||||" + ChatColor.WHITE + ("||||||||||||||||||||||||||||||||"));
					break;
				default:
					break;
			}
		} else {
			stand.setCustomName(ChatColor.stripColor("0% ||||||||||||||||||||||||||||||||||||||||"));
		}
	}
	
	public void removeArmorstand() {
		Location origin = this.getOrigin();
		Collection<Entity> entities = origin.getWorld().getNearbyEntities(origin, 1, 3, 1);
		for(Entity en : entities) {
			if(en.getType().name().toUpperCase().equals("ARMOR_STAND")) {
				en.remove();
			}
		}
	}

	public String getName() {
		return name;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location, Warzone zone) {
		this.location = new Location(location.getWorld(), location.getBlockX(), location.getBlockY(),
				location.getBlockZ(), location.getYaw(), 0);
		int radius = zone.getWarzoneConfig().getInt(WarzoneConfig.CPRADIUS);
		this.volume.setCornerOne(this.getOrigin().subtract(radius,1,radius));
		this.volume.setCornerTwo(this.getOrigin().add(radius,3,radius));
		this.volume.saveBlocks();
		this.updateBlocks();
	}
	
	public void setRadius(int radius) {
		this.volume.setCornerOne(this.getOrigin().subtract(radius,1,radius));
		this.volume.setCornerTwo(this.getOrigin().add(radius,3,radius));
	}

	public TeamKind getDefaultController() {
		return defaultController;
	}

	public TeamKind getController() {
		return controller;
	}

	public void setController(TeamKind controller) {
		this.controller = controller;
		if (strength > 0) {
			this.updateBlocks();
		}
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		Validate.isTrue(strength <= getMaxStrength());
		this.strength = strength;
		this.updateBlocks();
	}

	public int getControlTime() {
		return controlTime;
	}

	public void setControlTime(int controlTime) {
		this.controlTime = controlTime;
	}

	public Volume getVolume() {
		return volume;
	}

	public void setVolume(Volume volume) {
		this.volume = volume;
	}

	public void reset(Warzone zone) {
		this.controller = defaultController;
		if (this.controller != null) {
			this.strength = 4;
		} else {
			this.strength = 0;
		}
		this.setLocation(this.getOrigin(),zone);
		this.updateBlocks();
	}

	private long lastMessage = 0;
	public boolean antiChatSpam() {
		long now = System.currentTimeMillis();
		if (now - lastMessage > 3000) {
			lastMessage = now;
			return true;
		}
		return false;
	}

	public int getMaxStrength() {
		return warzone.getWarzoneConfig().getInt(WarzoneConfig.CAPTUREPOINTTIME);
	}
}
