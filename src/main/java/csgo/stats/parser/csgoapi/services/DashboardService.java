package csgo.stats.parser.csgoapi.services;

import csgo.stats.parser.csgoapi.model.response.DashboardStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private PlayerService playerService;
    private GameService gameService;

    @Autowired
    public DashboardService(PlayerService playerService, GameService gameService) {
        this.playerService = playerService;
        this.gameService = gameService;
    }

    public DashboardStats getDashboardStatistics() {
        return playerService.getDashboardStats();
    }
}
