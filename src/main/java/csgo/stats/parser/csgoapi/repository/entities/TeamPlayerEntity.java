package csgo.stats.parser.csgoapi.repository.entities;

import csgo.stats.parser.csgoapi.model.DuelStatisticsEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "TEAM_PLAYER")
public class TeamPlayerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "STEAM_ID")
    private String steamID;
    @Column(name = "GAME_ID")
    private Long gameID;
    @Column(name = "KILLS")
    private Integer kills;
    @Column(name = "TEAM_KILLS")
    private Integer teamKills;
    @Column(name = "DEATHS")
    private Integer deaths;
    @Column(name = "ASSISTS")
    private Integer assists;
    @Column(name = "HEADSHOTS")
    private Integer headshots;
    @Column(name = "KILLS_WHILE_FLASHED")
    private Integer killsWhileFlashed;
    @Column(name = "WALLBANG_KILLS")
    private Integer wallbangKills;
    @Column(name = "ENEMIES_FLASHED")
    private Integer enemiesFlashed;
    @Column(name = "TEAMMATES_FLASHED")
    private Integer teammatesFlashed;
    @Column(name = "FLASH_DURATION")
    private Double flashDuration;
    @Column(name = "TEAM_FLASH_DURATION")
    private Double teamFlashDuration;
    @Column(name = "TEAM_NAME")
    private String teamName;
    @Column(name = "NAME")
    private String name;
    @Column(name = "SMOKE_KILLS")
    private Integer smokeKills;
    @Column(name = "UTILITY_DAMAGE")
    private Integer utilityDamage;
    @Column(name = "TEAM_UTILITY_DAMAGE")
    private Integer teamUtilityDamage;
    @Column(name = "DAMAGE_DONE")
    private Integer damageDone;
    @Column(name = "TEAM_DAMAGE_DONE")
    private Integer teamDamageDone;
    @Column(name = "FLASH_ASSISTS")
    private Integer flashAssists;
    @Column(name = "MVP")
    private Integer mvp;
    @Column(name = "KD")
    private BigDecimal kd;
    @Column(name = "ADR")
    private Integer adr;
    @Column(name = "HEADSHOT_PERCENTAGE")
    private Integer headshotPercentage;
    @Column(name = "BOMBS_PLANTED")
    private Integer bombsPlanted;
    @Column(name = "BOMBS_DEFUSED")
    private Integer bombsDefused;
    @Column(name = "NO_SCOPE_KILLS")
    private Integer noScopeKills;
    @Column(name = "FALL_DAMAGE")
    private Integer fallDamage;
    @Column(name = "KILL_REWARD")
    private Integer killReward;

    //MultiKills
    @Column(name = "ONE_KILL_ROUNDS")
    private Integer oneKillRounds;
    @Column(name = "TWO_KILL_ROUNDS")
    private Integer twoKillRounds;
    @Column(name = "THREE_KILL_ROUNDS")
    private Integer threeKillRounds;
    @Column(name = "FOUR_KILL_ROUNDS")
    private Integer fourKillRounds;
    @Column(name = "FIVE_KILL_ROUNDS")
    private Integer fiveKillRounds;

    //Clutch Stats
    @Column(name = "ONE_VERSUS_FIVE")
    private Integer oneVersusFive;
    @Column(name = "ONE_VERSUS_FOUR")
    private Integer oneVersusFour;
    @Column(name = "ONE_VERSUS_THREE")
    private Integer oneVersusThree;
    @Column(name = "ONE_VERSUS_TWO")
    private Integer oneVersusTwo;
    @Column(name = "ONE_VERSUS_ONE")
    private Integer oneVersusOne;

    @Column(name = "ONE_VERSUS_FIVE_CLUTCHED")
    private Integer oneVersusFiveClutched;
    @Column(name = "ONE_VERSUS_FOUR_CLUTCHED")
    private Integer oneVersusFourClutched;
    @Column(name = "ONE_VERSUS_THREE_CLUTCHED")
    private Integer oneVersusThreeClutched;
    @Column(name = "ONE_VERSUS_TWO_CLUTCHED")
    private Integer oneVersusTwoClutched;
    @Column(name = "ONE_VERSUS_ONE_CLUTCHED")
    private Integer oneVersusOneClutched;

    @Column(name = "CLUTCH_PERCENTAGE")
    private Integer clutchPercentage;

    @Column(name = "FIRST_KILL_ATTEMPT")
    private Integer firstKillAttempt;
    @Column(name = "FIRST_KILL")
    private Integer firstKill;
    @Column(name = "FIRST_DEATH")
    private Integer firstDeath;

    @Column(name = "ENTRY_SUCCESS")
    private Integer entrySuccess;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("totalWonDuels DESC")
    private Map<String, DuelStatisticsEntity> playerDuels;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("kills DESC")
    private List<PlayerGameGunEntity> guns;
}

