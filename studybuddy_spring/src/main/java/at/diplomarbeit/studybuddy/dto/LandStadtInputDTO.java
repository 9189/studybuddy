package at.diplomarbeit.studybuddy.dto;

import at.diplomarbeit.studybuddy.data.entity.Land;
import at.diplomarbeit.studybuddy.data.entity.Stadt;

import java.util.Set;

public class LandStadtInputDTO {
    private String land;
    private String stadt;

    public LandStadtInputDTO(String stadt, String land) {
        this.land = land;
        this.stadt = stadt;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public String getStadt() {
        return stadt;
    }

    public void setStadt(String stadt) {
        this.stadt = stadt;
    }
}
