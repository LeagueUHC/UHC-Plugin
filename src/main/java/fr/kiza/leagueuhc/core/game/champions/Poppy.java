package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class Poppy extends Champion {
    public Poppy() {
        super(
                "Poppy",
                "Gardienne du marteau",
                Category.TANK,
                Region.DEMACIA,
                Material.DIAMOND_CHESTPLATE
        );
    }
}
