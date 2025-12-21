package fr.kiza.leagueuhc.core.game.drakes.listeners;

import fr.kiza.leagueuhc.core.api.drake.Drake;
import fr.kiza.leagueuhc.core.api.drake.DrakeManager;
import fr.kiza.leagueuhc.core.api.packets.builder.ActionBarBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class DrakeDeathListener implements Listener {

    private final DrakeManager drakeManager;

    public DrakeDeathListener(DrakeManager drakeManager) {
        this.drakeManager = drakeManager;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDrakeDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        if (!drakeManager.isDrake(entity)) return;

        Drake drake = drakeManager.getDrake(entity);
        if (drake == null) return;

        event.getDrops().clear();
        event.setDroppedExp(0);

        Player killer = entity.getKiller();
        if (killer == null && entity.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) entity.getLastDamageCause();
            Entity damager = damageEvent.getDamager();
            if (damager instanceof Player) {
                killer = (Player) damager;
            }
        }

        if (killer != null) {
            killer.getInventory().addItem(drake.createItem());

            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(drake.getColor() + "☠ " + ChatColor.WHITE + killer.getName() + ChatColor.GRAY + " a vaincu le " + drake.getDisplayName() + ChatColor.GRAY + " !");
            Bukkit.broadcastMessage("");

            killer.sendMessage(ChatColor.GREEN + "Tu as obtenu le " + drake.getDisplayName() + ChatColor.GREEN + " !");
            killer.sendMessage(ChatColor.GRAY + "Fais clic droit avec l'item pour activer le passif.");
        } else {
            entity.getWorld().dropItemNaturally(entity.getLocation(), drake.createItem());

            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(drake.getColor() + "☠ " + ChatColor.GRAY + "Le " + drake.getDisplayName() + ChatColor.GRAY + " a été vaincu !");
            Bukkit.broadcastMessage("");
        }

        drakeManager.removeDrake(entity);
    }

    @EventHandler
    public void onDrakeDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        if (!drakeManager.isDrake(event.getEntity())) return;

        LivingEntity drakeEntity = (LivingEntity) event.getEntity();
        Drake drake = drakeManager.getDrake(drakeEntity);
        if (drake == null) return;

        if (event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            double healthAfter = Math.max(0, drakeEntity.getHealth() - event.getFinalDamage());
            int heartsRemaining = (int) Math.ceil(healthAfter / 2);

            new ActionBarBuilder()
                    .message(ChatColor.RED + "❤ " + heartsRemaining)
                    .send(attacker);
        }
    }
}
