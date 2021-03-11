package net.openobject.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import net.openobject.domain.enumeration.Priority;
import net.openobject.domain.enumeration.Status;
import net.openobject.domain.enumeration.Type;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ticket.
 */
@Entity
@Table(name = "ticket")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "date")
    private ZonedDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority;

    @OneToMany(mappedBy = "ticket")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ticket" }, allowSetters = true)
    private Set<Attachment> attachments = new HashSet<>();

    @ManyToOne
    private Project project;

    @ManyToOne
    private User assignedTo;

    @ManyToOne
    private User reportedBy;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_ticket__label",
        joinColumns = @JoinColumn(name = "ticket_id"),
        inverseJoinColumns = @JoinColumn(name = "label_id")
    )
    @JsonIgnoreProperties(value = { "tickets" }, allowSetters = true)
    private Set<Label> labels = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ticket id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Ticket title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Ticket description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public Ticket dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public Ticket date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Status getStatus() {
        return this.status;
    }

    public Ticket status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return this.type;
    }

    public Ticket type(Type type) {
        this.type = type;
        return this;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public Ticket priority(Priority priority) {
        this.priority = priority;
        return this;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Set<Attachment> getAttachments() {
        return this.attachments;
    }

    public Ticket attachments(Set<Attachment> attachments) {
        this.setAttachments(attachments);
        return this;
    }

    public Ticket addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
        attachment.setTicket(this);
        return this;
    }

    public Ticket removeAttachment(Attachment attachment) {
        this.attachments.remove(attachment);
        attachment.setTicket(null);
        return this;
    }

    public void setAttachments(Set<Attachment> attachments) {
        if (this.attachments != null) {
            this.attachments.forEach(i -> i.setTicket(null));
        }
        if (attachments != null) {
            attachments.forEach(i -> i.setTicket(this));
        }
        this.attachments = attachments;
    }

    public Project getProject() {
        return this.project;
    }

    public Ticket project(Project project) {
        this.setProject(project);
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getAssignedTo() {
        return this.assignedTo;
    }

    public Ticket assignedTo(User user) {
        this.setAssignedTo(user);
        return this;
    }

    public void setAssignedTo(User user) {
        this.assignedTo = user;
    }

    public User getReportedBy() {
        return this.reportedBy;
    }

    public Ticket reportedBy(User user) {
        this.setReportedBy(user);
        return this;
    }

    public void setReportedBy(User user) {
        this.reportedBy = user;
    }

    public Set<Label> getLabels() {
        return this.labels;
    }

    public Ticket labels(Set<Label> labels) {
        this.setLabels(labels);
        return this;
    }

    public Ticket addLabel(Label label) {
        this.labels.add(label);
        label.getTickets().add(this);
        return this;
    }

    public Ticket removeLabel(Label label) {
        this.labels.remove(label);
        label.getTickets().remove(this);
        return this;
    }

    public void setLabels(Set<Label> labels) {
        this.labels = labels;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ticket)) {
            return false;
        }
        return id != null && id.equals(((Ticket) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ticket{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", date='" + getDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", type='" + getType() + "'" +
            ", priority='" + getPriority() + "'" +
            "}";
    }
}
