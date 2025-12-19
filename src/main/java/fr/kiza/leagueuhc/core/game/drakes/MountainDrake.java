package fr.kiza.leagueuhc.core.game.drakes;

import fr.kiza.leagueuhc.core.api.drake.Drake;
import fr.kiza.leagueuhc.core.api.drake.annotation.DrakeEntry;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

@DrakeEntry(priority = 1)
public class MountainDrake extends Drake {

    @Override
    public String getName() {
        return "Drake des montagnes";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.GRAY;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.IRON_GOLEM;
    }

    @Override
    public int getHearts() {
        return 150;
    }

    @Override
    public String getPassiveDescription() {
        return "+2 cœurs permanents";
    }

    @Override
    public void applyPassive(Player player) {
        double currentMax = player.getMaxHealth();
        player.setMaxHealth(currentMax + 4);
        
        player.sendMessage(getColor() + "» " + ChatColor.GREEN + "+2 cœurs permanents !");
    }
}
