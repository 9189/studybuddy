package at.diplomarbeit.studybuddy.controller;

import at.diplomarbeit.studybuddy.data.entity.Inserat;
import at.diplomarbeit.studybuddy.data.repository.InseratRepository;
import at.diplomarbeit.studybuddy.dto.InseratInputDTO;
import at.diplomarbeit.studybuddy.service.InseratService;
import at.diplomarbeit.studybuddy.exception.InseratNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "/api/inserate")
public class InseratController {
    private final InseratRepository inseratRepository;
    private final InseratService inseratService;

    @Autowired
    public InseratController(InseratRepository inseratRepository,
                             InseratService inseratService) {
        this.inseratRepository = inseratRepository;
        this.inseratService = inseratService;
    }

    @PostMapping
    public void addInserat(@RequestBody InseratInputDTO inseratDTO, HttpServletRequest request) {
        inseratService.addInserat(inseratDTO, request);
    }

    @PostMapping(path = "/{id}")
    public void inseratAnfrage(HttpServletRequest request, @PathVariable Long id) {
        inseratService.inseratAnfrage(request, id);
    }

    @PostMapping(path = "/merken/{id}")
    public void inseratMerken(@PathVariable Long id, HttpServletRequest request) {
        inseratService.inseratMerken(id, request);
    }

    @DeleteMapping(path = "/entmerken/{id}")
    public void inseratEntmerken(@PathVariable Long id, HttpServletRequest request) {
        inseratService.inseratEntmerken(id, request);
    }

    @GetMapping(path = "/normal")
    public Iterable<Inserat> getInserate() {
        return inseratRepository.findAll();
    }

    @GetMapping(path = "/paged")
    public Iterable<Inserat> getInserate(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        return inseratRepository.findAll(pageable).getContent();
    }

    @GetMapping
    public Iterable<Inserat> getFiltered(@RequestParam(name = "land", required = false) Long landId,
                                         @RequestParam(name = "stadt", required = false) Long stadtId,
                                         @RequestParam(name = "von", required = false)
                                             @DateTimeFormat(pattern = "dd.MM.yyyy") Date vonDat,
                                         @RequestParam(name = "bis", required = false)
                                             @DateTimeFormat(pattern = "dd.MM.yyyy") Date bisDat,
                                         @RequestParam(name = "page") Integer pageNo) {
        Sort sort = Sort.by("inseratId");
        //if(order.equals("asc")) sort.ascending();
        Pageable pageable = PageRequest.of(pageNo, 5, sort);

        return inseratService.search(landId, stadtId, vonDat, bisDat, pageable);
    }

    @GetMapping(path = "/mine")
    public Inserat getMine(HttpServletRequest request) {
        return inseratService.getMine(request).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND
        ));
    }

    @GetMapping(path = "/reqmine/{id}")
    public Boolean hasRequestedToJoinMine(HttpServletRequest request, @PathVariable Long id) {
        return inseratService.hasRequestedToJoinMine(request, id);
    }

    @GetMapping(path = "/{id}")
    public Inserat getInserat(@PathVariable Long id) {
        return inseratRepository.findById(id).orElseThrow(() -> new InseratNotFoundException(id));
    }

    @PutMapping(path = "/{id}")
    public Inserat replaceInserat(@RequestBody InseratInputDTO newInserat, @PathVariable Long id) {
        return inseratService.replaceInserat(newInserat, id);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        inseratRepository.deleteById(id);
    }


    @PostMapping(path = "/add-to-gruppe/{uid}")
    public void addToGruppe(HttpServletRequest request, @PathVariable Long uid) {
        inseratService.addToGruppe(request, uid);
    }

    @DeleteMapping(path = "/remove-from-gruppe/{uid}")
    public void removeFromGruppe(HttpServletRequest request, @PathVariable Long uid) {
        inseratService.removeFromGruppe(request, uid);
    }
}
