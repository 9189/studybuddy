package at.diplomarbeit.studybuddy.dto;

import java.util.Date;

public class InseratInputDTO {
    private Long zielStadtId;
    private String uni;
    private int maxUser;
    private String beschreibung;
    private Date vonDat;
    private Date bisDat;
    private Long erstellerId;

    public InseratInputDTO(Long zielStadtId, String uni, int maxUser, String beschreibung, Date vonDat, Date bisDat, Long erstellerId) {
        this.zielStadtId = zielStadtId;
        this.uni = uni;
        this.maxUser = maxUser;
        this.beschreibung = beschreibung;
        this.vonDat = vonDat;
        this.bisDat = bisDat;
        this.erstellerId = erstellerId;
    }

    public Long getZielStadtId() {
        return zielStadtId;
    }

    public String getUni() {
        return uni;
    }

    public int getMaxUser() {
        return maxUser;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public Date getVonDat() {
        return vonDat;
    }

    public Date getBisDat() {
        return bisDat;
    }

    public Long getErstellerId() {
        return erstellerId;
    }
}
