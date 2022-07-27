package csgo.stats.parser.csgoapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Game {
    private String id;
    private Team team1;
    private Team team2;
    private Team spectators;
    private List<Round> rounds;
    private String map;
    private String ctscore;
    private String tscore;
    private Date date;
}
