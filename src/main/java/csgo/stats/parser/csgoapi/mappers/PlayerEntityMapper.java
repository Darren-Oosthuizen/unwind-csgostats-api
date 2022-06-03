package csgo.stats.parser.csgoapi.mappers;

import csgo.stats.parser.csgoapi.repository.entities.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class PlayerEntityMapper {

    public static final String TERRORISTS = "TERRORISTS";
    public static final String COUNTER_TERRORISTS = "Counter-Terrorists";

    public static PlayerEntity combinePlayerEntityInformation(PlayerEntity playerEntity,
                                                              TeamPlayerEntity player,
                                                              GameEntity game,
                                                              Integer teamScore) {
        playerEntity.setName(player.getName());
        playerEntity.setKills(playerEntity.getKills() + player.getKills());
        playerEntity.setTeamKills(playerEntity.getTeamKills() + player.getTeamKills());
        playerEntity.setDeaths(playerEntity.getDeaths() + player.getDeaths());
        playerEntity.setAssists(playerEntity.getAssists() + player.getAssists());
        playerEntity.setDamage(playerEntity.getDamage() + player.getDamageDone());
        playerEntity.setTeamDamage(playerEntity.getTeamDamage() + player.getTeamDamageDone());
        playerEntity.setEnemiesFlashed(playerEntity.getEnemiesFlashed() + player.getEnemiesFlashed());
        playerEntity.setFlashAssists(playerEntity.getFlashAssists() + player.getFlashAssists());
        playerEntity.setFlashDuration(playerEntity.getFlashDuration() + player.getFlashDuration());
        playerEntity.setTeamFlashDuration(playerEntity.getTeamFlashDuration() + player.getTeamFlashDuration());
        playerEntity.setHeadshots(playerEntity.getHeadshots() + player.getHeadshots());
        playerEntity.setKillsWhileFlashed(playerEntity.getKillsWhileFlashed() + player.getKillsWhileFlashed());
        playerEntity.setMvp(playerEntity.getMvp() + player.getMvp());
        playerEntity.setSmokeKills(playerEntity.getSmokeKills() + player.getSmokeKills());
        playerEntity.setWallbangKills(playerEntity.getWallbangKills() + player.getWallbangKills());
        playerEntity.setUtilityDamage(playerEntity.getUtilityDamage() + player.getUtilityDamage());
        playerEntity.setTeamUtilityDamage(playerEntity.getTeamUtilityDamage() + player.getTeamUtilityDamage());
        playerEntity.setTeammatesFlashed(playerEntity.getTeammatesFlashed() + player.getTeammatesFlashed());
        playerEntity.setBombsPlanted(playerEntity.getBombsPlanted() + player.getBombsPlanted());
        playerEntity.setBombsDefused(playerEntity.getBombsDefused() + player.getBombsDefused());
        if (playerEntity.getNoScopeKills() == null) {
            playerEntity.setNoScopeKills(player.getNoScopeKills());
        } else {
            playerEntity.setNoScopeKills(player.getNoScopeKills() + playerEntity.getNoScopeKills());
        }
        if (playerEntity.getKillReward() == null) {
            playerEntity.setKillReward(player.getKillReward());
        } else {
            playerEntity.setKillReward(playerEntity.getKillReward() + player.getKillReward());
        }
        if (playerEntity.getFallDamage() == null) {
            playerEntity.setFallDamage(player.getFallDamage());
        } else {
            playerEntity.setFallDamage(playerEntity.getFallDamage() + player.getFallDamage());
        }
        playerEntity.setFirstKillAttempt(playerEntity.getFirstKillAttempt() + player.getFirstKillAttempt());
        playerEntity.setFirstKill(playerEntity.getFirstKill() + player.getFirstKill());
        playerEntity.setFirstDeath(playerEntity.getFirstDeath() + player.getFirstDeath());
        playerEntity.setOneVersusOne(playerEntity.getOneVersusOne() + player.getOneVersusOne());
        playerEntity.setOneVersusTwo(playerEntity.getOneVersusTwo() + player.getOneVersusTwo());
        playerEntity.setOneVersusThree(playerEntity.getOneVersusThree() + player.getOneVersusThree());
        playerEntity.setOneVersusFour(playerEntity.getOneVersusFour() + player.getOneVersusFour());
        playerEntity.setOneVersusFive(playerEntity.getOneVersusFive() + player.getOneVersusFive());
        playerEntity.setOneVersusOneClutched(playerEntity.getOneVersusOneClutched() + player.getOneVersusOneClutched());
        playerEntity.setOneVersusTwoClutched(playerEntity.getOneVersusTwoClutched() + player.getOneVersusTwoClutched());
        playerEntity.setOneVersusThreeClutched(playerEntity.getOneVersusThreeClutched() + player.getOneVersusThreeClutched());
        playerEntity.setOneVersusFourClutched(playerEntity.getOneVersusFourClutched() + player.getOneVersusFourClutched());
        playerEntity.setOneVersusFiveClutched(playerEntity.getOneVersusFiveClutched() + player.getOneVersusFiveClutched());
        playerEntity.setOneKillRounds(playerEntity.getOneKillRounds() + player.getOneKillRounds());
        playerEntity.setTwoKillRounds(playerEntity.getTwoKillRounds() + player.getTwoKillRounds());
        playerEntity.setThreeKillRounds(playerEntity.getThreeKillRounds() + player.getThreeKillRounds());
        playerEntity.setFourKillRounds(playerEntity.getFourKillRounds() + player.getFourKillRounds());
        playerEntity.setFiveKillRounds(playerEntity.getFiveKillRounds() + player.getFiveKillRounds());

        setKillDeathRatio(playerEntity);
        setHeadshotPercentage(playerEntity);

        //Increment Gun Kill Info
        List<PlayerGunEntity> newGuns = new ArrayList<>();
        for (PlayerGameGunEntity newGun : player.getGuns()) {
            boolean found = false;
            for (PlayerGunEntity oldGun : playerEntity.getGuns()) {
                if (newGun.getName().equalsIgnoreCase(oldGun.getName())) {
                    //Already exists
                    found = true;
                    oldGun.setKills(newGun.getKills() + oldGun.getKills());
                    oldGun.setHeadshots(newGun.getHeadshots() + oldGun.getHeadshots());
                    oldGun.setDamage(newGun.getDamage() + oldGun.getDamage());
                }
            }
            if (!found) {
                //Does not exist
                PlayerGunEntity playerGunEntity = new PlayerGunEntity();
                playerGunEntity.setKills(newGun.getKills());
                playerGunEntity.setHeadshots(newGun.getHeadshots());
                playerGunEntity.setDamage(newGun.getDamage());
                playerGunEntity.setName(newGun.getName());
                newGuns.add(playerGunEntity);
            }
        }
        //Adding new guns
        playerEntity.getGuns().addAll(newGuns);

        //Increment Totals
        playerEntity.setTotalGames(playerEntity.getTotalGames() + 1);
        playerEntity.setTotalRounds(playerEntity.getTotalRounds() + game.getRounds().size());
        switch (teamScore) {
            case 0: {
                playerEntity.setTotalDraws(playerEntity.getTotalDraws() + 1);
                break;
            }
            case 1: {
                playerEntity.setTotalWins(playerEntity.getTotalWins() + 1);
                break;
            }
            case -1: {
                playerEntity.setTotalLosses(playerEntity.getTotalLosses() + 1);
                break;
            }
            default: {
                playerEntity.setTotalLosses(playerEntity.getTotalLosses() + 1);
                break;
            }
        }

        //Incrementing map information
        boolean foundMap = false;
        for (PlayerMapEntity map : playerEntity.getMaps()) {
            if (map.getName().equalsIgnoreCase(game.getMap())) {
                foundMap = true;
                map.setTotalGames(map.getTotalGames() + 1);
                switch (teamScore) {
                    case 0: {
                        map.setDraws(map.getDraws() + 1);
                        break;
                    }
                    case 1: {
                        map.setWins(map.getWins() + 1);
                        break;
                    }
                    case -1: {
                        map.setLosses(map.getLosses() + 1);
                        break;
                    }
                    default: {
                        map.setLosses(map.getLosses() + 1);
                        break;
                    }
                }
                break;
            }
        }
        if (!foundMap) {
            PlayerMapEntity newMap = new PlayerMapEntity();
            newMap.setName(game.getMap());
            newMap.setTotalGames(1);
            setMapWinsAndLosses(teamScore, newMap);
            playerEntity.getMaps().add(newMap);
        }

        playerEntity.setAdr(new BigDecimal(playerEntity.getDamage()).divide(new BigDecimal(playerEntity.getTotalRounds()), 0, RoundingMode.HALF_UP).intValueExact());

//        Adding this game to list of games
        playerEntity.getGames().add(game);
        return playerEntity;
    }


    public static PlayerEntity createNewPlayerEntityFromTeamPlayer(TeamPlayerEntity player,
                                                                   GameEntity game,
                                                                   Integer teamScore) {

        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setName(player.getName());
        playerEntity.setSteamID(player.getSteamID());
        playerEntity.setKills(player.getKills());
        playerEntity.setTeamKills(player.getTeamKills());
        playerEntity.setDeaths(player.getDeaths());
        playerEntity.setAssists(player.getAssists());
        playerEntity.setDamage(player.getDamageDone().longValue());
        playerEntity.setTeamDamage(player.getTeamDamageDone().longValue());
        playerEntity.setEnemiesFlashed(player.getEnemiesFlashed());
        playerEntity.setFlashAssists(player.getFlashAssists());
        playerEntity.setFlashDuration(player.getFlashDuration());
        playerEntity.setTeamFlashDuration(player.getTeamFlashDuration());
        playerEntity.setHeadshots(player.getHeadshots());
        playerEntity.setKillsWhileFlashed(player.getKillsWhileFlashed());
        playerEntity.setMvp(player.getMvp());
        playerEntity.setSmokeKills(player.getSmokeKills());
        playerEntity.setWallbangKills(player.getWallbangKills());
        playerEntity.setUtilityDamage(player.getUtilityDamage());
        playerEntity.setTeamUtilityDamage(player.getTeamUtilityDamage());
        playerEntity.setTeammatesFlashed(player.getTeammatesFlashed());
        playerEntity.setBombsPlanted(player.getBombsPlanted() == null ? 0 : player.getBombsPlanted());
        playerEntity.setBombsDefused(player.getBombsDefused() == null ? 0 : player.getBombsDefused());
        playerEntity.setNoScopeKills(player.getNoScopeKills());
        playerEntity.setKillReward(player.getKillReward());
        playerEntity.setFallDamage(player.getFallDamage());
        playerEntity.setFirstKillAttempt(player.getFirstKillAttempt());
        playerEntity.setFirstKill(player.getFirstKill());
        playerEntity.setFirstDeath(player.getFirstDeath());
        playerEntity.setOneVersusOne(player.getOneVersusOne());
        playerEntity.setOneVersusTwo(player.getOneVersusTwo());
        playerEntity.setOneVersusThree(player.getOneVersusThree());
        playerEntity.setOneVersusFour(player.getOneVersusFour());
        playerEntity.setOneVersusFive(player.getOneVersusFive());
        playerEntity.setOneVersusOneClutched(player.getOneVersusOneClutched());
        playerEntity.setOneVersusTwoClutched(player.getOneVersusTwoClutched());
        playerEntity.setOneVersusThreeClutched(player.getOneVersusThreeClutched());
        playerEntity.setOneVersusFourClutched(player.getOneVersusFourClutched());
        playerEntity.setOneVersusFiveClutched(player.getOneVersusFiveClutched());
        playerEntity.setOneKillRounds(player.getOneKillRounds());
        playerEntity.setTwoKillRounds(player.getTwoKillRounds());
        playerEntity.setThreeKillRounds(player.getThreeKillRounds());
        playerEntity.setFourKillRounds(player.getFourKillRounds());
        playerEntity.setFiveKillRounds(player.getFiveKillRounds());

        setKillDeathRatio(playerEntity);
        setHeadshotPercentage(playerEntity);

        //Create player gun list
        List<PlayerGunEntity> guns = new ArrayList<>();
        for (PlayerGameGunEntity gun : player.getGuns()) {
            //Does not exist
            PlayerGunEntity playerGunEntity = new PlayerGunEntity();
            playerGunEntity.setKills(gun.getKills());
            playerGunEntity.setHeadshots(gun.getHeadshots());
            playerGunEntity.setDamage(gun.getDamage());
            playerGunEntity.setName(gun.getName());
            guns.add(playerGunEntity);
        }
        //Set player guns
        playerEntity.setGuns(guns);

        //Set Totals, Games, Rounds, Wins, Losses, Draws
        playerEntity.setTotalGames(1);
        playerEntity.setTotalRounds(game.getRounds().size());
        setWinsAndLosses(teamScore, playerEntity);

        //Set new map list
        List<PlayerMapEntity> maps = new ArrayList<>();
        PlayerMapEntity newMap = new PlayerMapEntity();

        newMap.setName(game.getMap());
        newMap.setTotalGames(1);
        setMapWinsAndLosses(teamScore, newMap);

        maps.add(newMap);
        playerEntity.setMaps(maps);

        playerEntity.setAdr(new BigDecimal(playerEntity.getDamage()).divide(new BigDecimal(playerEntity.getTotalRounds()), 0, RoundingMode.HALF_UP).intValueExact());

        //Set this game as first game in list
        List<GameEntity> games = new ArrayList<>();
        games.add(game);
        playerEntity.setGames(games);
        return playerEntity;
    }

    private static void setKillDeathRatio(PlayerEntity playerEntity) {
        playerEntity.setKd(new BigDecimal(playerEntity.getKills()).divide(new BigDecimal(playerEntity.getDeaths()), 2, RoundingMode.HALF_UP));
    }

    private static void setHeadshotPercentage(PlayerEntity playerEntity) {
        if (playerEntity.getKills() > 0) {
            playerEntity.setHeadshotPercentage(new BigDecimal(playerEntity.getHeadshots()).divide(new BigDecimal(playerEntity.getKills()), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).intValueExact());
        } else {
            playerEntity.setHeadshotPercentage(0);
        }
    }

    private static void setMapWinsAndLosses(Integer teamScore, PlayerMapEntity newMap) {
        switch (teamScore) {
            case 1: {
                newMap.setWins(1);
                newMap.setLosses(0);
                newMap.setDraws(0);
                break;
            }

            case 0: {
                newMap.setWins(0);
                newMap.setLosses(0);
                newMap.setDraws(1);
            }

            case -1: {
                newMap.setWins(0);
                newMap.setLosses(1);
                newMap.setDraws(0);
            }

            default: {
                newMap.setWins(0);
                newMap.setLosses(1);
                newMap.setDraws(0);
            }
        }
//        switch (teamScore) {
//            case 15: {
//                newMap.setDraws(1);
//                newMap.setWins(0);
//                newMap.setLosses(0);
//                break;
//            }
//            case 22:
//            case 19:
//            case 16: {
//                newMap.setDraws(0);
//                newMap.setWins(1);
//                newMap.setLosses(0);
//                break;
//            }
//            default: {
//                newMap.setDraws(0);
//                newMap.setWins(0);
//                newMap.setLosses(1);
//                break;
//            }
//        }
    }

    private static void setWinsAndLosses(Integer teamScore, PlayerEntity playerEntity) {
        switch (teamScore) {
            case 1: {
                playerEntity.setTotalWins(1);
                playerEntity.setTotalLosses(0);
                playerEntity.setTotalDraws(0);
                break;
            }

            case 0: {
                playerEntity.setTotalWins(0);
                playerEntity.setTotalLosses(0);
                playerEntity.setTotalDraws(1);
            }

            case -1: {
                playerEntity.setTotalWins(0);
                playerEntity.setTotalLosses(1);
                playerEntity.setTotalDraws(0);
            }

            default: {
                playerEntity.setTotalWins(0);
                playerEntity.setTotalLosses(1);
                playerEntity.setTotalDraws(0);
            }
        }
//        switch (teamScore) {
//            case 15: {
//                playerEntity.setTotalWins(0);
//                playerEntity.setTotalLosses(0);
//                playerEntity.setTotalDraws(1);
//                break;
//            }
//            case 22:
//            case 19:
//            case 16: {
//                playerEntity.setTotalWins(1);
//                playerEntity.setTotalLosses(0);
//                playerEntity.setTotalDraws(0);
//                break;
//            }
//            default: {
//                playerEntity.setTotalWins(0);
//                playerEntity.setTotalLosses(1);
//                playerEntity.setTotalDraws(0);
//                break;
//            }
//        }
    }

}
