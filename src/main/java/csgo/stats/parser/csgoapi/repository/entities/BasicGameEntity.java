package csgo.stats.parser.csgoapi.repository.entities;

import java.util.Date;
import java.util.List;

public interface BasicGameEntity {
    Long getId();
    String getName();
    String getMap();
    String getCTScore();
    String getTScore();
    Date getDate();
    List<GameTeamEntity> getTeams();
}
