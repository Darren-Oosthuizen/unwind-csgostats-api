package csgo.stats.parser.csgoapi.services;

import csgo.stats.parser.csgoapi.model.response.MatchHistory;
import csgo.stats.parser.csgoapi.model.response.TeamResponse;
import csgo.stats.parser.csgoapi.repository.IGameEntityRepository;
import csgo.stats.parser.csgoapi.repository.IGameTeamEntityRepository;
import csgo.stats.parser.csgoapi.repository.IPlayerEntityRepository;
import csgo.stats.parser.csgoapi.repository.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {

    private IGameTeamEntityRepository gameTeamEntityRepository;
    private IGameEntityRepository gameEntityRepository;
    private IPlayerEntityRepository playerEntityRepository;
    private GameService gameService;

    @Autowired
    public TeamService(IGameTeamEntityRepository gameTeamEntityRepository,
                       IPlayerEntityRepository playerEntityRepository,
                       GameService gameService,
                       IGameEntityRepository gameEntityRepository) {
        this.gameTeamEntityRepository = gameTeamEntityRepository;
        this.playerEntityRepository = playerEntityRepository;
        this.gameService = gameService;
        this.gameEntityRepository = gameEntityRepository;
    }

    public TeamResponse getTeamByTeamName(String name) {
        TeamResponse team = new TeamResponse();

        team.setTeamName(name);
        List<BasicPlayer> playerEntityList = playerEntityRepository.findAllByClanNameOrderByTotalRoundsDesc(name);
        team.setPlayers(playerEntityList);
        setTeamGamesAndWins(team, name);
        team.setMatchHistory(gameService.getAllGamesForClanName(name, 10));

        List<BasicGameEntity> gameEntityList = gameEntityRepository.findAllByTeamsClanOrderByDateAsc(name);
        List<PlayerMapEntity> maps = new ArrayList<>();
        for (BasicGameEntity game : gameEntityList) {
            boolean found = false;
            for (PlayerMapEntity map : maps) {
                if (map.getName().equalsIgnoreCase(game.getMap())) {
                    map.setTotalGames(map.getTotalGames() + 1);
                    for (GameTeamEntity teamEntity : game.getTeams()) {
                        if (teamEntity.getClan().equalsIgnoreCase(name)) {
                            switch (teamEntity.getResult()) {
                                case -1 : {
                                    map.setLosses(1 + map.getLosses());
                                    break;
                                }

                                case 0 : {
                                    map.setDraws(1 + map.getDraws());
                                    break;
                                }
                                case 1 : {
                                    map.setWins(1 + map.getWins());
                                    break;
                                }
                            }
                        }
                    }
                    found = true;
                }
            }
            if (!found) {
                PlayerMapEntity map = new PlayerMapEntity();
                map.setName(game.getMap());
                map.setTotalGames(1);
                for (GameTeamEntity teamEntity : game.getTeams()) {
                    if (teamEntity.getClan().equalsIgnoreCase(name)) {
                        switch (teamEntity.getResult()) {
                            case -1 : {
                                map.setLosses(1);
                                map.setDraws(0);
                                map.setWins(0);
                                break;
                            }

                            case 0 : {
                                map.setLosses(0);
                                map.setDraws(1);
                                map.setWins(0);
                                break;
                            }
                            case 1 : {
                                map.setLosses(0);
                                map.setDraws(0);
                                map.setWins(1);
                                break;
                            }
                        }
                    }
                }
                maps.add(map);
            }
        }

        team.setMaps(maps);
        return team;
    }

    public List<TeamResponse> getAllTeams() {
        List<TeamResponse> teams = new ArrayList<>();
        TeamResponse team;
        List<ClanName> clanNameList = gameTeamEntityRepository.findDistinctClanNames();

        for (ClanName clanName : clanNameList) {
            team = new TeamResponse();
            List<BasicPlayer> playerEntityList = playerEntityRepository.findAllByClanNameOrderByTotalRoundsDesc(clanName.getClanName());
            team.setPlayers(playerEntityList);
            team.setTeamName(clanName.getClanName());
            setTeamGamesAndWins(team, clanName.getClanName());
            team.setRoundDifference(team.getRoundDifference() - (playerEntityList.get(0).getTotalRounds() - team.getRoundDifference()));
            List<MatchHistory> matchHistoryList = gameService.getAllGamesForClanName(clanName.getClanName(), 10);
            int totalRounds = 0;
            for (MatchHistory match : matchHistoryList) {
                totalRounds += match.getTeam1Score();
                totalRounds += match.getTeam2Score();
            }
            team.setTotalRounds(totalRounds);
            team.setMatchHistory(matchHistoryList);
            teams.add(team);
        }

        return teams;
    }

    private void setTeamGamesAndWins(TeamResponse team, String clanName) {
        List<GameTeamEntity> gameTeamEntities = gameTeamEntityRepository.findAllByClan(clanName);
        team.setTotalGames(gameTeamEntities.size());
        int wins = 0;
        int loss = 0;
        int roundsWon = 0;
        for (GameTeamEntity gameTeamEntity : gameTeamEntities) {
            switch (gameTeamEntity.getResult()) {
                case -1: {
                    loss++;
                    roundsWon += gameTeamEntity.getScore();
                    break;
                }
                case 1: {
                    wins++;
                    roundsWon += gameTeamEntity.getScore();
                    break;
                }
            }
        }
        team.setTotalWins(wins);
        team.setTotalLosses(loss);
        team.setRoundDifference(roundsWon);
        team.setWinPercentage(new BigDecimal(wins).divide(new BigDecimal(team.getTotalGames()), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).intValueExact());
    }
}
