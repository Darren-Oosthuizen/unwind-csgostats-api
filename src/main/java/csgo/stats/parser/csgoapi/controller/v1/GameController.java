package csgo.stats.parser.csgoapi.controller.v1;

import csgo.stats.parser.csgoapi.model.Game;
import csgo.stats.parser.csgoapi.model.response.MatchHistory;
import csgo.stats.parser.csgoapi.repository.entities.BasicGameEntity;
import csgo.stats.parser.csgoapi.repository.entities.GameEntity;
import csgo.stats.parser.csgoapi.services.GameService;
import csgo.stats.parser.csgoapi.services.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/game/")
public class GameController {

    private final Logger logger = LoggerFactory.getLogger(GameController.class);

    private GameService gameService;
    private PlayerService playerService;

    @Autowired
    public GameController(GameService gameService, PlayerService playerService) {
        this.gameService = gameService;
        this.playerService = playerService;
    }

    @PostMapping("/create")
    public ResponseEntity<GameEntity> createGame(@RequestBody Game game) {

        GameEntity gameEntity = gameService.createGameEntity(game);

        //Update Player Stats Involved in the Game
//        playerService.updatePlayers(gameEntity);

        return new ResponseEntity<>(gameEntity, HttpStatus.CREATED);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<GameEntity> getGameEntity(@PathVariable(value = "name") String name) {
        return new ResponseEntity<>(gameService.getGameEntity(name), HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<GameEntity> getGameEntity(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(gameService.getGameEntityById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<GameEntity>> getGameEntities(@RequestParam(required = false) Integer limit) {
        return new ResponseEntity<>(gameService.getAllGames(limit), HttpStatus.OK);
    }

    @GetMapping("/team/{team}/all")
    public ResponseEntity<List<MatchHistory>> getGameEntitiesByClan(@PathVariable("team") String team, @RequestParam(required = false) Integer limit) {
        return new ResponseEntity<>(gameService.getAllGamesForClanName(team, limit), HttpStatus.OK);
    }

}
