package csgo.stats.parser.csgoapi.mappers;

import csgo.stats.parser.csgoapi.model.*;
import csgo.stats.parser.csgoapi.repository.entities.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class GameEntityMapper {

    public static final String TERRORISTS = "TERRORIST";
    public static final String COUNTER_TERRORISTS = "CT";

    public static GameEntity mapGameToGameEntity(Game game) {
        GameEntity gameEntity = new GameEntity();
        gameEntity.setName(game.getId());
        gameEntity.setMap(game.getMap());
        gameEntity.setCTScore(game.getCtscore());
        gameEntity.setTScore(game.getTscore());
        gameEntity.setDate(game.getDate());

        //Create team 1
        GameTeamEntity team1 = getGameTeamEntity(game.getTeam1(), game.getTeam1().getName());
        //Create team 2
        GameTeamEntity team2 = getGameTeamEntity(game.getTeam2(), game.getTeam2().getName());
        //Create Spectator/Coaches team
//        GameTeamEntity spec = getGameTeamEntity(game.getSpectators(), game.getSpectators().getName());

        //Create Rounds
        List<RoundEntity> roundEntityList = getRoundEntity(game);
        gameEntity.setRounds(roundEntityList);

        initTeamPlayerGunKillsAndDamage(gameEntity, team1);
        initTeamPlayerGunKillsAndDamage(gameEntity, team2);

        //Add teams to list
        List<GameTeamEntity> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
        //Set teams
        gameEntity.setTeams(teams);


        for (TeamPlayerEntity player : team1.getPlayers()) {
            setClutchAndMultiKillRounds(gameEntity, player);
        }
        for (TeamPlayerEntity player : team2.getPlayers()) {
            setClutchAndMultiKillRounds(gameEntity, player);
        }

        return gameEntity;
    }

    public static void setClutchAndMultiKillRounds(GameEntity gameEntity, TeamPlayerEntity player) {
        //Determine clutches and multi kills
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

        for (RoundEntity round : gameEntity.getRounds()) {
            int kills = 0;
            List<String> T = new ArrayList<>();
            List<String> CT = new ArrayList<>();

            for (GameTeamEntity team : gameEntity.getTeams()) {
                for (TeamPlayerEntity teamPlayer : team.getPlayers()) {
                    if (teamPlayer.getTeamName().equalsIgnoreCase("Terrorists")) {
                        T.add(teamPlayer.getSteamID());
                    } else if (teamPlayer.getTeamName().equalsIgnoreCase("Counter-Terrorists")) {
                        CT.add(teamPlayer.getSteamID());
                    }
                }
            }
            ClutchPlayer clutchPlayer = null;
            int index = 0;
            for (KillEntity kill : round.getKills()) {
                T.remove(kill.getKilledSteamId());
                CT.remove(kill.getKilledSteamId());

                if (index == 0) {
                    if (kill.getKillerSteamId().equalsIgnoreCase(player.getSteamID())) {
                        player.setFirstKill(player.getFirstKill() + 1);
                        player.setFirstKillAttempt(player.getFirstKillAttempt() + 1);
                    }
                    if (kill.getKilledSteamId().equalsIgnoreCase(player.getSteamID())) {
                        player.setFirstDeath(player.getFirstDeath() + 1);
                        player.setFirstKillAttempt(player.getFirstKillAttempt() + 1);
                    }
                }
                index++;

                if (clutchPlayer == null && T.size() == 1 && T.contains(player.getSteamID())) {
                    //You're in a clutch
                    int against = CT.size();
                    clutchPlayer = new ClutchPlayer();
                    clutchPlayer.setEnemiesAlive(against);
                    clutchPlayer.setSteamId(player.getSteamID());
                    clutchPlayer.setName(player.getName());
                    clutchPlayer.setTeamName(kill.getKilledTeamName());
                }

                if (clutchPlayer == null && CT.size() == 1 && CT.contains(player.getSteamID())) {
                    //You're in a clutch
                    int against = T.size();
                    clutchPlayer = new ClutchPlayer();
                    clutchPlayer.setEnemiesAlive(against);
                    clutchPlayer.setSteamId(player.getSteamID());
                    clutchPlayer.setName(player.getName());
                    clutchPlayer.setTeamName(kill.getKilledTeamName());
                }

                if (kill.getKillerSteamId().equalsIgnoreCase(player.getSteamID()) && !kill.getKillerTeamName().equalsIgnoreCase(kill.getKilledTeamName())) {
                    kills++;
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
            }

            if (clutchPlayer != null) {
                switch (round.getReason()) {
                    case "1":
                    case "9": {
                        if (clutchPlayer.getTeamName().equalsIgnoreCase(TERRORISTS)) {
                            //You've clutched
                            switch (clutchPlayer.getEnemiesAlive()) {
                                case 1: {
                                    player.setOneVersusOne(player.getOneVersusOne() + 1);
                                    player.setOneVersusOneClutched(player.getOneVersusOneClutched() + 1);
                                    round.setClutch("1v1 - " + player.getName());
                                    break;
                                }

                                case 2: {
                                    player.setOneVersusTwo(player.getOneVersusTwo() + 1);
                                    player.setOneVersusTwoClutched(player.getOneVersusTwoClutched() + 1);
                                    round.setClutch("1v2 - " + player.getName());
                                    break;
                                }

                                case 3: {
                                    player.setOneVersusThree(player.getOneVersusThree() + 1);
                                    player.setOneVersusThreeClutched(player.getOneVersusThreeClutched() + 1);
                                    round.setClutch("1v3 - " + player.getName());
                                    break;
                                }

                                case 4: {
                                    player.setOneVersusFour(player.getOneVersusFour() + 1);
                                    player.setOneVersusFourClutched(player.getOneVersusFourClutched() + 1);
                                    round.setClutch("1v4 - " + player.getName());
                                    break;
                                }

                                case 5: {
                                    player.setOneVersusFive(player.getOneVersusFive() + 1);
                                    player.setOneVersusFiveClutched(player.getOneVersusFiveClutched() + 1);
                                    round.setClutch("1v5 - " + player.getName());
                                    break;
                                }
                            }
                        } else {
                            //Lost the clutch
                            switch (clutchPlayer.getEnemiesAlive()) {
                                case 1: {
                                    player.setOneVersusOne(player.getOneVersusOne() + 1);
                                    break;
                                }

                                case 2: {
                                    player.setOneVersusTwo(player.getOneVersusTwo() + 1);
                                    break;
                                }

                                case 3: {
                                    player.setOneVersusThree(player.getOneVersusThree() + 1);
                                    break;
                                }

                                case 4: {
                                    player.setOneVersusFour(player.getOneVersusFour() + 1);
                                    break;
                                }

                                case 5: {
                                    player.setOneVersusFive(player.getOneVersusFive() + 1);
                                    break;
                                }
                            }
                        }
                        break;
                    }
                    case "8":
                    case "7": {
                        if (clutchPlayer.getTeamName().equalsIgnoreCase(COUNTER_TERRORISTS)) {
                            //You've clutched
                            switch (clutchPlayer.getEnemiesAlive()) {
                                case 1: {
                                    player.setOneVersusOne(player.getOneVersusOne() + 1);
                                    player.setOneVersusOneClutched(player.getOneVersusOneClutched() + 1);
                                    round.setClutch("1v1 - " + player.getName());
                                    break;
                                }

                                case 2: {
                                    player.setOneVersusTwo(player.getOneVersusTwo() + 1);
                                    player.setOneVersusTwoClutched(player.getOneVersusTwoClutched() + 1);
                                    round.setClutch("1v2 - " + player.getName());
                                    break;
                                }

                                case 3: {
                                    player.setOneVersusThree(player.getOneVersusThree() + 1);
                                    player.setOneVersusThreeClutched(player.getOneVersusThreeClutched() + 1);
                                    round.setClutch("1v3 - " + player.getName());
                                    break;
                                }

                                case 4: {
                                    player.setOneVersusFour(player.getOneVersusFour() + 1);
                                    player.setOneVersusFourClutched(player.getOneVersusFourClutched() + 1);
                                    round.setClutch("1v4 - " + player.getName());
                                    break;
                                }

                                case 5: {
                                    player.setOneVersusFive(player.getOneVersusFive() + 1);
                                    player.setOneVersusFiveClutched(player.getOneVersusFiveClutched() + 1);
                                    round.setClutch("1v5 - " + player.getName());
                                    break;
                                }
                            }
                        } else {
                            switch (clutchPlayer.getEnemiesAlive()) {
                                case 1: {
                                    player.setOneVersusOne(player.getOneVersusOne() + 1);
                                    break;
                                }

                                case 2: {
                                    player.setOneVersusTwo(player.getOneVersusTwo() + 1);
                                    break;
                                }

                                case 3: {
                                    player.setOneVersusThree(player.getOneVersusThree() + 1);
                                    break;
                                }

                                case 4: {
                                    player.setOneVersusFour(player.getOneVersusFour() + 1);
                                    break;
                                }

                                case 5: {
                                    player.setOneVersusFive(player.getOneVersusFive() + 1);
                                    break;
                                }
                            }
                        }
                        break;
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
    }

    private static void initTeamPlayerGunKillsAndDamage(GameEntity gameEntity, GameTeamEntity team) {
        for (TeamPlayerEntity player : team.getPlayers()) {
            Integer utilityDamage = 0;
            Integer friendlyUtilityDamage = 0;
            Double enemyFlashDuration = 0.0;
            Double friendlyFlashDuration = 0.0;
            Integer enemiesFlashed = 0;
            Integer friendliesFlashed = 0;
            for (RoundEntity round : gameEntity.getRounds()) {

                for (KillEntity kill : round.getKills()) {
                    if (kill.getKillerSteamId().equalsIgnoreCase(player.getSteamID()) && !kill.getFriendly()) {
                        boolean found = false;
                        String weapon = kill.getGun();
                        if (weapon.contains("knife")) {
                            weapon = "knife";
                        }
                        for (PlayerGameGunEntity gun : player.getGuns()) {
                            if (gun.getName().equalsIgnoreCase(weapon)) {
                                found = true;
                                gun.setKills(gun.getKills() + 1);
                                if (kill.getHeadshot()) {
                                    gun.setHeadshots(gun.getHeadshots() + 1);
                                }
                            }
                        }
                        if (!found) {
                            PlayerGameGunEntity gunEntity = new PlayerGameGunEntity();
                            gunEntity.setKills(1);
                            gunEntity.setHeadshots(kill.getHeadshot() ? 1 : 0);
                            gunEntity.setName(weapon);
                            gunEntity.setDamage(0L);
                            player.getGuns().add(gunEntity);
                        }

                    }
                }

                for (RoundDamageEntity roundDamage : round.getDamage()) {
                    if (roundDamage.getAttackerSteamId().equalsIgnoreCase(player.getSteamID()) && !roundDamage.getFriendly()) {
                        boolean found = false;
                        String weapon = roundDamage.getWeapon();
                        if (weapon.contains("knife")) {
                            weapon = "knife";
                        }
                        for (PlayerGameGunEntity gun : player.getGuns()) {
                            if (gun.getName().equalsIgnoreCase(weapon)) {
                                found = true;
                                gun.setDamage(gun.getDamage() + roundDamage.getDamage());
                            }
                        }
                        if (!found) {
                            PlayerGameGunEntity gunEntity = new PlayerGameGunEntity();
                            gunEntity.setKills(0);
                            gunEntity.setHeadshots(0);
                            gunEntity.setName(weapon);
                            gunEntity.setDamage(roundDamage.getDamage().longValue());
                            player.getGuns().add(gunEntity);
                        }
                        if (weapon.equalsIgnoreCase("hegrenade") || weapon.equalsIgnoreCase("inferno")
                                || weapon.equalsIgnoreCase("molotov") || weapon.equalsIgnoreCase("flashbang")
                                || weapon.equalsIgnoreCase("decoy") || weapon.equalsIgnoreCase("smokegrenade")) {
                            utilityDamage += roundDamage.getDamage();
                        }
                    } else if (roundDamage.getAttackerSteamId().equalsIgnoreCase(player.getSteamID()) && roundDamage.getFriendly()) {
                        String weapon = roundDamage.getWeapon();
                        if (weapon.equalsIgnoreCase("hegrenade") || weapon.equalsIgnoreCase("inferno")
                                || weapon.equalsIgnoreCase("molotov") || weapon.equalsIgnoreCase("flashbang")
                                || weapon.equalsIgnoreCase("decoy") || weapon.equalsIgnoreCase("smokegrenade")) {
                            friendlyUtilityDamage += roundDamage.getDamage();
                        }
                    }
                }

                for (RoundFlashbangEntity flash : round.getFlashbangs()) {
                    if (flash.getAttackerSteamId().equalsIgnoreCase(player.getSteamID())) {
                        if (flash.getFriendly()) {
                            friendliesFlashed++;
                            friendlyFlashDuration += flash.getDuration();
                        } else {
                            enemyFlashDuration += flash.getDuration();
                            enemiesFlashed++;
                        }
                    }
                }

            }
            player.setFlashDuration(enemyFlashDuration);
            player.setEnemiesFlashed(enemiesFlashed);
            player.setTeamFlashDuration(friendlyFlashDuration);
            player.setTeammatesFlashed(friendliesFlashed);
            player.setUtilityDamage(utilityDamage);
            player.setTeamUtilityDamage(friendlyUtilityDamage);
            player.setAdr(new BigDecimal(player.getDamageDone()).divide(new BigDecimal(gameEntity.getRounds().size()), 0, RoundingMode.HALF_UP).intValueExact());
            player.setKd(new BigDecimal(player.getKills()).divide(new BigDecimal(player.getDeaths()), 2, RoundingMode.HALF_UP));
            if (player.getKills() > 0) {
                player.setHeadshotPercentage(new BigDecimal(player.getHeadshots()).divide(new BigDecimal(player.getKills()), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).intValueExact());
            } else {
                player.setHeadshotPercentage(0);
            }
        }
    }

    private static List<RoundEntity> getRoundEntity(Game game) {
        List<RoundEntity> rounds = new ArrayList<>();
        for (Round round : game.getRounds()) {
            RoundEntity roundEntity = new RoundEntity();
            roundEntity.setRoundNumber(round.getRoundNo() + 1);
            roundEntity.setReason(round.getReason() == null ? "-1" : round.getReason().toString());
            roundEntity.setWinner(round.getWinner() == null ? "-1" : round.getWinner().toString());

            List<KillEntity> kills = new ArrayList<>();
            //Set Kills for Round
            for (Kill kill : round.getKills()) {
                KillEntity killEntity = new KillEntity();

                killEntity.setGun(kill.getGun());
                killEntity.setHeadshot(kill.getHeadshot());
                killEntity.setKilledName(kill.getKilled());
                killEntity.setKilledSteamId(kill.getKilledSteamId());
                killEntity.setKillerName(kill.getKiller());
                killEntity.setKillerSteamId(kill.getKillerSteamId());
                killEntity.setKillerTeamName(kill.getKillerTeamName());
                killEntity.setKilledTeamName(kill.getKilledTeamName());
                killEntity.setTime(kill.getTime());
                killEntity.setAssisted(kill.getAssisted());
                killEntity.setAssisterSteamId(kill.getAssisterSteamId());
                killEntity.setFlashAssist(kill.getFlashAssist());
                killEntity.setAttackerBlind(kill.getAttackerBlind());
                killEntity.setPenetrated(kill.getPenetrated());
                killEntity.setThroughSmoke(kill.getThroughSmoke());
                killEntity.setNoscope(kill.getNoscope());
                killEntity.setAssisterName(kill.getAssisterName());
                killEntity.setAssisterTeamName(kill.getAssisterTeamName());
                killEntity.setFriendly(kill.getFriendly());

                kills.add(killEntity);
            }
            roundEntity.setKills(kills);

            List<RoundDamageEntity> damages = new ArrayList<>();
            //Set Damage for Round
            for (Damage damage : round.getDamage()) {
                RoundDamageEntity damageEntity = new RoundDamageEntity();

                damageEntity.setDamage(damage.getDamage());
                damageEntity.setAttackerSteamId(damage.getAttackerSteamId());
                damageEntity.setVictimSteamId(damage.getVictimSteamId());
                damageEntity.setFriendly(damage.getFriendly());
                damageEntity.setWeapon(damage.getWeapon());
                damageEntity.setHitgroup(damage.getHitgroup());

                damages.add(damageEntity);
            }
            roundEntity.setDamage(damages);

            List<RoundFlashbangEntity> flashbangs = new ArrayList<>();
            //Set Flashbangs for round
            for (Flashbang flash : round.getFlashbangs()) {
                RoundFlashbangEntity flashbangEntity = new RoundFlashbangEntity();

                flashbangEntity.setFriendly(flash.getFriendly());
                flashbangEntity.setAttackerSteamId(flash.getAttackerSteamId());
                flashbangEntity.setDuration(flash.getDuration());
                flashbangEntity.setVictimSteamId(flash.getVictimSteamId());

                flashbangs.add(flashbangEntity);
            }
            roundEntity.setFlashbangs(flashbangs);

            rounds.add(roundEntity);
        }
        return rounds;
    }

    private static GameTeamEntity getGameTeamEntity(Team team, String teamName) {
        GameTeamEntity gameTeamEntity = new GameTeamEntity();
        gameTeamEntity.setName(teamName);
        gameTeamEntity.setClan(teamName);
        gameTeamEntity.setFirstHalfScore(team.getFirstHalfScore());
        gameTeamEntity.setSecondHalfScore(team.getSecondHalfScore());
        gameTeamEntity.setScore(team.getScore());
        gameTeamEntity.setResult(team.getResult());

        List<TeamPlayerEntity> players = new ArrayList<>();

        //Set team players
        for (Player player : team.getPlayers()) {
            TeamPlayerEntity teamPlayerEntity = new TeamPlayerEntity();
            teamPlayerEntity.setName(player.getName());
            teamPlayerEntity.setSteamID(player.getSteamid());
            teamPlayerEntity.setAssists(player.getAssists());
            teamPlayerEntity.setKills(player.getKills());
            teamPlayerEntity.setTeamKills(player.getTeamKills());
            teamPlayerEntity.setDeaths(player.getDeaths());
            teamPlayerEntity.setEnemiesFlashed(player.getEnemiesFlashed());
            teamPlayerEntity.setFlashAssists(player.getFlashAssists());
            teamPlayerEntity.setWallbangKills(player.getWallbangKills());
            teamPlayerEntity.setSmokeKills(player.getSmokeKills());
            teamPlayerEntity.setKillsWhileFlashed(player.getKillsWhileFlashed());
            teamPlayerEntity.setFlashDuration(player.getFlashDuration());
            teamPlayerEntity.setTeamFlashDuration(player.getTeamFlashDuration());
            teamPlayerEntity.setDamageDone(player.getDamage());
            teamPlayerEntity.setTeamDamageDone(player.getTeamDamage());
            teamPlayerEntity.setHeadshots(player.getHeadshots());
            teamPlayerEntity.setMvp(player.getMvp());
            teamPlayerEntity.setTeamName(player.getTeam());
            teamPlayerEntity.setUtilityDamage(player.getUtilityDamage());
            teamPlayerEntity.setTeamUtilityDamage(player.getTeamUtilityDamage());
            teamPlayerEntity.setTeammatesFlashed(player.getTeammatesFlashed());
            teamPlayerEntity.setBombsPlanted(player.getBombsPlanted());
            teamPlayerEntity.setBombsDefused(player.getBombsDefused());
            teamPlayerEntity.setNoScopeKills(player.getNoScopeKills() == null ? 0 : player.getNoScopeKills());
            teamPlayerEntity.setKillReward(player.getKillReward());
            teamPlayerEntity.setFallDamage(player.getFallDamage());
            teamPlayerEntity.setTeamName(player.getTeam());

            List<PlayerGameGunEntity> gunList = new ArrayList<>();
            //Set Player Gun Information
            for (Gun gun : player.getGunKills()) {
                PlayerGameGunEntity playerGun = new PlayerGameGunEntity();
                playerGun.setName(gun.getName());
                playerGun.setKills(gun.getKills());
                playerGun.setHeadshots(gun.getHeadshots());
                playerGun.setDamage(gun.getDamage());
                //Add Gun to player
                gunList.add(playerGun);
            }
            //Set player Guns
            teamPlayerEntity.setGuns(gunList);
            players.add(teamPlayerEntity);
        }

        gameTeamEntity.setPlayers(players);
        return gameTeamEntity;
    }
}
