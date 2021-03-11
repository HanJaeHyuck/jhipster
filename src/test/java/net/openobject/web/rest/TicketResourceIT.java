package net.openobject.web.rest;

import static net.openobject.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import net.openobject.IntegrationTest;
import net.openobject.domain.Attachment;
import net.openobject.domain.Label;
import net.openobject.domain.Project;
import net.openobject.domain.Ticket;
import net.openobject.domain.User;
import net.openobject.domain.enumeration.Priority;
import net.openobject.domain.enumeration.Status;
import net.openobject.domain.enumeration.Type;
import net.openobject.repository.TicketRepository;
import net.openobject.service.TicketQueryService;
import net.openobject.service.TicketService;
import net.openobject.service.dto.TicketCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TicketResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TicketResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DUE_DATE = LocalDate.ofEpochDay(-1L);

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Status DEFAULT_STATUS = Status.OPEN;
    private static final Status UPDATED_STATUS = Status.WAITING_FOR_RESPONSE;

    private static final Type DEFAULT_TYPE = Type.BUG;
    private static final Type UPDATED_TYPE = Type.FEATURE;

    private static final Priority DEFAULT_PRIORITY = Priority.HIGHEST;
    private static final Priority UPDATED_PRIORITY = Priority.HIGHER;

    @Autowired
    private TicketRepository ticketRepository;

    @Mock
    private TicketRepository ticketRepositoryMock;

    @Mock
    private TicketService ticketServiceMock;

    @Autowired
    private TicketQueryService ticketQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTicketMockMvc;

    private Ticket ticket;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticket createEntity(EntityManager em) {
        Ticket ticket = new Ticket()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .dueDate(DEFAULT_DUE_DATE)
            .date(DEFAULT_DATE)
            .status(DEFAULT_STATUS)
            .type(DEFAULT_TYPE)
            .priority(DEFAULT_PRIORITY);
        return ticket;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticket createUpdatedEntity(EntityManager em) {
        Ticket ticket = new Ticket()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .dueDate(UPDATED_DUE_DATE)
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS)
            .type(UPDATED_TYPE)
            .priority(UPDATED_PRIORITY);
        return ticket;
    }

    @BeforeEach
    public void initTest() {
        ticket = createEntity(em);
    }

    @Test
    @Transactional
    void createTicket() throws Exception {
        int databaseSizeBeforeCreate = ticketRepository.findAll().size();
        // Create the Ticket
        restTicketMockMvc
            .perform(post("/api/tickets").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticket)))
            .andExpect(status().isCreated());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeCreate + 1);
        Ticket testTicket = ticketList.get(ticketList.size() - 1);
        assertThat(testTicket.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTicket.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTicket.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testTicket.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testTicket.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTicket.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTicket.getPriority()).isEqualTo(DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void createTicketWithExistingId() throws Exception {
        // Create the Ticket with an existing ID
        ticket.setId(1L);

        int databaseSizeBeforeCreate = ticketRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTicketMockMvc
            .perform(post("/api/tickets").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticket)))
            .andExpect(status().isBadRequest());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = ticketRepository.findAll().size();
        // set the field null
        ticket.setTitle(null);

        // Create the Ticket, which fails.

        restTicketMockMvc
            .perform(post("/api/tickets").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticket)))
            .andExpect(status().isBadRequest());

        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTickets() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList
        restTicketMockMvc
            .perform(get("/api/tickets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticket.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTicketsWithEagerRelationshipsIsEnabled() throws Exception {
        when(ticketServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTicketMockMvc.perform(get("/api/tickets?eagerload=true")).andExpect(status().isOk());

        verify(ticketServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTicketsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ticketServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTicketMockMvc.perform(get("/api/tickets?eagerload=true")).andExpect(status().isOk());

        verify(ticketServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTicket() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get the ticket
        restTicketMockMvc
            .perform(get("/api/tickets/{id}", ticket.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ticket.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()));
    }

    @Test
    @Transactional
    void getTicketsByIdFiltering() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        Long id = ticket.getId();

        defaultTicketShouldBeFound("id.equals=" + id);
        defaultTicketShouldNotBeFound("id.notEquals=" + id);

        defaultTicketShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTicketShouldNotBeFound("id.greaterThan=" + id);

        defaultTicketShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTicketShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTicketsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where title equals to DEFAULT_TITLE
        defaultTicketShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the ticketList where title equals to UPDATED_TITLE
        defaultTicketShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTicketsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where title not equals to DEFAULT_TITLE
        defaultTicketShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the ticketList where title not equals to UPDATED_TITLE
        defaultTicketShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTicketsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultTicketShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the ticketList where title equals to UPDATED_TITLE
        defaultTicketShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTicketsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where title is not null
        defaultTicketShouldBeFound("title.specified=true");

        // Get all the ticketList where title is null
        defaultTicketShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketsByTitleContainsSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where title contains DEFAULT_TITLE
        defaultTicketShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the ticketList where title contains UPDATED_TITLE
        defaultTicketShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTicketsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where title does not contain DEFAULT_TITLE
        defaultTicketShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the ticketList where title does not contain UPDATED_TITLE
        defaultTicketShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTicketsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where description equals to DEFAULT_DESCRIPTION
        defaultTicketShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the ticketList where description equals to UPDATED_DESCRIPTION
        defaultTicketShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTicketsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where description not equals to DEFAULT_DESCRIPTION
        defaultTicketShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the ticketList where description not equals to UPDATED_DESCRIPTION
        defaultTicketShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTicketsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTicketShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the ticketList where description equals to UPDATED_DESCRIPTION
        defaultTicketShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTicketsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where description is not null
        defaultTicketShouldBeFound("description.specified=true");

        // Get all the ticketList where description is null
        defaultTicketShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where description contains DEFAULT_DESCRIPTION
        defaultTicketShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the ticketList where description contains UPDATED_DESCRIPTION
        defaultTicketShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTicketsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where description does not contain DEFAULT_DESCRIPTION
        defaultTicketShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the ticketList where description does not contain UPDATED_DESCRIPTION
        defaultTicketShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTicketsByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where dueDate equals to DEFAULT_DUE_DATE
        defaultTicketShouldBeFound("dueDate.equals=" + DEFAULT_DUE_DATE);

        // Get all the ticketList where dueDate equals to UPDATED_DUE_DATE
        defaultTicketShouldNotBeFound("dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllTicketsByDueDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where dueDate not equals to DEFAULT_DUE_DATE
        defaultTicketShouldNotBeFound("dueDate.notEquals=" + DEFAULT_DUE_DATE);

        // Get all the ticketList where dueDate not equals to UPDATED_DUE_DATE
        defaultTicketShouldBeFound("dueDate.notEquals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllTicketsByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where dueDate in DEFAULT_DUE_DATE or UPDATED_DUE_DATE
        defaultTicketShouldBeFound("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE);

        // Get all the ticketList where dueDate equals to UPDATED_DUE_DATE
        defaultTicketShouldNotBeFound("dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllTicketsByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where dueDate is not null
        defaultTicketShouldBeFound("dueDate.specified=true");

        // Get all the ticketList where dueDate is null
        defaultTicketShouldNotBeFound("dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketsByDueDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where dueDate is greater than or equal to DEFAULT_DUE_DATE
        defaultTicketShouldBeFound("dueDate.greaterThanOrEqual=" + DEFAULT_DUE_DATE);

        // Get all the ticketList where dueDate is greater than or equal to UPDATED_DUE_DATE
        defaultTicketShouldNotBeFound("dueDate.greaterThanOrEqual=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllTicketsByDueDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where dueDate is less than or equal to DEFAULT_DUE_DATE
        defaultTicketShouldBeFound("dueDate.lessThanOrEqual=" + DEFAULT_DUE_DATE);

        // Get all the ticketList where dueDate is less than or equal to SMALLER_DUE_DATE
        defaultTicketShouldNotBeFound("dueDate.lessThanOrEqual=" + SMALLER_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllTicketsByDueDateIsLessThanSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where dueDate is less than DEFAULT_DUE_DATE
        defaultTicketShouldNotBeFound("dueDate.lessThan=" + DEFAULT_DUE_DATE);

        // Get all the ticketList where dueDate is less than UPDATED_DUE_DATE
        defaultTicketShouldBeFound("dueDate.lessThan=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllTicketsByDueDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where dueDate is greater than DEFAULT_DUE_DATE
        defaultTicketShouldNotBeFound("dueDate.greaterThan=" + DEFAULT_DUE_DATE);

        // Get all the ticketList where dueDate is greater than SMALLER_DUE_DATE
        defaultTicketShouldBeFound("dueDate.greaterThan=" + SMALLER_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllTicketsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where date equals to DEFAULT_DATE
        defaultTicketShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the ticketList where date equals to UPDATED_DATE
        defaultTicketShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTicketsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where date not equals to DEFAULT_DATE
        defaultTicketShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the ticketList where date not equals to UPDATED_DATE
        defaultTicketShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTicketsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where date in DEFAULT_DATE or UPDATED_DATE
        defaultTicketShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the ticketList where date equals to UPDATED_DATE
        defaultTicketShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTicketsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where date is not null
        defaultTicketShouldBeFound("date.specified=true");

        // Get all the ticketList where date is null
        defaultTicketShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where date is greater than or equal to DEFAULT_DATE
        defaultTicketShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the ticketList where date is greater than or equal to UPDATED_DATE
        defaultTicketShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTicketsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where date is less than or equal to DEFAULT_DATE
        defaultTicketShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the ticketList where date is less than or equal to SMALLER_DATE
        defaultTicketShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllTicketsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where date is less than DEFAULT_DATE
        defaultTicketShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the ticketList where date is less than UPDATED_DATE
        defaultTicketShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTicketsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where date is greater than DEFAULT_DATE
        defaultTicketShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the ticketList where date is greater than SMALLER_DATE
        defaultTicketShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllTicketsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where status equals to DEFAULT_STATUS
        defaultTicketShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the ticketList where status equals to UPDATED_STATUS
        defaultTicketShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTicketsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where status not equals to DEFAULT_STATUS
        defaultTicketShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the ticketList where status not equals to UPDATED_STATUS
        defaultTicketShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTicketsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultTicketShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the ticketList where status equals to UPDATED_STATUS
        defaultTicketShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTicketsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where status is not null
        defaultTicketShouldBeFound("status.specified=true");

        // Get all the ticketList where status is null
        defaultTicketShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where type equals to DEFAULT_TYPE
        defaultTicketShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the ticketList where type equals to UPDATED_TYPE
        defaultTicketShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTicketsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where type not equals to DEFAULT_TYPE
        defaultTicketShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the ticketList where type not equals to UPDATED_TYPE
        defaultTicketShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTicketsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultTicketShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the ticketList where type equals to UPDATED_TYPE
        defaultTicketShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTicketsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where type is not null
        defaultTicketShouldBeFound("type.specified=true");

        // Get all the ticketList where type is null
        defaultTicketShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketsByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where priority equals to DEFAULT_PRIORITY
        defaultTicketShouldBeFound("priority.equals=" + DEFAULT_PRIORITY);

        // Get all the ticketList where priority equals to UPDATED_PRIORITY
        defaultTicketShouldNotBeFound("priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllTicketsByPriorityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where priority not equals to DEFAULT_PRIORITY
        defaultTicketShouldNotBeFound("priority.notEquals=" + DEFAULT_PRIORITY);

        // Get all the ticketList where priority not equals to UPDATED_PRIORITY
        defaultTicketShouldBeFound("priority.notEquals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllTicketsByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where priority in DEFAULT_PRIORITY or UPDATED_PRIORITY
        defaultTicketShouldBeFound("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY);

        // Get all the ticketList where priority equals to UPDATED_PRIORITY
        defaultTicketShouldNotBeFound("priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllTicketsByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where priority is not null
        defaultTicketShouldBeFound("priority.specified=true");

        // Get all the ticketList where priority is null
        defaultTicketShouldNotBeFound("priority.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketsByAttachmentIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);
        Attachment attachment = AttachmentResourceIT.createEntity(em);
        em.persist(attachment);
        em.flush();
        ticket.addAttachment(attachment);
        ticketRepository.saveAndFlush(ticket);
        Long attachmentId = attachment.getId();

        // Get all the ticketList where attachment equals to attachmentId
        defaultTicketShouldBeFound("attachmentId.equals=" + attachmentId);

        // Get all the ticketList where attachment equals to attachmentId + 1
        defaultTicketShouldNotBeFound("attachmentId.equals=" + (attachmentId + 1));
    }

    @Test
    @Transactional
    void getAllTicketsByProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);
        Project project = ProjectResourceIT.createEntity(em);
        em.persist(project);
        em.flush();
        ticket.setProject(project);
        ticketRepository.saveAndFlush(ticket);
        Long projectId = project.getId();

        // Get all the ticketList where project equals to projectId
        defaultTicketShouldBeFound("projectId.equals=" + projectId);

        // Get all the ticketList where project equals to projectId + 1
        defaultTicketShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    @Test
    @Transactional
    void getAllTicketsByAssignedToIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);
        User assignedTo = UserResourceIT.createEntity(em);
        em.persist(assignedTo);
        em.flush();
        ticket.setAssignedTo(assignedTo);
        ticketRepository.saveAndFlush(ticket);
        Long assignedToId = assignedTo.getId();

        // Get all the ticketList where assignedTo equals to assignedToId
        defaultTicketShouldBeFound("assignedToId.equals=" + assignedToId);

        // Get all the ticketList where assignedTo equals to assignedToId + 1
        defaultTicketShouldNotBeFound("assignedToId.equals=" + (assignedToId + 1));
    }

    @Test
    @Transactional
    void getAllTicketsByReportedByIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);
        User reportedBy = UserResourceIT.createEntity(em);
        em.persist(reportedBy);
        em.flush();
        ticket.setReportedBy(reportedBy);
        ticketRepository.saveAndFlush(ticket);
        Long reportedById = reportedBy.getId();

        // Get all the ticketList where reportedBy equals to reportedById
        defaultTicketShouldBeFound("reportedById.equals=" + reportedById);

        // Get all the ticketList where reportedBy equals to reportedById + 1
        defaultTicketShouldNotBeFound("reportedById.equals=" + (reportedById + 1));
    }

    @Test
    @Transactional
    void getAllTicketsByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);
        Label label = LabelResourceIT.createEntity(em);
        em.persist(label);
        em.flush();
        ticket.addLabel(label);
        ticketRepository.saveAndFlush(ticket);
        Long labelId = label.getId();

        // Get all the ticketList where label equals to labelId
        defaultTicketShouldBeFound("labelId.equals=" + labelId);

        // Get all the ticketList where label equals to labelId + 1
        defaultTicketShouldNotBeFound("labelId.equals=" + (labelId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTicketShouldBeFound(String filter) throws Exception {
        restTicketMockMvc
            .perform(get("/api/tickets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticket.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())));

        // Check, that the count call also returns 1
        restTicketMockMvc
            .perform(get("/api/tickets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTicketShouldNotBeFound(String filter) throws Exception {
        restTicketMockMvc
            .perform(get("/api/tickets?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTicketMockMvc
            .perform(get("/api/tickets/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTicket() throws Exception {
        // Get the ticket
        restTicketMockMvc.perform(get("/api/tickets/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateTicket() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        int databaseSizeBeforeUpdate = ticketRepository.findAll().size();

        // Update the ticket
        Ticket updatedTicket = ticketRepository.findById(ticket.getId()).get();
        // Disconnect from session so that the updates on updatedTicket are not directly saved in db
        em.detach(updatedTicket);
        updatedTicket
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .dueDate(UPDATED_DUE_DATE)
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS)
            .type(UPDATED_TYPE)
            .priority(UPDATED_PRIORITY);

        restTicketMockMvc
            .perform(put("/api/tickets").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(updatedTicket)))
            .andExpect(status().isOk());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
        Ticket testTicket = ticketList.get(ticketList.size() - 1);
        assertThat(testTicket.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTicket.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTicket.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testTicket.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testTicket.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTicket.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTicket.getPriority()).isEqualTo(UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void updateNonExistingTicket() throws Exception {
        int databaseSizeBeforeUpdate = ticketRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTicketMockMvc
            .perform(put("/api/tickets").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticket)))
            .andExpect(status().isBadRequest());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTicketWithPatch() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        int databaseSizeBeforeUpdate = ticketRepository.findAll().size();

        // Update the ticket using partial update
        Ticket partialUpdatedTicket = new Ticket();
        partialUpdatedTicket.setId(ticket.getId());

        partialUpdatedTicket.title(UPDATED_TITLE).dueDate(UPDATED_DUE_DATE).priority(UPDATED_PRIORITY);

        restTicketMockMvc
            .perform(
                patch("/api/tickets")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTicket))
            )
            .andExpect(status().isOk());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
        Ticket testTicket = ticketList.get(ticketList.size() - 1);
        assertThat(testTicket.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTicket.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTicket.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testTicket.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testTicket.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTicket.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTicket.getPriority()).isEqualTo(UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void fullUpdateTicketWithPatch() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        int databaseSizeBeforeUpdate = ticketRepository.findAll().size();

        // Update the ticket using partial update
        Ticket partialUpdatedTicket = new Ticket();
        partialUpdatedTicket.setId(ticket.getId());

        partialUpdatedTicket
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .dueDate(UPDATED_DUE_DATE)
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS)
            .type(UPDATED_TYPE)
            .priority(UPDATED_PRIORITY);

        restTicketMockMvc
            .perform(
                patch("/api/tickets")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTicket))
            )
            .andExpect(status().isOk());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
        Ticket testTicket = ticketList.get(ticketList.size() - 1);
        assertThat(testTicket.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTicket.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTicket.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testTicket.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testTicket.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTicket.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTicket.getPriority()).isEqualTo(UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void partialUpdateTicketShouldThrown() throws Exception {
        // Update the ticket without id should throw
        Ticket partialUpdatedTicket = new Ticket();

        restTicketMockMvc
            .perform(
                patch("/api/tickets")
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTicket))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void deleteTicket() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        int databaseSizeBeforeDelete = ticketRepository.findAll().size();

        // Delete the ticket
        restTicketMockMvc
            .perform(delete("/api/tickets/{id}", ticket.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
