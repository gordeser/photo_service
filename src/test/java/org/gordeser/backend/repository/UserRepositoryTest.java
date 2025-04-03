package org.gordeser.backend.repository;

import org.gordeser.backend.client.AssociationServiceClient;
import org.gordeser.backend.elasticsearch.PostElasticsearchRepository;
import org.gordeser.backend.entity.User;
import org.gordeser.backend.mock.TestEntities;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;
    @MockBean
    private PostElasticsearchRepository postElasticsearchRepository;
    @MockBean
    private AssociationServiceClient associationServiceClient;

    @Test
    void testFindByUsername() {
        User user = TestEntities.getDefaultUser1();
        entityManager.persist(user);
        entityManager.flush();

        User foundExisting = userRepository.findByUsername("john_doe").orElse(null);
        assertNotNull(foundExisting);
        assertEquals("john_doe", foundExisting.getUsername());
        assertEquals("john.doe@example.com", foundExisting.getEmail());

        User notFound = userRepository.findByUsername("doradura").orElse(null);
        assertNull(notFound);
    }
    @Test
    void testFindByEmail() {
        User user = TestEntities.getDefaultUser1();
        entityManager.persist(user);
        entityManager.flush();

        User foundExisting = userRepository.findByEmail("john.doe@example.com").orElse(null);
        assertNotNull(foundExisting);
        assertEquals("john_doe", foundExisting.getUsername());
        assertEquals("john.doe@example.com", foundExisting.getEmail());

        User notFound = userRepository.findByEmail("unknown@example.com").orElse(null);
        assertNull(notFound);
    }
}
