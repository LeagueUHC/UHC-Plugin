package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class Garen extends Champion {
    public Garen() {
        super(
                "Garen",
                "Force de Demacia",
                Category.FIGHTER,
                Region.DEMACIA,
                Material.DIAMOND_SWORD
        );
    }
}
