package com.tommytony.war.event;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import com.tommytony.war.War;
import com.tommytony.war.Warzone;
import com.tommytony.war.config.WarzoneConfig;
import com.tommytony.war.utility.InteractiveBlocks;
import com.tommytony.war.utility.RunnableSeries;

public class SwordBlockingListener implements Listener {
	private static final ItemStack SHIELD = new ItemStack(Material.SHIELD);
    private final Map<UUID, ItemStack> storedOffhandItems = new HashMap<>();
    private final Map<UUID, RunnableSeries> correspondingTasks = new HashMap<>();
    private int restoreDelay;

    public SwordBlockingListener(){
    	restoreDelay = 40;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRightClick(PlayerInteractEvent e){
    	Warzone zone = Warzone.getZoneByLocation(e.getPlayer()); 					//Check if player is in Warzone...
		if(zone == null) {
			return;
		}
		if(!zone.getWarzoneConfig().getBoolean(WarzoneConfig.SWORDBLOCKING)) {		//...that has SwordBlocking enabled
			return;
		}
		
        if(e.getItem() == null) return;

        Action action = e.getAction();

        if(action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;
        if(e.getHand() == EquipmentSlot.OFF_HAND) return;

        final Block block = e.getClickedBlock();
        if(action == Action.RIGHT_CLICK_BLOCK && block != null &&
        		InteractiveBlocks.getList().contains(block.getType()))
            return;

        Player p = e.getPlayer();

        UUID id = p.getUniqueId();

        if(!p.isBlocking()){
            ItemStack item = e.getItem();

            if(!isHoldingSword(item.getType()) || hasShield(p)) return;

            PlayerInventory inv = p.getInventory();

            storedOffhandItems.put(id, inv.getItemInOffHand());

            inv.setItemInOffHand(SHIELD);
        }

        scheduleRestore(p);
    }

    @EventHandler
    public void onHotBarChange(PlayerItemHeldEvent e){
        restore(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldChange(PlayerChangedWorldEvent e){
        restore(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogout(PlayerQuitEvent e){
        restore(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent e){
        if(!isBlocking(e.getEntity().getUniqueId())) return;

        Player p = e.getEntity();
        UUID id = p.getUniqueId();

        e.getDrops().replaceAll(item -> {

            if(item.getType().equals(Material.SHIELD))
                item = storedOffhandItems.remove(id);

            return item;
        });

        // Handle keepInventory = true
        restore(p);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent e){
        Player p = e.getPlayer();
        if(isBlocking(p.getUniqueId()))
            e.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemSwitch(PlayerItemHeldEvent e) {
    	Player p = e.getPlayer();
    	if(hasShield(p)) {
        	UUID id = p.getUniqueId();
        	p.getInventory().setItemInOffHand(storedOffhandItems.get(id));
            storedOffhandItems.remove(id);
    	}
	}

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e){

        if(e.getWhoClicked() instanceof Player){
            Player p = (Player) e.getWhoClicked();

            if(isBlocking(p.getUniqueId())){
                ItemStack cursor = e.getCursor();
                ItemStack current = e.getCurrentItem();
                if(cursor != null && cursor.getType() == Material.SHIELD ||
                        current != null && current.getType() == Material.SHIELD){
                    e.setCancelled(true);
                    restore(p);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemDrop(PlayerDropItemEvent e){
        Item is = e.getItemDrop();

        Player p = e.getPlayer();

        if(isBlocking(p.getUniqueId()) && is.getItemStack().getType() == Material.SHIELD){
            e.setCancelled(true);
            restore(p);
        } else if (isBlocking(p.getUniqueId()) && is.getItemStack().getType().name().contains("SWORD")) {
            UUID id = p.getUniqueId();
        	p.getInventory().setItemInOffHand(storedOffhandItems.get(id));
            storedOffhandItems.remove(id);
        }
    }

    private void restore(Player p){
        UUID id = p.getUniqueId();

        tryCancelTask(id);

        if(!isBlocking(id)) return;

        if(p.isBlocking()) 					//They are still blocking with the shield so postpone restoring
            scheduleRestore(p);
        else {
            p.getInventory().setItemInOffHand(storedOffhandItems.get(id));
            storedOffhandItems.remove(id);
        }
    }

    private void tryCancelTask(UUID id){
        Optional.ofNullable(correspondingTasks.remove(id))
                .ifPresent(RunnableSeries::cancelAll);
    }

    private void scheduleRestore(Player p){
        UUID id = p.getUniqueId();
        tryCancelTask(id);

        BukkitRunnable removeItem = new BukkitRunnable() {
            @Override
            public void run(){
                restore(p);
            }
        };
        removeItem.runTaskLater(War.war, restoreDelay);

        BukkitRunnable checkBlocking = new BukkitRunnable() {
            @Override
            public void run(){
                if(!p.isBlocking()) {
                    restore(p);
                }
            }
        };
        checkBlocking.runTaskTimer(War.war, 10L, 2L);

        correspondingTasks.put(p.getUniqueId(), new RunnableSeries(removeItem, checkBlocking));
    }

    private boolean isBlocking(UUID uuid){
        return storedOffhandItems.containsKey(uuid);
    }

    private boolean hasShield(Player p){
        return p.getInventory().getItemInOffHand().getType() == Material.SHIELD;
    }

    private boolean isHoldingSword(Material mat){
        return mat.toString().endsWith("_SWORD");
    }
}
