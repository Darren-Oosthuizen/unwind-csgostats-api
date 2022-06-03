package csgo.stats.parser.csgoapi.repository.entities;

public interface PlayerKillsAndHeadshots {
    Long getId();
    String getName();
    String getGunsName();
    Integer getGunsKills();
    Integer getGunsHeadshots();
    Integer getGunsDamage();
}
