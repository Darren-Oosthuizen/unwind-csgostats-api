package csgo.stats.parser.csgoapi.repository;

import csgo.stats.parser.csgoapi.repository.entities.PlayerKillsAndHeadshots;
import csgo.stats.parser.csgoapi.repository.entities.TeamPlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITeamPlayerEntityRepository extends JpaRepository<TeamPlayerEntity, Long> {

    List<PlayerKillsAndHeadshots> findTop5ByGunsNameAndIdInOrderByGunsKillsDescGunsDamageDesc(String name, List<Long> ids);
}
