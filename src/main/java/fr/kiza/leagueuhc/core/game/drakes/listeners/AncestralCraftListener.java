package fr.kiza.leagueuhc.core.game.drakes.listeners;

import fr.kiza.leagueuhc.core.api.drake.Drake;
import fr.kiza.leagueuhc.core.api.drake.DrakeRegistry;
import fr.kiza.leagueuhc.core.game.drakes.AncestralDrake;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Gère le craft du Drake Ancestral.
 * Nécessite tous les 6 drakes dans la table de craft.
 */
public class AncestralCraftListener implements Listener {

    private final JavaPlugin plugin;

    public AncestralCraftListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCraftClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (event.getInventory().getType() != InventoryType.WORKBENCH) return;

        Player player = (Player) event.getWhoClicked();
        CraftingInventory craftInv = (CraftingInventory) event.getInventory();

        Bukkit.getScheduler().runTaskLater(plugin, () -> checkAncestralCraft(craftInv, player), 1L);
    }

    private void checkAncestralCraft(CraftingInventory craftInv, Player player) {
        ItemStack[] matrix = craftInv.getMatrix();
        if (matrix == null || matrix.length < 9) return;

        Set<String> foundDrakeIds = new HashSet<>();
        int drakeCount = 0;

        for (ItemStack item : matrix) {
            if (item == null || item.getType() == Material.AIR) continue;

            if (Drake.isDrakeItem(item)) {
                String drakeId = Drake.getDrakeIdFromItem(item);
                if (drakeId != null) {
                    foundDrakeIds.add(drakeId);
                    drakeCount++;
                }
            }
        }

        int requiredCount = DrakeRegistry.getSpawnableDrakes().size();

        if (drakeCount == requiredCount && foundDrakeIds.size() == requiredCount) {
            boolean allValid = true;
            for (String id : foundDrakeIds) {
                Drake drake = DrakeRegistry.getDrake(id);
                if (drake == null || drake instanceof AncestralDrake) {
                    allValid = false;
                    break;
                }
            }

            if (allValid) {
                AncestralDrake ancestral = DrakeRegistry.getDrakeByClass(AncestralDrake.class);
                if (ancestral != null) {
                    craftInv.setResult(ancestral.createItem());
                }
            }
        }
    }

    @EventHandler
    public void onCraftResult(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        ItemStack result = event.getCurrentItem();
        if (result == null || result.getType() != Material.DRAGON_EGG) return;

        String drakeId = Drake.getDrakeIdFromItem(result);
        if (drakeId == null) return;

        Drake drake = DrakeRegistry.getDrake(drakeId);
        if (!(drake instanceof AncestralDrake)) return;

        Player player = (Player) event.getWhoClicked();

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(ChatColor.GOLD + "★★★ " + ChatColor.WHITE + player.getName() + 
                ChatColor.GOLD + " a crafté le " + ChatColor.YELLOW + "Drake Ancestral" + ChatColor.GOLD + " ! ★★★");
        Bukkit.broadcastMessage(ChatColor.GRAY + "La fin du monde approche...");
        Bukkit.broadcastMessage("");

        CraftingInventory craftInv = (CraftingInventory) event.getInventory();
        ItemStack[] matrix = craftInv.getMatrix();
        Arrays.fill(matrix, null);
        craftInv.setMatrix(matrix);
    }
}
