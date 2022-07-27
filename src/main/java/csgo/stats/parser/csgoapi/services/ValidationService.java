package csgo.stats.parser.csgoapi.services;

import csgo.stats.parser.csgoapi.mappers.GameEntityMapper;
import csgo.stats.parser.csgoapi.model.DuelStatisticsEntity;
import csgo.stats.parser.csgoapi.repository.IGameEntityRepository;
import csgo.stats.parser.csgoapi.repository.IPlayerEntityRepository;
import csgo.stats.parser.csgoapi.repository.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ValidationService {

    private IPlayerEntityRepository playerEntityRepository;
    private IGameEntityRepository gameEntityRepository;

    @Autowired
    public ValidationService(IPlayerEntityRepository playerEntityRepository,
                             IGameEntityRepository gameEntityRepository) {
        this.playerEntityRepository = playerEntityRepository;
        this.gameEntityRepository = gameEntityRepository;
    }

    public void validateHeadToHeadStatistics() {

        List<PlayerEntity> playerEntityList = playerEntityRepository.findAll();

        Map<String, DuelStatisticsEntity> playerDuels;

        for (PlayerEntity player : playerEntityList) {
            for (GameEntity game : player.getGames()) {
                playerDuels = new HashMap<>();
                for (RoundEntity round : game.getRounds()) {
                    for (KillEntity kill : round.getKills()) {
                        if (kill.getKillerSteamId().equalsIgnoreCase(player.getSteamID())) {
                            if (!playerDuels.containsKey(kill.getKilledSteamId())) {
                                DuelStatisticsEntity duel = new DuelStatisticsEntity();
                                duel.setTotalDuels(1);
                                duel.setTotalWonDuels(1);
                                playerDuels.put(kill.getKilledSteamId(), duel);
                            } else {
                                DuelStatisticsEntity duel = playerDuels.get(kill.getKilledSteamId());
                                duel.setTotalDuels(duel.getTotalDuels() + 1);
                                duel.setTotalWonDuels(duel.getTotalWonDuels() + 1);
                            }
                        } else if (kill.getKilledSteamId().equalsIgnoreCase(player.getSteamID())) {
                            if (!playerDuels.containsKey(kill.getKillerSteamId())) {
                                DuelStatisticsEntity duel = new DuelStatisticsEntity();
                                duel.setTotalDuels(1);
                                duel.setTotalLostDuels(1);
                                playerDuels.put(kill.getKillerSteamId(), duel);
                            } else {
                                DuelStatisticsEntity duel = playerDuels.get(kill.getKillerSteamId());
                                duel.setTotalDuels(duel.getTotalDuels() + 1);
                                duel.setTotalLostDuels(duel.getTotalLostDuels() + 1);
                            }
                        }
                    }
                }

                Map<String, DuelStatisticsEntity> duelStats = new HashMap<>();
                for (String key : playerDuels.keySet()) {
                    for (PlayerEntity playerEntity : playerEntityList) {
                        if (playerEntity.getSteamID().equalsIgnoreCase(key)) {
                            duelStats.put(playerEntity.getName(), playerDuels.get(key));
                        }
                    }
                }

                for (GameTeamEntity team : game.getTeams()) {
                    for (TeamPlayerEntity teamPlayer : team.getPlayers()) {
                        if (teamPlayer.getSteamID().equalsIgnoreCase(player.getSteamID())) {
                            teamPlayer.setPlayerDuels(duelStats);
                        }
                    }
                }
                gameEntityRepository.save(game);
            }
        }
    }

    public void validateGameWins() {
        List<GameEntity> gameEntityList = gameEntityRepository.findAll();

        for (GameEntity game : gameEntityList) {
            Integer ctScore = Integer.parseInt(game.getCTScore());
            Integer tScore = Integer.parseInt(game.getTScore());

            for (GameTeamEntity team : game.getTeams()) {
                if (team.getScore().equals(tScore) && tScore > ctScore) {
                    team.setResult(1);
                } else if (team.getScore().equals(ctScore) && ctScore > tScore) {
                    team.setResult(1);
                } else if (team.getScore().equals(tScore) && tScore < ctScore) {
                    team.setResult(-1);
                } else if (team.getScore().equals(ctScore) && ctScore < tScore) {
                    team.setResult(-1);
                }
            }
            gameEntityRepository.save(game);
        }
        List<PlayerEntity> playerEntityList = playerEntityRepository.findAll();
        for (PlayerEntity player : playerEntityList) {
            int wins = 0;
            int losses = 0;
            for (PlayerMapEntity map : player.getMaps()) {
                map.setWins(0);
                map.setLosses(0);
                map.setTotalGames(0);
                map.setDraws(0);
            }
            for (GameEntity gameEntity : player.getGames()) {
                for (GameTeamEntity team : gameEntity.getTeams()) {
                    for (TeamPlayerEntity teamPlayer : team.getPlayers()) {
                        if (teamPlayer.getSteamID().equalsIgnoreCase(player.getSteamID())) {
                            if (team.getResult().equals(1)) {
                                for (PlayerMapEntity map : player.getMaps()) {
                                    if (map.getName().equalsIgnoreCase(gameEntity.getMap())) {
                                        map.setWins(map.getWins() + 1);
                                        map.setTotalGames(map.getTotalGames() + 1);
                                    }
                                }
                                wins++;
                            } else {
                                for (PlayerMapEntity map : player.getMaps()) {
                                    if (map.getName().equalsIgnoreCase(gameEntity.getMap())) {
                                        map.setLosses(map.getLosses() + 1);
                                        map.setTotalGames(map.getTotalGames() + 1);
                                    }
                                }
                                losses++;
                            }
                            break;
                        }
                    }
                }
            }
            player.setTotalWins(wins);
            player.setTotalLosses(losses);
            player.setTotalGames(wins + losses);
            playerEntityRepository.save(player);
        }
    }

    public void validatePlayerTeams() {

        List<PlayerEntity> playerEntityList = playerEntityRepository.findAll();

        for (PlayerEntity player : playerEntityList) {
            for (GameEntity game : player.getGames()) {
                for (GameTeamEntity team : game.getTeams()) {
                    for (TeamPlayerEntity teamPlayer : team.getPlayers()) {
                        if (teamPlayer.getSteamID().equalsIgnoreCase(player.getSteamID())) {
                            player.setClanName(team.getClan());
                            break;
                        }
                    }
                }
            }
            playerEntityRepository.save(player);
        }
    }

    public void validateClutchForGame(Long id) {

        List<GameEntity> gameEntityList = gameEntityRepository.findAll();

        for (GameEntity gameEntity : gameEntityList) {
            for (RoundEntity round : gameEntity.getRounds()) {
                round.setClutch("");
            }
            for (GameTeamEntity team : gameEntity.getTeams()) {
                for (TeamPlayerEntity teamPlayer : team.getPlayers()) {
                    GameEntityMapper.setClutchAndMultiKillRounds(gameEntity, teamPlayer);
                }
            }
            gameEntityRepository.save(gameEntity);
        }

        List<PlayerEntity> playerEntityList = playerEntityRepository.findAll();

        for (PlayerEntity player : playerEntityList) {

            player.setOneKillRounds(0);
            player.setTwoKillRounds(0);
            player.setThreeKillRounds(0);
            player.setFourKillRounds(0);
            player.setFiveKillRounds(0);

            player.setOneVersusFive(0);
            player.setOneVersusFour(0);
            player.setOneVersusThree(0);
            player.setOneVersusTwo(0);
            player.setOneVersusOne(0);

            player.setOneVersusFiveClutched(0);
            player.setOneVersusFourClutched(0);
            player.setOneVersusThreeClutched(0);
            player.setOneVersusTwoClutched(0);
            player.setOneVersusOneClutched(0);

            player.setFirstKill(0);
            player.setFirstKillAttempt(0);
            player.setFirstDeath(0);
            for (GameEntity gameEntity : player.getGames()) {
                for (GameTeamEntity team : gameEntity.getTeams()) {
                    for (TeamPlayerEntity teamPlayer : team.getPlayers()) {
                        if (teamPlayer.getSteamID().equalsIgnoreCase(player.getSteamID())) {
                            player.setOneKillRounds(player.getOneKillRounds() + teamPlayer.getOneKillRounds());
                            player.setTwoKillRounds(player.getTwoKillRounds() + teamPlayer.getTwoKillRounds());
                            player.setThreeKillRounds(player.getThreeKillRounds() + teamPlayer.getThreeKillRounds());
                            player.setFourKillRounds(player.getFourKillRounds() + teamPlayer.getFourKillRounds());
                            player.setFiveKillRounds(player.getFiveKillRounds() + teamPlayer.getFiveKillRounds());

                            player.setOneVersusFive(player.getOneVersusFive() + teamPlayer.getOneVersusFive());
                            player.setOneVersusFour(player.getOneVersusFour() + teamPlayer.getOneVersusFour());
                            player.setOneVersusThree(player.getOneVersusThree() + teamPlayer.getOneVersusThree());
                            player.setOneVersusTwo(player.getOneVersusTwo() + teamPlayer.getOneVersusTwo());
                            player.setOneVersusOne(player.getOneVersusOne() + teamPlayer.getOneVersusOne());

                            player.setOneVersusFiveClutched(player.getOneVersusFiveClutched() + teamPlayer.getOneVersusFiveClutched());
                            player.setOneVersusFourClutched(player.getOneVersusFourClutched() + teamPlayer.getOneVersusFourClutched());
                            player.setOneVersusThreeClutched(player.getOneVersusThreeClutched() + teamPlayer.getOneVersusThreeClutched());
                            player.setOneVersusTwoClutched(player.getOneVersusTwoClutched() + teamPlayer.getOneVersusTwoClutched());
                            player.setOneVersusOneClutched(player.getOneVersusOneClutched() + teamPlayer.getOneVersusOneClutched());

                            player.setFirstKill(player.getFirstKill() + teamPlayer.getFirstKill());
                            player.setFirstKillAttempt(player.getFirstKillAttempt() + teamPlayer.getFirstKillAttempt());
                            player.setFirstDeath(player.getFirstDeath() + teamPlayer.getFirstDeath());
                            Integer clutchAttempts = player.getOneVersusFive() + player.getOneVersusFour() + player.getOneVersusThree() + player.getOneVersusTwo() + player.getOneVersusOne();
                            Integer clutches = player.getOneVersusFiveClutched() + player.getOneVersusFourClutched() + player.getOneVersusThreeClutched() + player.getOneVersusTwoClutched() + player.getOneVersusOneClutched();
                            if (clutchAttempts == 0) {
                                player.setClutchPercentage(0);
                            } else {
                                player.setClutchPercentage(new BigDecimal(clutches).divide(new BigDecimal(clutchAttempts), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).intValueExact());
                            }
                        }
                    }
                }
            }
            Integer clutchAttempts = player.getOneVersusFive() + player.getOneVersusFour() + player.getOneVersusThree() + player.getOneVersusTwo() + player.getOneVersusOne();
            Integer clutches = player.getOneVersusFiveClutched() + player.getOneVersusFourClutched() + player.getOneVersusThreeClutched() + player.getOneVersusTwoClutched() + player.getOneVersusOneClutched();
            if (clutchAttempts == 0) {
                player.setClutchPercentage(0);
            } else {
                player.setClutchPercentage(new BigDecimal(clutches).divide(new BigDecimal(clutchAttempts), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).intValueExact());
            }
            if (player.getFirstKillAttempt() < 1) {
                player.setEntrySuccess(0);
            } else {
                player.setEntrySuccess(new BigDecimal(player.getFirstKill()).divide(new BigDecimal(player.getFirstKillAttempt()), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).intValueExact());
            }
            playerEntityRepository.save(player);
        }
    }

    public void validateTeamDamage() {

        List<PlayerEntity> playerEntityList = playerEntityRepository.findAll();

        for (PlayerEntity player : playerEntityList) {
            player.setTeamDamage(0L);
        }

        for (PlayerEntity player : playerEntityList) {
            Long totalTeamDamage = 0L;
            Integer teamKills = 0;
            for (GameEntity game : player.getGames()) {
                for (GameTeamEntity team : game.getTeams()) {
                    for (TeamPlayerEntity teamPlayer : team.getPlayers()) {
                        teamPlayer.setTeamDamageDone(0);
                        teamPlayer.setTeamKills(0);
                        if (teamPlayer.getSteamID().equalsIgnoreCase(player.getSteamID())) {
                            for (RoundEntity round : game.getRounds()) {
                                for (KillEntity kill : round.getKills()) {
                                    if (kill.getKillerSteamId().equalsIgnoreCase(teamPlayer.getSteamID()) && kill.getFriendly()) {
                                        teamKills++;
                                    }
                                }
                                for (RoundDamageEntity roundDamage : round.getDamage()) {
                                    if (roundDamage.getAttackerSteamId().equalsIgnoreCase(teamPlayer.getSteamID()) && roundDamage.getFriendly()) {
                                        teamPlayer.setTeamDamageDone(teamPlayer.getTeamDamageDone() + roundDamage.getDamage());
                                        totalTeamDamage += roundDamage.getDamage();
                                    }
                                }
                            }
                        }
                    }
                }
            }
            player.setTeamDamage(totalTeamDamage);
            player.setTeamKills(teamKills);
            playerEntityRepository.save(player);
        }
    }

    public void validateKnifeKills() {

        List<PlayerEntity> playerEntityList = playerEntityRepository.findAll();

        for (PlayerEntity player : playerEntityList) {
            int knifeKills = 0;
            int knifeDamage = 0;
            boolean foundKnife = false;
            for (PlayerGunEntity gun : player.getGuns()) {
                if (gun.getName().equalsIgnoreCase("knife")) {
                    foundKnife = true;
                    gun.setKills(knifeKills);
                    gun.setDamage((long) knifeDamage);
                }
            }

            if (!foundKnife) {
                PlayerGunEntity gun = new PlayerGunEntity();
                gun.setName("knife");
                gun.setKills(0);
                gun.setHeadshots(0);
                gun.setDamage(0L);
                player.getGuns().add(gun);
            }

            for (GameEntity game : player.getGames()) {
                for (GameTeamEntity team : game.getTeams()) {
                    for (TeamPlayerEntity teamPlayer : team.getPlayers()) {
                        if (teamPlayer.getSteamID().equalsIgnoreCase(player.getSteamID())) {
                            for (PlayerGameGunEntity gun : teamPlayer.getGuns()) {
                                if (gun.getName().toLowerCase().contains("knife")) {
                                    knifeKills += gun.getKills();
                                    knifeDamage += gun.getDamage();
                                }
                            }
                        }
                    }
                }
            }

            for (PlayerGunEntity gun : player.getGuns()) {
                if (gun.getName().equalsIgnoreCase("knife")) {
                    gun.setKills(knifeKills);
                    gun.setDamage((long) knifeDamage);
                }
            }
            playerEntityRepository.save(player);
        }
    }

    public void validate(Long id) {
        List<PlayerEntity> playerList = playerEntityRepository.findAll();
        List<GameEntity> gameList = gameEntityRepository.findAll();

        for (PlayerEntity player : playerList) {
            for (GameEntity game : player.getGames()) {
                for (GameTeamEntity team : game.getTeams()) {
                    for (TeamPlayerEntity teamPlayer : team.getPlayers()) {
                        if (teamPlayer.getSteamID().equalsIgnoreCase(player.getSteamID())) {
                            teamPlayer.setFirstKill(0);
                            teamPlayer.setFirstDeath(0);
                            teamPlayer.setFirstKillAttempt(0);
                        }
                    }
                }
            }
            player.setFirstKill(0);
            player.setFirstDeath(0);
            player.setFirstKillAttempt(0);
            player.setFiveKillRounds(0);
            player.setFourKillRounds(0);
            player.setThreeKillRounds(0);
            player.setTwoKillRounds(0);
            player.setOneKillRounds(0);
        }

        for (GameEntity game : gameList) {
            //Loop over rounds to check first kill and death
            for (RoundEntity round : game.getRounds()) {
                int index = 0;
                for (KillEntity kill : round.getKills()) {
                    if (index == 0) {
                        index++;
                        String firstKill = kill.getKillerSteamId();
                        String firstDeath = kill.getKilledSteamId();
                        //Increment firstKill and firstDeath player

                        for (GameTeamEntity team : game.getTeams()) {
                            for (TeamPlayerEntity teamPlayer : team.getPlayers()) {
                                if (teamPlayer.getSteamID().equalsIgnoreCase(firstKill)) {
                                    teamPlayer.setFirstKillAttempt(teamPlayer.getFirstKillAttempt() + 1);
                                    teamPlayer.setFirstKill(teamPlayer.getFirstKill() + 1);
                                }

                                if (teamPlayer.getSteamID().equalsIgnoreCase(firstDeath)) {
                                    teamPlayer.setFirstKillAttempt(teamPlayer.getFirstKillAttempt() + 1);
                                    teamPlayer.setFirstDeath(teamPlayer.getFirstDeath() + 1);
                                }
                            }
                        }
                    }
                }
            }
            gameEntityRepository.save(game);
        }

        for (PlayerEntity player : playerList) {
            int firstKills = 0;
            int firstDeaths = 0;
            int firstKillAttempt = 0;
            for (GameEntity game : player.getGames()) {
                for (GameTeamEntity team : game.getTeams()) {
                    for (TeamPlayerEntity teamPlayer : team.getPlayers()) {
                        if (teamPlayer.getSteamID().equalsIgnoreCase(player.getSteamID())) {
                            firstKills += teamPlayer.getFirstKill();
                            firstKillAttempt += teamPlayer.getFirstKillAttempt();
                            firstDeaths += teamPlayer.getFirstDeath();
                        }
                    }
                }

                for (RoundEntity round : game.getRounds()) {
                    int kills = 0;
                    for (KillEntity kill : round.getKills()) {
                        if (kill.getKillerSteamId().equalsIgnoreCase(player.getSteamID())) {
                            if (!kill.getKilledTeamName().equalsIgnoreCase(kill.getKillerTeamName())) {
                                kills++;
                            }
                        }
                    }
                    switch (kills) {
                        case 1: {
                            player.setOneKillRounds(player.getOneKillRounds() + 1);
                            break;
                        }
                        case 2: {
                            player.setTwoKillRounds(player.getTwoKillRounds() + 1);
                            break;
                        }
                        case 3: {
                            player.setThreeKillRounds(player.getThreeKillRounds() + 1);
                            break;
                        }
                        case 4: {
                            player.setFourKillRounds(player.getFourKillRounds() + 1);
                            break;
                        }
                        case 5: {
                            player.setFiveKillRounds(player.getFiveKillRounds() + 1);
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                }
            }
            player.setFirstKill(firstKills);
            player.setFirstDeath(firstDeaths);
            player.setFirstKillAttempt(firstKillAttempt);
            playerEntityRepository.save(player);
        }
    }
}
