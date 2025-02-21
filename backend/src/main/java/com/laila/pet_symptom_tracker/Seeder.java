package com.laila.pet_symptom_tracker;

import com.laila.pet_symptom_tracker.entities.pet.PetRepository;
import com.laila.pet_symptom_tracker.entities.user.User;
import com.laila.pet_symptom_tracker.entities.user.UserService;
import com.laila.pet_symptom_tracker.mainconfig.MockData;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {
  private final PasswordEncoder passwordEncoder;
  private final UserService userService;
  private final PetRepository petRepository;

  @Override
  public void run(String... args) throws Exception {
    seedPets();
    seedUsers();
  }

  private void seedUsers() {
    List<User> users = MockData.getUsers();
    users.forEach(user -> user.setPassword(passwordEncoder.encode("Password123!")));
    users.forEach(userService::createUser);
  }

  private void seedPets() {
    if (!petRepository.findAll().isEmpty()) return;

    petRepository.saveAll(MockData.getPets());
  }
}
