package com.tommytony.war.utility;

import org.bukkit.configuration.serialization.ConfigurationSerialization;

import com.tommytony.war.War;

public class Weapon implements Comparable<Weapon> {
	private String name;
	private double rate;
	private float power;
	private float spread;
	private boolean rapid;
	private double damage;
	private int projectileCount;
		
	public Weapon(String name, double rate, float power, float spread, boolean rapid, double damage, int projectileCount) {
		this.name = name;
		this.rate = rate;
		this.power = power;
		this.spread = spread;
		this.rapid = rapid;
		this.damage = damage;
		this.projectileCount = projectileCount;
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
	
	public double getDamage() {
		return damage;
	}
	
	public int getProjectileCount() {
		return projectileCount;
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
