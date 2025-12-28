package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class AurelionSol extends Champion {
    public AurelionSol() {
        super(
                "Aurelion Sol",
                "Le forgeron stellaire",
                Category.MAGE,
                Region.TARGON,
                Material.NETHER_STAR
        );
    }
}
