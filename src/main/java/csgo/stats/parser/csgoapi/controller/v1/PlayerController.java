package csgo.stats.parser.csgoapi.controller.v1;

import csgo.stats.parser.csgoapi.repository.entities.GameEntity;
import csgo.stats.parser.csgoapi.repository.entities.GunTop5Players;
import csgo.stats.parser.csgoapi.repository.entities.PlayerEntity;
import csgo.stats.parser.csgoapi.repository.entities.PlayerKillsAndHeadshots;
import csgo.stats.parser.csgoapi.services.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"https://unwindcsgo.flystudio.co.za", "http://localhost:4200"}, maxAge = 3600)
@RestController
@RequestMapping("/api/v1/player/")
public class PlayerController {
    private final Logger logger = LoggerFactory.getLogger(PlayerController.class);

    private PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<PlayerEntity> getPlayerEntityByName(@PathVariable(value = "name") String name) {
        return new ResponseEntity<>(playerService.getPlayerEntityByName(name), HttpStatus.OK);
    }

    @GetMapping("/steamID/{steamID}")
    public ResponseEntity<PlayerEntity> getPlayerEntityBySteamId(@PathVariable(value = "steamID") String id) {
        return new ResponseEntity<>(playerService.getPlayerEntityBySteamId(id), HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<PlayerEntity> getPlayerEntityBySteamId(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(playerService.getPlayerEntityById(id), HttpStatus.OK);
    }

    @GetMapping("/steamID/{steamID}/games")
    public ResponseEntity<List<GameEntity>> getGameEntitiesBySteamID(@PathVariable(value = "steamID") String steamID) {
        return new ResponseEntity<>(playerService.getPlayerGamesBySteamID(steamID), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PlayerEntity>> getAllPlayers(@RequestParam(required = false) Integer limit) {
        return new ResponseEntity<>(playerService.getAllPlayers(limit), HttpStatus.OK);
    }

    @GetMapping("/guns/name/{name}")
    public ResponseEntity<List<PlayerKillsAndHeadshots>> getAllPlayersUsingSpecificGun(@PathVariable(value = "name") String name) {
        return new ResponseEntity<>(playerService.getAllPlayersByGunName(name), HttpStatus.OK);
    }

    @GetMapping("/guns/all")
    public ResponseEntity<List<GunTop5Players>> getTop5PlayersPerGun() {
        return new ResponseEntity<>(playerService.getTop5PlayersPerGun(), HttpStatus.OK);
    }

    @GetMapping("/guns/game/id/{id}")
    public ResponseEntity<List<GunTop5Players>> getTop5PlayersPerGun(@PathVariable("id") Long id) {
        return new ResponseEntity<>(playerService.getTop5PlayersPerGun(id), HttpStatus.OK);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> deletePlayerEntity(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(playerService.deletePlayer(id), HttpStatus.ACCEPTED);
    }

}
