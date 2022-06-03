package csgo.stats.parser.csgoapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Gun {
    private String name;
    private Integer kills;
    private Integer headshots;
    private Long damage;
    private Long teamDamage;
}
