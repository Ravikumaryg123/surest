package com.surest.member.repository;

import com.surest.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void existsByEmail_ShouldReturnTrue_WhenEmailExists() {
        Member member = new Member();
        member.setFirstName("John");
        member.setLastName("Doe");
        member.setEmail("john@example.com");
        member.setDateOfBirth(LocalDate.of(1990, 1, 1));

        memberRepository.save(member);
        boolean exists = memberRepository.existsByEmail("john@example.com");
        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmail_ShouldReturnFalse_WhenEmailDoesNotExist() {
        boolean exists = memberRepository.existsByEmail("nonexistent@example.com");
        assertThat(exists).isFalse();
    }
}
