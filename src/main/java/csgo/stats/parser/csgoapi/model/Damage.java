package csgo.stats.parser.csgoapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Damage {
    private String attackerSteamId;
    private String weapon;
    private Integer damage;
    private Boolean friendly;
    private String victimSteamId;
    private Integer hitgroup;
}
