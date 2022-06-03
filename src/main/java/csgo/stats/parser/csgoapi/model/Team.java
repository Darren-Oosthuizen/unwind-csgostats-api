package csgo.stats.parser.csgoapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Team {
    private List<Player> players;
    private Integer firstHalfScore;
    private Integer secondHalfScore;
    private Integer score;
    private Integer result;
    private String name;
}
