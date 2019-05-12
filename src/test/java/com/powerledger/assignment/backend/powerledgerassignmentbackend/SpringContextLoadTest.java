package com.powerledger.assignment.backend.powerledgerassignmentbackend;

import com.powerledger.assignment.backend.powerledgerassignmentbackend.controller.ContactController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by @author Sankash on 5/11/2019
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringContextLoadTest {
    @Autowired
    private ContactController controller;

    @Test
    public void contexLoads() throws Exception {
        assertThat(controller).isNotNull();
    }
}
