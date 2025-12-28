package fr.kiza.leagueuhc.core.game.champions;

import fr.kiza.leagueuhc.core.api.champion.Champion;
import fr.kiza.leagueuhc.core.api.champion.annotation.ChampionEntry;
import org.bukkit.Material;

@ChampionEntry
public class XinZhao extends Champion {
    public XinZhao() {
        super(
                "Xin Zhao",
                "Sénéchal de Demacia",
                Category.FIGHTER,
                Region.DEMACIA,
                Material.IRON_AXE
        );
    }
}
