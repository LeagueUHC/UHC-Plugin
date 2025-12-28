package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class Draven extends Champion {
    public Draven() {
        super(
                "Draven",
                "Glorieux exécuteur",
                Category.MARKSMAN,
                Region.NOXUS,
                Material.IRON_AXE
        );
    }
}
