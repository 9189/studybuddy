package at.diplomarbeit.studybuddy.controller;

import at.diplomarbeit.studybuddy.data.entity.Land;
import at.diplomarbeit.studybuddy.data.repository.LandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "/api/laender")
public class LandController {
    private final LandRepository landRepository;

    @Autowired
    public LandController(LandRepository landRepository) {
        this.landRepository = landRepository;
    }

    @GetMapping
    public Iterable<Land> getLaender() {
        return landRepository.findAll();
    }

    @GetMapping(params = "name")
    public Land getLandByName(@RequestParam String name) {
        return landRepository.getLandByName(name);
    }
}
