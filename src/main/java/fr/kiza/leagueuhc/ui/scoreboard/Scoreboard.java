package fr.kiza.leagueuhc.ui.scoreboard;

import fr.kiza.leagueuhc.LeagueUHC;
import fr.kiza.leagueuhc.config.GameConfig;
import fr.kiza.leagueuhc.core.game.GamePlayer;
import fr.kiza.leagueuhc.core.game.gold.GoldManager;
import fr.kiza.leagueuhc.core.game.host.HostManager;
import fr.kiza.leagueuhc.core.game.timer.GameTimerManager;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.SimpleDateFormat;
import java.util.*;

public class Scoreboard implements Listener {

    protected final LeagueUHC instance;
    protected final Server server;

    private final Map<UUID, FastBoard> boards = new HashMap<>();
    private int ipCharIndex = 0;
    private int cooldown = 0;

    public Scoreboard(LeagueUHC instance) {
        this.instance = instance;
        this.server = instance.getServer();

        this.server.getPluginManager().registerEvents(this, this.instance);
        this.server.getScheduler().runTaskTimer(this.instance, () -> {
            this.boards.values().forEach(this::updateBoard);
        }, 0, 2);
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onLogin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final FastBoard board = new FastBoard(player);

        board.updateTitle(ChatColor.GOLD + "" + ChatColor.BOLD + "League" + ChatColor.YELLOW + ChatColor.BOLD + "UHC");
        this.boards.put(player.getUniqueId(), board);
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onLogout(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final FastBoard board = this.boards.remove(player.getUniqueId());

        if (board != null) board.delete();
    }

    private void updateBoard(final FastBoard board) {
        final String currentState = this.instance.getGameEngine().getCurrentState();

        switch (currentState) {
            case "PLAYING":
                this.updatePlayingBoard(board);
                break;
            case "ENDING":
            case "FINISHED":
                this.updateEndingBoard(board);
                break;
            default:
                this.updateDefaultBoard(board);
                break;
        }
    }

    private void updatePlayingBoard(final FastBoard board) {
        final List<String> lines = new ArrayList<>();

        final GamePlayer gamePlayer = GamePlayer.get(board.getPlayer());

        if (gamePlayer == null) {
            this.updateDefaultBoard(board);
            return;
        }

        lines.add(this.getCurrentDate() + ChatColor.DARK_GRAY + " lol-uhc");
        lines.add("");
        lines.add(ChatColor.WHITE + "  Durée: " + ChatColor.YELLOW + GameTimerManager.getInstance().getFormattedTime());
        lines.add(ChatColor.WHITE + "  Joueurs: " + ChatColor.YELLOW + this.instance.getGameEngine().getContext().getPlayers().size() + ChatColor.WHITE + "/" + ChatColor.YELLOW + GameConfig.MAX_PLAYERS);
        lines.add("");
        lines.add(ChatColor.WHITE + "  Kill" + (gamePlayer.getKills() <= 1 ? "" : "s") + ": " + ChatColor.YELLOW + gamePlayer.getKills());
        lines.add(ChatColor.WHITE + "  Gold" + (gamePlayer.getGold() <= 1 ? "" : "s") + ": " + ChatColor.YELLOW + GoldManager.formatGold(gamePlayer.getGold()));
        lines.add("");
        lines.add(ChatColor.WHITE + "  Episode: " + ChatColor.YELLOW + this.instance.getGameEngine().getGameHelper().getManager().getDayCycleManager().getCurrentEpisode());
        lines.add(ChatColor.WHITE + "  Bordure: " + ChatColor.YELLOW + "1000" + ChatColor.WHITE + "x" + ChatColor.YELLOW + "1000");
        lines.add("");
        lines.add(getAnimatedIP());

        board.updateLines(lines);
    }

    private void updateEndingBoard(final FastBoard board) {
        List<String> lines = new ArrayList<>();
        lines.add(this.getCurrentDate() + ChatColor.DARK_GRAY + " lol-uhc");
        lines.add("");
        lines.add(ChatColor.GRAY + "┃ En attente");
        lines.add(ChatColor.GRAY + "┃ du prochain");
        lines.add(ChatColor.GRAY + "┃ redémarrage...");
        lines.add("");
        lines.add(getAnimatedIP());

        board.updateLines(lines);
    }

    private void updateDefaultBoard(final FastBoard board) {
        List<String> lines = new ArrayList<>();
        lines.add(this.getCurrentDate() + ChatColor.DARK_GRAY + " lol-uhc");
        lines.add("");
        lines.add(ChatColor.WHITE + "  Host: " + ChatColor.RED + HostManager.getFirstHostName());
        lines.add(ChatColor.WHITE + "  Joueurs: " + ChatColor.YELLOW + this.instance.getGameEngine().getContext().getPlayers().size() + ChatColor.WHITE + "/" + ChatColor.YELLOW + GameConfig.MAX_PLAYERS);
        lines.add("");

        this.instance.getDatabaseManager().getPlayerService()
                .get(board.getPlayer().getUniqueId())
                .ifPresent(data -> {
                    int totalGames = data.getTotalGames();
                    int totalWins = data.getTotalWins();

                    lines.add(ChatColor.WHITE + "  Partie" + (totalGames == 0 ? "" : "s") + " jouée" + (totalGames == 0 ? "" : "s") + ": " + ChatColor.YELLOW + totalGames);
                    lines.add(ChatColor.WHITE + "  Partie" + (totalWins == 0 ? "" : "s") + " gagnée" + (totalWins == 0 ? "" : "s") + ": " + ChatColor.YELLOW + totalWins);
                });

        lines.add("");
        lines.add(getAnimatedIP());

        board.updateLines(lines);
    }

    private String getAnimatedIP() {
        String ip = "play.leagueuhc.fr";

        if (this.cooldown > 0) {
            this.cooldown--;
            return ChatColor.YELLOW + ip;
        }

        StringBuilder formattedIp = new StringBuilder();

        if (this.ipCharIndex > 0) {
            formattedIp.append(ChatColor.YELLOW).append(ip, 0, this.ipCharIndex - 1);
            formattedIp.append(ChatColor.GOLD).append(ip.charAt(this.ipCharIndex - 1));
        } else {
            formattedIp.append(ChatColor.YELLOW).append(ip, 0, this.ipCharIndex);
        }

        formattedIp.append(ChatColor.WHITE).append(ip.charAt(this.ipCharIndex));

        if (this.ipCharIndex + 1 < ip.length()) {
            formattedIp.append(ChatColor.GOLD).append(ip.charAt(this.ipCharIndex + 1));

            if (this.ipCharIndex + 2 < ip.length()) {
                formattedIp.append(ChatColor.YELLOW).append(ip.substring(this.ipCharIndex + 2));
            }

            this.ipCharIndex++;
        } else {
            this.ipCharIndex = 0;
            this.cooldown = 25;
        }

        return formattedIp.toString();
    }

    private String getCurrentDate() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return ChatColor.GRAY +  dateFormat.format(new Date());
    }
}