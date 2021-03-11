package net.openobject.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Comment.
 */
@Entity
@Table(name = "comment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "text")
    private String text;

    @OneToMany(mappedBy = "child")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parents", "login", "child" }, allowSetters = true)
    private Set<Comment> parents = new HashSet<>();

    @ManyToOne
    private User login;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parents", "login", "child" }, allowSetters = true)
    private Comment child;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Comment id(Long id) {
        this.id = id;
        return this;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public Comment date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getText() {
        return this.text;
    }

    public Comment text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<Comment> getParents() {
        return this.parents;
    }

    public Comment parents(Set<Comment> comments) {
        this.setParents(comments);
        return this;
    }

    public Comment addParent(Comment comment) {
        this.parents.add(comment);
        comment.setChild(this);
        return this;
    }

    public Comment removeParent(Comment comment) {
        this.parents.remove(comment);
        comment.setChild(null);
        return this;
    }

    public void setParents(Set<Comment> comments) {
        if (this.parents != null) {
            this.parents.forEach(i -> i.setChild(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setChild(this));
        }
        this.parents = comments;
    }

    public User getLogin() {
        return this.login;
    }

    public Comment login(User user) {
        this.setLogin(user);
        return this;
    }

    public void setLogin(User user) {
        this.login = user;
    }

    public Comment getChild() {
        return this.child;
    }

    public Comment child(Comment comment) {
        this.setChild(comment);
        return this;
    }

    public void setChild(Comment comment) {
        this.child = comment;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comment)) {
            return false;
        }
        return id != null && id.equals(((Comment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Comment{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", text='" + getText() + "'" +
            "}";
    }
}
