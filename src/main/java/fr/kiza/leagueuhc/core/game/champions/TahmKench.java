package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class TahmKench extends Champion {
    public TahmKench() {
        super(
                "Tahm Kench",
                "Roi des rivières",
                Category.TANK,
                Region.BILGEWATER,
                Material.WATER_BUCKET
        );
    }
}
