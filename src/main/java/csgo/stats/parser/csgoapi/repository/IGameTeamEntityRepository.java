package csgo.stats.parser.csgoapi.repository;

import csgo.stats.parser.csgoapi.repository.entities.ClanName;
import csgo.stats.parser.csgoapi.repository.entities.GameTeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IGameTeamEntityRepository extends JpaRepository<GameTeamEntity, Long> {

    @Query(value = "SELECT distinct (clan) as clanName from GameTeamEntity")
    List<ClanName> findDistinctClanNames();

    List<GameTeamEntity> findAllByClan(String clan);

}
