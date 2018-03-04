/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * UserControllerTest.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package net.za.grasser.test.api;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import net.za.grasser.test.api.repo.SessionRepository;
import net.za.grasser.test.api.repo.UserRepository;

/**
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class UserControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
        MediaType.APPLICATION_JSON.getSubtype(),
        Charset.forName("utf8"));

    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private SessionRepository sessionRepository;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {

    }

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .orElse(null);

        assertNotNull("the JSON message converter must not be null",
            this.mappingJackson2HttpMessageConverter);
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

        //this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.userRepository.deleteAll();
        this.sessionRepository.deleteAll();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {

    }

    /**
     * Test method for {@link net.za.grasser.test.api.UserController#encode(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testEncode() {

        fail("Not yet implemented");
    }

    /**
     * Test method for {@link net.za.grasser.test.api.UserController#add(net.za.grasser.test.api.model.User)}.
     */
    @Test
    public void testAdd() {

        fail("Not yet implemented");
    }

    /**
     * Test method for {@link net.za.grasser.test.api.UserController#findAll()}.
     */
    @Test
    public void testFindAll() {

        fail("Not yet implemented");
    }

    /**
     * Test method for {@link net.za.grasser.test.api.UserController#find(java.lang.Long)}.
     */
    @Test
    public void testFind() {

        fail("Not yet implemented");
    }

    /**
     * Test method for {@link net.za.grasser.test.api.UserController#login(net.za.grasser.test.api.model.Login)}.
     */
    @Test
    public void testLogin() {

        fail("Not yet implemented");
    }

    /**
     * Test method for {@link net.za.grasser.test.api.UserController#logout(java.lang.Long)}.
     */
    @Test
    public void testLogout() {

        fail("Not yet implemented");
    }

    /**
     * Test method for
     * {@link net.za.grasser.test.api.UserController#UserController(net.za.grasser.test.api.repo.UserRepository, net.za.grasser.test.api.repo.SessionRepository)}.
     */
    @Test
    public void testUserController() {

        fail("Not yet implemented");
    }

}
