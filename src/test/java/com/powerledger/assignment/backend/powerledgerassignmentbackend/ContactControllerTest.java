package com.powerledger.assignment.backend.powerledgerassignmentbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.powerledger.assignment.backend.powerledgerassignmentbackend.model.Contact;
import com.powerledger.assignment.backend.powerledgerassignmentbackend.repository.ContactRepository;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by @author Sankash on 5/11/2019
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ContactControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ContactRepository mockRepository;

    @Before
    public void init() {
        Contact contact = new Contact(1L, "Sankash", "sankash.thakuria@gmail.com", "0123123123");
        when(mockRepository.findById(1L)).thenReturn(Optional.of(contact));
    }

    @Test
    public void find_contactId_OK() throws JSONException {

        String expected = "{id:1,name:\"Sankash\",email:\"sankash.thakuria@gmail.com\",phone:\"0123123123\"}";

        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/contacts/1", String.class);
        System.out.println(response.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, response.getHeaders().getContentType());

        JSONAssert.assertEquals(expected, response.getBody(), false);

        verify(mockRepository, times(1)).findById(1L);

    }

    @Test
    public void find_allContacts_OK() throws Exception {

        List<Contact> contacts = Arrays.asList(
                new Contact(2L, "John", "john@gmail.com", "111111111"),
                new Contact(3L, "Jane", "jane@gmail.com", "222222222"));

        when(mockRepository.findAll()).thenReturn(contacts);

        String expected = om.writeValueAsString(contacts);

        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/contacts", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);

        verify(mockRepository, times(1)).findAll();
    }

    @Test
    public void find_contactIdNotFound_404() throws Exception {

        String expected = "{message:\"Contact not found for this id :: 5\",details:\"uri=/api/v1/contacts/5\"}";

        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/contacts/5", String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);

    }

    @Test
    public void save_contact_OK() throws Exception {

        Contact newContact = new Contact(100L, "John", "john@gmail.com", "413610699");
        when(mockRepository.save(any(Contact.class))).thenReturn(newContact);

        String expected = om.writeValueAsString(newContact);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/contacts", newContact, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);

        verify(mockRepository, times(1)).save(any(Contact.class));

    }

    @Test
    public void delete_contact_OK() {

        doNothing().when(mockRepository).deleteById(1L);

        HttpEntity<String> entity = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<String> response = restTemplate.exchange("/api/v1/contacts/1", HttpMethod.DELETE, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(mockRepository, times(1)).deleteById(1L);
    }

    @Test
    public void delete_contactIdNotFound_404() throws Exception {

        String expected = "{message:\"Contact not found for this id :: 100\",details:\"uri=/api/v1/contacts/100\"}";

        HttpEntity<String> entity = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<String> response = restTemplate.exchange("/api/v1/contacts/100", HttpMethod.DELETE, entity, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        JSONAssert.assertEquals(expected, response.getBody(), false);

    }

    @Test
    public void update_contact_OK() throws Exception {

        Contact updateContact = new Contact(1L, "test", "abc@hotmail.com", "413610699");
        when(mockRepository.save(any(Contact.class))).thenReturn(updateContact);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(om.writeValueAsString(updateContact), headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/v1/contacts/1", HttpMethod.PUT, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(om.writeValueAsString(updateContact), response.getBody(), false);

        verify(mockRepository, times(1)).findById(1L);
        verify(mockRepository, times(1)).save(any(Contact.class));

    }

}
