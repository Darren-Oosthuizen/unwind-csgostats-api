package csgo.stats.parser.csgoapi.repository.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "ROUND_FLASHBANG")
public class RoundFlashbangEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "ATTACKER_STEAM_ID")
    private String attackerSteamId;
    @Column(name = "VICTIM_STEAM_ID")
    private String victimSteamId;
    @Column(name = "DURATION")
    private Double duration;
    @Column(name = "FRIENDLY")
    private Boolean friendly;
}
