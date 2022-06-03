package csgo.stats.parser.csgoapi.controller.v1;

import csgo.stats.parser.csgoapi.repository.entities.GunName;
import csgo.stats.parser.csgoapi.services.GunService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gun/")
public class GunController {

    private final Logger logger = LoggerFactory.getLogger(GunController.class);

    private GunService gunService;

    @Autowired
    public GunController(GunService gunService) {
        this.gunService = gunService;
    }

    @GetMapping("/distinct/name")
    public ResponseEntity<List<GunName>> getDistinctGunNames() {
        return new ResponseEntity<>(gunService.findDistinctGunNames(), HttpStatus.OK);
    }
}
