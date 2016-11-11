package gov.ca.emsa.pulse.broker.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="patient_record")
public class PatientRecordEntity {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column( name = "id", nullable = false )
	private Long id;
	
	@Column(name = "dob")
	private String dateOfBirth;
	
	@Column(name = "ssn")
	private String ssn;
	
	@Column(name = "patient_gender_id")
	private Long patientGenderId;
	
	@OneToOne
	@JoinColumn(name="patient_gender_id", insertable= false, updatable = false)
	private PatientGenderEntity patientGender;
	
	@Column(name = "organization_patient_record_id")
	private String organizationPatientRecordId;
	
	@Column(name = "phone_number")
	private String phoneNumber;
	
	@Column(name = "query_organization_id")
	private Long queryOrganizationId;
	
	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "query_organization_id", unique=true, nullable = true, insertable=false, updatable= false)
	private QueryOrganizationEntity queryOrganization;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="patientRecordId")
	@Column( name = "patient_record_id", nullable = false  )
	private Set<PatientRecordNameEntity> patientRecordName = new HashSet<PatientRecordNameEntity>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="patientRecordId")
	@Column( name = "patient_record_id", nullable = false  )
	private Set<PatientRecordAddressEntity> patientRecordAddress = new HashSet<PatientRecordAddressEntity>();
	
	@Column( name = "creation_date", insertable = false, updatable = false)
	private Date creationDate;
	
	@Column( name = "last_modified_date", insertable = false, updatable = false)
	private Date lastModifiedDate;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public Long getPatientGenderId() {
		return patientGenderId;
	}

	public void setPatientGenderId(Long patientGenderId) {
		this.patientGenderId = patientGenderId;
	}

	public PatientGenderEntity getPatientGender() {
		return patientGender;
	}

	public void setPatientGender(PatientGenderEntity patientGender) {
		this.patientGender = patientGender;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Long getQueryOrganizationId() {
		return queryOrganizationId;
	}

	public void setQueryOrganizationId(Long queryOrganizationId) {
		this.queryOrganizationId = queryOrganizationId;
	}

	public QueryOrganizationEntity getQueryOrganization() {
		return queryOrganization;
	}

	public void setQueryOrganization(QueryOrganizationEntity queryOrganization) {
		this.queryOrganization = queryOrganization;
	}
	
	public Set<PatientRecordNameEntity> getPatientRecordName() {
		return patientRecordName;
	}

	public void setPatientRecordName(Set<PatientRecordNameEntity> patientRecordName) {
		this.patientRecordName = patientRecordName;
	}

	public String getOrganizationPatientRecordId() {
		return organizationPatientRecordId;
	}

	public void setOrganizationPatientRecordId(String organizationPatientRecordId) {
		this.organizationPatientRecordId = organizationPatientRecordId;
	}

	public Set<PatientRecordAddressEntity> getPatientRecordAddress() {
		return patientRecordAddress;
	}

	public void setPatientRecordAddress(
			Set<PatientRecordAddressEntity> patientRecordAddress) {
		this.patientRecordAddress = patientRecordAddress;
	}
	
}