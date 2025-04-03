package org.gordeser.backend.facade;

import org.gordeser.backend.client.AssociationServiceClient;
import org.gordeser.backend.elasticsearch.PostElasticsearchRepository;
import org.gordeser.backend.entity.User;
import org.gordeser.backend.exception.NotFound;
import org.gordeser.backend.mock.TestEntities;
import org.gordeser.backend.service.TagService;
import org.gordeser.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
class UserFacadeTest {
    @Autowired
    private UserFacade userFacade;
    @MockBean
    private UserService userService;
    @MockBean
    private TagService tagService;
    @MockBean
    private PostFacade postFacade;
    @MockBean
    private PostElasticsearchRepository postElasticsearchRepository;
    @MockBean
    private AssociationServiceClient associationServiceClient;

    @Test
    void testDeleteUserById() throws NotFound {
        User user = TestEntities.getDefaultUser1();
        doNothing().when(tagService).deleteUserFromTags(null, user);
        doNothing().when(userService).deletePreferedTags(user);
        doNothing().when(postFacade).deleteTagsFromPosts(null);
        doNothing().when(userService).deleteById(user.getId());

        userFacade.deleteUserById(user);
        verify(userService, times(1)).deleteById(user.getId());
    }
    @Test
    void testAddTagsToUser(){
        User user = TestEntities.getDefaultUser1();
        when(tagService.getTagsByIds(List.of())).thenReturn(List.of());
        doNothing().when(userService).addTagsToUser(user, List.of());
        doNothing().when(tagService).addUserToTags( List.of(), user);

        userFacade.addTagsToUser(user, List.of());
        verify(userService, times(1)).addTagsToUser(user, List.of());
    }
}
