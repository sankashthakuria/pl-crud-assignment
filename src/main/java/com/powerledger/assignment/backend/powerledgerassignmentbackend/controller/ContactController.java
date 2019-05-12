package com.powerledger.assignment.backend.powerledgerassignmentbackend.controller;

import com.powerledger.assignment.backend.powerledgerassignmentbackend.exception.ResourceNotFoundException;
import com.powerledger.assignment.backend.powerledgerassignmentbackend.model.Contact;
import com.powerledger.assignment.backend.powerledgerassignmentbackend.repository.ContactRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * Created by @author Sankash on 5/11/2019
 */
@RestController
@RequestMapping("/api/v1")

public class ContactController {
    @Autowired
    private ContactRepository repository;

    @ApiOperation(value = "View a list of available contacts", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 201, message = "Resource created in the backend"),
            @ApiResponse(code = 400, message = "Bad request, please check your request"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping(path = {"/contacts"})
    public List<Contact> findAll() {
        return repository.findAll();
    }


    @ApiOperation(value = "Get a contact by Id")
    @GetMapping(path = {"/contacts/{id}"})
    public Contact findById(
            @ApiParam(value = "Id to fetch a contact", required = true)
            @Valid @Min(1) @PathVariable long id) throws ResourceNotFoundException {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contact not found for this id :: " + id));
    }


    @ApiOperation(value = "Add a contact")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = {"/contacts"})
    public Contact create(
            @ApiParam(value = "Add a contact", required = true)
            @RequestBody @Valid Contact contact) {
        return repository.save(contact);
    }


    @ApiOperation(value = "Update a contact")
    @PutMapping(value = "/contacts/{id}")
    public Contact update(@ApiParam(value = "Id to update contact", required = true) @PathVariable("id") long id,
                          @ApiParam(value = "Update Contact", required = true) @Valid @RequestBody Contact contact) throws ResourceNotFoundException {

        return repository.findById(id)
                .map(p -> {
                    p.setName(contact.getName());
                    p.setEmail(contact.getEmail());
                    p.setPhone(contact.getPhone());
                    return repository.save(p);
                })
                .orElseGet(() -> {
                    contact.setId(id);
                    return repository.save(contact);
                });

    }

    @ApiOperation(value = "Delete a contact")
    @DeleteMapping(path = {"/contacts/{id}"})
    public void delete(@ApiParam(value = "Id of the contact to delete", required = true) @PathVariable("id") long id) throws ResourceNotFoundException {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found for this id :: " + id));
        repository.deleteById(id);

    }
}