package com.tommytony.war.event;

import com.tommytony.war.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class WarScoreEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Team team;
    private Player player;
    private Integer score;

    public WarScoreEvent(Team team, Player player, Integer score) {
        this.team = team;
        this.player = player;
        this.score = score;
    }

    public Team getTeam() {
        return team;
    }

    public Integer getScore() {
        return score;
    }

    public Player getPlayer() {
        return player;
    }
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
