package csgo.stats.parser.csgoapi.repository.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "KILL")
public class KillEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "KILLER")
    private String killerName;

    @Column(name = "KILLER_STEAMID")
    private String killerSteamId;

    @Column(name = "KILLER_TEAM_NAME")
    private String killerTeamName;

    @Column(name = "KILLED")
    private String killedName;

    @Column(name = "KILLED_STEAMID")
    private String killedSteamId;

    @Column(name = "KILLED_TEAM_NAME")
    private String killedTeamName;

    @Column(name = "GUN")
    private String gun;

    @Column(name = "HEADSHOT")
    private Boolean headshot;

    @Column(name = "FRIENDLY")
    private Boolean friendly;

    @Column(name = "TIME")
    private Integer time;
    @Column(name = "PENETRATED")
    private Boolean penetrated;
    @Column(name = "THROUGH_SMOKE")
    private Boolean throughSmoke;
    @Column(name = "ATTACKER_BLIND")
    private Boolean attackerBlind;
    @Column(name = "NOSCOPE")
    private Boolean noscope;
    @Column(name = "ASSISTED")
    private Boolean assisted;
    @Column(name = "FLASH_ASSIST")
    private Boolean flashAssist;
    @Column(name = "ASSISTER_STEAM_ID")
    private String assisterSteamId;
    @Column(name = "ASSISTER_NAME")
    private String assisterName;
    @Column(name = "ASSISTER_TEAM_NAME")
    private String assisterTeamName;
}
