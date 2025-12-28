package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class Sion extends Champion {
    public Sion() {
        super(
                "Sion",
                "Colosse mort-vivant",
                Category.TANK,
                Region.NOXUS,
                Material.DIAMOND_BOOTS
        );
    }
}
