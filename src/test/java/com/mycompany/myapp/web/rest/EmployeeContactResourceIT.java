package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.EmployeeContact;
import com.mycompany.myapp.repository.EmployeeContactRepository;
import com.mycompany.myapp.service.criteria.EmployeeContactCriteria;
import com.mycompany.myapp.service.dto.EmployeeContactDTO;
import com.mycompany.myapp.service.mapper.EmployeeContactMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EmployeeContactResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmployeeContactResourceIT {

    private static final Integer DEFAULT_EMPLOYEE_CONTACT_ID = 1;
    private static final Integer UPDATED_EMPLOYEE_CONTACT_ID = 2;
    private static final Integer SMALLER_EMPLOYEE_CONTACT_ID = 1 - 1;

    private static final Integer DEFAULT_EMPLOYEE_ID = 1;
    private static final Integer UPDATED_EMPLOYEE_ID = 2;
    private static final Integer SMALLER_EMPLOYEE_ID = 1 - 1;

    private static final Integer DEFAULT_CONTACT_TYPE = 1;
    private static final Integer UPDATED_CONTACT_TYPE = 2;
    private static final Integer SMALLER_CONTACT_TYPE = 1 - 1;

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/employee-contacts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeContactRepository employeeContactRepository;

    @Autowired
    private EmployeeContactMapper employeeContactMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeContactMockMvc;

    private EmployeeContact employeeContact;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeContact createEntity(EntityManager em) {
        EmployeeContact employeeContact = new EmployeeContact()
            .employeeContactId(DEFAULT_EMPLOYEE_CONTACT_ID)
            .employeeId(DEFAULT_EMPLOYEE_ID)
            .contactType(DEFAULT_CONTACT_TYPE)
            .contact(DEFAULT_CONTACT);
        return employeeContact;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeContact createUpdatedEntity(EntityManager em) {
        EmployeeContact employeeContact = new EmployeeContact()
            .employeeContactId(UPDATED_EMPLOYEE_CONTACT_ID)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .contactType(UPDATED_CONTACT_TYPE)
            .contact(UPDATED_CONTACT);
        return employeeContact;
    }

    @BeforeEach
    public void initTest() {
        employeeContact = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployeeContact() throws Exception {
        int databaseSizeBeforeCreate = employeeContactRepository.findAll().size();
        // Create the EmployeeContact
        EmployeeContactDTO employeeContactDTO = employeeContactMapper.toDto(employeeContact);
        restEmployeeContactMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeContactDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EmployeeContact in the database
        List<EmployeeContact> employeeContactList = employeeContactRepository.findAll();
        assertThat(employeeContactList).hasSize(databaseSizeBeforeCreate + 1);
        EmployeeContact testEmployeeContact = employeeContactList.get(employeeContactList.size() - 1);
        assertThat(testEmployeeContact.getEmployeeContactId()).isEqualTo(DEFAULT_EMPLOYEE_CONTACT_ID);
        assertThat(testEmployeeContact.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testEmployeeContact.getContactType()).isEqualTo(DEFAULT_CONTACT_TYPE);
        assertThat(testEmployeeContact.getContact()).isEqualTo(DEFAULT_CONTACT);
    }

    @Test
    @Transactional
    void createEmployeeContactWithExistingId() throws Exception {
        // Create the EmployeeContact with an existing ID
        employeeContact.setId(1L);
        EmployeeContactDTO employeeContactDTO = employeeContactMapper.toDto(employeeContact);

        int databaseSizeBeforeCreate = employeeContactRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeContactMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeContact in the database
        List<EmployeeContact> employeeContactList = employeeContactRepository.findAll();
        assertThat(employeeContactList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmployeeContacts() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList
        restEmployeeContactMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeContact.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeContactId").value(hasItem(DEFAULT_EMPLOYEE_CONTACT_ID)))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID)))
            .andExpect(jsonPath("$.[*].contactType").value(hasItem(DEFAULT_CONTACT_TYPE)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)));
    }

    @Test
    @Transactional
    void getEmployeeContact() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get the employeeContact
        restEmployeeContactMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeContact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeContact.getId().intValue()))
            .andExpect(jsonPath("$.employeeContactId").value(DEFAULT_EMPLOYEE_CONTACT_ID))
            .andExpect(jsonPath("$.employeeId").value(DEFAULT_EMPLOYEE_ID))
            .andExpect(jsonPath("$.contactType").value(DEFAULT_CONTACT_TYPE))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT));
    }

    @Test
    @Transactional
    void getEmployeeContactsByIdFiltering() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        Long id = employeeContact.getId();

        defaultEmployeeContactShouldBeFound("id.equals=" + id);
        defaultEmployeeContactShouldNotBeFound("id.notEquals=" + id);

        defaultEmployeeContactShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmployeeContactShouldNotBeFound("id.greaterThan=" + id);

        defaultEmployeeContactShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmployeeContactShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByEmployeeContactIdIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where employeeContactId equals to DEFAULT_EMPLOYEE_CONTACT_ID
        defaultEmployeeContactShouldBeFound("employeeContactId.equals=" + DEFAULT_EMPLOYEE_CONTACT_ID);

        // Get all the employeeContactList where employeeContactId equals to UPDATED_EMPLOYEE_CONTACT_ID
        defaultEmployeeContactShouldNotBeFound("employeeContactId.equals=" + UPDATED_EMPLOYEE_CONTACT_ID);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByEmployeeContactIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where employeeContactId not equals to DEFAULT_EMPLOYEE_CONTACT_ID
        defaultEmployeeContactShouldNotBeFound("employeeContactId.notEquals=" + DEFAULT_EMPLOYEE_CONTACT_ID);

        // Get all the employeeContactList where employeeContactId not equals to UPDATED_EMPLOYEE_CONTACT_ID
        defaultEmployeeContactShouldBeFound("employeeContactId.notEquals=" + UPDATED_EMPLOYEE_CONTACT_ID);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByEmployeeContactIdIsInShouldWork() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where employeeContactId in DEFAULT_EMPLOYEE_CONTACT_ID or UPDATED_EMPLOYEE_CONTACT_ID
        defaultEmployeeContactShouldBeFound("employeeContactId.in=" + DEFAULT_EMPLOYEE_CONTACT_ID + "," + UPDATED_EMPLOYEE_CONTACT_ID);

        // Get all the employeeContactList where employeeContactId equals to UPDATED_EMPLOYEE_CONTACT_ID
        defaultEmployeeContactShouldNotBeFound("employeeContactId.in=" + UPDATED_EMPLOYEE_CONTACT_ID);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByEmployeeContactIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where employeeContactId is not null
        defaultEmployeeContactShouldBeFound("employeeContactId.specified=true");

        // Get all the employeeContactList where employeeContactId is null
        defaultEmployeeContactShouldNotBeFound("employeeContactId.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByEmployeeContactIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where employeeContactId is greater than or equal to DEFAULT_EMPLOYEE_CONTACT_ID
        defaultEmployeeContactShouldBeFound("employeeContactId.greaterThanOrEqual=" + DEFAULT_EMPLOYEE_CONTACT_ID);

        // Get all the employeeContactList where employeeContactId is greater than or equal to UPDATED_EMPLOYEE_CONTACT_ID
        defaultEmployeeContactShouldNotBeFound("employeeContactId.greaterThanOrEqual=" + UPDATED_EMPLOYEE_CONTACT_ID);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByEmployeeContactIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where employeeContactId is less than or equal to DEFAULT_EMPLOYEE_CONTACT_ID
        defaultEmployeeContactShouldBeFound("employeeContactId.lessThanOrEqual=" + DEFAULT_EMPLOYEE_CONTACT_ID);

        // Get all the employeeContactList where employeeContactId is less than or equal to SMALLER_EMPLOYEE_CONTACT_ID
        defaultEmployeeContactShouldNotBeFound("employeeContactId.lessThanOrEqual=" + SMALLER_EMPLOYEE_CONTACT_ID);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByEmployeeContactIdIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where employeeContactId is less than DEFAULT_EMPLOYEE_CONTACT_ID
        defaultEmployeeContactShouldNotBeFound("employeeContactId.lessThan=" + DEFAULT_EMPLOYEE_CONTACT_ID);

        // Get all the employeeContactList where employeeContactId is less than UPDATED_EMPLOYEE_CONTACT_ID
        defaultEmployeeContactShouldBeFound("employeeContactId.lessThan=" + UPDATED_EMPLOYEE_CONTACT_ID);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByEmployeeContactIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where employeeContactId is greater than DEFAULT_EMPLOYEE_CONTACT_ID
        defaultEmployeeContactShouldNotBeFound("employeeContactId.greaterThan=" + DEFAULT_EMPLOYEE_CONTACT_ID);

        // Get all the employeeContactList where employeeContactId is greater than SMALLER_EMPLOYEE_CONTACT_ID
        defaultEmployeeContactShouldBeFound("employeeContactId.greaterThan=" + SMALLER_EMPLOYEE_CONTACT_ID);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByEmployeeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where employeeId equals to DEFAULT_EMPLOYEE_ID
        defaultEmployeeContactShouldBeFound("employeeId.equals=" + DEFAULT_EMPLOYEE_ID);

        // Get all the employeeContactList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultEmployeeContactShouldNotBeFound("employeeId.equals=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByEmployeeIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where employeeId not equals to DEFAULT_EMPLOYEE_ID
        defaultEmployeeContactShouldNotBeFound("employeeId.notEquals=" + DEFAULT_EMPLOYEE_ID);

        // Get all the employeeContactList where employeeId not equals to UPDATED_EMPLOYEE_ID
        defaultEmployeeContactShouldBeFound("employeeId.notEquals=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByEmployeeIdIsInShouldWork() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where employeeId in DEFAULT_EMPLOYEE_ID or UPDATED_EMPLOYEE_ID
        defaultEmployeeContactShouldBeFound("employeeId.in=" + DEFAULT_EMPLOYEE_ID + "," + UPDATED_EMPLOYEE_ID);

        // Get all the employeeContactList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultEmployeeContactShouldNotBeFound("employeeId.in=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByEmployeeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where employeeId is not null
        defaultEmployeeContactShouldBeFound("employeeId.specified=true");

        // Get all the employeeContactList where employeeId is null
        defaultEmployeeContactShouldNotBeFound("employeeId.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByEmployeeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where employeeId is greater than or equal to DEFAULT_EMPLOYEE_ID
        defaultEmployeeContactShouldBeFound("employeeId.greaterThanOrEqual=" + DEFAULT_EMPLOYEE_ID);

        // Get all the employeeContactList where employeeId is greater than or equal to UPDATED_EMPLOYEE_ID
        defaultEmployeeContactShouldNotBeFound("employeeId.greaterThanOrEqual=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByEmployeeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where employeeId is less than or equal to DEFAULT_EMPLOYEE_ID
        defaultEmployeeContactShouldBeFound("employeeId.lessThanOrEqual=" + DEFAULT_EMPLOYEE_ID);

        // Get all the employeeContactList where employeeId is less than or equal to SMALLER_EMPLOYEE_ID
        defaultEmployeeContactShouldNotBeFound("employeeId.lessThanOrEqual=" + SMALLER_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByEmployeeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where employeeId is less than DEFAULT_EMPLOYEE_ID
        defaultEmployeeContactShouldNotBeFound("employeeId.lessThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the employeeContactList where employeeId is less than UPDATED_EMPLOYEE_ID
        defaultEmployeeContactShouldBeFound("employeeId.lessThan=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByEmployeeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where employeeId is greater than DEFAULT_EMPLOYEE_ID
        defaultEmployeeContactShouldNotBeFound("employeeId.greaterThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the employeeContactList where employeeId is greater than SMALLER_EMPLOYEE_ID
        defaultEmployeeContactShouldBeFound("employeeId.greaterThan=" + SMALLER_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByContactTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where contactType equals to DEFAULT_CONTACT_TYPE
        defaultEmployeeContactShouldBeFound("contactType.equals=" + DEFAULT_CONTACT_TYPE);

        // Get all the employeeContactList where contactType equals to UPDATED_CONTACT_TYPE
        defaultEmployeeContactShouldNotBeFound("contactType.equals=" + UPDATED_CONTACT_TYPE);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByContactTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where contactType not equals to DEFAULT_CONTACT_TYPE
        defaultEmployeeContactShouldNotBeFound("contactType.notEquals=" + DEFAULT_CONTACT_TYPE);

        // Get all the employeeContactList where contactType not equals to UPDATED_CONTACT_TYPE
        defaultEmployeeContactShouldBeFound("contactType.notEquals=" + UPDATED_CONTACT_TYPE);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByContactTypeIsInShouldWork() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where contactType in DEFAULT_CONTACT_TYPE or UPDATED_CONTACT_TYPE
        defaultEmployeeContactShouldBeFound("contactType.in=" + DEFAULT_CONTACT_TYPE + "," + UPDATED_CONTACT_TYPE);

        // Get all the employeeContactList where contactType equals to UPDATED_CONTACT_TYPE
        defaultEmployeeContactShouldNotBeFound("contactType.in=" + UPDATED_CONTACT_TYPE);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByContactTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where contactType is not null
        defaultEmployeeContactShouldBeFound("contactType.specified=true");

        // Get all the employeeContactList where contactType is null
        defaultEmployeeContactShouldNotBeFound("contactType.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByContactTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where contactType is greater than or equal to DEFAULT_CONTACT_TYPE
        defaultEmployeeContactShouldBeFound("contactType.greaterThanOrEqual=" + DEFAULT_CONTACT_TYPE);

        // Get all the employeeContactList where contactType is greater than or equal to UPDATED_CONTACT_TYPE
        defaultEmployeeContactShouldNotBeFound("contactType.greaterThanOrEqual=" + UPDATED_CONTACT_TYPE);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByContactTypeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where contactType is less than or equal to DEFAULT_CONTACT_TYPE
        defaultEmployeeContactShouldBeFound("contactType.lessThanOrEqual=" + DEFAULT_CONTACT_TYPE);

        // Get all the employeeContactList where contactType is less than or equal to SMALLER_CONTACT_TYPE
        defaultEmployeeContactShouldNotBeFound("contactType.lessThanOrEqual=" + SMALLER_CONTACT_TYPE);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByContactTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where contactType is less than DEFAULT_CONTACT_TYPE
        defaultEmployeeContactShouldNotBeFound("contactType.lessThan=" + DEFAULT_CONTACT_TYPE);

        // Get all the employeeContactList where contactType is less than UPDATED_CONTACT_TYPE
        defaultEmployeeContactShouldBeFound("contactType.lessThan=" + UPDATED_CONTACT_TYPE);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByContactTypeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where contactType is greater than DEFAULT_CONTACT_TYPE
        defaultEmployeeContactShouldNotBeFound("contactType.greaterThan=" + DEFAULT_CONTACT_TYPE);

        // Get all the employeeContactList where contactType is greater than SMALLER_CONTACT_TYPE
        defaultEmployeeContactShouldBeFound("contactType.greaterThan=" + SMALLER_CONTACT_TYPE);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where contact equals to DEFAULT_CONTACT
        defaultEmployeeContactShouldBeFound("contact.equals=" + DEFAULT_CONTACT);

        // Get all the employeeContactList where contact equals to UPDATED_CONTACT
        defaultEmployeeContactShouldNotBeFound("contact.equals=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByContactIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where contact not equals to DEFAULT_CONTACT
        defaultEmployeeContactShouldNotBeFound("contact.notEquals=" + DEFAULT_CONTACT);

        // Get all the employeeContactList where contact not equals to UPDATED_CONTACT
        defaultEmployeeContactShouldBeFound("contact.notEquals=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByContactIsInShouldWork() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where contact in DEFAULT_CONTACT or UPDATED_CONTACT
        defaultEmployeeContactShouldBeFound("contact.in=" + DEFAULT_CONTACT + "," + UPDATED_CONTACT);

        // Get all the employeeContactList where contact equals to UPDATED_CONTACT
        defaultEmployeeContactShouldNotBeFound("contact.in=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByContactIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where contact is not null
        defaultEmployeeContactShouldBeFound("contact.specified=true");

        // Get all the employeeContactList where contact is null
        defaultEmployeeContactShouldNotBeFound("contact.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByContactContainsSomething() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where contact contains DEFAULT_CONTACT
        defaultEmployeeContactShouldBeFound("contact.contains=" + DEFAULT_CONTACT);

        // Get all the employeeContactList where contact contains UPDATED_CONTACT
        defaultEmployeeContactShouldNotBeFound("contact.contains=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void getAllEmployeeContactsByContactNotContainsSomething() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        // Get all the employeeContactList where contact does not contain DEFAULT_CONTACT
        defaultEmployeeContactShouldNotBeFound("contact.doesNotContain=" + DEFAULT_CONTACT);

        // Get all the employeeContactList where contact does not contain UPDATED_CONTACT
        defaultEmployeeContactShouldBeFound("contact.doesNotContain=" + UPDATED_CONTACT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeContactShouldBeFound(String filter) throws Exception {
        restEmployeeContactMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeContact.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeContactId").value(hasItem(DEFAULT_EMPLOYEE_CONTACT_ID)))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID)))
            .andExpect(jsonPath("$.[*].contactType").value(hasItem(DEFAULT_CONTACT_TYPE)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)));

        // Check, that the count call also returns 1
        restEmployeeContactMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeeContactShouldNotBeFound(String filter) throws Exception {
        restEmployeeContactMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeContactMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeContact() throws Exception {
        // Get the employeeContact
        restEmployeeContactMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmployeeContact() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        int databaseSizeBeforeUpdate = employeeContactRepository.findAll().size();

        // Update the employeeContact
        EmployeeContact updatedEmployeeContact = employeeContactRepository.findById(employeeContact.getId()).get();
        // Disconnect from session so that the updates on updatedEmployeeContact are not directly saved in db
        em.detach(updatedEmployeeContact);
        updatedEmployeeContact
            .employeeContactId(UPDATED_EMPLOYEE_CONTACT_ID)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .contactType(UPDATED_CONTACT_TYPE)
            .contact(UPDATED_CONTACT);
        EmployeeContactDTO employeeContactDTO = employeeContactMapper.toDto(updatedEmployeeContact);

        restEmployeeContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeContactDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeContactDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeContact in the database
        List<EmployeeContact> employeeContactList = employeeContactRepository.findAll();
        assertThat(employeeContactList).hasSize(databaseSizeBeforeUpdate);
        EmployeeContact testEmployeeContact = employeeContactList.get(employeeContactList.size() - 1);
        assertThat(testEmployeeContact.getEmployeeContactId()).isEqualTo(UPDATED_EMPLOYEE_CONTACT_ID);
        assertThat(testEmployeeContact.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testEmployeeContact.getContactType()).isEqualTo(UPDATED_CONTACT_TYPE);
        assertThat(testEmployeeContact.getContact()).isEqualTo(UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeContact() throws Exception {
        int databaseSizeBeforeUpdate = employeeContactRepository.findAll().size();
        employeeContact.setId(count.incrementAndGet());

        // Create the EmployeeContact
        EmployeeContactDTO employeeContactDTO = employeeContactMapper.toDto(employeeContact);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeContactDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeContact in the database
        List<EmployeeContact> employeeContactList = employeeContactRepository.findAll();
        assertThat(employeeContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeContact() throws Exception {
        int databaseSizeBeforeUpdate = employeeContactRepository.findAll().size();
        employeeContact.setId(count.incrementAndGet());

        // Create the EmployeeContact
        EmployeeContactDTO employeeContactDTO = employeeContactMapper.toDto(employeeContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeContactMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeContact in the database
        List<EmployeeContact> employeeContactList = employeeContactRepository.findAll();
        assertThat(employeeContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeContact() throws Exception {
        int databaseSizeBeforeUpdate = employeeContactRepository.findAll().size();
        employeeContact.setId(count.incrementAndGet());

        // Create the EmployeeContact
        EmployeeContactDTO employeeContactDTO = employeeContactMapper.toDto(employeeContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeContactMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeContactDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeContact in the database
        List<EmployeeContact> employeeContactList = employeeContactRepository.findAll();
        assertThat(employeeContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeContactWithPatch() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        int databaseSizeBeforeUpdate = employeeContactRepository.findAll().size();

        // Update the employeeContact using partial update
        EmployeeContact partialUpdatedEmployeeContact = new EmployeeContact();
        partialUpdatedEmployeeContact.setId(employeeContact.getId());

        partialUpdatedEmployeeContact
            .employeeContactId(UPDATED_EMPLOYEE_CONTACT_ID)
            .contactType(UPDATED_CONTACT_TYPE)
            .contact(UPDATED_CONTACT);

        restEmployeeContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeContact.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeContact))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeContact in the database
        List<EmployeeContact> employeeContactList = employeeContactRepository.findAll();
        assertThat(employeeContactList).hasSize(databaseSizeBeforeUpdate);
        EmployeeContact testEmployeeContact = employeeContactList.get(employeeContactList.size() - 1);
        assertThat(testEmployeeContact.getEmployeeContactId()).isEqualTo(UPDATED_EMPLOYEE_CONTACT_ID);
        assertThat(testEmployeeContact.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testEmployeeContact.getContactType()).isEqualTo(UPDATED_CONTACT_TYPE);
        assertThat(testEmployeeContact.getContact()).isEqualTo(UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void fullUpdateEmployeeContactWithPatch() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        int databaseSizeBeforeUpdate = employeeContactRepository.findAll().size();

        // Update the employeeContact using partial update
        EmployeeContact partialUpdatedEmployeeContact = new EmployeeContact();
        partialUpdatedEmployeeContact.setId(employeeContact.getId());

        partialUpdatedEmployeeContact
            .employeeContactId(UPDATED_EMPLOYEE_CONTACT_ID)
            .employeeId(UPDATED_EMPLOYEE_ID)
            .contactType(UPDATED_CONTACT_TYPE)
            .contact(UPDATED_CONTACT);

        restEmployeeContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeContact.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployeeContact))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeContact in the database
        List<EmployeeContact> employeeContactList = employeeContactRepository.findAll();
        assertThat(employeeContactList).hasSize(databaseSizeBeforeUpdate);
        EmployeeContact testEmployeeContact = employeeContactList.get(employeeContactList.size() - 1);
        assertThat(testEmployeeContact.getEmployeeContactId()).isEqualTo(UPDATED_EMPLOYEE_CONTACT_ID);
        assertThat(testEmployeeContact.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testEmployeeContact.getContactType()).isEqualTo(UPDATED_CONTACT_TYPE);
        assertThat(testEmployeeContact.getContact()).isEqualTo(UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeContact() throws Exception {
        int databaseSizeBeforeUpdate = employeeContactRepository.findAll().size();
        employeeContact.setId(count.incrementAndGet());

        // Create the EmployeeContact
        EmployeeContactDTO employeeContactDTO = employeeContactMapper.toDto(employeeContact);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeContactDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeContact in the database
        List<EmployeeContact> employeeContactList = employeeContactRepository.findAll();
        assertThat(employeeContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeContact() throws Exception {
        int databaseSizeBeforeUpdate = employeeContactRepository.findAll().size();
        employeeContact.setId(count.incrementAndGet());

        // Create the EmployeeContact
        EmployeeContactDTO employeeContactDTO = employeeContactMapper.toDto(employeeContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeContactMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeContactDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeContact in the database
        List<EmployeeContact> employeeContactList = employeeContactRepository.findAll();
        assertThat(employeeContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeContact() throws Exception {
        int databaseSizeBeforeUpdate = employeeContactRepository.findAll().size();
        employeeContact.setId(count.incrementAndGet());

        // Create the EmployeeContact
        EmployeeContactDTO employeeContactDTO = employeeContactMapper.toDto(employeeContact);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeContactMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeContactDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeContact in the database
        List<EmployeeContact> employeeContactList = employeeContactRepository.findAll();
        assertThat(employeeContactList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeContact() throws Exception {
        // Initialize the database
        employeeContactRepository.saveAndFlush(employeeContact);

        int databaseSizeBeforeDelete = employeeContactRepository.findAll().size();

        // Delete the employeeContact
        restEmployeeContactMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeContact.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmployeeContact> employeeContactList = employeeContactRepository.findAll();
        assertThat(employeeContactList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
