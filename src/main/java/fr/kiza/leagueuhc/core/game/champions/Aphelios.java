package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class Aphelios extends Champion {
    public Aphelios() {
        super(
                "Aphelios",
                "Arme des Lunaris",
                Category.MARKSMAN,
                Region.TARGON,
                Material.BOW
        );
    }
}
