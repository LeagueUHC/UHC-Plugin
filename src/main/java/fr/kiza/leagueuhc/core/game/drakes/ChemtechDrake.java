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
    public void applyPassive(Player player) {
        ItemStack chestplate = player.getInventory().getChestplate();
        
        if (chestplate == null || chestplate.getType() == Material.AIR) {
            chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        }
        
        chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        player.getInventory().setChestplate(chestplate);
        
        player.sendMessage(getColor() + "» " + ChatColor.GREEN + "Protection III activée sur votre plastron !");
    }
}
