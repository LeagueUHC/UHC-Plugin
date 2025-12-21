package fr.kiza.leagueuhc.core.api.drake;

import fr.kiza.leagueuhc.core.api.drake.annotation.DrakeEntry;
import fr.kiza.leagueuhc.utils.ClassScanner;
import org.bukkit.Bukkit;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Registre central pour tous les drakes.
 * Gère l'enregistrement automatique et manuel des drakes.
 */
public final class DrakeRegistry {

    private static final Map<String, Drake> REGISTERED = new LinkedHashMap<>();
    private static boolean initialized = false;

    private DrakeRegistry() {}

    /**
     * Initialise automatiquement tous les drakes trouvés dans le package donné.
     * Les classes doivent être annotées avec @DrakeEntry et hériter de Drake.
     *
     * @param basePackage Le package racine à scanner
     */
    public static void initialize(String basePackage) {
        if (initialized) {
            Bukkit.getLogger().warning("DrakeRegistry already initialized!");
            return;
        }

        Bukkit.getLogger().info("Initializing DrakeRegistry from package: " + basePackage);
        List<DrakeWithPriority> drakes = new ArrayList<>();

        try {
            for (Class<?> clazz : ClassScanner.findClasses(basePackage)) {
                if (!Drake.class.isAssignableFrom(clazz)) continue;
                if (!clazz.isAnnotationPresent(DrakeEntry.class)) continue;
                if (Modifier.isAbstract(clazz.getModifiers())) continue;

                try {
                    Drake drake = (Drake) clazz.getDeclaredConstructor().newInstance();
                    DrakeEntry entry = clazz.getAnnotation(DrakeEntry.class);
                    drakes.add(new DrakeWithPriority(drake, entry.priority(), entry.excludeFromSpawn()));
                } catch (Exception e) {
                    Bukkit.getLogger().log(Level.WARNING, "Failed to instantiate drake: " + clazz.getName(), e);
                }
            }
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "Error scanning for drakes", e);
        }

        // Trier par priorité et enregistrer
        drakes.sort(Comparator.comparingInt(d -> d.priority));
        for (DrakeWithPriority dwp : drakes) {
            register(dwp.drake);
        }

        initialized = true;
        Bukkit.getLogger().info("DrakeRegistry initialized with " + REGISTERED.size() + " drakes");
    }

    /**
     * Enregistre manuellement un drake.
     */
    public static void register(Drake drake) {
        String key = drake.getId();

        if (REGISTERED.containsKey(key)) {
            Bukkit.getLogger().warning("Drake already registered: " + drake.getName() + " (overwriting)");
        }

        REGISTERED.put(key, drake);
        Bukkit.getLogger().info("Registered drake: " + drake.getName() + 
                " [" + drake.getEntityType() + ", " + drake.getHearts() + " hearts]");
    }

    /**
     * Désenregistre un drake.
     */
    public static void unregister(String id) {
        Drake removed = REGISTERED.remove(id.toLowerCase());
        if (removed != null) {
            Bukkit.getLogger().info("Unregistered drake: " + removed.getName());
        }
    }

    /**
     * Récupère un drake par son ID.
     */
    public static Drake getDrake(String id) {
        if (id == null) return null;
        return REGISTERED.get(id.toLowerCase());
    }

    /**
     * Récupère un drake par nom de classe.
     */
    public static <T extends Drake> T getDrakeByClass(Class<T> clazz) {
        for (Drake drake : REGISTERED.values()) {
            if (clazz.isInstance(drake)) {
                return clazz.cast(drake);
            }
        }
        return null;
    }

    /**
     * Vérifie si un drake est enregistré.
     */
    public static boolean isRegistered(String id) {
        return id != null && REGISTERED.containsKey(id.toLowerCase());
    }

    /**
     * Renvoie une vue non modifiable de tous les drakes enregistrés.
     */
    public static Collection<Drake> getDrakes() {
        return Collections.unmodifiableCollection(REGISTERED.values());
    }

    /**
     * Renvoie les drakes qui peuvent spawn automatiquement.
     * (exclut ceux avec excludeFromSpawn = true)
     */
    public static List<Drake> getSpawnableDrakes() {
        List<Drake> result = new ArrayList<>();
        for (Drake drake : REGISTERED.values()) {
            DrakeEntry entry = drake.getClass().getAnnotation(DrakeEntry.class);
            if (entry == null || !entry.excludeFromSpawn()) {
                result.add(drake);
            }
        }
        return result;
    }

    /**
     * Renvoie les drakes ayant un pouvoir actif.
     */
    public static List<Drake> getActivePowerDrakes() {
        return REGISTERED.values().stream()
                .filter(Drake::hasActivePower)
                .collect(Collectors.toList());
    }

    /**
     * Renvoie la liste triée des IDs de drakes enregistrés.
     */
    public static List<String> getRegisteredIds() {
        return new ArrayList<>(REGISTERED.keySet());
    }

    /**
     * Renvoie la liste triée des noms de drakes enregistrés.
     */
    public static List<String> getRegisteredNames() {
        return REGISTERED.values().stream()
                .map(Drake::getName)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }

    /**
     * Renvoie les IDs filtrés par une chaîne de recherche.
     */
    public static List<String> getMatchingIds(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return getRegisteredIds();
        }

        String lowerPrefix = prefix.toLowerCase();
        return REGISTERED.keySet().stream()
                .filter(id -> id.startsWith(lowerPrefix))
                .collect(Collectors.toList());
    }

    /**
     * Renvoie un drake aléatoire parmi les spawnables.
     */
    public static Drake getRandomDrake() {
        List<Drake> spawnable = getSpawnableDrakes();
        if (spawnable.isEmpty()) return null;
        return spawnable.get(new Random().nextInt(spawnable.size()));
    }

    /**
     * Renvoie des drakes aléatoires uniques parmi les spawnables.
     */
    public static List<Drake> getRandomDrakes(int count) {
        List<Drake> drakes = new ArrayList<>(getSpawnableDrakes());
        Collections.shuffle(drakes);
        return drakes.subList(0, Math.min(count, drakes.size()));
    }

    /**
     * Renvoie le nombre de drakes enregistrés.
     */
    public static int getCount() {
        return REGISTERED.size();
    }

    /**
     * Nettoie le registre.
     */
    public static void clear() {
        REGISTERED.clear();
        initialized = false;
        Bukkit.getLogger().info("DrakeRegistry cleared");
    }

    private static class DrakeWithPriority {
        final Drake drake;
        final int priority;
        final boolean excludeFromSpawn;

        DrakeWithPriority(Drake drake, int priority, boolean excludeFromSpawn) {
            this.drake = drake;
            this.priority = priority;
            this.excludeFromSpawn = excludeFromSpawn;
        }
    }
}
