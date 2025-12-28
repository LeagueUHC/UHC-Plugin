package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class Yone extends Champion {
    public Yone() {
        super(
                "Yone",
                "Inoublié",
                Category.FIGHTER,
                Region.DUO,
                Material.IRON_AXE
        );
    }
}
