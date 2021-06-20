package com.tommytony.war.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.tommytony.war.War;

public class WarVersionCommand extends AbstractWarCommand {
	public WarVersionCommand(WarCommandHandler handler, CommandSender sender, String[] args) {
		super(handler, sender, args);
	}

	@Override
	public boolean handle() {
		if (this.args.length != 0) {
			return false;
		}
		StringBuilder warzonesMessage = new StringBuilder(War.war.getString("war.title"));
		warzonesMessage.append(" "+War.war.getDescription().getVersion());
		this.msg(warzonesMessage.toString());
		
		return true;
	}
}
