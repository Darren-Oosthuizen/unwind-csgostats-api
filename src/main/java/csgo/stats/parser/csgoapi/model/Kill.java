package csgo.stats.parser.csgoapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Kill {
    private String killer;
    private String killerSteamId;
    private String killerTeamName;
    private String killed;
    private String killedSteamId;
    private String killedTeamName;
    private String gun;
    private Boolean headshot;
    private Integer time;
    private Boolean penetrated;
    private Boolean throughSmoke;
    private Boolean attackerBlind;
    private Boolean noscope;
    private Boolean assisted;
    private Boolean flashAssist;
    private String assisterSteamId;
    private String assisterName;
    private String assisterTeamName;
    private Boolean friendly;
}
