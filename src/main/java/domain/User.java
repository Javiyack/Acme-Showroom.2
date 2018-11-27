
package domain;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {
	private String 		photo; 			// Optional
	private Date 		birthdate; 			// Optional
	private String 		genere; 			// Optional

	@NotBlank
	@URL
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@Past
	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date bithdate) {
		this.birthdate = bithdate;
	}


	@Pattern(regexp = "MALE|FEMALE|UNDEFINED")
	public String getGenere() {
		return genere;
	}

	public void setGenere(String genere) {
		this.genere = genere;
	}
	
}
