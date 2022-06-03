package csgo.stats.parser.csgoapi.repository.entities;

import java.math.BigDecimal;

public interface BasicPlayer {
    Long getId();
    String getName();
    Integer getKills();
    Integer getDeaths();
    Integer getAssists();
    Integer getAdr();
    BigDecimal getKd();
    Integer getHeadshotPercentage();
    Integer getClutchPercentage();
    Integer getMvp();
    Integer getTotalWins();
    Integer getTotalGames();
    Integer getEntrySuccess();
    Integer getTotalRounds();
}
