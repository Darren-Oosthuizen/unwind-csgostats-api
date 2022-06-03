package csgo.stats.parser.csgoapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PLAYER_DUEL_STATISTICS")
public class DuelStatisticsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TOTAL_DUELS")
    private Integer totalDuels = 0;
    @Column(name = "TOTAL_WON_DUELS")
    private Integer totalWonDuels = 0;
    @Column(name = "TOTAL_LOST_DUELS")
    private Integer totalLostDuels = 0;
}
