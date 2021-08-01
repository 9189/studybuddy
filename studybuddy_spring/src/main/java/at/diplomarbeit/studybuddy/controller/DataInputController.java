package at.diplomarbeit.studybuddy.controller;

import at.diplomarbeit.studybuddy.data.entity.Land;
import at.diplomarbeit.studybuddy.data.entity.Sprache;
import at.diplomarbeit.studybuddy.data.entity.Stadt;
import at.diplomarbeit.studybuddy.data.entity.Studienrichtung;
import at.diplomarbeit.studybuddy.data.repository.LandRepository;
import at.diplomarbeit.studybuddy.data.repository.SpracheRepository;
import at.diplomarbeit.studybuddy.data.repository.StadtRepository;
import at.diplomarbeit.studybuddy.data.repository.StudienrichtungRepository;
import at.diplomarbeit.studybuddy.dto.LandStadtInputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Dieser Controller ist nur zum einfügen der Location Daten

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "/api/input")
public class DataInputController {

    private final LandRepository landRepository;
    private final StadtRepository stadtRepository;
    private final StudienrichtungRepository studienrichtungRepository;
    private final SpracheRepository spracheRepository;

    @Autowired
    public DataInputController(LandRepository landRepository,
                               StadtRepository stadtRepository,
                               StudienrichtungRepository studienrichtungRepository,
                               SpracheRepository spracheRepository) {
        this.landRepository = landRepository;
        this.stadtRepository = stadtRepository;
        this.studienrichtungRepository = studienrichtungRepository;
        this.spracheRepository = spracheRepository;
    }

    @PostMapping(path = "/location")
    public void inputLocationData(@RequestBody List<LandStadtInputDTO> landStadtInputDTOS) {
        for (LandStadtInputDTO landStadtInputDTO : landStadtInputDTOS) {
            if(!landRepository.existsByName(landStadtInputDTO.getLand())) {
                landRepository.save(new Land(landStadtInputDTO.getLand()));
            }

            if(!stadtRepository.existsByName(landStadtInputDTO.getStadt())) {
                stadtRepository.save(new Stadt(landStadtInputDTO.getStadt(),
                        landRepository.getLandByName(landStadtInputDTO.getLand())));
            }
        }
    }

    @PostMapping(path = "/studien")
    public void inputStudienData(@RequestBody List<Studienrichtung> studienrichtungen) {
        for(Studienrichtung studienrichtung : studienrichtungen) {
            if(!studienrichtungRepository.existsByBezeichnungDeutsch(studienrichtung.getBezeichnungDeutsch())) {
                studienrichtungRepository.save(studienrichtung);
            }
        }
    }

    @PostMapping(path = "/sprachen")
    public void inputSprachen(@RequestBody List<Sprache> sprachen) {
        for(Sprache sprache : sprachen) {
            spracheRepository.save(sprache);
        }
    }
}
