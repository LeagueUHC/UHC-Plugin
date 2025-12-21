package fr.kiza.leagueuhc.core.game.drakes;

import fr.kiza.leagueuhc.core.api.drake.Drake;
import fr.kiza.leagueuhc.core.api.drake.annotation.DrakeEntry;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

@DrakeEntry(priority = 4)
public class HextechDrake extends Drake {

    private static final double LIGHTNING_CHANCE = 0.10;
    private final Random random = new Random();

    @Override
    public String getName() {
        return "Drake hextech";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.LIGHT_PURPLE;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.PIG_ZOMBIE;
    }

    @Override
    public int getHearts() {
        return 50;
    }

    @Override
    public String getPassiveDescription() {
        return "10% de chance d'infliger un éclair (1 cœur)";
    }

    @Override
    public boolean applyPassive(Player player) {
        player.sendMessage(getColor() + "» " + ChatColor.GREEN + "10% de chance d'infliger un éclair !");
        return true;
    }

    @Override
    public void removePassive(Player player) {
        player.sendMessage(getColor() + "» " + ChatColor.RED + "Passif d'éclair retiré");
    }

    @Override
    public boolean onAttack(EntityDamageByEntityEvent event, Player attacker, Player victim) {
        if (random.nextDouble() < LIGHTNING_CHANCE) {
            victim.getWorld().strikeLightningEffect(victim.getLocation());
            victim.damage(2.0, attacker);
            attacker.sendMessage(ChatColor.LIGHT_PURPLE + "⚡ " + ChatColor.GRAY + "Tu as électrocuté " + victim.getName() + " !");
            return true;
        }
        return false;
    }

    public double getLightningChance() {
        return LIGHTNING_CHANCE;
    }
}
