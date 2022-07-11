package csgo.stats.parser.csgoapi.services;

import csgo.stats.parser.csgoapi.mappers.PlayerEntityMapper;
import csgo.stats.parser.csgoapi.model.response.DashboardStatObject;
import csgo.stats.parser.csgoapi.model.response.DashboardStats;
import csgo.stats.parser.csgoapi.repository.IGameEntityRepository;
import csgo.stats.parser.csgoapi.repository.IPlayerEntityRepository;
import csgo.stats.parser.csgoapi.repository.IPlayerGunEntityRepository;
import csgo.stats.parser.csgoapi.repository.ITeamPlayerEntityRepository;
import csgo.stats.parser.csgoapi.repository.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private IPlayerEntityRepository playerEntityRepository;
    private ITeamPlayerEntityRepository teamPlayerEntityRepository;
    private IPlayerGunEntityRepository gunEntityRepository;
    private IGameEntityRepository gameEntityRepository;

    @Autowired
    public PlayerService(IPlayerEntityRepository playerEntityRepository,
                         IPlayerGunEntityRepository gunEntityRepository,
                         IGameEntityRepository gameEntityRepository,
                         ITeamPlayerEntityRepository teamPlayerEntityRepository) {
        this.playerEntityRepository = playerEntityRepository;
        this.gunEntityRepository = gunEntityRepository;
        this.gameEntityRepository = gameEntityRepository;
        this.teamPlayerEntityRepository = teamPlayerEntityRepository;
    }

    public void savePlayerEntity(PlayerEntity player) {
        this.playerEntityRepository.save(player);
    }

    public List<GunTop5Players> getTop5PlayersPerGun(Long id) {
        List<Long> ids = new ArrayList<>();
        List<Long> gunIds = new ArrayList<>();

        GameEntity gameEntity = gameEntityRepository.findById(id).get();

        for (GameTeamEntity team : gameEntity.getTeams()) {
            for (TeamPlayerEntity teamPlayer : team.getPlayers()) {
                ids.add(teamPlayer.getId());
                for (PlayerGameGunEntity gun : teamPlayer.getGuns()) {
                    gunIds.add(gun.getId());
                }
            }
        }
        List<GunName> gunNames = gunEntityRepository.findDistinctNameTeamPlayer(gunIds);

        List<GunTop5Players> gunsList = new ArrayList<>();
        GunTop5Players top5Players;
        for (GunName gun : gunNames) {
            top5Players = new GunTop5Players();
            top5Players.setName(gun.getName());
            List<PlayerKillsAndHeadshots> playerKillsAndHeadshots = teamPlayerEntityRepository.findTop5ByGunsNameAndIdInOrderByGunsKillsDescGunsDamageDesc(gun.getName(), ids);
            top5Players.setPlayers(playerKillsAndHeadshots);
            gunsList.add(top5Players);
        }

        return gunsList;
    }

    public List<GunTop5Players> getTop5PlayersPerGun() {
        List<GunName> gunNames = gunEntityRepository.findDistinctName();

        List<GunTop5Players> gunsList = new ArrayList<>();
        GunTop5Players top5Players;
        for (GunName gun : gunNames) {
            top5Players = new GunTop5Players();
            top5Players.setName(gun.getName());
            List<PlayerKillsAndHeadshots> playerKillsAndHeadshots = playerEntityRepository.findTop5ByGunsNameOrderByGunsKillsDescGunsDamageDesc(gun.getName());
            top5Players.setPlayers(playerKillsAndHeadshots);
            gunsList.add(top5Players);
        }

        return gunsList;
    }

    public List<PlayerKillsAndHeadshots> getTop5PlayersByGunName(String name) {
        return this.playerEntityRepository.findTop5ByGunsNameOrderByGunsKillsDescGunsDamageDesc(name);
    }

    public List<PlayerKillsAndHeadshots> getAllPlayersByGunName(String name) {
        return this.playerEntityRepository.findByGunsNameOrderByGunsKillsDesc(name);
    }

    public List<PlayerEntity> getAllPlayers(Integer limit) {
        return this.playerEntityRepository.findAllByOrderByKillsDesc();
    }

    public List<GameEntity> getPlayerGamesBySteamID(String steamID) {
        return this.playerEntityRepository.findBySteamID(steamID).getGames();
    }

    public PlayerEntity getPlayerEntityById(Long id) {
        Optional<PlayerEntity> playerEntity = playerEntityRepository.findById(id);
        if (playerEntity.isPresent()) {
            return playerEntity.get();
        }
        return null;
    }

    public PlayerEntity getPlayerEntityBySteamId(String id) {
        PlayerEntity playerEntity = playerEntityRepository.findBySteamID(id);
        return playerEntity;
    }

    public PlayerEntity getPlayerEntityByName(String name) {
        PlayerEntity playerEntity = playerEntityRepository.findByName(name);
        return playerEntity;
    }

    public List<PlayerEntity> getPlayersFromGameEntity(GameEntity game) {
        List<PlayerEntity> playerEntityList = new ArrayList<>();
        for (GameTeamEntity team : game.getTeams()) {
            Integer teamScore = team.getResult();
            for (TeamPlayerEntity player : team.getPlayers()) {
                PlayerEntity playerEntity = playerEntityRepository.findBySteamID(player.getSteamID());
                if (playerEntity == null) {
                    //Not found, create new player Entity.
                    playerEntity = PlayerEntityMapper.createNewPlayerEntityFromTeamPlayer(player, game, teamScore);
                } else {
                    playerEntity = PlayerEntityMapper.combinePlayerEntityInformation(playerEntity, player, game, teamScore);
                }
                playerEntityList.add(playerEntity);
            }
        }

        return playerEntityList;
    }

    public DashboardStats getDashboardStats() {
        DashboardStatObject mostKillsPlayer = getMostKillsPlayer();
        DashboardStatObject mostDeathsPlayer = getMostDeathsPlayer();
        DashboardStatObject mostAssistsPlayer = getMostAssistsPlayer();
        DashboardStatObject mostMvpPlayer = getMostMvpPlayer();
        DashboardStatObject mostEnemiesFlashedPlayer = getMostEnemiesFlashedPlayer();
        DashboardStatObject mostTeammatesFlashedPlayer = getMostTeammatesFlashedPlayer();
        DashboardStatObject mostUtilityDamagePlayer = getMostUtilityDamagePlayer();
        DashboardStatObject mostFlashAssistsPlayer = getMostFlashAssistsPlayer();
        DashboardStatObject mostTeamDamagePlayer = getMostTeamDamagePlayer();
        DashboardStatObject mostTeamUtilityDamagePlayer = getMostTeamUtilityDamagePlayer();
        DashboardStatObject mostFiveKillRoundsPlayer = getMostFiveKillRoundsPlayer();
        DashboardStatObject mostFourKillRoundsPlayer = getMostFourKillRoundsPlayer();
        DashboardStatObject mostThreeKillRoundsPlayer = getMostThreeKillRoundsPlayer();
        DashboardStatObject mostTwoKillRoundsPlayer = getMostTwoKillRoundsPlayer();
        DashboardStatObject mostTeamKillsPlayer = getMostTeamKillsPlayer();

        DashboardStatObject mostBombsDefused = getMostBombsDefusedPlayer();
        DashboardStatObject mostBombsPlanted = getMostBombsPlantedPlayer();

        DashboardStatObject highestTeamFlashDurationPlayer = getHighestTeamFlashDurationPlayer();
        DashboardStatObject highestFlashDurationPlayer = getHighestFlashDurationPlayer();
        DashboardStatObject highestHS = getHighestHSPlayer();
        DashboardStatObject highestADR = getHighestADRPlayer();
        DashboardStatObject highestKD = getHighestKDPlayer();

        DashboardStatObject mostFallDamage = getHighestFallDamage();
        DashboardStatObject mostNoScopeKills = getMostNoScopeKills();
        DashboardStatObject highestKillReward = getHighestKillReward();

        DashboardStats dashboardStats = new DashboardStats();
        dashboardStats.setMostKills(mostKillsPlayer);
        dashboardStats.setMostAssists(mostAssistsPlayer);
        dashboardStats.setMostDeaths(mostDeathsPlayer);
        dashboardStats.setMostUtilityDamage(mostUtilityDamagePlayer);
        dashboardStats.setMostEnemiesFlashed(mostEnemiesFlashedPlayer);
        dashboardStats.setMostTeammatesFlashed(mostTeammatesFlashedPlayer);
        dashboardStats.setMostMvp(mostMvpPlayer);
        dashboardStats.setMostFlashAssists(mostFlashAssistsPlayer);
        dashboardStats.setHighestADR(highestADR);
        dashboardStats.setHighestKD(highestKD);
        dashboardStats.setHighestHS(highestHS);

        dashboardStats.setMostTeamDamage(mostTeamDamagePlayer);
        dashboardStats.setMostTeamUtilityDamage(mostTeamUtilityDamagePlayer);
        dashboardStats.setHighestTeamFlashDuration(highestTeamFlashDurationPlayer);
        dashboardStats.setMostTeamKills(mostTeamKillsPlayer);
        dashboardStats.setMostFiveKillRounds(mostFiveKillRoundsPlayer);
        dashboardStats.setMostFourKillRounds(mostFourKillRoundsPlayer);
        dashboardStats.setMostThreeKillRounds(mostThreeKillRoundsPlayer);
        dashboardStats.setMostTwoKillRounds(mostTwoKillRoundsPlayer);
        dashboardStats.setHighestFlashDuration(highestFlashDurationPlayer);

        //Clutch Stats
        dashboardStats.setMostOneVOnes(getMostOneVOnePlayer());
        dashboardStats.setMostOneVTwos(getMostOneVTwoPlayer());
        dashboardStats.setMostOneVThrees(getMostOneVThreePlayer());
        dashboardStats.setMostOneVFours(getMostOneVFourPlayer());
        dashboardStats.setMostOneVFives(getMostOneVFivePlayer());

        dashboardStats.setMostOneVOneClutches(getMostOneVOneClutchPlayer());
        dashboardStats.setMostOneVTwoClutches(getMostOneVTwoClutchPlayer());
        dashboardStats.setMostOneVThreeClutches(getMostOneVThreeClutchPlayer());
        dashboardStats.setMostOneVFourClutches(getMostOneVFourClutchPlayer());
        dashboardStats.setMostOneVFiveClutches(getMostOneVFiveClutchPlayer());

        dashboardStats.setMostWallbangKills(getMostWallbangKills());
        dashboardStats.setMostSmokeKills(getMostSmokeKills());
        dashboardStats.setMostKillsWhileFlashed(getMostKillsWhileFlashed());

        dashboardStats.setMostBombsDefused(mostBombsDefused);
        dashboardStats.setMostBombsPlanted(mostBombsPlanted);

        dashboardStats.setMostFallDamage(mostFallDamage);
        dashboardStats.setHighestKillReward(highestKillReward);
        dashboardStats.setMostNoScopeKills(mostNoScopeKills);

        return dashboardStats;
    }

    private DashboardStatObject getMostSmokeKills() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderBySmokeKillsDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Most Kills Through Smoke");
        most.setValue(player.getSmokeKills().toString());
        return most;
    }

    private DashboardStatObject getMostKillsWhileFlashed() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByKillsWhileFlashedDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Most Kills While Flashed");
        most.setValue(player.getKillsWhileFlashed().toString());
        return most;
    }

    private DashboardStatObject getMostWallbangKills() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByWallbangKillsDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Most Wallbang Kills");
        most.setValue(player.getWallbangKills().toString());
        return most;
    }

    private DashboardStatObject getHighestKillReward() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByKillRewardDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Highest Kill Reward Earned");
        most.setValue(player.getKillReward().toString());
        return most;
    }

    private DashboardStatObject getMostNoScopeKills() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByNoScopeKillsDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Most No Scope Kills");
        most.setValue(player.getNoScopeKills().toString());
        return most;
    }

    private DashboardStatObject getHighestFallDamage() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByFallDamageDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Most Fall Damage Taken");
        most.setValue(player.getFallDamage().toString());
        return most;
    }

    private DashboardStatObject getMostOneVFivePlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByOneVersusFiveDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Been in most 1v5 Clutches");
        most.setValue(player.getOneVersusFive().toString());
        return most;
    }

    private DashboardStatObject getMostOneVFourPlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByOneVersusFourDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Been in most 1v4 Clutches");
        most.setValue(player.getOneVersusFour().toString());
        return most;
    }

    private DashboardStatObject getMostOneVThreePlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByOneVersusThreeDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Been in most 1v3 Clutches");
        most.setValue(player.getOneVersusThree().toString());
        return most;
    }

    private DashboardStatObject getMostOneVTwoPlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByOneVersusTwoDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Been in most 1v2 Clutches");
        most.setValue(player.getOneVersusTwo().toString());
        return most;
    }

    private DashboardStatObject getMostOneVOnePlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByOneVersusOneDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Been in most 1v1 Clutches");
        most.setValue(player.getOneVersusOne().toString());
        return most;
    }

    private DashboardStatObject getMostOneVFiveClutchPlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByOneVersusFiveClutchedDescOneVersusFiveAsc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Highest number of 1v5 Clutches");
        most.setValue(player.getOneVersusFiveClutched().toString());
        return most;
    }

    private DashboardStatObject getMostOneVFourClutchPlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByOneVersusFourClutchedDescOneVersusFourAsc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Highest number of 1v4 Clutches");
        most.setValue(player.getOneVersusFourClutched().toString());
        return most;
    }

    private DashboardStatObject getMostOneVThreeClutchPlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByOneVersusThreeClutchedDescOneVersusThreeAsc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Highest 1v3 Clutch %");
        most.setValue(player.getOneVersusThreeClutched().toString());
        return most;
    }

    private DashboardStatObject getMostOneVTwoClutchPlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByOneVersusTwoClutchedDescOneVersusTwoAsc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Highest 1v2 Clutch %");
        most.setValue(player.getOneVersusTwoClutched().toString());
        return most;
    }

    private DashboardStatObject getMostOneVOneClutchPlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByOneVersusOneClutchedDescOneVersusOneAsc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Highest 1v1 Clutch %");
        most.setValue(player.getOneVersusOneClutched().toString());
        return most;
    }

    private DashboardStatObject getHighestFlashDurationPlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByFlashDurationDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Highest Flash Duration");
        most.setValue(player.getFlashDuration().toString());
        return most;
    }

    private DashboardStatObject getHighestTeamFlashDurationPlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByTeamFlashDurationDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Highest Team Flash Duration");
        most.setValue(player.getTeamFlashDuration().toString());
        return most;
    }

    private DashboardStatObject getMostBombsDefusedPlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByBombsDefusedDescTotalRoundsDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Most Bombs Defused");
        most.setValue(player.getBombsDefused().toString());
        return most;
    }

    private DashboardStatObject getMostBombsPlantedPlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByBombsPlantedDescTotalRoundsDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Most Bombs Planted");
        most.setValue(player.getBombsPlanted().toString());
        return most;
    }

    private DashboardStatObject getMostTeamKillsPlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByTeamKillsDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Most Team Kills");
        most.setValue(player.getTeamKills().toString());
        return most;
    }

    private DashboardStatObject getMostTeamUtilityDamagePlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByTeamUtilityDamageDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Most Team Utility Damage");
        most.setValue(player.getTeamUtilityDamage().toString());
        return most;
    }

    private DashboardStatObject getMostTeamDamagePlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByTeamDamageDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Most Team Damage");
        most.setValue(player.getTeamDamage().toString());
        return most;
    }

    private DashboardStatObject getMostTwoKillRoundsPlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByTwoKillRoundsDescKillsDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Most 2 Kill Rounds");
        most.setValue(player.getTwoKillRounds().toString());
        return most;
    }

    private DashboardStatObject getMostThreeKillRoundsPlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByThreeKillRoundsDescKillsDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Most 3 Kill Rounds");
        most.setValue(player.getThreeKillRounds().toString());
        return most;
    }

    private DashboardStatObject getMostFourKillRoundsPlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByFourKillRoundsDescKillsDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Most 4 Kill Rounds");
        most.setValue(player.getFourKillRounds().toString());
        return most;
    }

    private DashboardStatObject getMostFiveKillRoundsPlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByFiveKillRoundsDescKillsDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Most Aces");
        most.setValue(player.getFiveKillRounds().toString());
        return most;
    }

    private DashboardStatObject getHighestHSPlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByHeadshotPercentageDescKillsDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Highest HS%");
        most.setValue(player.getHeadshotPercentage().toString());
        return most;
    }

    private DashboardStatObject getHighestADRPlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByAdrDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Highest ADR");
        most.setValue(player.getAdr().toString());
        return most;
    }

    private DashboardStatObject getHighestKDPlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByKdDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Highest K/D");
        most.setValue(player.getKd().toString());
        return most;
    }

    private DashboardStatObject getMostUtilityDamagePlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByUtilityDamageDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Most Utility Damage");
        most.setValue(player.getUtilityDamage().toString());
        return most;
    }

    private DashboardStatObject getMostFlashAssistsPlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByFlashAssistsDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Most Flash Assists");
        most.setValue(player.getFlashAssists().toString());
        return most;
    }

    private DashboardStatObject getMostTeammatesFlashedPlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByTeammatesFlashedDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Most Teammates Flashed");
        most.setValue(player.getTeammatesFlashed().toString());
        return most;
    }

    private DashboardStatObject getMostEnemiesFlashedPlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByEnemiesFlashedDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Most Enemies Flashed");
        most.setValue(player.getEnemiesFlashed().toString());
        return most;
    }

    private DashboardStatObject getMostMvpPlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByMvpDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Most MVP's");
        most.setValue(player.getMvp().toString());
        return most;
    }

    private DashboardStatObject getMostAssistsPlayer() {
        PlayerEntity player = playerEntityRepository.findFirstByOrderByAssistsDesc();

        DashboardStatObject most = new DashboardStatObject();
        most.setPlayerName(player.getName());
        most.setKey("Most Assists");
        most.setValue(player.getAssists().toString());
        return most;
    }

    private DashboardStatObject getMostDeathsPlayer() {
        PlayerEntity mostDeathsPlayer = playerEntityRepository.findFirstByOrderByDeathsDesc();

        DashboardStatObject mostDeaths = new DashboardStatObject();
        mostDeaths.setPlayerName(mostDeathsPlayer.getName());
        mostDeaths.setKey("Most Deaths");
        mostDeaths.setValue(mostDeathsPlayer.getDeaths().toString());
        return mostDeaths;
    }

    private DashboardStatObject getMostKillsPlayer() {
        PlayerEntity mostKillsPlayer = playerEntityRepository.findFirstByOrderByKillsDesc();

        DashboardStatObject mostKills = new DashboardStatObject();
        mostKills.setPlayerName(mostKillsPlayer.getName());
        mostKills.setKey("Most Kills");
        mostKills.setValue(mostKillsPlayer.getKills().toString());
        return mostKills;
    }
}
