package wsg.lol.dmo.league;


import wsg.lol.common.base.BaseDmo;
import wsg.lol.common.constants.annotation.JsonKey;
import wsg.lol.common.enums.impl.others.DivisionEnum;

public class ItemDmo extends BaseDmo {

    private Integer id;
    private String summonerId;
    private String leagueId;
    private String summonerName;
    private DivisionEnum rank;
    private Integer leaguePoints;
    private Integer wins;
    private Integer losses;
    private Boolean veteran;
    private Boolean inactive;
    private Boolean freshBlood;
    private Boolean hotStreak;

    @JsonKey
    private Integer miniSeries_target;

    @JsonKey
    private Integer miniSeries_wins;

    @JsonKey
    private Integer miniSeries_losses;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(String summonerId) {
        this.summonerId = summonerId;
    }


    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }


    public String getSummonerName() {
        return summonerName;
    }

    public void setSummonerName(String summonerName) {
        this.summonerName = summonerName;
    }

    public Integer getLeaguePoints() {
        return leaguePoints;
    }

    public void setLeaguePoints(Integer leaguePoints) {
        this.leaguePoints = leaguePoints;
    }


    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }


    public Integer getLosses() {
        return losses;
    }

    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    public DivisionEnum getRank() {
        return rank;
    }

    public void setRank(DivisionEnum rank) {
        this.rank = rank;
    }

    public Boolean getVeteran() {
        return veteran;
    }

    public void setVeteran(Boolean veteran) {
        this.veteran = veteran;
    }

    public Boolean getInactive() {
        return inactive;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    public Boolean getFreshBlood() {
        return freshBlood;
    }

    public void setFreshBlood(Boolean freshBlood) {
        this.freshBlood = freshBlood;
    }

    public Boolean getHotStreak() {
        return hotStreak;
    }

    public void setHotStreak(Boolean hotStreak) {
        this.hotStreak = hotStreak;
    }

    public Integer getMiniSeries_target() {
        return miniSeries_target;
    }

    public void setMiniSeries_target(Integer miniSeries_target) {
        this.miniSeries_target = miniSeries_target;
    }

    public Integer getMiniSeries_wins() {
        return miniSeries_wins;
    }

    public void setMiniSeries_wins(Integer miniSeries_wins) {
        this.miniSeries_wins = miniSeries_wins;
    }

    public Integer getMiniSeries_losses() {
        return miniSeries_losses;
    }

    public void setMiniSeries_losses(Integer miniSeries_losses) {
        this.miniSeries_losses = miniSeries_losses;
    }
}
