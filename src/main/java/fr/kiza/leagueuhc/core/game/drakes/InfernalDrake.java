package fr.kiza.leagueuhc.core.game.drakes;

import fr.kiza.leagueuhc.core.api.drake.Drake;
import fr.kiza.leagueuhc.core.api.drake.annotation.DrakeEntry;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

@DrakeEntry(priority = 3)
public class InfernalDrake extends Drake {

    private static final double FIRE_CHANCE = 0.20;
    private final Random random = new Random();

    @Override
    public String getName() {
        return "Drake infernal";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.RED;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.BLAZE;
    }

    @Override
    public int getHearts() {
        return 50;
    }

    @Override
    public String getPassiveDescription() {
        return "20% de chance d'enflammer la cible";
    }

    @Override
    public void applyPassive(Player player) {
        player.sendMessage(getColor() + "» " + ChatColor.GREEN + "20% de chance d'enflammer vos cibles !");
    }

    @Override
    public boolean onAttack(EntityDamageByEntityEvent event, Player attacker, Player victim) {
        if (random.nextDouble() < FIRE_CHANCE) {
            victim.setFireTicks(60);
            attacker.sendMessage(ChatColor.RED + "🔥 " + ChatColor.GRAY + "Tu as enflammé " + victim.getName() + " !");
            return true;
        }
        return false;
    }

    public double getFireChance() {
        return FIRE_CHANCE;
    }
}
