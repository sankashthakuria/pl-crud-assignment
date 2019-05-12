package com.powerledger.assignment.backend.powerledgerassignmentbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.powerledger.assignment.backend.powerledgerassignmentbackend.model.Contact;
import com.powerledger.assignment.backend.powerledgerassignmentbackend.repository.ContactRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by @author Sankash on 5/11/2019
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DataValidationTest {
    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactRepository mockRepository;

    @Test
    public void throw_validation_error_on_save_contact_with_incorrect_email_address() throws Exception {

        String contactJson = "{\"id\":1,\"name\":\"sankash.thakuria@gmail.com\",\"email\":\"Sankash\",\"phone\":\"413610699\"}";

        mockMvc.perform(post("/api/v1/contacts")
                .content(contactJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.message", is("Validation Failed")))
                .andExpect(jsonPath("$.details", is("Must provide a valid email address")));

        verify(mockRepository, times(0)).save(any(Contact.class));

    }

    @Test
    public void throw_validation_error_on_save_contact_with_incorrect_phone() throws Exception {

        String contactJson = "{\"id\":1,\"name\":\"sankash\",\"email\":\"sankash.thakuria@gmail.com\",\"phone\":\"0012123123123\"}";

        mockMvc.perform(post("/api/v1/contacts")
                .content(contactJson)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.message", is("Validation Failed")))
                .andExpect(jsonPath("$.details", is("Phone number invalid")));

        verify(mockRepository, times(0)).save(any(Contact.class));

    }
}
