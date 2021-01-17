package com.tommytony.war.utility;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;

import com.tommytony.war.War;

public class Weapon implements Comparable<Weapon> {
	private String name;
	private double rate;
	private float power;
	
	private float spread;
	private float sneSpread;
	private float sprSpread;
	private float jmpSpread;
	
	private boolean rapid;
	private int knockback;
	private boolean scope;
	private double damage;
	private int projectileCount;
	private int pierce;
	private Sound sound;
		
	public Weapon(String name, double rate, float power, float spread, float sneSpread, float sprSpread, float jmpSpread,
			boolean rapid, int knockback,
			boolean scope, double damage, int projectileCount, int pierce, String sound) {
		this.name = name;
		this.rate = rate;
		this.power = power;
		
		this.spread = spread;
		this.sneSpread = sneSpread;
		this.sprSpread = sprSpread;	
		this.jmpSpread = jmpSpread;
		
		this.rapid = rapid;
		this.knockback = knockback;
		this.scope = scope;
		this.damage = damage;
		this.projectileCount = projectileCount;
		this.pierce = pierce;
		this.sound = Sound.valueOf(sound);
	}
	
	static {
		ConfigurationSerialization.registerClass(Loadout.class);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getRate() {
		return rate;
	}
	
	public float getPower() {
		return power;
	}
	
	public int getKnockback() {
		return knockback;
	}
	
	public float getSpread(Player player) {
		Float spr = spread;
		
		boolean inAir = false;
		
		if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
			inAir = true;
		}
		
		if(player.isSprinting()) {
			spr = sprSpread;
		} else if(player.isSneaking()) {
			spr = sneSpread;
		}
		
		if(inAir) {
			spr = spr + jmpSpread;
		}
	
		return spr;
	}
	
	public boolean getRapid() {
		return rapid;
	}
	
	public boolean getScope() {
		return scope;
	}
	
	public double getDamage() {
		return damage;
	}
	
	public int getProjectileCount() {
		return projectileCount;
	}
	
	public int getPierce() {
		return pierce;
	}
	
	public Sound getSound() {
		return sound;
	}
	
	public static Weapon getWeaponByString(String wpnName) {
		for(Weapon wp : War.war.getWeapons()) {
			if(wpnName.toLowerCase().equals(wp.getName().toLowerCase())) {
				return wp;
			}
		}
		return null;
	}

	@Override
	public int compareTo(Weapon wpn) {
		if ("default".equals(wpn.getName()) && !"default".equals(this.getName())) {
			return -1;
		} else if ("default".equals(this.getName()) && !"default".equals(wpn.getName())) {
			return 1;
		} else {
			return 0;
		}
	}
}
