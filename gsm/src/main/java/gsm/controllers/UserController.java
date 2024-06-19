package gsm.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gsm.dto.UserRegistrationRequest;
import gsm.dto.VerificationRequest;
import gsm.entities.User;
import gsm.repositories.UserRepository;
import gsm.service.SmsService;
import gsm.service.UserService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private UserRepository userRepository;

    private Map<String, UserRegistrationRequest> registeredUserDetails = new HashMap<>();
    private Map<String, String> verificationCodes = new HashMap<>();

    private String generateVerificationCode() {
        // Generate a random 6-digit verification code
        return String.format("%06d", new Random().nextInt(999999));
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/connexion")
    public ResponseEntity<?> getUserByTelAndPassword(@RequestBody User user) {
        try {
            String tel = user.getTel();
            String password = user.getPassword();
            System.out.println("Received request with Nmr°: " + tel + " and password: " + password);
            User conxuser = userService.getUserByMailAndPassword(tel, password);

            if (conxuser != null) {
                return new ResponseEntity<>(conxuser, HttpStatus.OK);
            } else {
                System.out.println("User not found");
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id) {
        User user = userService.findUserById(id);

        if (user == null) {
            return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest request) {
        try {
            // Check if the phone number already exists
            Optional<User> existingUser = userRepository.findByTel(request.getTel());
            if (existingUser.isPresent()) {
                // Return error response if the phone number is already registered
                Map<String, String> response = new HashMap<>();
                response.put("error", "Phone number already registered");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Log incoming request
            System.out.println("Received registration request for tel: " + request.getTel());

            // Generate verification code
            String verificationCode = generateVerificationCode();
            System.out.println("Generated verification code: " + verificationCode);

            // Send verification code via SMS
            smsService.sendSms(request.getTel(), "Your verification code is: " + verificationCode);

            // Store user registration request and verification code in memory
            registeredUserDetails.put(request.getTel(), request);
            verificationCodes.put(request.getTel(), verificationCode);

            // Log success
            System.out.println("Verification code sent successfully to tel: " + request.getTel());

            // Return success response
            Map<String, String> response = new HashMap<>();
            response.put("message", "Verification code sent successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Log error
            System.err.println("Error in registration process: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to send verification code: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyUser(@RequestBody VerificationRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            // Log incoming request
            System.out.println("Verification request received for tel: " + request.getTel() + ", code: " + request.getVerificationCode());

            // Retrieve the stored verification code
            String storedVerificationCode = verificationCodes.get(request.getTel());
            System.out.println("Stored verification code: " + storedVerificationCode);

            // Validate verification code
            if (storedVerificationCode != null && storedVerificationCode.equals(request.getVerificationCode())) {
                // Verification code matches, create user
                UserRegistrationRequest userDetails = registeredUserDetails.get(request.getTel());
                if (userDetails != null) {
                    User user = new User();
                    user.setTel(request.getTel());
                    user.setPassword(userDetails.getPassword());
                    user.setRole(userDetails.getRole());

                    // Set name and last name
                    user.setName(userDetails.getName());
                    user.setLastName(userDetails.getLastName());

                    // Save user object to the database
                    userService.createUser(user);

                    // Remove verification code and user details from memory
                    verificationCodes.remove(request.getTel());
                    registeredUserDetails.remove(request.getTel());

                    // Return success response
                    response.put("message", "User account created successfully");
                    return ResponseEntity.ok(response);
                } else {
                    // Log error
                    System.out.println("No user registration request found for tel: " + request.getTel());
                    response.put("error", "User registration request not found");
                    return ResponseEntity.badRequest().body(response);
                }
            } else {
                // Verification code does not match
                // Log invalid verification code
                System.out.println("Invalid verification code");
                response.put("error", "Invalid verification code");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            // Log error
            System.out.println("Error verifying user: " + e.getMessage());
            response.put("error", "Failed to verify user: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping(path = "/supprimer/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setName(updatedUser.getName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setTel(updatedUser.getTel());
            existingUser.setRole(updatedUser.getRole());
            User updatedUtilisateur = userRepository.save(existingUser);
            return ResponseEntity.ok(updatedUtilisateur);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
