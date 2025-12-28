package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class SoldatDemacien extends Champion {
    public SoldatDemacien() {
        super(
                "Soldat Demacien",
                "Garde royale de Demacia",
                Category.SUPPORT,
                Region.DEMACIA,
                Material.DIRT
        );
    }
}
