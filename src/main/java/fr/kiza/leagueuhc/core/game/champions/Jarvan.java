package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class Jarvan extends Champion {
    public Jarvan() {
        super(
                "Jarvan",
                "Exemple demacien",
                Category.FIGHTER,
                Region.DEMACIA,
                Material.IRON_HELMET
        );
    }
}
