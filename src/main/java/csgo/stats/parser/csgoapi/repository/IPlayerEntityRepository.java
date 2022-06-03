package csgo.stats.parser.csgoapi.repository;

import csgo.stats.parser.csgoapi.repository.entities.BasicPlayer;
import csgo.stats.parser.csgoapi.repository.entities.PlayerEntity;
import csgo.stats.parser.csgoapi.repository.entities.PlayerKillsAndHeadshots;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPlayerEntityRepository extends JpaRepository<PlayerEntity, Long> {
    PlayerEntity findByName(String name);

    PlayerEntity findFirstByOrderByKillsDesc();

    PlayerEntity findFirstByOrderByDeathsDesc();

    PlayerEntity findFirstByOrderByAssistsDesc();

    PlayerEntity findFirstByOrderByMvpDesc();

    PlayerEntity findFirstByOrderByEnemiesFlashedDesc();

    PlayerEntity findFirstByOrderByTeammatesFlashedDesc();

    PlayerEntity findFirstByOrderByUtilityDamageDesc();

    PlayerEntity findFirstByOrderByFlashAssistsDesc();

    PlayerEntity findFirstByOrderByKdDesc();

    PlayerEntity findFirstByOrderByAdrDesc();

    PlayerEntity findFirstByOrderByHeadshotPercentageDescKillsDesc();

    PlayerEntity findFirstByOrderByFiveKillRoundsDescKillsDesc();

    PlayerEntity findFirstByOrderByFourKillRoundsDescKillsDesc();

    PlayerEntity findFirstByOrderByThreeKillRoundsDescKillsDesc();

    PlayerEntity findFirstByOrderByTwoKillRoundsDescKillsDesc();

    PlayerEntity findFirstByOrderByTeamDamageDesc();

    PlayerEntity findFirstByOrderByTeamUtilityDamageDesc();

    PlayerEntity findFirstByOrderByTeamFlashDurationDesc();

    PlayerEntity findFirstByOrderByFlashDurationDesc();

    PlayerEntity findFirstByOrderByTeamKillsDesc();

    PlayerEntity findFirstByOrderByBombsPlantedDescTotalRoundsDesc();

    PlayerEntity findFirstByOrderByBombsDefusedDescTotalRoundsDesc();

    PlayerEntity findFirstByOrderByOneVersusOneClutchedDescOneVersusOneAsc();

    PlayerEntity findFirstByOrderByOneVersusTwoClutchedDescOneVersusTwoAsc();

    PlayerEntity findFirstByOrderByOneVersusThreeClutchedDescOneVersusThreeAsc();

    PlayerEntity findFirstByOrderByOneVersusFourClutchedDescOneVersusFourAsc();

    PlayerEntity findFirstByOrderByOneVersusFiveClutchedDescOneVersusFiveAsc();

    PlayerEntity findFirstByOrderByOneVersusOneDesc();

    PlayerEntity findFirstByOrderByOneVersusTwoDesc();

    PlayerEntity findFirstByOrderByOneVersusThreeDesc();

    PlayerEntity findFirstByOrderByOneVersusFourDesc();

    PlayerEntity findFirstByOrderByOneVersusFiveDesc();
    PlayerEntity findFirstByOrderByNoScopeKillsDesc();
    PlayerEntity findFirstByOrderByKillRewardDesc();
    PlayerEntity findFirstByOrderByFallDamageDesc();
    PlayerEntity findFirstByOrderBySmokeKillsDesc();
    PlayerEntity findFirstByOrderByKillsWhileFlashedDesc();
    PlayerEntity findFirstByOrderByWallbangKillsDesc();

    PlayerEntity findBySteamID(String steamID);

    List<PlayerEntity> findAllByOrderByKillsDesc();
    List<BasicPlayer> findAllByClanNameOrderByTotalRoundsDesc(String clanName);

    List<PlayerKillsAndHeadshots> findByGunsNameOrderByGunsKillsDesc(String name);
    List<PlayerKillsAndHeadshots> findTop5ByGunsNameOrderByGunsKillsDescGunsDamageDesc(String name);

}
