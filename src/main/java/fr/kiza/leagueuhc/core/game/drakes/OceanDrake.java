package fr.kiza.leagueuhc.core.game.drakes;

import fr.kiza.leagueuhc.core.api.drake.Drake;
import fr.kiza.leagueuhc.core.api.drake.annotation.DrakeEntry;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@DrakeEntry(priority = 2)
public class OceanDrake extends Drake {

    @Override
    public String getName() {
        return "Drake des océans";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.BLUE;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.GUARDIAN;
    }

    @Override
    public int getHearts() {
        return 200;
    }

    @Override
    public String getPassiveDescription() {
        return "Depth Strider I sur les bottes";
    }

    @Override
    public boolean applyPassive(Player player) {
        ItemStack boots = player.getInventory().getBoots();

        if (boots == null || boots.getType() != Material.DIAMOND_BOOTS) {
            player.sendMessage(ChatColor.RED + "✖ Tu dois porter des bottes en diamant !");
            return false;
        }

        int currentLevel = boots.getEnchantmentLevel(Enchantment.DEPTH_STRIDER);
        boots.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, currentLevel + 1);

        player.sendMessage(getColor() + "» " + ChatColor.GREEN + "Depth Strider activé sur vos bottes !");
        return true;
    }

    @Override
    public void removePassive(Player player) {
        ItemStack boots = player.getInventory().getBoots();

        if (boots != null && boots.containsEnchantment(Enchantment.DEPTH_STRIDER)) {
            int level = boots.getEnchantmentLevel(Enchantment.DEPTH_STRIDER);
            if (level <= 1) {
                boots.removeEnchantment(Enchantment.DEPTH_STRIDER);
            } else {
                boots.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, level - 1);
            }
        }

        player.sendMessage(getColor() + "» " + ChatColor.RED + "Depth Strider retiré");
    }
}
