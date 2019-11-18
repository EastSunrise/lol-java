package wsg.lol.common.pojo.dto.match;

import wsg.lol.common.base.BaseDto;

/**
 * // TODO: (Kingen, 2019/11/18)
 * @author EastSunrise
 */
public class PlayerDto extends BaseDto {

    private String currentPlatformId;
    private String summonerName;
    private String matchHistoryUri;

    /**
     * Original platformId.
     */
    private String platformId;

    /**
     * Player's current accountId (Encrypted)
     */
    private String currentAccountId;
    private int profileIcon;

    /**
     * Player's summonerId (Encrypted)
     */
    private String summonerId;

    /**
     * Player's original accountId (Encrypted)
     */
    private String accountId;

    public String getCurrentPlatformId() {
        return currentPlatformId;
    }

    public void setCurrentPlatformId(String currentPlatformId) {
        this.currentPlatformId = currentPlatformId;
    }

    public String getSummonerName() {
        return summonerName;
    }

    public void setSummonerName(String summonerName) {
        this.summonerName = summonerName;
    }

    public String getMatchHistoryUri() {
        return matchHistoryUri;
    }

    public void setMatchHistoryUri(String matchHistoryUri) {
        this.matchHistoryUri = matchHistoryUri;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getCurrentAccountId() {
        return currentAccountId;
    }

    public void setCurrentAccountId(String currentAccountId) {
        this.currentAccountId = currentAccountId;
    }

    public int getProfileIcon() {
        return profileIcon;
    }

    public void setProfileIcon(int profileIcon) {
        this.profileIcon = profileIcon;
    }

    public String getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(String summonerId) {
        this.summonerId = summonerId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
