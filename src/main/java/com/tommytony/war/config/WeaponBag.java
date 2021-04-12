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
	
	public void addWeapon(String name, Double rate, float power, float spread, float sneSpread, float sprSpread, float jmpSpread, float scopeSpread,
			boolean rapid, int knockback, boolean scope, double damage, double HSDamage, int projectileCount, int pierce, String sound, String bullet) {
		this.weapons.add(new Weapon(name, rate, power, spread, sneSpread, sprSpread, jmpSpread, scopeSpread,
				rapid, knockback, scope, damage, HSDamage, projectileCount, pierce, sound, bullet));
	}

	public void addWeapon(Weapon weapon) {
		this.weapons.add(weapon);
	}
	
	public void setWeapons(List<Weapon> weapons) {
		this.weapons = weapons;
	}
}
