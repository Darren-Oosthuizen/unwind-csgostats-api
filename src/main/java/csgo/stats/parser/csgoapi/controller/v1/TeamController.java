package csgo.stats.parser.csgoapi.controller.v1;

import csgo.stats.parser.csgoapi.model.response.TeamResponse;
import csgo.stats.parser.csgoapi.services.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"https://unwindcsgo.flystudio.co.za", "http://localhost:4200"}, maxAge = 3600)
@RestController
@RequestMapping("/api/v1/teams/")
public class TeamController {
    private final Logger logger = LoggerFactory.getLogger(TeamController.class);

    private TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }


    @GetMapping("/name/{name}")
    public ResponseEntity<TeamResponse> getTeamByTeamName(@PathVariable(value = "name") String name) {
        return new ResponseEntity<>(teamService.getTeamByTeamName(name), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TeamResponse>> getAllTeams() {
        return new ResponseEntity<>(teamService.getAllTeams(), HttpStatus.OK);
    }

}
