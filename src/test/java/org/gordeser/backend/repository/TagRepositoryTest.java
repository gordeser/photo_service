package org.gordeser.backend.repository;

import org.gordeser.backend.client.AssociationServiceClient;
import org.gordeser.backend.elasticsearch.PostElasticsearchRepository;
import org.gordeser.backend.entity.Tag;
import org.gordeser.backend.mock.TestEntities;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
class TagRepositoryTest {
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TestEntityManager entityManager;
    @MockBean
    private PostElasticsearchRepository postElasticsearchRepository;
    @MockBean
    private AssociationServiceClient associationServiceClient;

    @Test
    void testFindByName() {
        Tag tag = TestEntities.getDefaultTag1();
        entityManager.persist(tag);
        entityManager.flush();

        Tag findExisting = (Tag)tagRepository.findByName("tag1").orElse(null);
        assertNotNull(findExisting);
        assert(findExisting.getName().equals(tag.getName()));

        Tag notFound = (Tag)tagRepository.findByName("Not Found").orElse(null);
        assertNull(notFound);

    }
}
