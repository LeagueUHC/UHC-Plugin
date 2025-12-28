package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class KaynAssassin extends Champion {
    public KaynAssassin() {
        super(
                "Kayn Assassin",
                "Faucheur de l'ombre",
                Category.ASSASSIN,
                Region.SOLITAIRE,
                Material.BOW
        );
    }
}
