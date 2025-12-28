package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class Galio extends Champion {
    public Galio() {
        super(
                "Galio",
                "Colosse",
                Category.TANK,
                Region.DEMACIA,
                Material.BEDROCK
        );
    }
}
