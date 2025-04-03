package org.gordeser.backend.repository;

import jakarta.persistence.EntityManager;
import org.gordeser.backend.client.AssociationServiceClient;
import org.gordeser.backend.elasticsearch.PostElasticsearchRepository;
import org.gordeser.backend.entity.PasswordResetToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PasswordResetTokenRepositoryTest {
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private EntityManager entityManager;
    @MockBean
    private PostElasticsearchRepository postElasticsearchRepository;
    @MockBean
    private AssociationServiceClient associationServiceClient;


    @Test
    void testFindByTokenFailure(){
        Optional<PasswordResetToken> found = passwordResetTokenRepository.findByToken("token1");
        assertFalse(found.isPresent());
    }
}
