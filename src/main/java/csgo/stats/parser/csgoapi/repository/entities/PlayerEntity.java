package csgo.stats.parser.csgoapi.repository.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "PLAYER")
public class PlayerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "STEAM_ID", nullable = false, unique = true)
    private String steamID;

    @Column(name="CLAN_NAME")
    private String clanName;

    @Column(name = "NAME")
    private String name;
    @Column(name = "KILLS")
    private Integer kills;
    @Column(name = "TEAM_KILLS")
    private Integer teamKills;
    @Column(name = "HEADSHOTS")
    private Integer headshots;
    @Column(name = "WALLBANG_KILLS")
    private Integer wallbangKills;
    @Column(name = "SMOKE_KILLS")
    private Integer smokeKills;
    @Column(name = "UTILITY_DAMAGE")
    private Integer utilityDamage;
    @Column(name = "TEAM_UTILITY_DAMAGE")
    private Integer teamUtilityDamage;
    @Column(name = "KILLS_WHILE_FLASHED")
    private Integer killsWhileFlashed;
    @Column(name = "ASSISTS")
    private Integer assists;
    @Column(name = "DEATHS")
    private Integer deaths;
    @Column(name = "DAMAGE")
    private Long damage;
    @Column(name = "TEAM_DAMAGE")
    private Long teamDamage;
    @Column(name = "ENEMIES_FLASHED")
    private Integer enemiesFlashed;
    @Column(name = "TEAMMATES_FLASHED")
    private Integer teammatesFlashed;
    @Column(name = "FLASH_DURATION")
    private Double flashDuration;
    @Column(name = "TEAM_FLASH_DURATION")
    private Double teamFlashDuration;
    @Column(name = "FLASH_ASSISTS")
    private Integer flashAssists;
    @Column(name = "MVP")
    private Integer mvp;
    @Column(name = "TOTAL_GAMES")
    private Integer totalGames;
    @Column(name = "TOTAL_ROUNDS")
    private Integer totalRounds;
    @Column(name = "TOTAL_WINS")
    private Integer totalWins;
    @Column(name = "TOTAL_LOSSES")
    private Integer totalLosses;
    @Column(name = "TOTAL_DRAWS")
    private Integer totalDraws;
    @Column(name = "KD")
    private BigDecimal kd;
    @Column(name = "ADR")
    private Integer adr;
    @Column(name = "HEADSHOT_PERCENTAGE")
    private Integer headshotPercentage;
    @Column(name = "BOMBS_PLANTED")
    private Integer bombsPlanted;
    @Column(name = "BOMBS_DEFUSED", nullable = false)
    private Integer bombsDefused;
    @Column(name = "FALL_DAMAGE")
    private Integer fallDamage;
    @Column(name = "KILL_REWARD")
    private Integer killReward;
    @Column(name = "NO_SCOPE_KILLS")
    private Integer noScopeKills;

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

    //First Kill Success
    @Column(name = "FIRST_KILL_ATTEMPT")
    private Integer firstKillAttempt;
    @Column(name = "FIRST_KILL")
    private Integer firstKill;
    @Column(name = "FIRST_DEATH")
    private Integer firstDeath;

    @Column(name = "ENTRY_SUCCESS")
    private Integer entrySuccess;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("kills DESC")
    private List<PlayerGunEntity> guns;

    @ManyToMany
    @JsonIgnoreProperties({"players", "rounds"})
    private List<GameEntity> games;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("wins DESC")
    private List<PlayerMapEntity> maps;

}

