package com.surest.member.repository;

import com.surest.member.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsernameOrEmail_ShouldReturnUser_WhenUsernameExists() {
        User user = new User();
        user.setUsername("john");
        user.setName("john");
        user.setEmail("john@example.com");
        user.setPassword("securePassword");
        userRepository.save(user);
        Optional<User> foundUser = userRepository.findByUsernameOrEmail("john", "wrong@example.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("john");
    }

    @Test
    void findByUsernameOrEmail_ShouldReturnUser_WhenEmailExists() {
        User user = new User();
        user.setUsername("john");
        user.setName("john");
        user.setEmail("john@example.com");
        user.setPassword("securePassword");
        userRepository.save(user);
        Optional<User> foundUser = userRepository.findByUsernameOrEmail("wrongUsername", "john@example.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void findByUsernameOrEmail_ShouldReturnEmpty_WhenNeitherExists() {
        Optional<User> foundUser = userRepository.findByUsernameOrEmail("unknown", "unknown@example.com");

        assertThat(foundUser).isNotPresent();
    }
}
