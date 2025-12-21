package fr.kiza.leagueuhc.core.game.drakes;

import fr.kiza.leagueuhc.core.api.drake.Drake;
import fr.kiza.leagueuhc.core.api.drake.annotation.DrakeEntry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@DrakeEntry(priority = 6)
public class ChemtechDrake extends Drake {

    @Override
    public String getName() {
        return "Drake techno-chimique";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.GREEN;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.WITCH;
    }

    @Override
    public int getHearts() {
        return 70;
    }

    @Override
    public String getPassiveDescription() {
        return "Protection III sur le plastron";
    }

    @Override
    public boolean applyPassive(Player player) {
        ItemStack chestplate = player.getInventory().getChestplate();

        if (chestplate == null || chestplate.getType() != Material.DIAMOND_CHESTPLATE) {
            player.sendMessage(ChatColor.RED + "✖ Tu dois porter un plastron en diamant !");
            return false;
        }

        int currentLevel = chestplate.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
        chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, currentLevel + 3);

        player.sendMessage(getColor() + "» " + ChatColor.GREEN + "Protection III activée sur votre plastron !");
        return true;
    }

    @Override
    public void removePassive(Player player) {
        ItemStack chestplate = player.getInventory().getChestplate();

        if (chestplate != null && chestplate.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)) {
            int level = chestplate.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
            if (level <= 3) {
                chestplate.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
            } else {
                chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, level - 3);
            }
        }

        player.sendMessage(getColor() + "» " + ChatColor.RED + "Protection III retirée");
    }
}
