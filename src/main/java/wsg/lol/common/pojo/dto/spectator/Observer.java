package wsg.lol.common.pojo.dto.spectator;

/**
 * // TODO: (Kingen, 2019/11/18)
 * @author EastSunrise
 */
public class Observer {

    /**
     * Key used to decrypt the spectator grid game data for playback
     */
    private String encryptionKey;

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }
}
