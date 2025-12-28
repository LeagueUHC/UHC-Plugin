package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class Graves extends Champion {
    public Graves() {
        super(
                "Graves",
                "Hors-La-Loi",
                Category.MARKSMAN,
                Region.BILGEWATER,
                Material.BOW
        );
    }
}
