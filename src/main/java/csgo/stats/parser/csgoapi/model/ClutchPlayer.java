package csgo.stats.parser.csgoapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClutchPlayer {

    private String steamId;
    private String name;
    private String teamName;
    private Integer enemiesAlive;
}
