package fr.kiza.leagueuhc;

import fr.kiza.leagueuhc.config.GameConfig;
import fr.kiza.leagueuhc.core.database.DatabaseConfig;
import fr.kiza.leagueuhc.core.database.DatabaseManager;
import fr.kiza.leagueuhc.managers.commands.CommandUHC;
import fr.kiza.leagueuhc.core.game.GameEngine;

import org.bukkit.plugin.java.JavaPlugin;

public final class LeagueUHC extends JavaPlugin {

    private static LeagueUHC instance;

    private DatabaseManager databaseManager;

    private GameEngine gameEngine;

    @Override
    public void onEnable() {
        instance = this;
        long startTime = System.currentTimeMillis();

        this.getLogger().info("==== LeagueUHC START ====");
        this.print();

        this.saveDefaultConfig();

        this.databaseManager = new DatabaseManager(this);
        DatabaseManager.init(new DatabaseConfig(
                GameConfig.DATABASE_HOST,
                GameConfig.DATABASE_PORT,
                GameConfig.DATABASE_NAME,
                GameConfig.DATABASE_USER,
                GameConfig.DATABASE_PASSWORD
        ));

        this.gameEngine = new GameEngine(this);
        this.gameEngine.start();

        final CommandUHC command = new CommandUHC(this);
        this.getCommand("uhc").setExecutor(command);
        this.getCommand("uhc").setTabCompleter(command);

        CommandUHC.pregenManager.unloadAndDeleteWorld();

        final long loadTime = System.currentTimeMillis() - startTime;

        this.getLogger().info("═══════════════════════════════════════");
        this.getLogger().info("  " + GameConfig.PLUGIN_NAME + " chargé en " + loadTime + "ms");
        this.getLogger().info("═══════════════════════════════════════");
        this.getLogger().info("==== LeagueUHC READY ====");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("═══════════════════════════════════════");
        this.getLogger().info("  Shutdown of " + GameConfig.PLUGIN_NAME + "...");
        this.getLogger().info("═══════════════════════════════════════");

        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");

        this.databaseManager.getPlayerService().saveAllSync();
        if (this.databaseManager != null) DatabaseManager.shutdown();

        if (this.gameEngine != null) this.gameEngine.stop();
    }

    private void print() {
        getLogger().info("");
        getLogger().info("╔═══════════════════════════════════════╗");
        getLogger().info("║                                       ║");
        getLogger().info("║     ██╗      ██████╗ ██╗              ║");
        getLogger().info("║     ██║     ██╔═══██╗██║              ║");
        getLogger().info("║     ██║     ██║   ██║██║              ║");
        getLogger().info("║     ██║     ██║   ██║██║              ║");
        getLogger().info("║     ███████╗╚██████╔╝███████╗         ║");
        getLogger().info("║     ╚══════╝ ╚═════╝ ╚══════╝         ║");
        getLogger().info("║                                       ║");
        getLogger().info("║              LoL-UHC v1.0             ║");
        getLogger().info("║                                       ║");
        getLogger().info("╚═══════════════════════════════════════╝");
        getLogger().info("");
    }


    public static LeagueUHC getInstance() {
        return instance;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

}
