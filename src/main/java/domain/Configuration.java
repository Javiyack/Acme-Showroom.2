
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class Configuration extends DomainEntity {

	private String systemTitle;
	private String	logo;
	private Collection<String> tabooWords;
	private Collection<String> positiveAuditWords;
	private Collection<String> negativeAuditWords;
	private Collection<String> stoppingPositiveAuditWords;
	private Collection<String> stoppingNegativeAuditWords;
	private String	welcomeMessageEs;
	private String	welcomeMessageEn;

	@ElementCollection
	@NotNull
	public Collection<String> getTabooWords() {
		return tabooWords;
	}

	public void setTabooWords(Collection<String> tabooWords) {
		this.tabooWords = tabooWords;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getSystemTitle() {
		return this.systemTitle;
	}

	public void setSystemTitle(final String systemTitle) {
		this.systemTitle = systemTitle;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	@NotBlank
	public String getWelcomeMessageEs() {
		return this.welcomeMessageEs;
	}

	public void setWelcomeMessageEs(final String welcomeMessageEs) {
		this.welcomeMessageEs = welcomeMessageEs;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	@NotBlank
	public String getWelcomeMessageEn() {
		return this.welcomeMessageEn;
	}

	public void setWelcomeMessageEn(final String wecolmeMessageEn) {
		this.welcomeMessageEn = wecolmeMessageEn;
	}



	@SafeHtml(whitelistType = WhiteListType.NONE)
	@NotBlank
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}


	@ElementCollection
	@NotNull
	public Collection<String> getPositiveAuditWords() {
		return positiveAuditWords;
	}

	public void setPositiveAuditWords(Collection<String> positiveAuditWords) {
		this.positiveAuditWords = positiveAuditWords;
	}

	@ElementCollection
	@NotNull
	public Collection <String> getNegativeAuditWords() {
		return negativeAuditWords;
	}

	public void setNegativeAuditWords(Collection <String> negativeAuditWords) {
		this.negativeAuditWords = negativeAuditWords;
	}

	@ElementCollection
	@NotNull
	public Collection <String> getStoppingNegativeAuditWords() {
		return stoppingNegativeAuditWords;
	}

	public void setStoppingNegativeAuditWords(Collection <String> stoppingNegativeAuditWords) {
		this.stoppingNegativeAuditWords = stoppingNegativeAuditWords;
	}


	@ElementCollection
	@NotNull
	public Collection <String> getStoppingPositiveAuditWords() {
		return stoppingPositiveAuditWords;
	}

	public void setStoppingPositiveAuditWords(Collection <String> stoppingPositiveAuditWords) {
		this.stoppingPositiveAuditWords = stoppingPositiveAuditWords;
	}
}
