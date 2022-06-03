package csgo.stats.parser.csgoapi.services;

import csgo.stats.parser.csgoapi.mappers.GameEntityMapper;
import csgo.stats.parser.csgoapi.model.Game;
import csgo.stats.parser.csgoapi.model.response.MatchHistory;
import csgo.stats.parser.csgoapi.repository.IGameEntityRepository;
import csgo.stats.parser.csgoapi.repository.entities.BasicGameEntity;
import csgo.stats.parser.csgoapi.repository.entities.GameEntity;
import csgo.stats.parser.csgoapi.repository.entities.GameTeamEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private IGameEntityRepository gameEntityRepository;
    private PlayerService playerService;

    @Autowired
    public GameService(IGameEntityRepository gameEntityRepository, PlayerService playerService) {
        this.gameEntityRepository = gameEntityRepository;
        this.playerService = playerService;
    }

    public void saveGameEntity(GameEntity game) {
        gameEntityRepository.save(game);
    }

    public List<GameEntity> getAllGames(Integer limit) {
        return this.gameEntityRepository.findAllByOrderByIdDesc();
    }

    public List<MatchHistory> getAllGamesForClanName(String clanName, Integer limit) {
        List<BasicGameEntity> gameEntityList = gameEntityRepository.findAllByTeamsClanOrderByDateAsc(clanName);

        List<MatchHistory> matchHistoryList = new ArrayList<>();
        MatchHistory matchHistory;
        for (BasicGameEntity game : gameEntityList) {
            matchHistory = new MatchHistory();
            matchHistory.setTeam1Score(game.getTeams().get(0).getScore());
            matchHistory.setTeam1Name(game.getTeams().get(0).getClan());
            matchHistory.setTeam2Score(game.getTeams().get(1).getScore());
            matchHistory.setTeam2Name(game.getTeams().get(1).getClan());
            matchHistory.setMap(game.getMap());
            matchHistory.setDate(game.getDate());
            matchHistory.setId(game.getId());

            for (GameTeamEntity team : game.getTeams()) {
                if (team.getClan().equalsIgnoreCase(clanName)) {
                    matchHistory.setResult(team.getResult());
                }
            }
            matchHistoryList.add(matchHistory);
        }
        return matchHistoryList;
    }

    public GameEntity getGameEntityById(Long id) {
        Optional<GameEntity> gameEntity = gameEntityRepository.findById(id);
        return gameEntity.get();
    }

    public GameEntity getGameEntity(String name) {
        GameEntity gameEntity = gameEntityRepository.findByName(name);
        return gameEntity;
    }

    public GameEntity createGameEntity(Game game) {
        //Map from Game -> GameEntity
        GameEntity gameEntity = GameEntityMapper.mapGameToGameEntity(game);

        //Set players
        gameEntity.setPlayers(playerService.getPlayersFromGameEntity(gameEntity));

        saveGameEntity(gameEntity);

        return gameEntity;
    }
}
