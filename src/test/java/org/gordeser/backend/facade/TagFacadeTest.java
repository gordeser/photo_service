package org.gordeser.backend.facade;

import org.gordeser.backend.client.AssociationServiceClient;
import org.gordeser.backend.elasticsearch.PostElasticsearchRepository;
import org.gordeser.backend.entity.Tag;
import org.gordeser.backend.exception.NotFound;
import org.gordeser.backend.service.PostService;
import org.gordeser.backend.service.TagService;
import org.gordeser.backend.mock.TestEntities;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class TagFacadeTest {
    @Autowired
    private TagFacade tagFacade;
    @MockBean
    private TagService tagService;
    @MockBean
    private PostService postService;
    @MockBean
    private PostElasticsearchRepository postElasticsearchRepository;
    @MockBean
    private AssociationServiceClient associationServiceClient;

    @Test
    void testDeleteByIdExisting() throws Exception {
        Tag tag = TestEntities.getDefaultTag1();
        tag.setPosts(new ArrayList<>());
        when(tagService.getTagById(tag.getId())).thenReturn(tag);
        doNothing().when(tagService).deleteById(tag.getId());
        doNothing().when(postService).deleteTagsFromPost(tag, List.of());

        tagFacade.deleteById(tag.getId());
        verify(postService, times(1)).deleteTagsFromPost(tag, List.of());
        verify(tagService, atLeastOnce()).deleteById(tag.getId());
    }
    @Test
    void testDeleteByIdNonExisting() throws Exception {
        when(tagService.getTagById(1L)).thenReturn(null);
        assertThrows(NotFound.class, () -> tagFacade.deleteById(1L) );
    }
}
