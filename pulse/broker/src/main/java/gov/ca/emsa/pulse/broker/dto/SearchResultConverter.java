package gov.ca.emsa.pulse.broker.dto;

import gov.ca.emsa.pulse.broker.dao.PatientGenderDAO;
import gov.ca.emsa.pulse.common.domain.GivenName;
import gov.ca.emsa.pulse.common.domain.NameAssembly;
import gov.ca.emsa.pulse.common.domain.NameRepresentation;
import gov.ca.emsa.pulse.common.domain.NameType;
import gov.ca.emsa.pulse.common.domain.Patient;
import gov.ca.emsa.pulse.common.domain.PatientGender;
import gov.ca.emsa.pulse.common.domain.PatientRecordName;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class SearchResultConverter {
	private static final Logger logger = LogManager.getLogger(SearchResultConverter.class);
	@Autowired static PatientGenderDAO patientGenderDao;
	public static PatientRecordDTO convertToPatientRecord(Patient domainObj) {
		PatientRecordDTO result = new PatientRecordDTO();
		GivenNameDTO givenNameDTO = new GivenNameDTO();
		String[] givenAndFamily = domainObj.getFullName().split(" ");
		givenNameDTO.setGivenName(givenAndFamily[0]);
		String familyName = givenAndFamily[1];
		result.getPatientRecordName().get(0).getGivenName().add(givenNameDTO);
		result.getPatientRecordName().get(0).setFamilyName(familyName);
		PatientGenderDTO pg = patientGenderDao.getByCode(domainObj.getGender());
		result.setPatientGender(pg);
		result.setDateOfBirth(domainObj.getDateOfBirth());
		result.setPhoneNumber(domainObj.getPhoneNumber());
		result.setSsn(domainObj.getSsn());
		result.setOrganizationPatientRecordId(domainObj.getOrgPatientId());

		if(domainObj.getAddress() != null) {
			PatientRecordAddressDTO address = new PatientRecordAddressDTO();
			address.setCity(domainObj.getAddress().getCity());
			address.setState(domainObj.getAddress().getState());
			address.setZipcode(domainObj.getAddress().getZipcode());
			//result.setAddress(address);
		}

		return result;
	}
}
