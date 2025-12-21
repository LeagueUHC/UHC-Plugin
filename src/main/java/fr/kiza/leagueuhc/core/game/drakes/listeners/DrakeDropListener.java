package fr.kiza.leagueuhc.core.game.drakes.listeners;

import fr.kiza.leagueuhc.core.api.drake.Drake;
import fr.kiza.leagueuhc.core.api.drake.DrakeRegistry;
import fr.kiza.leagueuhc.core.api.drake.PlayerDrakeData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class DrakeDropListener implements Listener {

    @EventHandler
    public void onDrakeDrop(final PlayerDropItemEvent event) {
        final ItemStack item = event.getItemDrop().getItemStack();

        if (!Drake.isDrakeItem(item)) return;

        final String drakeId = Drake.getDrakeIdFromItem(item);
        if (drakeId == null) return;

        final Drake drake = DrakeRegistry.getDrake(drakeId);
        if (drake == null) return;

        final Player player = event.getPlayer();
        final PlayerDrakeData data = PlayerDrakeData.get(player);

        if (data.hasDrake(drake)) {
            data.removeDrake(drake);

            player.sendMessage("");
            player.sendMessage(ChatColor.RED + "✖ Tu as drop le " + drake.getDisplayName() + ChatColor.RED + " !");
            player.sendMessage(ChatColor.GRAY + "Le passif a été retiré.");
            player.sendMessage("");
        }
    }
}
