package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class Kindred extends Champion {
    public Kindred() {
        super(
                "Kindred",
                "Chasseurs éternels",
                Category.ASSASSIN,
                Region.SOLITAIRE,
                Material.BONE
        );
    }
}
