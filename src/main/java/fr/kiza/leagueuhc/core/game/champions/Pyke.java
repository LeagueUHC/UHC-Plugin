package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class Pyke extends Champion {
    public Pyke() {
        super(
                "Pyke",
                "L'éventreur des abysses",
                Category.ASSASSIN,
                Region.BILGEWATER,
                Material.GOLD_INGOT
        );
    }
}
