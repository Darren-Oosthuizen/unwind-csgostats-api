package csgo.stats.parser.csgoapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Player {

    private Integer userid;
    private String steamid;
    private String team;
    private String name;
    private Integer kills;
    private Integer teamKills;
    private Integer headshots;
    private Integer wallbangKills;
    private Integer smokeKills;
    private Integer utilityDamage;
    private Integer teamUtilityDamage;
    private Integer killsWhileFlashed;
    private Integer assists;
    private Integer deaths;
    private Integer damage;
    private Integer teamDamage;
    private Integer enemiesFlashed;
    private Integer teammatesFlashed;
    private Double flashDuration;
    private Double teamFlashDuration;
    private Integer flashAssists;
    private Integer mvp;
    private Integer bombsPlanted;
    private Integer bombsDefused;
    private List<Gun> gunKills;
    private Integer fallDamage;
    private Integer  killReward;
    private Integer noScopeKills;
}
