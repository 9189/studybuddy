package at.diplomarbeit.studybuddy.dto;

import at.diplomarbeit.studybuddy.data.entity.Land;
import at.diplomarbeit.studybuddy.data.entity.Sprache;
import at.diplomarbeit.studybuddy.data.entity.Studienrichtung;


public class RegistrierungsOptionsDTO {
    Iterable<Land> laender;
    Iterable<Sprache> sprachen;
    Iterable<Studienrichtung> studienrichtungen;

    public RegistrierungsOptionsDTO(Iterable<Land> laender,
                                    Iterable<Sprache> sprachen,
                                    Iterable<Studienrichtung> studienrichtungen
    ) {
        this.laender = laender;
        this.sprachen = sprachen;
        this.studienrichtungen = studienrichtungen;
    }

    public Iterable<Land> getLaender() {
        return laender;
    }

    public Iterable<Sprache> getSprachen() {
        return sprachen;
    }

    public Iterable<Studienrichtung> getStudienrichtungen() {
        return studienrichtungen;
    }
}
