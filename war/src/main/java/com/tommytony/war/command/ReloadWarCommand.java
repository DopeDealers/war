package com.tommytony.war.command;

import org.bukkit.command.CommandSender;

import com.tommytony.war.War;

/**
 * Unloads war.
 *
 * @author Jakl
 */

public class ReloadWarCommand extends AbstractWarAdminCommand {
	public ReloadWarCommand(WarCommandHandler handler, CommandSender sender, String[] args) throws NotWarAdminException {
		super(handler, sender, args);
	}

	@Override
	public boolean handle() {
		if (this.args.length != 0) {
			return false;
		}

		War.war.unloadWar();
		War.war.loadWar();
		this.msg("War reloaded.");
		return true;
	}
}
