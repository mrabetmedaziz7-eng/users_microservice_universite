package com.aziz.users;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.aziz.users.entities.Role;
import com.aziz.users.entities.User;
import com.aziz.users.repos.RoleRepository;
import com.aziz.users.service.UserService;

@SpringBootApplication
public class UsersMicroserviceApplication {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(UsersMicroserviceApplication.class, args);
    }

    @PostConstruct
    void init_users() {
        // Ajouter les rôles s'ils n'existent pas
        if (roleRepository.findByRole("ADMIN") == null) {
            userService.addRole(new Role(null, "ADMIN"));
        }
        if (roleRepository.findByRole("USER") == null) {
            userService.addRole(new Role(null, "USER"));
        }

        // Ajouter admin s'il n'existe pas (ADMIN + USER, enabled = true)
        if (userService.findUserByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("123");
            admin.setEmail("admin@gmail.com");
            admin.setEnabled(true);
            userService.saveUser(admin);

            userService.addRoleToUser("admin", "ADMIN");
            userService.addRoleToUser("admin", "USER");
        }

        // Ajouter aziz s'il n'existe pas (USER, enabled = true)
        if (userService.findUserByUsername("aziz") == null) {
            User aziz = new User();
            aziz.setUsername("aziz");
            aziz.setPassword("123");
            aziz.setEmail("aziz@gmail.com");
            aziz.setEnabled(true);
            userService.saveUser(aziz);

            userService.addRoleToUser("aziz", "USER");
        }
    }
}
