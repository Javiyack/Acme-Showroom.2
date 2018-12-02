
package domain;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {@Index(columnList = "name, description")})
public class Showroom extends Commentable {


    private String name;
    private String description;
    private String logo;

    // Relationships
    private User user;

    @Valid
    @NotNull
    @ManyToOne(optional = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @NotNull
    @NotBlank
    @SafeHtml
    @Size(min = 2, max = 32)
    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        super.setObjectName(name);
        this.name = name;
    }

    @SafeHtml(whitelistType = WhiteListType.NONE)
    @NotNull
    @NotBlank
    @Size(min = 2, max = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @NotNull
    @NotBlank
    @URL
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
