package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.entity.GivenNameEntity;
import gov.ca.emsa.pulse.broker.entity.PatientOrganizationMapEntity;
import gov.ca.emsa.pulse.broker.entity.PatientRecordNameEntity;
import gov.ca.emsa.pulse.broker.entity.PatientRecordEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientRecordDTO {
	private Long id;
	private String organizationPatientRecordId;
	private List<PatientRecordNameDTO> patientRecordName;
	private LocalDate dateOfBirth;
	private String ssn;
	private String gender;
	private String phoneNumber;
	private AddressDTO address;
	private Long queryOrganizationId;
	private Date lastModifiedDate;
	
	public PatientRecordDTO() {
		patientRecordName = new ArrayList<PatientRecordNameDTO>();
	}
	
	public PatientRecordDTO(PatientRecordEntity entity) {
		this();
		this.id = entity.getId();
		if(entity.getPatientRecordName() != null && entity.getPatientRecordName().size() > 0) {
			for(PatientRecordNameEntity patientRecordNameEntity : entity.getPatientRecordName()){
				PatientRecordNameDTO patientRecordNameDTO = new PatientRecordNameDTO();
				patientRecordNameDTO.setFamilyName(patientRecordNameEntity.getFamilyName());
				ArrayList<GivenNameDTO> givens = new ArrayList<GivenNameDTO>();
				for(GivenNameEntity given : patientRecordNameEntity.getGivenNames()){
					GivenNameDTO givenDTO = new GivenNameDTO(given);
					givens.add(givenDTO);
				}
				patientRecordNameDTO.setGivenName(givens);
				patientRecordNameDTO.setSuffix(patientRecordNameEntity.getSuffix());
				patientRecordNameDTO.setPrefix(patientRecordNameEntity.getPrefix());
				if(patientRecordNameEntity.getNameType() != null){
					NameTypeDTO nameType = new NameTypeDTO(patientRecordNameEntity.getNameType());
					patientRecordNameDTO.setNameType(nameType);
				}
				if(patientRecordNameEntity.getNameRepresentation() != null){
					NameRepresentationDTO nameRep = new NameRepresentationDTO(patientRecordNameEntity.getNameRepresentation());
					patientRecordNameDTO.setNameRepresentation(nameRep);
				}
				if(patientRecordNameEntity.getNameAssembly() != null){
					NameAssemblyDTO nameAssembly = new NameAssemblyDTO(patientRecordNameEntity.getNameAssembly());
					patientRecordNameDTO.setNameAssembly(nameAssembly);
				}
				patientRecordNameDTO.setEffectiveDate(patientRecordNameEntity.getEffectiveDate());
				patientRecordNameDTO.setExpirationDate(patientRecordNameEntity.getExpirationDate());
				this.patientRecordName.add(patientRecordNameDTO);
			}
		}
		if(entity.getDateOfBirth() != null) {
			this.dateOfBirth = entity.getDateOfBirth().toLocalDate();
		}
		this.ssn = entity.getSsn();
		this.gender = entity.getGender();
		this.phoneNumber = entity.getPhoneNumber();
		this.organizationPatientRecordId = entity.getOrganizationPatientRecordId();
		
		this.address = new AddressDTO();
		this.address.setStreetLineOne(entity.getStreetLineOne());
		this.address.setStreetLineTwo(entity.getStreetLineTwo());
		this.address.setCity(entity.getCity());
		this.address.setState(entity.getState());
		this.address.setZipcode(entity.getZipcode());
		this.address.setCountry(entity.getCountry());
		
		this.queryOrganizationId = entity.getQueryOrganizationId();
		this.lastModifiedDate = entity.getLastModifiedDate();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public AddressDTO getAddress() {
		return address;
	}
	public void setAddress(AddressDTO address) {
		this.address = address;
	}

	public String getOrganizationPatientRecordId() {
		return organizationPatientRecordId;
	}

	public void setOrgPatientRecordId(String orgPatientRecordId) {
		this.organizationPatientRecordId = organizationPatientRecordId;
	}

	public Long getQueryOrganizationId() {
		return queryOrganizationId;
	}

	public void setQueryOrganizationId(Long queryOrganizationId) {
		this.queryOrganizationId = queryOrganizationId;
	}

	public List<PatientRecordNameDTO> getPatientRecordName() {
		return patientRecordName;
	}

	public void setPatientRecordName(List<PatientRecordNameDTO> patientRecordName) {
		this.patientRecordName = patientRecordName;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
}
