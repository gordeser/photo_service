package org.gordeser.backend.controller;

import org.gordeser.backend.client.AssociationServiceClient;
import org.gordeser.backend.dto.PasswordResetRequestDTO;
import org.gordeser.backend.elasticsearch.PostElasticsearchRepository;
import org.gordeser.backend.exception.ConfirmPasswordIsNotEqual;
import org.gordeser.backend.exception.NotFound;
import org.gordeser.backend.exception.TokenIsNotValid;
import org.gordeser.backend.service.PasswordResetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;


@SpringBootTest
class PasswordResetControllerTest {
    @MockBean
    private PasswordResetService passwordResetService;
    @Autowired
    private PasswordResetController passwordResetController;
    @MockBean
    private PostElasticsearchRepository postElasticsearchRepository;
    @MockBean
    private AssociationServiceClient associationServiceClient;

    @Test
    void testResetPasswordRequest() throws NotFound {
        String email = "test@test.com";
        doNothing().when(passwordResetService).resetPasswordRequest(email);
        ResponseEntity<String> response = passwordResetController.resetPasswordRequest(email);
        assertEquals(200, response.getStatusCode().value());
    }
    @Test
    void testResetPassword() throws NotFound, TokenIsNotValid, ConfirmPasswordIsNotEqual {
        String email = "test@test.com";
        PasswordResetRequestDTO requestDTO = new PasswordResetRequestDTO(email, email, email);
        doNothing().when(passwordResetService).resetPassword(requestDTO.getToken(), requestDTO.getPassword(), requestDTO.getPassword());
        ResponseEntity<String> response = passwordResetController.resetPassword(requestDTO);
        assertEquals(200, response.getStatusCode().value());
    }
}
