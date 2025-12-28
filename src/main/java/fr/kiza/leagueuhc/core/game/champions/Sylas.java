package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class Sylas extends Champion {
    public Sylas() {
        super(
                "Sylas",
                "Révolutionnaire déchaîné",
                Category.ASSASSIN,
                Region.DEMACIA,
                Material.LAVA_BUCKET
        );
    }
}
