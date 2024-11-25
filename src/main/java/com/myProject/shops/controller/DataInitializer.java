package com.myProject.shops.controller;

import com.myProject.shops.model.Roles;
import com.myProject.shops.model.User;
import com.myProject.shops.repository.RolesRepository;
import com.myProject.shops.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_ADMIN","ROLE_USER");
        //createDefaultUserIfNotExists();
        createDefaultRoleIfNotExists(defaultRoles);
        //createDefaultAdminIfNotExists();
    }

    public void createDefaultUserIfNotExists(){
        Roles roleUser = rolesRepository.findByName("ROLE_USER");
        for (int i=0;i<=5;i++){
            String defaultEmail="user"+i+"@gmail.com";
            if(userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user = new User();
            user.setFirstName("the user");
            user.setLastName("user"+i);
            user.setEmail(defaultEmail);
            user.setRoles(Set.of(roleUser));
            user.setPassword(passwordEncoder.encode("12345"));
            userRepository.save(user);
            System.out.println("the user "+i+" created successfully");

        }
    }

    public void createDefaultAdminIfNotExists(){
        Roles roleAdmin = rolesRepository.findByName("ROLE_ADMIN");
        for (int i=0;i<=5;i++){
            String defaultEmail="admin"+i+"@gmail.com";
            if(userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user = new User();
            user.setFirstName("the admin");
            user.setLastName("admin"+i);
            user.setEmail(defaultEmail);
            user.setRoles(Set.of(roleAdmin));
            user.setPassword(passwordEncoder.encode("12345"));
            userRepository.save(user);
            System.out.println("the admin "+i+" created successfully");

        }
    }

    public void createDefaultRoleIfNotExists(Set<String> roles){
        roles.stream()
                .filter(rolesRepository::existsByName)
                .map(Roles::new).forEach(rolesRepository::save);
    }
}
