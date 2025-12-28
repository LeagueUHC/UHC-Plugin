package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class Nautilus extends Champion {
    public Nautilus() {
        super(
                "Nautilus",
                "Titan des profondeurs",
                Category.TANK,
                Region.BILGEWATER,
                Material.DIAMOND_CHESTPLATE
        );
    }
}
