package csgo.stats.parser.csgoapi.model.response;

import csgo.stats.parser.csgoapi.repository.entities.BasicPlayer;
import csgo.stats.parser.csgoapi.repository.entities.PlayerMapEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamResponse {
    private String teamName;
    private List<BasicPlayer> players;
    private Integer totalGames;
    private Integer totalWins;
    private Integer totalLosses;
    private Integer totalRounds;
    private Integer roundDifference;
    private Integer winPercentage;
    private List<MatchHistory> matchHistory;
    private List<PlayerMapEntity> maps;
}
