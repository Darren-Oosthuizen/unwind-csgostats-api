package csgo.stats.parser.csgoapi.controller.v1;

import csgo.stats.parser.csgoapi.model.response.DashboardStats;
import csgo.stats.parser.csgoapi.services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"https://unwindcsgo.flystudio.co.za", "http://localhost:4200"}, maxAge = 3600)
@RestController
@RequestMapping("/api/v1/dashboard/")
public class DashboardController {

    private DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }



    @GetMapping("/all")
    public ResponseEntity<DashboardStats> getDashboard() {

        return new ResponseEntity<>(dashboardService.getDashboardStatistics(), HttpStatus.OK);
    }
}
