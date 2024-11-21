package br.ucsal;

import br.ucsal.domain.users.User;
import br.ucsal.domain.users.UserRole;
import br.ucsal.infrastructure.IUserRepository;
import br.ucsal.service.interfaces.IEncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IEncryptionService encryptionService;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByusername("admin").isEmpty()) {
            var securePassword = encryptionService.encode("admin");
            User admin = new User("Administrador", "admin@admin.com", "admin", securePassword, UserRole.ADMINISTRADOR);
            userRepository.save(admin);
            System.out.println("Default admin created.");
        } else {
            System.out.println("Default admin already exists.");
        }
    }
}