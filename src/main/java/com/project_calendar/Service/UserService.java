package com.project_calendar.Service;

import com.project_calendar.DTO.UserDTO;
import com.project_calendar.Entity.User;
import com.project_calendar.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerNewUser(UserDTO userDTO) {
        Optional<User> userControl = userRepository.findByEmail(userDTO.getEmail());

        if(userControl.isPresent()){
            throw new RuntimeException("User with this email already exists.");
        }

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole("ROLE_USER");

        userRepository.save(user);
    }
    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(Math.toIntExact(id));
        return user.orElse(null);
    }

    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        Object principal = auth.getPrincipal();
        User user = null;

        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            System.out.println("-" + user);
        }

        return user;
    }
}
