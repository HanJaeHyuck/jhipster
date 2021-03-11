package net.openobject.service.dto;

import java.io.Serializable;
import java.util.Objects;
import net.openobject.domain.enumeration.Priority;
import net.openobject.domain.enumeration.Status;
import net.openobject.domain.enumeration.Type;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link net.openobject.domain.Ticket} entity. This class is used
 * in {@link net.openobject.web.rest.TicketResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tickets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TicketCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Status
     */
    public static class StatusFilter extends Filter<Status> {

        public StatusFilter() {}

        public StatusFilter(StatusFilter filter) {
            super(filter);
        }

        @Override
        public StatusFilter copy() {
            return new StatusFilter(this);
        }
    }

    /**
     * Class for filtering Type
     */
    public static class TypeFilter extends Filter<Type> {

        public TypeFilter() {}

        public TypeFilter(TypeFilter filter) {
            super(filter);
        }

        @Override
        public TypeFilter copy() {
            return new TypeFilter(this);
        }
    }

    /**
     * Class for filtering Priority
     */
    public static class PriorityFilter extends Filter<Priority> {

        public PriorityFilter() {}

        public PriorityFilter(PriorityFilter filter) {
            super(filter);
        }

        @Override
        public PriorityFilter copy() {
            return new PriorityFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter description;

    private LocalDateFilter dueDate;

    private ZonedDateTimeFilter date;

    private StatusFilter status;

    private TypeFilter type;

    private PriorityFilter priority;

    private LongFilter attachmentId;

    private LongFilter projectId;

    private LongFilter assignedToId;

    private LongFilter reportedById;

    private LongFilter labelId;

    public TicketCriteria() {}

    public TicketCriteria(TicketCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.dueDate = other.dueDate == null ? null : other.dueDate.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.priority = other.priority == null ? null : other.priority.copy();
        this.attachmentId = other.attachmentId == null ? null : other.attachmentId.copy();
        this.projectId = other.projectId == null ? null : other.projectId.copy();
        this.assignedToId = other.assignedToId == null ? null : other.assignedToId.copy();
        this.reportedById = other.reportedById == null ? null : other.reportedById.copy();
        this.labelId = other.labelId == null ? null : other.labelId.copy();
    }

    @Override
    public TicketCriteria copy() {
        return new TicketCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LocalDateFilter getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateFilter dueDate) {
        this.dueDate = dueDate;
    }

    public ZonedDateTimeFilter getDate() {
        return date;
    }

    public void setDate(ZonedDateTimeFilter date) {
        this.date = date;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public TypeFilter getType() {
        return type;
    }

    public void setType(TypeFilter type) {
        this.type = type;
    }

    public PriorityFilter getPriority() {
        return priority;
    }

    public void setPriority(PriorityFilter priority) {
        this.priority = priority;
    }

    public LongFilter getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(LongFilter attachmentId) {
        this.attachmentId = attachmentId;
    }

    public LongFilter getProjectId() {
        return projectId;
    }

    public void setProjectId(LongFilter projectId) {
        this.projectId = projectId;
    }

    public LongFilter getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(LongFilter assignedToId) {
        this.assignedToId = assignedToId;
    }

    public LongFilter getReportedById() {
        return reportedById;
    }

    public void setReportedById(LongFilter reportedById) {
        this.reportedById = reportedById;
    }

    public LongFilter getLabelId() {
        return labelId;
    }

    public void setLabelId(LongFilter labelId) {
        this.labelId = labelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TicketCriteria that = (TicketCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(dueDate, that.dueDate) &&
            Objects.equals(date, that.date) &&
            Objects.equals(status, that.status) &&
            Objects.equals(type, that.type) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(attachmentId, that.attachmentId) &&
            Objects.equals(projectId, that.projectId) &&
            Objects.equals(assignedToId, that.assignedToId) &&
            Objects.equals(reportedById, that.reportedById) &&
            Objects.equals(labelId, that.labelId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            description,
            dueDate,
            date,
            status,
            type,
            priority,
            attachmentId,
            projectId,
            assignedToId,
            reportedById,
            labelId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (dueDate != null ? "dueDate=" + dueDate + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (priority != null ? "priority=" + priority + ", " : "") +
                (attachmentId != null ? "attachmentId=" + attachmentId + ", " : "") +
                (projectId != null ? "projectId=" + projectId + ", " : "") +
                (assignedToId != null ? "assignedToId=" + assignedToId + ", " : "") +
                (reportedById != null ? "reportedById=" + reportedById + ", " : "") +
                (labelId != null ? "labelId=" + labelId + ", " : "") +
            "}";
    }
}
