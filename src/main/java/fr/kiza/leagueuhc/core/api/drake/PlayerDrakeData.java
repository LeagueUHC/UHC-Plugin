package fr.kiza.leagueuhc.core.api.drake;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Stocke les données de drakes pour un joueur.
 */
public class PlayerDrakeData {

    private static final Map<UUID, PlayerDrakeData> PLAYER_DATA = new HashMap<>();

    private final UUID playerUUID;
    private final Set<String> ownedDrakes = new HashSet<>();
    private final Map<String, Long> cooldowns = new HashMap<>();
    private boolean hasAncestral = false;

    private PlayerDrakeData(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    // ==================== STATIC ACCESS ====================

    public static PlayerDrakeData get(Player player) {
        return get(player.getUniqueId());
    }

    public static PlayerDrakeData get(UUID uuid) {
        return PLAYER_DATA.computeIfAbsent(uuid, PlayerDrakeData::new);
    }

    public static void remove(UUID uuid) {
        PLAYER_DATA.remove(uuid);
    }

    public static void clear() {
        PLAYER_DATA.clear();
    }

    public static Collection<PlayerDrakeData> getAllData() {
        return Collections.unmodifiableCollection(PLAYER_DATA.values());
    }

    // ==================== INSTANCE METHODS ====================

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(playerUUID);
    }

    /**
     * Ajoute un drake au joueur et applique son passif.
     */
    public void addDrake(Drake drake) {
        if (ownedDrakes.add(drake.getId())) {
            Player player = getPlayer();
            if (player != null) {
                drake.applyPassive(player);
            }
        }
    }

    /**
     * Vérifie si le joueur possède un drake.
     */
    public boolean hasDrake(Drake drake) {
        return hasDrake(drake.getId());
    }

    public boolean hasDrake(String drakeId) {
        return ownedDrakes.contains(drakeId.toLowerCase());
    }

    /**
     * Renvoie les IDs des drakes possédés.
     */
    public Set<String> getOwnedDrakeIds() {
        return Collections.unmodifiableSet(ownedDrakes);
    }

    /**
     * Renvoie les drakes possédés.
     */
    public List<Drake> getOwnedDrakes() {
        List<Drake> drakes = new ArrayList<>();
        for (String id : ownedDrakes) {
            Drake drake = DrakeRegistry.getDrake(id);
            if (drake != null) {
                drakes.add(drake);
            }
        }
        return drakes;
    }

    /**
     * Vérifie si le joueur possède tous les drakes spawnables.
     */
    public boolean hasAllSpawnableDrakes() {
        for (Drake drake : DrakeRegistry.getSpawnableDrakes()) {
            if (!hasDrake(drake)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return Nombre de drakes possédés
     */
    public int getDrakeCount() {
        return ownedDrakes.size();
    }

    // ==================== ANCESTRAL ====================

    public void setAncestral(boolean value) {
        this.hasAncestral = value;
    }

    public boolean hasAncestral() {
        return hasAncestral;
    }

    // ==================== COOLDOWNS ====================

    public boolean isOnCooldown(String drakeId) {
        Long cooldownEnd = cooldowns.get(drakeId.toLowerCase());
        return cooldownEnd != null && System.currentTimeMillis() < cooldownEnd;
    }

    public long getCooldownRemaining(String drakeId) {
        Long cooldownEnd = cooldowns.get(drakeId.toLowerCase());
        if (cooldownEnd == null) return 0;
        return Math.max(0, cooldownEnd - System.currentTimeMillis());
    }

    public int getCooldownRemainingSeconds(String drakeId) {
        return (int) (getCooldownRemaining(drakeId) / 1000);
    }

    public void setCooldown(String drakeId, int seconds) {
        cooldowns.put(drakeId.toLowerCase(), System.currentTimeMillis() + (seconds * 1000L));
    }

    public void resetCooldown(String drakeId) {
        cooldowns.remove(drakeId.toLowerCase());
    }

    public void resetAllCooldowns() {
        cooldowns.clear();
    }

    // ==================== UTILITY ====================

    public void reset() {
        ownedDrakes.clear();
        cooldowns.clear();
        hasAncestral = false;
    }

    @Override
    public String toString() {
        return "PlayerDrakeData{uuid=" + playerUUID + ", drakes=" + ownedDrakes.size() + ", ancestral=" + hasAncestral + '}';
    }
}
