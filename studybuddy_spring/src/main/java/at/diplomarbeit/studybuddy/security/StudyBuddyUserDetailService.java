package at.diplomarbeit.studybuddy.security;

import at.diplomarbeit.studybuddy.data.entity.User;
import at.diplomarbeit.studybuddy.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.security.core.userdetails.User.withUsername;

@Component
public class StudyBuddyUserDetailService implements UserDetailsService {
    private UserRepository userRepository;
    private JwtProvider jwtProvider;

    @Autowired
    public StudyBuddyUserDetailService(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(s).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User mit email %s existiert nicht", s)));

        return withUsername(user.getEmail())
                .password(user.getPasswort())
                .authorities(new SimpleGrantedAuthority("USER"))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    public Optional<UserDetails> loadUserByJwtToken(String jwtToken) {
        if (jwtProvider.isValidToken(jwtToken)) {
            return Optional.of(
                    withUsername(jwtProvider.getEmail(jwtToken))
                            .authorities(new SimpleGrantedAuthority("USER"))
                            .password("") //token does not have password but field may not be empty
                            .accountExpired(false)
                            .accountLocked(false)
                            .credentialsExpired(false)
                            .disabled(false)
                            .build());
        }
        return Optional.empty();
    }

    public Optional<UserDetails> loadUserByJwtTokenAndDatabase(String jwtToken) {
        if (jwtProvider.isValidToken(jwtToken)) {
            return Optional.of(loadUserByUsername(jwtProvider.getEmail(jwtToken)));
        } else {
            return Optional.empty();
        }
    }
}
