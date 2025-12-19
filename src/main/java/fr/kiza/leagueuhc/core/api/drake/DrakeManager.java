package fr.kiza.leagueuhc.core.api.drake;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Gère le spawn automatique et le tracking des drakes en jeu.
 */
public class DrakeManager {

    private final JavaPlugin plugin;
    private final Map<UUID, Drake> activeDrakes = new HashMap<>();
    private final List<Drake> spawnQueue = new ArrayList<>();
    private final Set<String> spawnedDrakeIds = new HashSet<>();

    private boolean started = false;
    private int spawnTaskId = -1;

    // Configuration
    private int spawnStartMinutes = 1;
    private int spawnIntervalMinutes = 1;
    private int minDistance = 175;
    private int maxDistance = 250;

    public DrakeManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // ==================== CONFIGURATION ====================

    public void setSpawnStartMinutes(int minutes) {
        this.spawnStartMinutes = minutes;
    }

    public void setSpawnIntervalMinutes(int minutes) {
        this.spawnIntervalMinutes = minutes;
    }

    public void setSpawnDistance(int min, int max) {
        this.minDistance = min;
        this.maxDistance = max;
    }

    public int getSpawnStartMinutes() {
        return spawnStartMinutes;
    }

    public int getSpawnIntervalMinutes() {
        return spawnIntervalMinutes;
    }

    // ==================== SPAWN QUEUE ====================

    /**
     * Réinitialise la queue de spawn avec tous les drakes spawnables.
     */
    public void resetSpawnQueue() {
        spawnQueue.clear();
        spawnQueue.addAll(DrakeRegistry.getSpawnableDrakes());
        Collections.shuffle(spawnQueue);
        spawnedDrakeIds.clear();
        activeDrakes.clear();
    }

    /**
     * Démarre le spawn automatique des drakes.
     */
    public void startSpawnTask() {
        if (started) return;
        started = true;

        if (spawnQueue.isEmpty()) {
            resetSpawnQueue();
        }

        long initialDelay = spawnStartMinutes * 60 * 20L;
        long interval = spawnIntervalMinutes * 60 * 20L;

        spawnTaskId = new BukkitRunnable() {
            @Override
            public void run() {
                if (spawnQueue.isEmpty()) {
                    cancel();
                    plugin.getLogger().info("All drakes have been spawned!");
                    return;
                }
                spawnNextDrake();
            }
        }.runTaskTimer(plugin, initialDelay, interval).getTaskId();

        plugin.getLogger().info("Drake spawn task started (first spawn in " + spawnStartMinutes + " minutes)");
    }

    /**
     * Arrête le spawn automatique.
     */
    public void stopSpawnTask() {
        if (spawnTaskId != -1) {
            Bukkit.getScheduler().cancelTask(spawnTaskId);
            spawnTaskId = -1;
        }
        started = false;
    }

    /**
     * Spawn le prochain drake de la queue.
     */
    public void spawnNextDrake() {
        if (spawnQueue.isEmpty()) return;

        Drake drake = spawnQueue.remove(0);
        World world = Bukkit.getWorlds().get(0);
        Location loc = getRandomSpawnLocation(world);

        spawnDrake(drake, loc);
    }

    // ==================== SPAWN ====================

    /**
     * Spawn un drake à une location donnée.
     */
    public LivingEntity spawnDrake(Drake drake, Location location) {
        World world = location.getWorld();

        Location spawnLoc = location.clone();
        spawnLoc.setY(world.getHighestBlockYAt(location) + 1);

        LivingEntity entity = (LivingEntity) world.spawnEntity(spawnLoc, drake.getEntityType());

        entity.setCustomName(drake.getDisplayName());
        entity.setCustomNameVisible(true);
        entity.setRemoveWhenFarAway(false);

        try {
            entity.setMaxHealth(drake.getMaxHealth());
            entity.setHealth(drake.getMaxHealth());
        } catch (Exception e) {
            entity.setMaxHealth(drake.getMaxHealth());
            entity.setHealth(drake.getMaxHealth());
        }

        drake.setupEntity(entity);

        activeDrakes.put(entity.getUniqueId(), drake);
        spawnedDrakeIds.add(drake.getId());

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(ChatColor.GOLD + "⚔ " + drake.getDisplayName() + ChatColor.GOLD + " vient d'apparaître !");
        Bukkit.broadcastMessage(ChatColor.GRAY + "Position : " + ChatColor.WHITE + 
                (int) spawnLoc.getX() + ", " + (int) spawnLoc.getZ());
        Bukkit.broadcastMessage("");

        return entity;
    }

    /**
     * Force le spawn d'un drake spécifique.
     */
    public LivingEntity forceSpawnDrake(Drake drake) {
        World world = Bukkit.getWorlds().get(0);
        Location loc = getRandomSpawnLocation(world);
        return spawnDrake(drake, loc);
    }

    /**
     * Force le spawn d'un drake par son ID.
     */
    public LivingEntity forceSpawnDrake(String drakeId) {
        Drake drake = DrakeRegistry.getDrake(drakeId);
        if (drake == null) return null;
        return forceSpawnDrake(drake);
    }

    private Location getRandomSpawnLocation(World world) {
        Random random = new Random();
        double angle = random.nextDouble() * 2 * Math.PI;
        int distance = minDistance + random.nextInt(maxDistance - minDistance);

        int x = (int) (Math.cos(angle) * distance);
        int z = (int) (Math.sin(angle) * distance);

        return new Location(world, x, 0, z);
    }

    public boolean isDrake(Entity entity) {
        return activeDrakes.containsKey(entity.getUniqueId());
    }

    public Drake getDrake(Entity entity) {
        return activeDrakes.get(entity.getUniqueId());
    }

    public void removeDrake(Entity entity) {
        activeDrakes.remove(entity.getUniqueId());
    }

    public List<Drake> getSpawnQueue() {
        return Collections.unmodifiableList(spawnQueue);
    }

    public Set<String> getSpawnedDrakeIds() {
        return Collections.unmodifiableSet(spawnedDrakeIds);
    }

    public Map<UUID, Drake> getActiveDrakes() {
        return Collections.unmodifiableMap(activeDrakes);
    }

    public boolean isStarted() {
        return started;
    }

    public void cleanup() {
        stopSpawnTask();

        for (UUID uuid : new ArrayList<>(activeDrakes.keySet())) {
            Entity entity = this.getEntityByUUID(uuid);
            if (entity != null) {
                entity.remove();
            }
        }

        activeDrakes.clear();
        spawnQueue.clear();
        spawnedDrakeIds.clear();
    }

    private Entity getEntityByUUID(UUID uuid) {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getUniqueId().equals(uuid)) {
                    return entity;
                }
            }
        }
        return null;
    }
}
