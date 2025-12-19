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
    public void applyPassive(Player player) {
        ItemStack boots = player.getInventory().getBoots();
        
        if (boots == null || boots.getType() == Material.AIR) {
            boots = new ItemStack(Material.DIAMOND_BOOTS);
        }
        
        boots.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 1);
        player.getInventory().setBoots(boots);
        
        player.sendMessage(getColor() + "» " + ChatColor.GREEN + "Depth Strider I activé sur vos bottes !");
    }
}
