package csgo.stats.parser.csgoapi.repository;

import csgo.stats.parser.csgoapi.repository.entities.GunName;
import csgo.stats.parser.csgoapi.repository.entities.PlayerGunEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPlayerGunEntityRepository extends CrudRepository<PlayerGunEntity, Long> {

    @Query(value = "SELECT distinct (name) as name, max (kills) as maxkills from PlayerGunEntity group by name order by maxkills desc")
    List<GunName> findDistinctName();

    @Query(value = "SELECT distinct (name) as name, max (kills) as maxkills from PlayerGameGunEntity WHERE id IN (:ids) group by name order by maxkills desc")
    List<GunName> findDistinctNameTeamPlayer(List<Long> ids);
}
