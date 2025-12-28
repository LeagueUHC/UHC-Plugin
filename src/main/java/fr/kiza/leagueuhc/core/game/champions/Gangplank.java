package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class Gangplank extends Champion {
    public Gangplank() {
        super(
                "Gangplank",
                "Fléau des mers",
                Category.FIGHTER,
                Region.BILGEWATER,
                Material.TNT
        );
    }
}
