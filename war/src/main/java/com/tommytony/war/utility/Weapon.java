package com.tommytony.war.utility;

import org.bukkit.Sound;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import com.tommytony.war.War;

public class Weapon implements Comparable<Weapon> {
	private String name;
	private double rate;
	private float power;
	private float spread;
	private boolean rapid;
	private boolean scope;
	private double damage;
	private int projectileCount;
	private int pierce;
	private Sound sound;
		
	public Weapon(String name, double rate, float power, float spread, boolean rapid, boolean scope, double damage, int projectileCount, int pierce, String sound) {
		this.name = name;
		this.rate = rate;
		this.power = power;
		this.spread = spread;
		this.rapid = rapid;
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
	
	public float getSpread() {
		return spread;
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
