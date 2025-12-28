package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class Cassiopeia extends Champion {
    public Cassiopeia() {
        super(
                "Cassiopeia",
                "Étreinte du serpent",
                Category.MAGE,
                Region.NOXUS,
                Material.NETHER_STAR
        );
    }
}
