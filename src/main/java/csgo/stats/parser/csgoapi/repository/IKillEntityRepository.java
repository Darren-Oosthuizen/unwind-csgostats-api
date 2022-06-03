package csgo.stats.parser.csgoapi.repository;

import csgo.stats.parser.csgoapi.repository.entities.KillEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IKillEntityRepository extends CrudRepository<KillEntity, Long> {

    List<KillEntity> findAllByKillerSteamId(String steamId);
}
