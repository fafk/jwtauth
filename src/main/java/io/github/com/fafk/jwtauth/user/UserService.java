package io.github.com.fafk.jwtauth.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public User createUser(String email, String password) {
        String encodedPassword = bCryptPasswordEncoder.encode(password);

        userRepository.findByUsername(email).ifPresent((v) -> {
            throw new UserAlreadyExistsException("User already exits.");
        });

        return userRepository.save(
                User.builder().username(email).password(encodedPassword).build()
        );
    }
}
