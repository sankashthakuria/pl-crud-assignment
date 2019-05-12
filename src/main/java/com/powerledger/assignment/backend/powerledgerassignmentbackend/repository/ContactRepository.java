package com.powerledger.assignment.backend.powerledgerassignmentbackend.repository;

import com.powerledger.assignment.backend.powerledgerassignmentbackend.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by @author Sankash on 5/11/2019
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
}
