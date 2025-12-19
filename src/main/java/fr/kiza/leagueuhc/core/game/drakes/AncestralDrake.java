package fr.kiza.leagueuhc.core.game.drakes;

import fr.kiza.leagueuhc.core.api.drake.Drake;
import fr.kiza.leagueuhc.core.api.drake.PlayerDrakeData;
import fr.kiza.leagueuhc.core.api.drake.annotation.DrakeEntry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Random;

/**
 * Drake Ancestral - Le drake ultime obtenu en combinant les 6 drakes.
 * 
 * Bonus :
 * - +4 cœurs permanents
 * - Depth Strider III sur les bottes
 * - 50% de chance de mettre en feu
 * - 40% de chance d'infliger un éclair
 * - Speed III permanent
 * - Protection III sur le plastron
 * - Vision de la vie des joueurs
 * - Exécution des joueurs sous 2 cœurs
 */
@DrakeEntry(excludeFromSpawn = true)
public class AncestralDrake extends Drake {

    private static final double FIRE_CHANCE = 0.50;
    private static final double LIGHTNING_CHANCE = 0.40;
    private static final double EXECUTION_THRESHOLD = 4.0;

    private final Random random = new Random();

    @Override
    public String getName() {
        return "Drake Ancestral";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.GOLD;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.ENDER_DRAGON;
    }

    @Override
    public int getHearts() {
        return 0;
    }

    @Override
    public String getPassiveDescription() {
        return "Tous les pouvoirs ancestraux combinés";
    }

    @Override
    public ItemStack createItem() {
        ItemStack item = new ItemStack(Material.DRAGON_EGG);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.GOLD + "✦ Drake Ancestral ✦");
        meta.setLore(Arrays.asList(
                "",
                ChatColor.GRAY + "La fin du monde",
                "",
                ChatColor.GREEN + "» " + ChatColor.WHITE + "+4 cœurs permanents",
                ChatColor.GREEN + "» " + ChatColor.WHITE + "Depth Strider III (bottes)",
                ChatColor.GREEN + "» " + ChatColor.WHITE + "50% chance de feu",
                ChatColor.GREEN + "» " + ChatColor.WHITE + "40% chance d'éclair",
                ChatColor.GREEN + "» " + ChatColor.WHITE + "Speed III permanent",
                ChatColor.GREEN + "» " + ChatColor.WHITE + "Protection III (plastron)",
                ChatColor.GREEN + "» " + ChatColor.WHITE + "Vision de la vie des joueurs",
                ChatColor.GREEN + "» " + ChatColor.WHITE + "Exécution sous 2 cœurs",
                "",
                ChatColor.YELLOW + "Clic droit pour activer",
                "",
                ChatColor.DARK_GRAY + "§k§r§drake:" + getId()
        ));

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void applyPassive(Player player) {
        PlayerDrakeData data = PlayerDrakeData.get(player);
        
        if (data.hasAncestral()) {
            player.sendMessage(ChatColor.RED + "Tu as déjà activé le Drake Ancestral !");
            return;
        }

        data.setAncestral(true);

        // +4 cœurs permanents
        player.setMaxHealth(player.getMaxHealth() + 8);

        // Depth Strider III
        ItemStack boots = player.getInventory().getBoots();
        if (boots == null || boots.getType() == Material.AIR) {
            boots = new ItemStack(Material.DIAMOND_BOOTS);
        }
        boots.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 3);
        player.getInventory().setBoots(boots);

        // Protection III
        ItemStack chestplate = player.getInventory().getChestplate();
        if (chestplate == null || chestplate.getType() == Material.AIR) {
            chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        }
        chestplate.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        player.getInventory().setChestplate(chestplate);

        // Speed III permanent
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.SPEED,
                Integer.MAX_VALUE,
                2,
                false,
                false
        ));

        player.sendMessage("");
        player.sendMessage(ChatColor.GOLD + "★★★ Drake Ancestral activé ! ★★★");
        player.sendMessage("");
        player.sendMessage(ChatColor.GREEN + "» " + ChatColor.WHITE + "+4 cœurs permanents");
        player.sendMessage(ChatColor.GREEN + "» " + ChatColor.WHITE + "Depth Strider III");
        player.sendMessage(ChatColor.GREEN + "» " + ChatColor.WHITE + "50% chance de feu");
        player.sendMessage(ChatColor.GREEN + "» " + ChatColor.WHITE + "40% chance d'éclair");
        player.sendMessage(ChatColor.GREEN + "» " + ChatColor.WHITE + "Speed III permanent");
        player.sendMessage(ChatColor.GREEN + "» " + ChatColor.WHITE + "Protection III");
        player.sendMessage(ChatColor.GREEN + "» " + ChatColor.WHITE + "Vision de la vie");
        player.sendMessage(ChatColor.GREEN + "» " + ChatColor.WHITE + "Exécution sous 2 cœurs");
        player.sendMessage("");
    }

    @Override
    public boolean onAttack(EntityDamageByEntityEvent event, Player attacker, Player victim) {
        PlayerDrakeData data = PlayerDrakeData.get(attacker);
        if (!data.hasAncestral()) return false;

        boolean triggered = false;

        // 50% feu
        if (random.nextDouble() < FIRE_CHANCE) {
            victim.setFireTicks(60);
            attacker.sendMessage(ChatColor.RED + "🔥 " + ChatColor.GRAY + "Enflammé !");
            triggered = true;
        }

        // 40% éclair
        if (random.nextDouble() < LIGHTNING_CHANCE) {
            victim.getWorld().strikeLightningEffect(victim.getLocation());
            victim.damage(2.0, attacker);
            attacker.sendMessage(ChatColor.LIGHT_PURPLE + "⚡ " + ChatColor.GRAY + "Électrocuté !");
            triggered = true;
        }

        // Vision de la vie
        double healthAfter = Math.max(0, victim.getHealth() - event.getFinalDamage());
        int hearts = (int) Math.ceil(healthAfter / 2);
        attacker.sendMessage(ChatColor.GOLD + "👁 " + ChatColor.WHITE + victim.getName() + 
                ChatColor.GRAY + " : " + ChatColor.RED + "❤ " + hearts);

        // Exécution sous 2 cœurs
        if (healthAfter > 0 && healthAfter <= EXECUTION_THRESHOLD) {
            victim.setHealth(0);
            attacker.sendMessage(ChatColor.GOLD + "💀 " + ChatColor.GRAY + "Exécution sur " + victim.getName() + " !");
            triggered = true;
        }

        return triggered;
    }

    public static boolean hasAncestral(Player player) {
        return PlayerDrakeData.get(player).hasAncestral();
    }
}
