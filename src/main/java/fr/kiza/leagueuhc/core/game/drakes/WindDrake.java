package fr.kiza.leagueuhc.core.game.drakes;

import fr.kiza.leagueuhc.core.api.drake.Drake;
import fr.kiza.leagueuhc.core.api.drake.PlayerDrakeData;
import fr.kiza.leagueuhc.core.api.drake.annotation.DrakeEntry;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@DrakeEntry(priority = 5)
public class WindDrake extends Drake {

    private static final int SPEED_DURATION = 15;
    private static final int COOLDOWN = 180;

    @Override
    public String getName() {
        return "Drake des vents";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.WHITE;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.HORSE;
    }

    @Override
    public int getHearts() {
        return 150;
    }

    @Override
    public String getPassiveDescription() {
        return "Pouvoir : Speed III (15s) - Cooldown 3min";
    }

    @Override
    public boolean hasActivePower() {
        return true;
    }

    @Override
    public int getPowerCooldown() {
        return COOLDOWN;
    }

    @Override
    public boolean applyPassive(Player player) {
        player.sendMessage(getColor() + "» " + ChatColor.GREEN + "Pouvoir des vents débloqué !");
        player.sendMessage(ChatColor.GRAY + "   Utilise l'item pour activer Speed III pendant 15 secondes.");
        return true;
    }

    @Override
    public void removePassive(Player player) {
        player.removePotionEffect(PotionEffectType.SPEED);
        PlayerDrakeData.get(player).resetCooldown(getId());

        player.sendMessage(getColor() + "» " + ChatColor.RED + "Pouvoir des vents retiré");
    }
    @Override
    public void usePower(Player player) {
        PlayerDrakeData data = PlayerDrakeData.get(player);

        if (data.isOnCooldown(getId())) {
            int remaining = data.getCooldownRemainingSeconds(getId());
            player.sendMessage(ChatColor.RED + "Pouvoir en cooldown ! " + ChatColor.GRAY + "(" + remaining + "s)");
            return;
        }

        player.addPotionEffect(new PotionEffect(
                PotionEffectType.SPEED,
                SPEED_DURATION * 20,
                2,
                false,
                true
        ));

        data.setCooldown(getId(), COOLDOWN);

        player.sendMessage(ChatColor.WHITE + "⚡ Pouvoir des vents activé ! " + ChatColor.GRAY + "(Speed III - 15s)");
    }

    @Override
    public void setupEntity(LivingEntity entity) {
        if (entity instanceof Horse) {
            Horse horse = (Horse) entity;
            horse.setTamed(false);
            horse.setAdult();
            horse.getInventory().setSaddle(null);
        }
    }
}
