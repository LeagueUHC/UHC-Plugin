package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class Swain extends Champion {
    public Swain() {
        super(
                "Swain",
                "Grand général Noxien",
                Category.FIGHTER,
                Region.NOXUS,
                Material.LAVA_BUCKET
        );
    }
}
