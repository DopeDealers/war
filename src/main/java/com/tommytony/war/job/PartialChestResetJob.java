package com.tommytony.war.job;

import com.tommytony.war.War;
import com.tommytony.war.Warzone;
import com.tommytony.war.mapper.ZoneVolumeMapper;
import com.tommytony.war.structure.ZoneLobby;
import com.tommytony.war.volume.ZoneVolume;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class PartialChestResetJob extends BukkitRunnable implements Cloneable {
	
	// Watch out, this command sender's map is shared between all concurrent reset jobs on different warzones
	// - gotta make sure to notify the correct player that sent the command
	private static Map<Warzone, CommandSender> sendersToNotify = Collections.synchronizedMap(new HashMap<Warzone, CommandSender>());
	
	private final Warzone zone;
	private final ZoneVolume volume;
	private final int speed;
	private final int total;
	private int completed = 0;
	private final long startTime = System.currentTimeMillis();
	private long messageCounter = System.currentTimeMillis();
	boolean[][][] changes;
	public static final long MESSAGE_INTERVAL = 7500;
	// Ticks between job runs
	public static final int JOB_INTERVAL = 1;
	private int totalChanges = 0;
	private NumberFormat formatter = new DecimalFormat("#0.00");
	private Connection conn;

	/**
	 * Reset a warzone's blocks at a certain speed.
	 * 
	 * @param zone
	 *            Warzone to reset.
	 * @param speed
	 *            Blocks to modify per #INTERVAL.
	 */
	public PartialChestResetJob(Warzone zone, int speed) throws SQLException {
		this.zone = zone;
		this.volume = zone.getVolume();
		this.speed = speed;
		this.total = volume.getTotalSavedBlocks();
		this.changes = new boolean[volume.getSizeX()][volume.getSizeY()][volume.getSizeZ()];
	}

	@Override
	public void run() {
		try {
			if (conn == null || conn.isClosed()) {
				conn = ZoneVolumeMapper.getZoneConnection(volume, zone.getName());
			}
			if (completed >= total) {
				int airChanges = 0;
				int minX = volume.getMinX(), minY = volume.getMinY(), minZ = volume.getMinZ();
				air: for (int x = volume.getMinX(); x <= volume.getMaxX(); x++) {
					for (int y = volume.getMinY(); y <= volume.getMaxY(); y++) {
						for (int z = volume.getMinZ(); z <= volume.getMaxZ(); z++) {
							int xi = x - minX, yi = y - minY, zi = z - minZ;
							if (!changes[xi][yi][zi]) {
								changes[xi][yi][zi] = true;
								airChanges++;
								BlockState state = volume.getWorld().getBlockAt(x, y, z).getState();
								if (state.getType() != Material.AIR) {
									state.setType(Material.AIR);
									state.update(true, false);
								}
								if (airChanges >= speed) {
									this.displayStatusMessage();
									break air;
								}
							}
						}
					}
				}
				totalChanges += airChanges;
				if (this.doneAir()) {
					volume.resetEntities(conn);
					String secondsAsText = formatter.format(((double)(System.currentTimeMillis() - startTime)) / 1000);
					String message = MessageFormat.format(
							War.war.getString("zone.battle.resetcomplete"), secondsAsText);
					this.sendMessageToAllWarzonePlayers(message);
					PartialChestResetJob.setSenderToNotify(zone, null);	// stop notifying for this zone
					zone.initializeZone();
					War.war.getLogger().log(Level.INFO, "Finished reset cycle for warzone {0} (took {1} seconds)",
							new Object[]{volume.getName(), secondsAsText});
					conn.close();
				} else {
					War.war.getServer().getScheduler().runTaskLater(War.war, (Runnable) this.clone(), JOB_INTERVAL);
				}
			} else {
				int solidChanges = volume.resetSectionChests(conn, completed, speed, changes);
				completed += solidChanges;
				totalChanges += solidChanges;
				this.displayStatusMessage();
				War.war.getServer().getScheduler().runTaskLater(War.war, (Runnable) this.clone(), JOB_INTERVAL);
			}
		} catch (SQLException e) {
			War.war.getLogger().log(Level.WARNING, "Failed to load zone during reset loop", e);
		}
	}

	private void sendMessageToAllWarzonePlayers(String message) {
		for (Player player : War.war.getServer().getOnlinePlayers()) {
			ZoneLobby lobby = ZoneLobby.getLobbyByLocation(player);
			if (player != PartialChestResetJob.sendersToNotify.get(zone)
					&& (zone.getPlayers().contains(player)
						|| (lobby != null && lobby.getZone() == zone))) {
				War.war.msg(player, message);
			}
		}
		
		if (PartialChestResetJob.sendersToNotify.get(zone) != null) {
			War.war.msg(PartialChestResetJob.sendersToNotify.get(zone), message);
		}
	}

	private void displayStatusMessage() {
		if (System.currentTimeMillis() - messageCounter > MESSAGE_INTERVAL) {
			String secondsAsText = formatter.format(((double)(System.currentTimeMillis() - startTime)) / 1000);
			messageCounter = System.currentTimeMillis();
			int percent = (int) (((double) totalChanges / (double) volume.size()) * 100);
			String message = MessageFormat.format(
					War.war.getString("zone.battle.resetprogress"),
					percent, secondsAsText);
			this.sendMessageToAllWarzonePlayers(message);
		}
	}

	@Override
	protected PartialChestResetJob clone() {
		try {
			return (PartialChestResetJob) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	public static void setSenderToNotify(Warzone warzone, CommandSender sender) {
		PartialChestResetJob.sendersToNotify.put(warzone, sender);
	}

	private boolean doneAir() {
		int minX = volume.getMinX(), minY = volume.getMinY(), minZ = volume.getMinZ();
		for (int x = volume.getMinX(); x <= volume.getMaxX(); x++) {
			for (int y = volume.getMinY(); y <= volume.getMaxY(); y++) {
				for (int z = volume.getMinZ(); z <= volume.getMaxZ(); z++) {
					int xi = x - minX, yi = y - minY, zi = z - minZ;
					 if (!changes[xi][yi][zi]) {
						 return false;
					 }
				}
			}
		}
		return true;
	}
}
