package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class Vayne extends Champion {
    public Vayne() {
        super(
                "Vayne",
                "Chasseresse nocturne",
                Category.MARKSMAN,
                Region.DEMACIA,
                Material.BOW
        );
    }
}
