package fr.kiza.leagueuhc.core.game.drakes.listeners;

import fr.kiza.leagueuhc.core.api.drake.Drake;
import fr.kiza.leagueuhc.core.api.drake.DrakeRegistry;
import fr.kiza.leagueuhc.core.api.drake.PlayerDrakeData;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Gère l'activation des items drakes (clic droit).
 */
public class DrakeActivateListener implements Listener {

    @EventHandler
    public void onDrakeActivate(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();

        if (!Drake.isDrakeItem(item)) return;

        event.setCancelled(true);

        String drakeId = Drake.getDrakeIdFromItem(item);
        if (drakeId == null) {
            player.sendMessage(ChatColor.RED + "Item drake invalide !");
            return;
        }

        Drake drake = DrakeRegistry.getDrake(drakeId);
        if (drake == null) {
            player.sendMessage(ChatColor.RED + "Drake non trouvé : " + drakeId);
            return;
        }

        PlayerDrakeData data = PlayerDrakeData.get(player);

        if (data.hasDrake(drake)) {
            if (drake.hasActivePower()) {
                drake.usePower(player);
            } else {
                player.sendMessage(ChatColor.RED + "Tu as déjà activé le passif du " + drake.getDisplayName() + ChatColor.RED + " !");
            }
            return;
        }

        // Activer le passif
        data.addDrake(drake);

        player.sendMessage("");
        player.sendMessage(ChatColor.GREEN + "✓ Passif du " + drake.getDisplayName() + ChatColor.GREEN + " activé !");

        // Garder l'item si c'est un pouvoir actif
        if (drake.hasActivePower()) {
            player.sendMessage(ChatColor.GRAY + "Tu peux réutiliser l'item pour activer le pouvoir.");
        }

        player.sendMessage("");

        // Vérifier si le joueur a tous les drakes spawnables
        if (data.hasAllSpawnableDrakes()) {
            player.sendMessage(ChatColor.GOLD + "★ Tu possèdes tous les drakes !");
            player.sendMessage(ChatColor.GRAY + "Tu peux maintenant craft le Drake Ancestral !");
        }
    }
}
