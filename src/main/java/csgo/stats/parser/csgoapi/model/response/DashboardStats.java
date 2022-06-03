package csgo.stats.parser.csgoapi.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStats {

    private DashboardStatObject mostKills;
    private DashboardStatObject highestKD;
    private DashboardStatObject highestHS;
    private DashboardStatObject highestADR;
    private DashboardStatObject mostAssists;
    private DashboardStatObject mostDeaths;
    private DashboardStatObject mostFlashAssists;
    private DashboardStatObject mostEnemiesFlashed;
    private DashboardStatObject mostTeammatesFlashed;
    private DashboardStatObject mostUtilityDamage;
    private DashboardStatObject mostMvp;
    private DashboardStatObject mostWallbangKills;
    private DashboardStatObject mostKillsWhileFlashed;
    private DashboardStatObject mostSmokeKills;

    private DashboardStatObject mostTeamDamage;
    private DashboardStatObject mostTeamUtilityDamage;

    private DashboardStatObject mostFiveKillRounds;
    private DashboardStatObject mostFourKillRounds;
    private DashboardStatObject mostThreeKillRounds;
    private DashboardStatObject mostTwoKillRounds;

    private DashboardStatObject mostOneVOneClutches;
    private DashboardStatObject mostOneVTwoClutches;
    private DashboardStatObject mostOneVThreeClutches;
    private DashboardStatObject mostOneVFourClutches;
    private DashboardStatObject mostOneVFiveClutches;

    private DashboardStatObject highestClutchSuccess;

    private DashboardStatObject mostOneVOnes;
    private DashboardStatObject mostOneVTwos;
    private DashboardStatObject mostOneVThrees;
    private DashboardStatObject mostOneVFours;
    private DashboardStatObject mostOneVFives;


    private DashboardStatObject mostTeamKills;
    private DashboardStatObject highestTeamFlashDuration;
    private DashboardStatObject highestFlashDuration;

    private DashboardStatObject mostBombsDefused;
    private DashboardStatObject mostBombsPlanted;

    private DashboardStatObject mostFallDamage;
    private DashboardStatObject highestKillReward;
    private DashboardStatObject mostNoScopeKills;

}
