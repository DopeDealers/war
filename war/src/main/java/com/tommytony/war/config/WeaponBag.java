package com.tommytony.war.config;

import java.util.ArrayList;
import java.util.List;

import com.tommytony.war.utility.Weapon;

public class WeaponBag {
	private List<Weapon> weapons = new ArrayList<Weapon>();
	
	public WeaponBag() {
	}
	
	public boolean hasWeapons() {
		return weapons.size() > 0;
	}
	
	public List<Weapon> getWeapons() {
		return this.weapons;
	}
	
	public void addWeapon(String name, Double rate, float power, float spread, boolean rapid, boolean scope, double damage, int projectileCount, int pierce, String sound) {
		this.weapons.add(new Weapon(name, rate, power, spread, rapid, scope, damage, projectileCount, pierce, sound));
	}

	public void addWeapon(Weapon weapon) {
		this.weapons.add(weapon);
	}
	
	public void setWeapons(List<Weapon> weapons) {
		this.weapons = weapons;
	}
}
