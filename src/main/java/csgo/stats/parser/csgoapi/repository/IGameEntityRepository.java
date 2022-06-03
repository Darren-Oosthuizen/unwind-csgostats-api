package csgo.stats.parser.csgoapi.repository;

import csgo.stats.parser.csgoapi.repository.entities.BasicGameEntity;
import csgo.stats.parser.csgoapi.repository.entities.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IGameEntityRepository extends JpaRepository<GameEntity, Long> {

    GameEntity findByName(String name);

    List<GameEntity> findAllByOrderByIdDesc();
    List<BasicGameEntity> findAllByTeamsClanOrderByDateAsc(String clan);
}
