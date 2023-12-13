package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.LoginDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.RegisterDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.SuccessMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.BlacklistedJWTToken;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Role;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.RoleRepository;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.TokenBlacklistRepository;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.UserRepository;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.security.DeleteExpiredToken;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.security.FoxUserDetails;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.security.JwtTokenService;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.utils.SendGridService;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.utils.UnverifiedUserRemove;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final DeleteExpiredToken deleteExpiredToken;
    private final SendGridService sendGridService;
    private final UnverifiedUserRemove unverifiedUserRemove;

    @Override
    public Optional<User> findByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    @Override
    public Optional<User> findByLoginDTO(LoginDTO loginDTO) {
        if (loginDTO.getUsername() != null) {
            return userRepository.findByUsername(loginDTO.getUsername());
        }
        return userRepository.findByEmail(loginDTO.getEmail());
    }

    @Override
    public boolean checkPassword(User user, String password) {
        if (passwordEncoder.matches(password, user.getPassword())) {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public ResponseEntity<?> nullCheckLogin(LoginDTO loginDTO) {
        List<String> errors = new ArrayList<>();
        if (loginDTO.getEmail() == null && loginDTO.getUsername() == null) {
            errors.add("Please, provide username or email for your authentication.");
        }
        if (loginDTO.getEmail() != null && loginDTO.getUsername() != null) {
            errors.add("Please, choose only one identification data - email or username.  ");
        }
        if (loginDTO.getPassword() == null) {
            errors.add("There is missing password in your login request.");
        }
        if (!errors.isEmpty()) {
            String message = String.join(" ", errors);
            return ResponseEntity.badRequest().body(new ErrorMessageDTO(message));
        }
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> registrationNullCheck(RegisterDto registerDto) {
        List<String> missingProperties = new ArrayList<>();
        if (registerDto.getUsername() == null) {
            missingProperties.add("username");
        }
        if (registerDto.getEmail() == null) {
            missingProperties.add("email");
        }
        if (registerDto.getPassword() == null) {
            missingProperties.add("password");
        }
        if (registerDto.getDateOfBirth() == null) {
            missingProperties.add("date of birth");
        }
        if (!missingProperties.isEmpty()) {
            String message = "There are missing some data in your request: ".concat(String.join(", ", missingProperties)).concat(".");
            return ResponseEntity.badRequest().body(new ErrorMessageDTO(message));
        }
        return ResponseEntity.ok().build();
    }

    public boolean doesUsernameAlreadyExist(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean doesEmailAlreadyExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public void constructAndSaveUser(RegisterDto registerDto) {
        User user = new User();
        user.setUsername(registerDto.getUsername());
        if (registerDto.getFirstName() != null) {
            user.setFirstName(registerDto.getFirstName());
        }
        if (registerDto.getLastName() != null) {
            user.setLastName(registerDto.getLastName());
        }
        user.setDateOfBirth(registerDto.getDateOfBirth());
        user.setRegistrationDate(LocalDateTime.now());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRole(roleRepository.findByRoleName("USER").get());
        userRepository.save(user);
        sendGridService.sendVerificationEmail(user);
        unverifiedUserRemove.deleteUnverifiedUser(user);
    }

    /**
     * The below method is integrated in our custom logout endpoint.
     * After custom logic (blacklisting token),
     * it invokes its own Spring Security’s SecurityContextLogoutHandler,
     * which clears the authentication details from the security context
     * and invalidates the http session
     */
    public void handleSecurityContextAndBlacklistToken() {
        String jwtToken = jwtTokenService.resolveToken(httpServletRequest);
        if (jwtToken != null) {
            BlacklistedJWTToken blacklistedJWTToken = new BlacklistedJWTToken(jwtToken);
            tokenBlacklistRepository.save(blacklistedJWTToken);
            deleteExpiredToken.deleteAfterExpiration(jwtToken);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

        logoutHandler.logout(httpServletRequest, httpServletResponse, authentication);
    }

    /**
     * Retrieves the currently authenticated user from the SecurityContextHolder.
     *
     * @return The User object associated with the authenticated user.
     */

    public User getUserFromSecurityContextHolder() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username).get();
    }

    /**
     * Checks the role of the currently authenticated user.
     *
     * @return The roleName of the user, or null if the user is not authenticated
     * or if an error occurs while retrieving the role.
     * If the user is not authenticated, sets the role to "VISITOR" and returns it.
     */
    public String checkUserRole() {
        var user = (FoxUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user == null) {
            Optional<Role> roleOptional = roleRepository.findByRoleName("VISITOR");
            String visitor = roleOptional.map(Role::getRoleName).orElse(null);
            if (visitor != null) {
                user.setRoleName(visitor);
                return visitor;
            } else {
                return null;
            }
        } else if (user instanceof FoxUserDetails) {
            return user.getRoleName();
        } else {
            return null;
        }
    }

    /**
     * Retrieves a user by their username from the repository.
     *
     * @param username The username of the user to retrieve.
     * @return The user with the specified username, or null if not found.
     */
    @Override
    public User getUserByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        return userOpt.orElse(null);
    }

    /**
     * Verifies a user's email based on the provided user ID and verification token.
     * <p>
     * This method attempts to verify a user's email by checking the validity of the user ID and verification
     * token. If the user is found and the token is valid, the user's account is marked as verified in the
     * repository. A success response is returned if the verification is successful; otherwise, an error
     * response is returned with appropriate details.
     *
     * @param userId The unique identifier of the user whose email is to be verified.
     * @param token  The verification token associated with the user's email verification process.
     * @return A ResponseEntity representing the result of the email verification process.
     * If successful, returns an OK response with a success message.
     * If unsuccessful, returns a bad request response with an error message indicating the reason.
     */
    @Override
    public ResponseEntity<?> verifyUserEmail(Long userId, String token) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("The user Id in your verification link is invalid. Try to register again."));
        }
        User user = userOpt.get();
        if (!passwordEncoder.matches(user.getUsername(), token)) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("The token in your verification link is invalid. Try to register again."));
        }
        user.setVerified(true);
        userRepository.save(user);
        return ResponseEntity.ok().body(new SuccessMessageDTO("Your user account was successfully activated."));
    }
}
