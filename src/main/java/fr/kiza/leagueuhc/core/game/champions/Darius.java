package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class Darius extends Champion {
    public Darius() {
        super(
                "Darius",
                "Main de Noxus",
                Category.FIGHTER,
                Region.NOXUS,
                Material.DIAMOND_AXE
        );
    }
}
