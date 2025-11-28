package com.surest.member.repository;

import com.surest.member.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void findByName_ShouldReturnRole_WhenRoleExists() {
        Role role = new Role();
        role.setName("ADMIN");
        roleRepository.save(role);
        Role foundRole = roleRepository.findByName("ADMIN");
        assertThat(foundRole).isNotNull();
    }

    @Test
    void findByName_ShouldReturnNull_WhenRoleDoesNotExist() {
        Role foundRole = roleRepository.findByName("NON_EXISTENT");
        assertThat(foundRole).isNull();
    }
}
