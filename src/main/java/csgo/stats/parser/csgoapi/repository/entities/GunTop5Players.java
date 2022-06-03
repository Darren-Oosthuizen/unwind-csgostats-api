package csgo.stats.parser.csgoapi.repository.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GunTop5Players {
    private String name;
    private List<PlayerKillsAndHeadshots> players;

}
