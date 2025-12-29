package fr.kiza.leagueuhc.core.game;

import fr.kiza.leagueuhc.core.api.champion.Champion;

import fr.kiza.leagueuhc.core.database.data.GameHistoryData;
import fr.kiza.leagueuhc.core.game.gold.GoldSource;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Représente un joueur dans la partie UHC.
 * Contient les données de jeu transitoires et le champion assigné.
 * Ces données sont reset à chaque fin de partie.
 */
public class GamePlayer {

    private static final Map<UUID, GamePlayer> PLAYERS = new HashMap<>();

    private final UUID uuid;
    private final String name;
    private final long joinedAt;

    private Champion champion;

    private boolean alive = true;
    private boolean spectator = false;

    private int kills = 0;
    private int deaths = 0;
    private int assists = 0;
    private int currentKillStreak = 0;
    private int bestKillStreak = 0;

    private double damageDealt = 0;
    private double damageTaken = 0;
    private double healingDone = 0;

    private double gold = 0;
    private double goldEarned = 0;
    private double goldSpent = 0;

    private int drakesKilled = 0;
    private int drakeBuffsObtained = 0;
    private int abilitiesUsed = 0;

    private UUID lastDamager = null;

    public GamePlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.joinedAt = System.currentTimeMillis();
        PLAYERS.put(uuid, this);
    }

    /**
     * Récupère le GamePlayer d'un joueur Bukkit.
     * Retourne null si le joueur n'est pas enregistré.
     */
    public static GamePlayer get(Player player) {
        return player == null ? null : PLAYERS.get(player.getUniqueId());
    }

    /**
     * Récupère le GamePlayer par UUID.
     */
    public static GamePlayer get(UUID uuid) {
        return PLAYERS.get(uuid);
    }

    /**
     * Récupère ou crée le GamePlayer d'un joueur.
     */
    public static GamePlayer getOrCreate(Player player) {
        GamePlayer gp = get(player);
        return gp != null ? gp : new GamePlayer(player);
    }

    /**
     * Vérifie si un joueur est enregistré.
     */
    public static boolean isRegistered(Player player) {
        return player != null && PLAYERS.containsKey(player.getUniqueId());
    }

    /**
     * Retourne tous les GamePlayers enregistrés.
     */
    public static Map<UUID, GamePlayer> getAll() {
        return new HashMap<>(PLAYERS);
    }

    /**
     * Supprime un joueur du registre.
     */
    public static void remove(Player player) {
        if (player != null) {
            PLAYERS.remove(player.getUniqueId());
        }
    }

    /**
     * Supprime un joueur du registre par UUID.
     */
    public static void remove(UUID uuid) {
        PLAYERS.remove(uuid);
    }

    /**
     * Nettoie tous les joueurs.
     */
    public static void clearAll() {
        PLAYERS.clear();
    }

    // ==================== GOLD MANAGEMENT ====================

    /**
     * Ajoute des golds au joueur
     */
    public void addGold(double amount, GoldSource source) {
        if (amount <= 0) return;
        this.gold += amount;
        this.goldEarned += amount;
    }

    /**
     * Ajoute des golds au joueur (sans source)
     */
    public void addGold(double amount) {
        addGold(amount, GoldSource.OTHER);
    }

    /**
     * Retire des golds au joueur (achat)
     * @return true si le joueur avait assez de golds
     */
    public boolean spendGold(double amount) {
        if (amount <= 0) return false;
        if (this.gold < amount) return false;

        this.gold -= amount;
        this.goldSpent += amount;
        return true;
    }

    /**
     * Vérifie si le joueur peut payer un montant
     */
    public boolean canAfford(double amount) {
        return this.gold >= amount;
    }

    // ==================== KILL / DEATH / ASSIST ====================

    /**
     * Appelé quand le joueur fait un kill
     */
    public void addKill() {
        this.kills++;
        this.currentKillStreak++;
        if (this.currentKillStreak > this.bestKillStreak) {
            this.bestKillStreak = this.currentKillStreak;
        }
    }

    /**
     * Appelé quand le joueur fait un kill avec récompense gold
     */
    public void addKill(double goldReward) {
        addKill();
        addGold(goldReward, GoldSource.KILL);
    }

    /**
     * Appelé quand le joueur meurt
     */
    public void addDeath() {
        this.deaths++;
        this.currentKillStreak = 0;
        this.alive = false;
    }

    /**
     * Appelé quand le joueur fait un assist
     */
    public void addAssist() {
        this.assists++;
    }

    /**
     * Appelé quand le joueur fait un assist avec récompense gold
     */
    public void addAssist(double goldReward) {
        addAssist();
        addGold(goldReward, GoldSource.ASSIST);
    }

    /**
     * Enregistre les dégâts infligés
     */
    public void addDamageDealt(double damage) {
        this.damageDealt += damage;
    }

    /**
     * Enregistre les dégâts reçus
     */
    public void addDamageTaken(double damage) {
        this.damageTaken += damage;
    }

    /**
     * Enregistre les dégâts reçus avec le damager
     */
    public void addDamageTaken(double damage, UUID damager) {
        this.damageTaken += damage;
        this.lastDamager = damager;
    }

    /**
     * Enregistre les soins effectués
     */
    public void addHealingDone(double healing) {
        this.healingDone += healing;
    }

    public void addDrakeKill() {
        this.drakesKilled++;
    }

    public void addDrakeBuff() {
        this.drakeBuffsObtained++;
    }

    public void addAbilityUsed() {
        this.abilitiesUsed++;
    }

    /**
     * Récupère le champion assigné.
     */
    public Champion getChampion() {
        return champion;
    }

    /**
     * Définit le champion (utilisé par ChampionManager).
     */
    public void setChampion(Champion champion) {
        this.champion = champion;
    }

    /**
     * Assigne un champion via le ChampionManager.
     * Utilise cette méthode plutôt que setChampion directement.
     */
    public void assignChampion(Champion champion) {
        this.champion = champion;
    }

    /**
     * Vérifie si le joueur a un champion.
     */
    public boolean hasChampion() {
        return champion != null;
    }

    /**
     * Calcule le temps de jeu en secondes pour cette partie
     */
    public long getPlaytimeSeconds() {
        return (System.currentTimeMillis() - joinedAt) / 1000;
    }

    /**
     * Retourne le KDA de cette partie
     */
    public double getKDA() {
        return deaths == 0 ? (kills + assists) : (double) (kills + assists) / deaths;
    }

    /**
     * Retourne le KD ratio
     */
    public double getKDRatio() {
        return deaths == 0 ? kills : (double) kills / deaths;
    }

    /**
     * Convertit en GameHistory pour sauvegarde en BDD
     */
    public GameHistoryData toGameHistory(int gameId, boolean won) {
        return new GameHistoryData(
                gameId,
                uuid,
                name,
                champion != null ? champion.getName() : "NONE",
                kills,
                deaths,
                assists,
                bestKillStreak,
                (int) damageDealt,
                (int) damageTaken,
                (int) healingDone,
                goldEarned,
                goldSpent,
                drakesKilled,
                drakeBuffsObtained,
                abilitiesUsed,
                getPlaytimeSeconds(),
                won
        );
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    /**
     * Récupère le joueur Bukkit.
     * Peut retourner null si le joueur est déconnecté.
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    /**
     * Vérifie si le joueur est en ligne.
     */
    public boolean isOnline() {
        return getPlayer() != null;
    }

    public UUID getUuid() {
        return uuid;
    }

    public long getJoinedAt() {
        return joinedAt;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isSpectator() {
        return spectator;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getAssists() {
        return assists;
    }

    public int getCurrentKillStreak() {
        return currentKillStreak;
    }

    public int getBestKillStreak() {
        return bestKillStreak;
    }

    public double getDamageDealt() {
        return damageDealt;
    }

    public double getDamageTaken() {
        return damageTaken;
    }

    public double getHealingDone() {
        return healingDone;
    }

    public double getGold() {
        return gold;
    }

    public double getGoldEarned() {
        return goldEarned;
    }

    public double getGoldSpent() {
        return goldSpent;
    }

    public int getDrakesKilled() {
        return drakesKilled;
    }

    public int getDrakeBuffsObtained() {
        return drakeBuffsObtained;
    }

    public int getAbilitiesUsed() {
        return abilitiesUsed;
    }

    public UUID getLastDamager() {
        return lastDamager;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setSpectator(boolean spectator) {
        this.spectator = spectator;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public void setCurrentKillStreak(int currentKillStreak) {
        this.currentKillStreak = currentKillStreak;
    }

    public void setBestKillStreak(int bestKillStreak) {
        this.bestKillStreak = bestKillStreak;
    }

    public void setDamageDealt(double damageDealt) {
        this.damageDealt = damageDealt;
    }

    public void setDamageTaken(double damageTaken) {
        this.damageTaken = damageTaken;
    }

    public void setHealingDone(double healingDone) {
        this.healingDone = healingDone;
    }

    public void setGold(double gold) {
        this.gold = gold;
    }

    public void setGoldEarned(double goldEarned) {
        this.goldEarned = goldEarned;
    }

    public void setGoldSpent(double goldSpent) {
        this.goldSpent = goldSpent;
    }

    public void setDrakesKilled(int drakesKilled) {
        this.drakesKilled = drakesKilled;
    }

    public void setDrakeBuffsObtained(int drakeBuffsObtained) {
        this.drakeBuffsObtained = drakeBuffsObtained;
    }

    public void setAbilitiesUsed(int abilitiesUsed) {
        this.abilitiesUsed = abilitiesUsed;
    }

    public void setLastDamager(UUID lastDamager) {
        this.lastDamager = lastDamager;
    }

    /**
     * Réinitialise les stats du joueur.
     */
    public void resetStats() {
        this.kills = 0;
        this.deaths = 0;
        this.assists = 0;
        this.currentKillStreak = 0;
        this.bestKillStreak = 0;
        this.damageDealt = 0;
        this.damageTaken = 0;
        this.healingDone = 0;
        this.gold = 0;
        this.goldEarned = 0;
        this.goldSpent = 0;
        this.drakesKilled = 0;
        this.drakeBuffsObtained = 0;
        this.abilitiesUsed = 0;
        this.alive = true;
        this.spectator = false;
        this.lastDamager = null;
    }

    /**
     * Réinitialise complètement le joueur (stats + champion).
     */
    public void reset() {
        resetStats();
        this.champion = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof GamePlayer)) return false;
        return uuid.equals(((GamePlayer) obj).uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public String toString() {
        return "GamePlayer{" +
                "name='" + name + '\'' +
                ", champion=" + (champion != null ? champion.getName() : "none") +
                ", alive=" + alive +
                ", kda=" + kills + "/" + deaths + "/" + assists +
                ", gold=" + gold +
                '}';
    }
}