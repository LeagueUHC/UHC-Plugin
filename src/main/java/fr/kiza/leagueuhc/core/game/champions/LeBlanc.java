package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class LeBlanc extends Champion {
    public LeBlanc() {
        super(
                "LeBlanc",
                "Manipulatrice",
                Category.ASSASSIN,
                Region.NOXUS,
                Material.GLASS
        );
    }
}
