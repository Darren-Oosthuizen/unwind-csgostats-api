package csgo.stats.parser.csgoapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Flashbang {
    private String attackerSteamId;
    private String victimSteamId;
    private Double duration;
    private Boolean friendly;
}
