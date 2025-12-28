package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class Shyvana extends Champion {
    public Shyvana() {
        super(
                "Shyvana",
                "Demi-dragon",
                Category.FIGHTER,
                Region.DEMACIA,
                Material.DRAGON_EGG
        );
    }
}
