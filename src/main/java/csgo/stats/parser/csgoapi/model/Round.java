package csgo.stats.parser.csgoapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Round {
    private Integer roundNo;
    private Integer winner;
    private Integer reason;
    private List<Kill> kills;
    private List<Damage> damage;
    private List<Flashbang> flashbangs;
    private Integer playerCount;
}
