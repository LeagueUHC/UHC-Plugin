package fr.kiza.leagueuhc.managers.commands;

import fr.kiza.leagueuhc.core.api.gui.helper.GuiBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandShop implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) return  true;

        final Player player = (Player) sender;

        if (args.length != 0) {
            player.sendMessage(ChatColor.RED + "Usage: /shop");
            return true;
        }

        //open gui
        return false;
    }
}
