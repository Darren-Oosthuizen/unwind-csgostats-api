package csgo.stats.parser.csgoapi.controller.v1;

import csgo.stats.parser.csgoapi.model.Game;
import csgo.stats.parser.csgoapi.model.response.MatchHistory;
import csgo.stats.parser.csgoapi.repository.entities.GameEntity;
import csgo.stats.parser.csgoapi.services.GameService;
import csgo.stats.parser.csgoapi.services.PlayerService;
import csgo.stats.parser.csgoapi.services.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"https://unwindcsgo.flystudio.co.za", "http://localhost:4200"}, maxAge = 3600)
@RestController
@RequestMapping("/api/v1/game/")
public class GameController {

    private final Logger logger = LoggerFactory.getLogger(GameController.class);

    private GameService gameService;
    private PlayerService playerService;
    private ValidationService validationService;

    @Autowired
    public GameController(GameService gameService, PlayerService playerService,
                          ValidationService validationService) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.validationService = validationService;
    }

    @PostMapping("/create")
    public ResponseEntity<GameEntity> createGame(@RequestBody Game game) {

        GameEntity gameEntity = gameService.createGameEntity(game);

        //Update Player Stats Involved in the Game
//        playerService.updatePlayers(gameEntity);


        validationService.validate(gameEntity.getId());
        validationService.validateGameWins();
        validationService.validateClutchForGame(gameEntity.getId());
        validationService.validateKnifeKills();
        validationService.validatePlayerTeams();
        validationService.validateTeamDamage();
        validationService.validateHeadToHeadStatistics();

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

    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> deleteGameEntity(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(gameService.deleteGame(id), HttpStatus.ACCEPTED);
    }


}
