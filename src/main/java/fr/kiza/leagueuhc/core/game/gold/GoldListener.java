package fr.kiza.leagueuhc.core.game.gold;

import fr.kiza.leagueuhc.LeagueUHC;
import fr.kiza.leagueuhc.config.GameConfig;
import fr.kiza.leagueuhc.core.game.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GoldListener implements Listener {

    private final LeagueUHC instance;
    private final GoldManager goldManager;

    private final Map<UUID, Map<UUID, Long>> damageTracker = new HashMap<>();

    public GoldListener(LeagueUHC instance) {
        this.instance = instance;
        this.goldManager = this.instance.getGameEngine().getGameHelper().getManager().getGoldManager();

        Bukkit.getPluginManager().registerEvents(this, this.instance);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        GamePlayer gVictim = GamePlayer.get(victim);
        if (gVictim == null) return;

        if (killer != null && killer != victim) {
            goldManager.onPlayerKill(killer, victim);
            processAssists(victim, killer);
        }

        damageTracker.remove(victim.getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player victim = (Player) event.getEntity();
        Player damager = getDamager(event);

        if (damager == null || damager.equals(victim)) return;

        GamePlayer gVictim = GamePlayer.get(victim);
        GamePlayer gDamager = GamePlayer.get(damager);

        if (gVictim == null || gDamager == null) return;

        double damage = event.getFinalDamage();

        gDamager.addDamageDealt(damage);
        gVictim.addDamageTaken(damage);

        this.trackDamage(victim, damager);
    }

    public void trackDamage(Player victim, Player damager) {
        if (victim.equals(damager)) return;

        damageTracker
                .computeIfAbsent(victim.getUniqueId(), k -> new HashMap<>())
                .put(damager.getUniqueId(), System.currentTimeMillis());
    }

    private void processAssists(Player victim, Player killer) {
        Map<UUID, Long> damageSources = damageTracker.get(victim.getUniqueId());
        if (damageSources == null) return;

        long now = System.currentTimeMillis();

        for (Map.Entry<UUID, Long> entry : damageSources.entrySet()) {
            UUID damagerUuid = entry.getKey();
            long damageTime = entry.getValue();

            if (damagerUuid.equals(killer.getUniqueId())) continue;

            if (now - damageTime <= GameConfig.ASSIST_TIME_WINDOW) {
                Player assister = instance.getServer().getPlayer(damagerUuid);
                if (assister != null && assister.isOnline()) {
                    goldManager.onPlayerAssist(assister);
                }
            }
        }
    }

    private Player getDamager(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            return (Player) event.getDamager();
        }

        if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (projectile.getShooter() instanceof Player) {
                return (Player) projectile.getShooter();
            }
        }

        return null;
    }

    public void reset() {
        damageTracker.clear();
    }
}