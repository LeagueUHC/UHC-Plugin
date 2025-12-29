package fr.kiza.leagueuhc.core.database.service;

import fr.kiza.leagueuhc.LeagueUHC;
import fr.kiza.leagueuhc.core.database.data.GameHistoryData;
import fr.kiza.leagueuhc.core.database.data.PlayerData;
import fr.kiza.leagueuhc.core.database.repository.PlayerRepository;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerService {

    private final LeagueUHC instance;
    private final PlayerRepository repository;

    public PlayerService(final LeagueUHC instance, final PlayerRepository repository) {
        this.instance = instance;
        this.repository = repository;
    }

    public CompletableFuture<PlayerData> loadPlayer(final Player player) {
        final UUID uuid = player.getUniqueId();
        final String name = player.getName();

        return this.repository.createOrUpdate(uuid)
                .thenCompose(v -> this.repository.load(uuid))
                .thenApply(data -> {
                    if (data != null) {
                        PlayerData.PLAYER.put(uuid, data);
                        this.instance.getLogger().info("Player loaded: " + data.getName() + " | Games: " + data.getTotalGames());
                    } else {
                        final PlayerData newData = new PlayerData(uuid, name);
                        PlayerData.PLAYER.put(uuid, newData);
                        this.instance.getLogger().info("New player created: " + name);
                    }
                    return PlayerData.PLAYER.get(uuid);
                });
    }

    public CompletableFuture<Void> unloadPlayer(final UUID uuid) {
        final PlayerData data = PlayerData.PLAYER.remove(uuid);

        if (data == null) {
            return CompletableFuture.completedFuture(null);
        }

        return this.repository.save(data).thenRun(() -> this.instance.getLogger().info("Player saved: " + data.getName()));
    }

    public Optional<PlayerData> get(final UUID uuid) {
        return Optional.ofNullable(PlayerData.PLAYER.get(uuid));
    }

    public PlayerData getOrThrow(final UUID uuid) {
        return get(uuid).orElseThrow(() -> new IllegalStateException("PlayerData not loaded for " + uuid));
    }

    public void saveAllSync() {
        this.instance.getLogger().info("Saving " + PlayerData.PLAYER.size() + " players...");

        PlayerData.PLAYER.values().forEach(data -> {
            try {
                this.repository.saveSync(data);
                this.instance.getLogger().info("Saved: " + data.getName());
            } catch (final Exception e) {
                this.instance.getLogger().severe("Failed to save " + data.getName() + ": " + e.getMessage());
            }
        });

        PlayerData.PLAYER.clear();
        this.instance.getLogger().info("All players saved.");
    }

    public void recordGame(final UUID uuid, final GameHistoryData gameHistoryData) {
        this.get(uuid).ifPresent(data -> {
            data.addGame(gameHistoryData);
            this.repository.save(data);
        });
    }
}