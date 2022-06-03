package csgo.stats.parser.csgoapi.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class MatchHistory {
    private Integer team1Score;
    private String team1Name;
    private String team2Name;
    private Integer team2Score;
    private Integer result;
    private String map;
    private Date date;
    private Long id;
}
