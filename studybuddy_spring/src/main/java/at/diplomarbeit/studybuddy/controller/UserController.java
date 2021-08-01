package at.diplomarbeit.studybuddy.controller;

import at.diplomarbeit.studybuddy.data.entity.Inserat;
import at.diplomarbeit.studybuddy.data.entity.User;
import at.diplomarbeit.studybuddy.dto.*;
import at.diplomarbeit.studybuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/login")
    public PersonalDTO login(@RequestBody @Valid LoginDTO loginDTO, HttpServletResponse response) {
        return userService.login(loginDTO.getEmail(), loginDTO.getPasswort(), response).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.FORBIDDEN, "Login Failed"));
    }

    @PostMapping(path = "/registrieren")
    public ResponseEntity<?> registrieren(@RequestBody @Valid RegistrierungsDTO registrierungsDTO) {
        return userService.registrieren(
                registrierungsDTO.getName(),
                registrierungsDTO.getEmail(),
                registrierungsDTO.getPasswort(),
                registrierungsDTO.getBeschreibung(),
                registrierungsDTO.getGebDat(),
                registrierungsDTO.getHerkunftsStadtId(),
                registrierungsDTO.getStudienrichtungId(),
                registrierungsDTO.getSprachenIds()
                ) ? new ResponseEntity<>(HttpStatus.OK) :  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/registrieren/options")
    public RegistrierungsOptionsDTO getOptions() {
        return userService.getRegistrierungsOptions();
    }

    @GetMapping(path = "/refresh")
    public PersonalDTO getNewToken(HttpServletRequest request, HttpServletResponse response) {
        return userService.refresh(request, response).orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Refresh not possible"));
    }

    @GetMapping(path = "/me")
    public PersonalDTO getMe(HttpServletRequest request) {
        return userService.getMe(request);
    }

    @GetMapping(path = "/user-requested-my-inserat/{userId}")
    public boolean userRequestedMyInserat(HttpServletRequest request, @PathVariable Long userId) {
        return userService.userRequestedMyInserat(request, userId);
    }

    @GetMapping(path = "/gemerkt")
    public Iterable<Inserat> getGemerkteInserate(HttpServletRequest request) {
        return userService.getGemerkteInserate(request);
    }

    @GetMapping(path = "/check",params = "email")
    public boolean emailInUse(@RequestParam String email) {
        return userService.emailInUse(email);
    }

    @GetMapping(path = "/user/{id}")
    public Optional<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping(path = "/user/add-contact/{id}")
    public PersonalDTO addContact(HttpServletRequest request, @PathVariable(name = "id") Long cid) {
        return userService.addContact(request, cid);
    }
}
