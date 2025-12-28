package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class Katarina extends Champion {
    public Katarina() {
        super(
                "Katarina",
                "Lame sinistre",
                Category.FIGHTER,
                Region.NOXUS,
                Material.DIAMOND_SWORD
        );
    }
}
