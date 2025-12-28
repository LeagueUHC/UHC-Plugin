package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class Quinn extends Champion {
    public Quinn() {
        super(
                "Quinn",
                "Ailes de Demacia",
                Category.MARKSMAN,
                Region.DEMACIA,
                Material.BOW
        );
    }
}
