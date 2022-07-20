package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Employee;
import com.mycompany.myapp.repository.EmployeeRepository;
import com.mycompany.myapp.service.criteria.EmployeeCriteria;
import com.mycompany.myapp.service.dto.EmployeeDTO;
import com.mycompany.myapp.service.mapper.EmployeeMapper;
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
 * Integration tests for the {@link EmployeeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmployeeResourceIT {

    private static final Integer DEFAULT_EMPLOYEE_ID = 1;
    private static final Integer UPDATED_EMPLOYEE_ID = 2;
    private static final Integer SMALLER_EMPLOYEE_ID = 1 - 1;

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_GENDER = 1;
    private static final Integer UPDATED_GENDER = 2;
    private static final Integer SMALLER_GENDER = 1 - 1;

    private static final Integer DEFAULT_MARITAL_STATUS = 1;
    private static final Integer UPDATED_MARITAL_STATUS = 2;
    private static final Integer SMALLER_MARITAL_STATUS = 1 - 1;

    private static final Integer DEFAULT_BIRTH_DATE = 1;
    private static final Integer UPDATED_BIRTH_DATE = 2;
    private static final Integer SMALLER_BIRTH_DATE = 1 - 1;

    private static final Integer DEFAULT_HIRE_DATE = 1;
    private static final Integer UPDATED_HIRE_DATE = 2;
    private static final Integer SMALLER_HIRE_DATE = 1 - 1;

    private static final Integer DEFAULT_EMPLOYMENT_STATUS = 1;
    private static final Integer UPDATED_EMPLOYMENT_STATUS = 2;
    private static final Integer SMALLER_EMPLOYMENT_STATUS = 1 - 1;

    private static final Integer DEFAULT_MANAGER_ID = 1;
    private static final Integer UPDATED_MANAGER_ID = 2;
    private static final Integer SMALLER_MANAGER_ID = 1 - 1;

    private static final String ENTITY_API_URL = "/api/employees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeMockMvc;

    private Employee employee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createEntity(EntityManager em) {
        Employee employee = new Employee()
            .employeeId(DEFAULT_EMPLOYEE_ID)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .gender(DEFAULT_GENDER)
            .maritalStatus(DEFAULT_MARITAL_STATUS)
            .birthDate(DEFAULT_BIRTH_DATE)
            .hireDate(DEFAULT_HIRE_DATE)
            .employmentStatus(DEFAULT_EMPLOYMENT_STATUS)
            .managerId(DEFAULT_MANAGER_ID);
        return employee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createUpdatedEntity(EntityManager em) {
        Employee employee = new Employee()
            .employeeId(UPDATED_EMPLOYEE_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .gender(UPDATED_GENDER)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .birthDate(UPDATED_BIRTH_DATE)
            .hireDate(UPDATED_HIRE_DATE)
            .employmentStatus(UPDATED_EMPLOYMENT_STATUS)
            .managerId(UPDATED_MANAGER_ID);
        return employee;
    }

    @BeforeEach
    public void initTest() {
        employee = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployee() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();
        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);
        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isCreated());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate + 1);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testEmployee.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testEmployee.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testEmployee.getMaritalStatus()).isEqualTo(DEFAULT_MARITAL_STATUS);
        assertThat(testEmployee.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testEmployee.getHireDate()).isEqualTo(DEFAULT_HIRE_DATE);
        assertThat(testEmployee.getEmploymentStatus()).isEqualTo(DEFAULT_EMPLOYMENT_STATUS);
        assertThat(testEmployee.getManagerId()).isEqualTo(DEFAULT_MANAGER_ID);
    }

    @Test
    @Transactional
    void createEmployeeWithExistingId() throws Exception {
        // Create the Employee with an existing ID
        employee.setId(1L);
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmployees() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE)))
            .andExpect(jsonPath("$.[*].employmentStatus").value(hasItem(DEFAULT_EMPLOYMENT_STATUS)))
            .andExpect(jsonPath("$.[*].managerId").value(hasItem(DEFAULT_MANAGER_ID)));
    }

    @Test
    @Transactional
    void getEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get the employee
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL_ID, employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employee.getId().intValue()))
            .andExpect(jsonPath("$.employeeId").value(DEFAULT_EMPLOYEE_ID))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.maritalStatus").value(DEFAULT_MARITAL_STATUS))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE))
            .andExpect(jsonPath("$.employmentStatus").value(DEFAULT_EMPLOYMENT_STATUS))
            .andExpect(jsonPath("$.managerId").value(DEFAULT_MANAGER_ID));
    }

    @Test
    @Transactional
    void getEmployeesByIdFiltering() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        Long id = employee.getId();

        defaultEmployeeShouldBeFound("id.equals=" + id);
        defaultEmployeeShouldNotBeFound("id.notEquals=" + id);

        defaultEmployeeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmployeeShouldNotBeFound("id.greaterThan=" + id);

        defaultEmployeeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmployeeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeId equals to DEFAULT_EMPLOYEE_ID
        defaultEmployeeShouldBeFound("employeeId.equals=" + DEFAULT_EMPLOYEE_ID);

        // Get all the employeeList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultEmployeeShouldNotBeFound("employeeId.equals=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeId not equals to DEFAULT_EMPLOYEE_ID
        defaultEmployeeShouldNotBeFound("employeeId.notEquals=" + DEFAULT_EMPLOYEE_ID);

        // Get all the employeeList where employeeId not equals to UPDATED_EMPLOYEE_ID
        defaultEmployeeShouldBeFound("employeeId.notEquals=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeIdIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeId in DEFAULT_EMPLOYEE_ID or UPDATED_EMPLOYEE_ID
        defaultEmployeeShouldBeFound("employeeId.in=" + DEFAULT_EMPLOYEE_ID + "," + UPDATED_EMPLOYEE_ID);

        // Get all the employeeList where employeeId equals to UPDATED_EMPLOYEE_ID
        defaultEmployeeShouldNotBeFound("employeeId.in=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeId is not null
        defaultEmployeeShouldBeFound("employeeId.specified=true");

        // Get all the employeeList where employeeId is null
        defaultEmployeeShouldNotBeFound("employeeId.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeId is greater than or equal to DEFAULT_EMPLOYEE_ID
        defaultEmployeeShouldBeFound("employeeId.greaterThanOrEqual=" + DEFAULT_EMPLOYEE_ID);

        // Get all the employeeList where employeeId is greater than or equal to UPDATED_EMPLOYEE_ID
        defaultEmployeeShouldNotBeFound("employeeId.greaterThanOrEqual=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeId is less than or equal to DEFAULT_EMPLOYEE_ID
        defaultEmployeeShouldBeFound("employeeId.lessThanOrEqual=" + DEFAULT_EMPLOYEE_ID);

        // Get all the employeeList where employeeId is less than or equal to SMALLER_EMPLOYEE_ID
        defaultEmployeeShouldNotBeFound("employeeId.lessThanOrEqual=" + SMALLER_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeId is less than DEFAULT_EMPLOYEE_ID
        defaultEmployeeShouldNotBeFound("employeeId.lessThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the employeeList where employeeId is less than UPDATED_EMPLOYEE_ID
        defaultEmployeeShouldBeFound("employeeId.lessThan=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeId is greater than DEFAULT_EMPLOYEE_ID
        defaultEmployeeShouldNotBeFound("employeeId.greaterThan=" + DEFAULT_EMPLOYEE_ID);

        // Get all the employeeList where employeeId is greater than SMALLER_EMPLOYEE_ID
        defaultEmployeeShouldBeFound("employeeId.greaterThan=" + SMALLER_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName equals to DEFAULT_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the employeeList where firstName equals to UPDATED_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName not equals to DEFAULT_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the employeeList where firstName not equals to UPDATED_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the employeeList where firstName equals to UPDATED_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName is not null
        defaultEmployeeShouldBeFound("firstName.specified=true");

        // Get all the employeeList where firstName is null
        defaultEmployeeShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName contains DEFAULT_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the employeeList where firstName contains UPDATED_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName does not contain DEFAULT_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the employeeList where firstName does not contain UPDATED_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName equals to DEFAULT_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the employeeList where lastName equals to UPDATED_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName not equals to DEFAULT_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the employeeList where lastName not equals to UPDATED_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the employeeList where lastName equals to UPDATED_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName is not null
        defaultEmployeeShouldBeFound("lastName.specified=true");

        // Get all the employeeList where lastName is null
        defaultEmployeeShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName contains DEFAULT_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the employeeList where lastName contains UPDATED_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName does not contain DEFAULT_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the employeeList where lastName does not contain UPDATED_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where gender equals to DEFAULT_GENDER
        defaultEmployeeShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the employeeList where gender equals to UPDATED_GENDER
        defaultEmployeeShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllEmployeesByGenderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where gender not equals to DEFAULT_GENDER
        defaultEmployeeShouldNotBeFound("gender.notEquals=" + DEFAULT_GENDER);

        // Get all the employeeList where gender not equals to UPDATED_GENDER
        defaultEmployeeShouldBeFound("gender.notEquals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllEmployeesByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultEmployeeShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the employeeList where gender equals to UPDATED_GENDER
        defaultEmployeeShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllEmployeesByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where gender is not null
        defaultEmployeeShouldBeFound("gender.specified=true");

        // Get all the employeeList where gender is null
        defaultEmployeeShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByGenderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where gender is greater than or equal to DEFAULT_GENDER
        defaultEmployeeShouldBeFound("gender.greaterThanOrEqual=" + DEFAULT_GENDER);

        // Get all the employeeList where gender is greater than or equal to UPDATED_GENDER
        defaultEmployeeShouldNotBeFound("gender.greaterThanOrEqual=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllEmployeesByGenderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where gender is less than or equal to DEFAULT_GENDER
        defaultEmployeeShouldBeFound("gender.lessThanOrEqual=" + DEFAULT_GENDER);

        // Get all the employeeList where gender is less than or equal to SMALLER_GENDER
        defaultEmployeeShouldNotBeFound("gender.lessThanOrEqual=" + SMALLER_GENDER);
    }

    @Test
    @Transactional
    void getAllEmployeesByGenderIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where gender is less than DEFAULT_GENDER
        defaultEmployeeShouldNotBeFound("gender.lessThan=" + DEFAULT_GENDER);

        // Get all the employeeList where gender is less than UPDATED_GENDER
        defaultEmployeeShouldBeFound("gender.lessThan=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllEmployeesByGenderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where gender is greater than DEFAULT_GENDER
        defaultEmployeeShouldNotBeFound("gender.greaterThan=" + DEFAULT_GENDER);

        // Get all the employeeList where gender is greater than SMALLER_GENDER
        defaultEmployeeShouldBeFound("gender.greaterThan=" + SMALLER_GENDER);
    }

    @Test
    @Transactional
    void getAllEmployeesByMaritalStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where maritalStatus equals to DEFAULT_MARITAL_STATUS
        defaultEmployeeShouldBeFound("maritalStatus.equals=" + DEFAULT_MARITAL_STATUS);

        // Get all the employeeList where maritalStatus equals to UPDATED_MARITAL_STATUS
        defaultEmployeeShouldNotBeFound("maritalStatus.equals=" + UPDATED_MARITAL_STATUS);
    }

    @Test
    @Transactional
    void getAllEmployeesByMaritalStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where maritalStatus not equals to DEFAULT_MARITAL_STATUS
        defaultEmployeeShouldNotBeFound("maritalStatus.notEquals=" + DEFAULT_MARITAL_STATUS);

        // Get all the employeeList where maritalStatus not equals to UPDATED_MARITAL_STATUS
        defaultEmployeeShouldBeFound("maritalStatus.notEquals=" + UPDATED_MARITAL_STATUS);
    }

    @Test
    @Transactional
    void getAllEmployeesByMaritalStatusIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where maritalStatus in DEFAULT_MARITAL_STATUS or UPDATED_MARITAL_STATUS
        defaultEmployeeShouldBeFound("maritalStatus.in=" + DEFAULT_MARITAL_STATUS + "," + UPDATED_MARITAL_STATUS);

        // Get all the employeeList where maritalStatus equals to UPDATED_MARITAL_STATUS
        defaultEmployeeShouldNotBeFound("maritalStatus.in=" + UPDATED_MARITAL_STATUS);
    }

    @Test
    @Transactional
    void getAllEmployeesByMaritalStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where maritalStatus is not null
        defaultEmployeeShouldBeFound("maritalStatus.specified=true");

        // Get all the employeeList where maritalStatus is null
        defaultEmployeeShouldNotBeFound("maritalStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByMaritalStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where maritalStatus is greater than or equal to DEFAULT_MARITAL_STATUS
        defaultEmployeeShouldBeFound("maritalStatus.greaterThanOrEqual=" + DEFAULT_MARITAL_STATUS);

        // Get all the employeeList where maritalStatus is greater than or equal to UPDATED_MARITAL_STATUS
        defaultEmployeeShouldNotBeFound("maritalStatus.greaterThanOrEqual=" + UPDATED_MARITAL_STATUS);
    }

    @Test
    @Transactional
    void getAllEmployeesByMaritalStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where maritalStatus is less than or equal to DEFAULT_MARITAL_STATUS
        defaultEmployeeShouldBeFound("maritalStatus.lessThanOrEqual=" + DEFAULT_MARITAL_STATUS);

        // Get all the employeeList where maritalStatus is less than or equal to SMALLER_MARITAL_STATUS
        defaultEmployeeShouldNotBeFound("maritalStatus.lessThanOrEqual=" + SMALLER_MARITAL_STATUS);
    }

    @Test
    @Transactional
    void getAllEmployeesByMaritalStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where maritalStatus is less than DEFAULT_MARITAL_STATUS
        defaultEmployeeShouldNotBeFound("maritalStatus.lessThan=" + DEFAULT_MARITAL_STATUS);

        // Get all the employeeList where maritalStatus is less than UPDATED_MARITAL_STATUS
        defaultEmployeeShouldBeFound("maritalStatus.lessThan=" + UPDATED_MARITAL_STATUS);
    }

    @Test
    @Transactional
    void getAllEmployeesByMaritalStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where maritalStatus is greater than DEFAULT_MARITAL_STATUS
        defaultEmployeeShouldNotBeFound("maritalStatus.greaterThan=" + DEFAULT_MARITAL_STATUS);

        // Get all the employeeList where maritalStatus is greater than SMALLER_MARITAL_STATUS
        defaultEmployeeShouldBeFound("maritalStatus.greaterThan=" + SMALLER_MARITAL_STATUS);
    }

    @Test
    @Transactional
    void getAllEmployeesByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthDate equals to DEFAULT_BIRTH_DATE
        defaultEmployeeShouldBeFound("birthDate.equals=" + DEFAULT_BIRTH_DATE);

        // Get all the employeeList where birthDate equals to UPDATED_BIRTH_DATE
        defaultEmployeeShouldNotBeFound("birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByBirthDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthDate not equals to DEFAULT_BIRTH_DATE
        defaultEmployeeShouldNotBeFound("birthDate.notEquals=" + DEFAULT_BIRTH_DATE);

        // Get all the employeeList where birthDate not equals to UPDATED_BIRTH_DATE
        defaultEmployeeShouldBeFound("birthDate.notEquals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthDate in DEFAULT_BIRTH_DATE or UPDATED_BIRTH_DATE
        defaultEmployeeShouldBeFound("birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE);

        // Get all the employeeList where birthDate equals to UPDATED_BIRTH_DATE
        defaultEmployeeShouldNotBeFound("birthDate.in=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthDate is not null
        defaultEmployeeShouldBeFound("birthDate.specified=true");

        // Get all the employeeList where birthDate is null
        defaultEmployeeShouldNotBeFound("birthDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByBirthDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthDate is greater than or equal to DEFAULT_BIRTH_DATE
        defaultEmployeeShouldBeFound("birthDate.greaterThanOrEqual=" + DEFAULT_BIRTH_DATE);

        // Get all the employeeList where birthDate is greater than or equal to UPDATED_BIRTH_DATE
        defaultEmployeeShouldNotBeFound("birthDate.greaterThanOrEqual=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByBirthDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthDate is less than or equal to DEFAULT_BIRTH_DATE
        defaultEmployeeShouldBeFound("birthDate.lessThanOrEqual=" + DEFAULT_BIRTH_DATE);

        // Get all the employeeList where birthDate is less than or equal to SMALLER_BIRTH_DATE
        defaultEmployeeShouldNotBeFound("birthDate.lessThanOrEqual=" + SMALLER_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByBirthDateIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthDate is less than DEFAULT_BIRTH_DATE
        defaultEmployeeShouldNotBeFound("birthDate.lessThan=" + DEFAULT_BIRTH_DATE);

        // Get all the employeeList where birthDate is less than UPDATED_BIRTH_DATE
        defaultEmployeeShouldBeFound("birthDate.lessThan=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByBirthDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where birthDate is greater than DEFAULT_BIRTH_DATE
        defaultEmployeeShouldNotBeFound("birthDate.greaterThan=" + DEFAULT_BIRTH_DATE);

        // Get all the employeeList where birthDate is greater than SMALLER_BIRTH_DATE
        defaultEmployeeShouldBeFound("birthDate.greaterThan=" + SMALLER_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHireDateIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where hireDate equals to DEFAULT_HIRE_DATE
        defaultEmployeeShouldBeFound("hireDate.equals=" + DEFAULT_HIRE_DATE);

        // Get all the employeeList where hireDate equals to UPDATED_HIRE_DATE
        defaultEmployeeShouldNotBeFound("hireDate.equals=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHireDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where hireDate not equals to DEFAULT_HIRE_DATE
        defaultEmployeeShouldNotBeFound("hireDate.notEquals=" + DEFAULT_HIRE_DATE);

        // Get all the employeeList where hireDate not equals to UPDATED_HIRE_DATE
        defaultEmployeeShouldBeFound("hireDate.notEquals=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHireDateIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where hireDate in DEFAULT_HIRE_DATE or UPDATED_HIRE_DATE
        defaultEmployeeShouldBeFound("hireDate.in=" + DEFAULT_HIRE_DATE + "," + UPDATED_HIRE_DATE);

        // Get all the employeeList where hireDate equals to UPDATED_HIRE_DATE
        defaultEmployeeShouldNotBeFound("hireDate.in=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHireDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where hireDate is not null
        defaultEmployeeShouldBeFound("hireDate.specified=true");

        // Get all the employeeList where hireDate is null
        defaultEmployeeShouldNotBeFound("hireDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByHireDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where hireDate is greater than or equal to DEFAULT_HIRE_DATE
        defaultEmployeeShouldBeFound("hireDate.greaterThanOrEqual=" + DEFAULT_HIRE_DATE);

        // Get all the employeeList where hireDate is greater than or equal to UPDATED_HIRE_DATE
        defaultEmployeeShouldNotBeFound("hireDate.greaterThanOrEqual=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHireDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where hireDate is less than or equal to DEFAULT_HIRE_DATE
        defaultEmployeeShouldBeFound("hireDate.lessThanOrEqual=" + DEFAULT_HIRE_DATE);

        // Get all the employeeList where hireDate is less than or equal to SMALLER_HIRE_DATE
        defaultEmployeeShouldNotBeFound("hireDate.lessThanOrEqual=" + SMALLER_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHireDateIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where hireDate is less than DEFAULT_HIRE_DATE
        defaultEmployeeShouldNotBeFound("hireDate.lessThan=" + DEFAULT_HIRE_DATE);

        // Get all the employeeList where hireDate is less than UPDATED_HIRE_DATE
        defaultEmployeeShouldBeFound("hireDate.lessThan=" + UPDATED_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByHireDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where hireDate is greater than DEFAULT_HIRE_DATE
        defaultEmployeeShouldNotBeFound("hireDate.greaterThan=" + DEFAULT_HIRE_DATE);

        // Get all the employeeList where hireDate is greater than SMALLER_HIRE_DATE
        defaultEmployeeShouldBeFound("hireDate.greaterThan=" + SMALLER_HIRE_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmploymentStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employmentStatus equals to DEFAULT_EMPLOYMENT_STATUS
        defaultEmployeeShouldBeFound("employmentStatus.equals=" + DEFAULT_EMPLOYMENT_STATUS);

        // Get all the employeeList where employmentStatus equals to UPDATED_EMPLOYMENT_STATUS
        defaultEmployeeShouldNotBeFound("employmentStatus.equals=" + UPDATED_EMPLOYMENT_STATUS);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmploymentStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employmentStatus not equals to DEFAULT_EMPLOYMENT_STATUS
        defaultEmployeeShouldNotBeFound("employmentStatus.notEquals=" + DEFAULT_EMPLOYMENT_STATUS);

        // Get all the employeeList where employmentStatus not equals to UPDATED_EMPLOYMENT_STATUS
        defaultEmployeeShouldBeFound("employmentStatus.notEquals=" + UPDATED_EMPLOYMENT_STATUS);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmploymentStatusIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employmentStatus in DEFAULT_EMPLOYMENT_STATUS or UPDATED_EMPLOYMENT_STATUS
        defaultEmployeeShouldBeFound("employmentStatus.in=" + DEFAULT_EMPLOYMENT_STATUS + "," + UPDATED_EMPLOYMENT_STATUS);

        // Get all the employeeList where employmentStatus equals to UPDATED_EMPLOYMENT_STATUS
        defaultEmployeeShouldNotBeFound("employmentStatus.in=" + UPDATED_EMPLOYMENT_STATUS);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmploymentStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employmentStatus is not null
        defaultEmployeeShouldBeFound("employmentStatus.specified=true");

        // Get all the employeeList where employmentStatus is null
        defaultEmployeeShouldNotBeFound("employmentStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByEmploymentStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employmentStatus is greater than or equal to DEFAULT_EMPLOYMENT_STATUS
        defaultEmployeeShouldBeFound("employmentStatus.greaterThanOrEqual=" + DEFAULT_EMPLOYMENT_STATUS);

        // Get all the employeeList where employmentStatus is greater than or equal to UPDATED_EMPLOYMENT_STATUS
        defaultEmployeeShouldNotBeFound("employmentStatus.greaterThanOrEqual=" + UPDATED_EMPLOYMENT_STATUS);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmploymentStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employmentStatus is less than or equal to DEFAULT_EMPLOYMENT_STATUS
        defaultEmployeeShouldBeFound("employmentStatus.lessThanOrEqual=" + DEFAULT_EMPLOYMENT_STATUS);

        // Get all the employeeList where employmentStatus is less than or equal to SMALLER_EMPLOYMENT_STATUS
        defaultEmployeeShouldNotBeFound("employmentStatus.lessThanOrEqual=" + SMALLER_EMPLOYMENT_STATUS);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmploymentStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employmentStatus is less than DEFAULT_EMPLOYMENT_STATUS
        defaultEmployeeShouldNotBeFound("employmentStatus.lessThan=" + DEFAULT_EMPLOYMENT_STATUS);

        // Get all the employeeList where employmentStatus is less than UPDATED_EMPLOYMENT_STATUS
        defaultEmployeeShouldBeFound("employmentStatus.lessThan=" + UPDATED_EMPLOYMENT_STATUS);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmploymentStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employmentStatus is greater than DEFAULT_EMPLOYMENT_STATUS
        defaultEmployeeShouldNotBeFound("employmentStatus.greaterThan=" + DEFAULT_EMPLOYMENT_STATUS);

        // Get all the employeeList where employmentStatus is greater than SMALLER_EMPLOYMENT_STATUS
        defaultEmployeeShouldBeFound("employmentStatus.greaterThan=" + SMALLER_EMPLOYMENT_STATUS);
    }

    @Test
    @Transactional
    void getAllEmployeesByManagerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where managerId equals to DEFAULT_MANAGER_ID
        defaultEmployeeShouldBeFound("managerId.equals=" + DEFAULT_MANAGER_ID);

        // Get all the employeeList where managerId equals to UPDATED_MANAGER_ID
        defaultEmployeeShouldNotBeFound("managerId.equals=" + UPDATED_MANAGER_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByManagerIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where managerId not equals to DEFAULT_MANAGER_ID
        defaultEmployeeShouldNotBeFound("managerId.notEquals=" + DEFAULT_MANAGER_ID);

        // Get all the employeeList where managerId not equals to UPDATED_MANAGER_ID
        defaultEmployeeShouldBeFound("managerId.notEquals=" + UPDATED_MANAGER_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByManagerIdIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where managerId in DEFAULT_MANAGER_ID or UPDATED_MANAGER_ID
        defaultEmployeeShouldBeFound("managerId.in=" + DEFAULT_MANAGER_ID + "," + UPDATED_MANAGER_ID);

        // Get all the employeeList where managerId equals to UPDATED_MANAGER_ID
        defaultEmployeeShouldNotBeFound("managerId.in=" + UPDATED_MANAGER_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByManagerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where managerId is not null
        defaultEmployeeShouldBeFound("managerId.specified=true");

        // Get all the employeeList where managerId is null
        defaultEmployeeShouldNotBeFound("managerId.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByManagerIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where managerId is greater than or equal to DEFAULT_MANAGER_ID
        defaultEmployeeShouldBeFound("managerId.greaterThanOrEqual=" + DEFAULT_MANAGER_ID);

        // Get all the employeeList where managerId is greater than or equal to UPDATED_MANAGER_ID
        defaultEmployeeShouldNotBeFound("managerId.greaterThanOrEqual=" + UPDATED_MANAGER_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByManagerIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where managerId is less than or equal to DEFAULT_MANAGER_ID
        defaultEmployeeShouldBeFound("managerId.lessThanOrEqual=" + DEFAULT_MANAGER_ID);

        // Get all the employeeList where managerId is less than or equal to SMALLER_MANAGER_ID
        defaultEmployeeShouldNotBeFound("managerId.lessThanOrEqual=" + SMALLER_MANAGER_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByManagerIdIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where managerId is less than DEFAULT_MANAGER_ID
        defaultEmployeeShouldNotBeFound("managerId.lessThan=" + DEFAULT_MANAGER_ID);

        // Get all the employeeList where managerId is less than UPDATED_MANAGER_ID
        defaultEmployeeShouldBeFound("managerId.lessThan=" + UPDATED_MANAGER_ID);
    }

    @Test
    @Transactional
    void getAllEmployeesByManagerIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where managerId is greater than DEFAULT_MANAGER_ID
        defaultEmployeeShouldNotBeFound("managerId.greaterThan=" + DEFAULT_MANAGER_ID);

        // Get all the employeeList where managerId is greater than SMALLER_MANAGER_ID
        defaultEmployeeShouldBeFound("managerId.greaterThan=" + SMALLER_MANAGER_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeShouldBeFound(String filter) throws Exception {
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeId").value(hasItem(DEFAULT_EMPLOYEE_ID)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE)))
            .andExpect(jsonPath("$.[*].employmentStatus").value(hasItem(DEFAULT_EMPLOYMENT_STATUS)))
            .andExpect(jsonPath("$.[*].managerId").value(hasItem(DEFAULT_MANAGER_ID)));

        // Check, that the count call also returns 1
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeeShouldNotBeFound(String filter) throws Exception {
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmployee() throws Exception {
        // Get the employee
        restEmployeeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee
        Employee updatedEmployee = employeeRepository.findById(employee.getId()).get();
        // Disconnect from session so that the updates on updatedEmployee are not directly saved in db
        em.detach(updatedEmployee);
        updatedEmployee
            .employeeId(UPDATED_EMPLOYEE_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .gender(UPDATED_GENDER)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .birthDate(UPDATED_BIRTH_DATE)
            .hireDate(UPDATED_HIRE_DATE)
            .employmentStatus(UPDATED_EMPLOYMENT_STATUS)
            .managerId(UPDATED_MANAGER_ID);
        EmployeeDTO employeeDTO = employeeMapper.toDto(updatedEmployee);

        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testEmployee.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployee.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testEmployee.getMaritalStatus()).isEqualTo(UPDATED_MARITAL_STATUS);
        assertThat(testEmployee.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testEmployee.getHireDate()).isEqualTo(UPDATED_HIRE_DATE);
        assertThat(testEmployee.getEmploymentStatus()).isEqualTo(UPDATED_EMPLOYMENT_STATUS);
        assertThat(testEmployee.getManagerId()).isEqualTo(UPDATED_MANAGER_ID);
    }

    @Test
    @Transactional
    void putNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .hireDate(UPDATED_HIRE_DATE)
            .employmentStatus(UPDATED_EMPLOYMENT_STATUS)
            .managerId(UPDATED_MANAGER_ID);

        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmployeeId()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testEmployee.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployee.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testEmployee.getMaritalStatus()).isEqualTo(DEFAULT_MARITAL_STATUS);
        assertThat(testEmployee.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testEmployee.getHireDate()).isEqualTo(UPDATED_HIRE_DATE);
        assertThat(testEmployee.getEmploymentStatus()).isEqualTo(UPDATED_EMPLOYMENT_STATUS);
        assertThat(testEmployee.getManagerId()).isEqualTo(UPDATED_MANAGER_ID);
    }

    @Test
    @Transactional
    void fullUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .employeeId(UPDATED_EMPLOYEE_ID)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .gender(UPDATED_GENDER)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .birthDate(UPDATED_BIRTH_DATE)
            .hireDate(UPDATED_HIRE_DATE)
            .employmentStatus(UPDATED_EMPLOYMENT_STATUS)
            .managerId(UPDATED_MANAGER_ID);

        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmployeeId()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testEmployee.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployee.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testEmployee.getMaritalStatus()).isEqualTo(UPDATED_MARITAL_STATUS);
        assertThat(testEmployee.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testEmployee.getHireDate()).isEqualTo(UPDATED_HIRE_DATE);
        assertThat(testEmployee.getEmploymentStatus()).isEqualTo(UPDATED_EMPLOYMENT_STATUS);
        assertThat(testEmployee.getManagerId()).isEqualTo(UPDATED_MANAGER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(employeeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeDelete = employeeRepository.findAll().size();

        // Delete the employee
        restEmployeeMockMvc
            .perform(delete(ENTITY_API_URL_ID, employee.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
