package com.tommytony.war.utility;

import org.bukkit.configuration.serialization.ConfigurationSerialization;

public class Weapon implements Comparable<Weapon> {
	private String name;
	private double rate;
	private float power;
	private float spread;
	private boolean rapid;
		
	public Weapon(String name, double rate, float power, float spread, boolean rapid) {
		this.name = name;
		this.rate = rate;
		this.power = power;
		this.spread = spread;
		this.rapid = rapid;
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
