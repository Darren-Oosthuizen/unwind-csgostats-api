package csgo.stats.parser.csgoapi.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class DashboardStatObject {
    private String playerName;
    private String key;
    private String value;
}
