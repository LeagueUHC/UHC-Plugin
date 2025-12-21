package fr.kiza.leagueuhc.core.api.drake;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Classe abstraite représentant un Drake.
 * Tous les drakes doivent hériter de cette classe et être annotés avec @DrakeEntry.
 */
public abstract class Drake {

    private static final String DRAKE_NBT_KEY = "§k§r§ddrake";

    // ==================== MÉTHODES ABSTRAITES ====================

    /**
     * @return Nom d'affichage du drake
     */
    public abstract String getName();

    /**
     * @return Couleur du drake dans le chat
     */
    public abstract ChatColor getColor();

    /**
     * @return Type d'entité Minecraft représentant le drake
     */
    public abstract EntityType getEntityType();

    /**
     * @return Nombre de cœurs du drake
     */
    public abstract int getHearts();

    /**
     * @return Description courte du passif
     */
    public abstract String getPassiveDescription();

    /**
     * Applique le passif permanent au joueur.
     * Appelé quand le joueur active l'item drake.
     *
     * @param player Le joueur qui reçoit le passif
     * @return
     */
    public abstract boolean applyPassive(Player player);

    /**
     * Retire le passif du joueur.
     * Appelé quand le joueur drop l'item drake.
     *
     * @param player Le joueur qui perd le passif
     */
    public abstract void removePassive(Player player);

    // ==================== MÉTHODES OPTIONNELLES À OVERRIDE ====================

    /**
     * @return true si ce drake a un pouvoir actif (clic droit)
     */
    public boolean hasActivePower() {
        return false;
    }

    /**
     * @return Cooldown du pouvoir actif en secondes (0 si pas de cooldown)
     */
    public int getPowerCooldown() {
        return 0;
    }

    /**
     * Utilise le pouvoir actif du drake.
     * À override si hasActivePower() retourne true.
     *
     * @param player Le joueur qui utilise le pouvoir
     */
    public void usePower(Player player) {
        // Override si nécessaire
    }

    /**
     * Appelé quand le joueur avec ce passif frappe un autre joueur.
     * À override pour les passifs offensifs (feu, éclair, etc.)
     *
     * @param event L'événement de dégâts
     * @param attacker Le joueur attaquant (qui a le passif)
     * @param victim Le joueur victime
     * @return true si le passif s'est déclenché
     */
    public boolean onAttack(EntityDamageByEntityEvent event, Player attacker, Player victim) {
        return false;
    }

    /**
     * Appelé quand le joueur avec ce passif est frappé.
     * À override pour les passifs défensifs.
     *
     * @param event L'événement de dégâts
     * @param victim Le joueur victime (qui a le passif)
     * @param attacker L'attaquant
     * @return true si le passif s'est déclenché
     */
    public boolean onDefend(EntityDamageByEntityEvent event, Player victim, Player attacker) {
        return false;
    }

    /**
     * Configure l'entité après son spawn.
     * À override pour des configurations spécifiques (Horse, etc.)
     *
     * @param entity L'entité spawnée
     */
    public void setupEntity(LivingEntity entity) {
        // Override si nécessaire
    }

    // ==================== MÉTHODES FINALES ====================

    /**
     * @return Identifiant unique du drake (nom en lowercase)
     */
    public final String getId() {
        return getName().toLowerCase().replace(" ", "_");
    }

    /**
     * @return Nom coloré du drake
     */
    public final String getDisplayName() {
        return getColor() + getName();
    }

    /**
     * @return Santé maximale en HP (cœurs * 2)
     */
    public final double getMaxHealth() {
        return getHearts() * 2.0;
    }

    /**
     * Crée l'item drake à donner au joueur.
     *
     * @return ItemStack de l'item drake
     */
    public ItemStack createItem() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(getDisplayName());
        meta.setLore(Arrays.asList(
                "",
                ChatColor.GRAY + getPassiveDescription(),
                "",
                hasActivePower() 
                    ? ChatColor.YELLOW + "Clic droit pour utiliser le pouvoir"
                    : ChatColor.YELLOW + "Clic droit pour activer le passif",
                "",
                ChatColor.DARK_GRAY + DRAKE_NBT_KEY + ":" + getId()
        ));

        item.setItemMeta(meta);
        return item;
    }

    /**
     * Vérifie si un item est un item drake.
     */
    public static boolean isDrakeItem(ItemStack item) {
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasLore()) {
            return false;
        }
        for (String line : item.getItemMeta().getLore()) {
            if (line.contains(DRAKE_NBT_KEY)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Extrait l'ID du drake depuis un item.
     */
    public static String getDrakeIdFromItem(ItemStack item) {
        if (!isDrakeItem(item)) return null;

        for (String line : item.getItemMeta().getLore()) {
            if (line.contains(DRAKE_NBT_KEY)) {
                String[] parts = line.split(":");
                if (parts.length >= 2) {
                    return parts[1];
                }
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Drake)) return false;
        return getId().equals(((Drake) obj).getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return "Drake{" + getName() + ", " + getEntityType() + ", " + getHearts() + " hearts}";
    }
}
