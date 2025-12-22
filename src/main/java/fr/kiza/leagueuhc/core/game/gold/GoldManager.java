package fr.kiza.leagueuhc.core.game.gold;

import fr.kiza.leagueuhc.LeagueUHC;
import fr.kiza.leagueuhc.core.game.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static fr.kiza.leagueuhc.config.GameConfig.*;

public class GoldManager {

    private final LeagueUHC instance;
    private boolean firstBloodClaimed = false;

    public GoldManager(LeagueUHC instance) {
        this.instance = instance;
    }

    public void onPlayerKill(Player killer, Player victim) {
        GamePlayer gKiller = GamePlayer.get(killer);
        GamePlayer gVictim = GamePlayer.get(victim);

        if (gKiller == null || gVictim == null) return;

        double reward = KILL_BASE;

        double victimGold = gVictim.getGold();
        reward += victimGold;

        if (!firstBloodClaimed) {
            reward += FIRST_BLOOD;
            firstBloodClaimed = true;
            broadcastMessage(ChatColor.RED + "☠ FIRST BLOOD! " + ChatColor.GOLD + killer.getName() + ChatColor.GRAY + " reçoit " + ChatColor.YELLOW + formatGold(FIRST_BLOOD) + " bonus!");
        }

        if (gVictim.getCurrentKillStreak() >= 3) {
            reward += SHUTDOWN;
            killer.sendMessage(ChatColor.GOLD + "⚔ SHUTDOWN! " + ChatColor.GRAY + "+" + formatGold(SHUTDOWN));
        }

        gKiller.addGold(reward, GoldSource.KILL);
        gKiller.addKill();

        gVictim.setGold(0);
        gVictim.addDeath();

        killer.sendMessage(ChatColor.GREEN + "+ " + formatGold(reward) + ChatColor.GRAY + " (" + formatGold(KILL_BASE) + " kill + " + formatGold(victimGold) + " loot)");
        victim.sendMessage(ChatColor.RED + "- " + formatGold(victimGold) + ChatColor.GRAY + " perdus!");
    }

    public void onPlayerAssist(Player assister) {
        GamePlayer gAssister = GamePlayer.get(assister);
        if (gAssister == null) return;

        gAssister.addGold(KILL_ASSIST, GoldSource.ASSIST);
        gAssister.addAssist();

        assister.sendMessage(ChatColor.GREEN + "+ " + formatGold(KILL_ASSIST) + ChatColor.GRAY + " (assist)");
    }

    public void onDrakeKill(Player killer) {
        GamePlayer gKiller = GamePlayer.get(killer);
        if (gKiller == null) return;

        gKiller.addGold(DRAKE_KILL, GoldSource.DRAKE);
        gKiller.addDrakeKill();

        killer.sendMessage(ChatColor.GREEN + "+ " + formatGold(DRAKE_KILL) + ChatColor.GRAY + " (drake)");
    }

    public void onQuestComplete(Player player, QuestDifficulty difficulty) {
        GamePlayer gPlayer = GamePlayer.get(player);
        if (gPlayer == null) return;

        double reward;
        switch (difficulty) {
            case EASY:
                reward = QUEST_EASY;
                break;
            case MEDIUM:
                reward = QUEST_MEDIUM;
                break;
            case HARD:
                reward = QUEST_HARD;
                break;
            default:
                reward = 0;
        }

        gPlayer.addGold(reward, GoldSource.OTHER);

        player.sendMessage(ChatColor.GREEN + "+ " + formatGold(reward) +
                ChatColor.GRAY + " (quête " + difficulty.name().toLowerCase() + ")");
    }

    public void giveGold(Player player, double amount, String reason) {
        GamePlayer gPlayer = GamePlayer.get(player);
        if (gPlayer == null) return;

        gPlayer.addGold(amount, GoldSource.OTHER);
        player.sendMessage(ChatColor.GREEN + "+ " + formatGold(amount) +
                ChatColor.GRAY + " (" + reason + ")");
    }

    public void reset() {
        this.firstBloodClaimed = false;
    }

    public static String formatGold(double gold) {
        if (gold == (int) gold) {
            return (int) gold + " ⛃";
        }
        return String.format("%.1f ⛃", gold);
    }

    private void broadcastMessage(String message) {
        Bukkit.getOnlinePlayers().forEach(players -> players.sendMessage(message));
    }

    public enum QuestDifficulty {
        EASY, MEDIUM, HARD
    }
}