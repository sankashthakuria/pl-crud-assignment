package com.powerledger.assignment.backend.powerledgerassignmentbackend.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by @author Sankash on 5/11/2019
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "CONTACT")
public class Contact {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Unique identifier of the contact.", example = "1", required = true, position = 0)
    private Long id;

    @Column(name = "NAME")
    @NotBlank
    @ApiModelProperty(notes = "First name of the contact.", example = "John", required = true, position = 1)
    private String name;

    @Column(name = "EMAIL")
    @ApiModelProperty(notes = "Email address of contact. Must be a valid email address", example = "john.doe@email.com", required = true, position = 2)
    @Pattern(message = "Must provide a valid email address", regexp = "^.+?@.+?\\..+$")
    private String email;

    @Column(name = "PHONE")
    @ApiModelProperty(notes = "Must be a valid Australian phone number.", example = "John", required = true, position = 1)
    @Pattern(message = "Phone number invalid", regexp = "^(?:\\+?(61))? ?(?:\\((?=.*\\)))?(0?[2-57-8])\\)? ?(\\d\\d(?:[- ](?=\\d{3})|(?!\\d\\d[- ]?\\d[- ]))\\d\\d[- ]?\\d[- ]?\\d{3})$")
    private String phone;
}
