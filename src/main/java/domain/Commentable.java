
package domain;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
@Entity
@Access(AccessType.PROPERTY)
public abstract class Commentable extends DomainEntity {

	private String objectName;


	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
}
