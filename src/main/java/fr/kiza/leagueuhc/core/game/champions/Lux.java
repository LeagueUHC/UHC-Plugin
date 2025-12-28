package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class Lux extends Champion {
    public Lux() {
        super(
                "Lux",
                "Dame de lumière",
                Category.MAGE,
                Region.DEMACIA,
                Material.NETHER_STAR
        );
    }
}
