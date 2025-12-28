package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class Lucian extends Champion {
    public Lucian() {
        super(
                "Lucian",
                "Purificateur",
                Category.MARKSMAN,
                Region.DUO,
                Material.BOW
        );
    }
}
