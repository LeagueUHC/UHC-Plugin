package fr.kiza.leagueuhc.core.game.drakes.listeners;

import fr.kiza.leagueuhc.core.api.drake.Drake;
import fr.kiza.leagueuhc.core.api.drake.PlayerDrakeData;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Gère les passifs offensifs et défensifs des drakes.
 */
public class DrakePassiveListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player attacker = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();

        PlayerDrakeData attackerData = PlayerDrakeData.get(attacker);

        for (Drake drake : attackerData.getOwnedDrakes()) {
            drake.onAttack(event, attacker, victim);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDefend(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player victim = (Player) event.getEntity();
        PlayerDrakeData victimData = PlayerDrakeData.get(victim);

        Player attacker = null;
        if (event.getDamager() instanceof Player) {
            attacker = (Player) event.getDamager();
        }

        for (Drake drake : victimData.getOwnedDrakes()) {
            drake.onDefend(event, victim, attacker);
        }
    }
}
