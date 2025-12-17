package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotations.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class Kayn extends Champion {
    protected Kayn() {
        super(
                "Kayn",
                "Faucheur de l'ombre",
                Category.FIGHTER,
                Region.SOLITAIRE,
                Material.ANVIL
        );
    }
}
