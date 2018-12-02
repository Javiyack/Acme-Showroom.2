
package forms;

import domain.Actor;
import domain.Comment;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;
import utilities.URLCollection;

import javax.persistence.ElementCollection;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;


public class CommentForm extends ClassForm{

    private String title;
    private String text;
    private Date moment;
    private Integer rating;
    private Collection<String> pictures;
    // Relationships
    private Actor actor;
    private int commentedObjectId;
    private String targetName;
    private String path;
    // Static
    public CommentForm(){
        super();
        this.setVersion(0);
        this.setId(0);

    }
    public CommentForm(Comment commet){
        this.setId(commet.getId());
        this.setVersion(commet.getVersion());
        this.setTitle(commet.getTitle());
        this.setMoment(commet.getMoment());
        this.setText(commet.getText());
        this.setPictures(commet.getPictures());
        this.setRating(commet.getRating());
        this.setActor(commet.getActor());
        this.setCommentedObjectId(commet.getCommentedObjectId());
    }

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    public Date getMoment() {
        return this.moment;
    }

    public void setMoment(final Date moment) {
        this.moment = moment;
    }

    @NotBlank
    @SafeHtml
    @Size(min = 2, max = 255)
    public String getText() {
        return this.text;
    }

    public void setText(final String body) {
        this.text = body;
    }

    @NotBlank
    @SafeHtml
    @Size(min = 2, max = 32)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }


    @Range(min = 0, max = 3)
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @ElementCollection
    @NotNull
    @URLCollection
    public Collection<String> getPictures() {
        return pictures;
    }

    public void setPictures(Collection<String> pictures) {
        this.pictures = pictures;
    }

    @NotNull
    @Valid
    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor user) {
        this.actor = user;
    }

    @NotNull
    public int getCommentedObjectId() {
        return commentedObjectId;
    }

    public void setCommentedObjectId(int commentedObjectId) {
        this.commentedObjectId = commentedObjectId;
    }

    @NotBlank
    @SafeHtml
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @NotBlank
    @SafeHtml
    @Size(min = 2, max = 32)
    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }
}
