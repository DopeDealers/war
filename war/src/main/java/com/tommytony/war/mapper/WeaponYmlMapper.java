package com.tommytony.war.mapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.tommytony.war.War;
import com.tommytony.war.utility.Weapon;

public class WeaponYmlMapper {
	public static void load() {
		File wpnYmlFile = new File(War.war.getDataFolder().getPath() + "/weapons.yml");
		
		if(!wpnYmlFile.exists()) {
			WeaponYmlMapper.save();
			War.war.log("weapon.yml settings file created.", Level.INFO);
		}
		
		YamlConfiguration wpnYmlConfig = YamlConfiguration.loadConfiguration(wpnYmlFile);
		ConfigurationSection wpnRootSection = wpnYmlConfig.getConfigurationSection("weapons");
	
		War.war.getWeaponManager().setWeapons(WeaponYmlMapper.fromConfigToWeapons(wpnRootSection, new HashMap<String, Double>()));
	}
	
	public static List<Weapon> fromConfigToWeapons(ConfigurationSection config, HashMap<String, Double> weapons) {
		List<String> weaponNames = config.getStringList("names");
		weapons.clear();
		List<Weapon> wpns = new ArrayList<Weapon>();
		for (String name : weaponNames) {
			double rate = config.getDouble(name + ".rate");	
			float power = (float) config.getDouble(name + ".power");
			float spread = (float) config.getDouble(name + ".spread");
			boolean rapid = config.getBoolean(name + ".rapid");
			boolean scope = config.getBoolean(name + ".scope");
			double damage = config.getDouble(name + ".damage");
			int projectileCount = config.getInt(name + ".ProjectileCount");
			int pierce = config.getInt(name + ".PierceLevel");
			String sound = config.getString(name + ".sound");
			Weapon wpn = fromConfigToWeapon(name, rate, power, spread, rapid, scope, damage, projectileCount, pierce, sound);
			wpns.add(wpn);
			weapons.put(name, rate);
		}
		Collections.sort(wpns);
		return wpns;
	}
	
	public static Weapon fromConfigToWeapon(String weaponName, double rate, float power, float spread , boolean rapid, boolean scope, double damage, int projectileCount, int pierce, String sound) {
		return new Weapon(weaponName, rate, power, spread, rapid, scope, damage, projectileCount, pierce, sound);
	}
	
	public static void save() {
		YamlConfiguration wpnYmlConfig = new YamlConfiguration();
		ConfigurationSection wpnRootSection = wpnYmlConfig.createSection("weapons");
		(new File(War.war.getDataFolder().getPath())).mkdir();
		
		// defaultWeapons
		List<String> wpnList = new ArrayList<String>();
		wpnList.add("P90");
		wpnList.add("AWP");
		wpnList.add("Nova");
		wpnRootSection.set("names", wpnList);
		
		ConfigurationSection p90ConfigSection = wpnRootSection.createSection("P90");
		ConfigurationSection awpConfigSection = wpnRootSection.createSection("AWP");
		ConfigurationSection novaConfigSection = wpnRootSection.createSection("Nova");
		
		p90ConfigSection.set("rate", 1);
		p90ConfigSection.set("power", 10);
		p90ConfigSection.set("spread", 2);
		p90ConfigSection.set("damage", 3);
		p90ConfigSection.set("PierceLevel", 0);
		p90ConfigSection.set("ProjectileCount", 1);
		p90ConfigSection.set("rapid", true);
		p90ConfigSection.set("scope", false);
		p90ConfigSection.set("sound", "ENTITY_BLAZE_SHOOT");
		
		awpConfigSection.set("rate", 20);
		awpConfigSection.set("power", 20);
		awpConfigSection.set("spread", 0.2);
		awpConfigSection.set("damage", 20);
		awpConfigSection.set("PierceLevel", 1);
		awpConfigSection.set("ProjectileCount", 1);
		awpConfigSection.set("rapid", false);
		awpConfigSection.set("scope", true);
		awpConfigSection.set("sound", "ENTITY_BLAZE_SHOOT");
		
		novaConfigSection.set("rate", 5);
		novaConfigSection.set("power", 7);
		novaConfigSection.set("spread", 6);
		novaConfigSection.set("damage", 7);
		novaConfigSection.set("PierceLevel", 0);
		novaConfigSection.set("ProjectileCount", 7);
		novaConfigSection.set("rapid", false);
		novaConfigSection.set("scope", false);
		novaConfigSection.set("sound", "ENTITY_BLAZE_SHOOT");
		
		// Save to disk
		File wpnConfigFile = new File(War.war.getDataFolder().getPath() + "/weapons.yml");
		try {
			wpnYmlConfig.save(wpnConfigFile);
		} catch (IOException e) {
			War.war.log("Failed to save weapon.yml", Level.WARNING);
			e.printStackTrace();
		}
	}
}
