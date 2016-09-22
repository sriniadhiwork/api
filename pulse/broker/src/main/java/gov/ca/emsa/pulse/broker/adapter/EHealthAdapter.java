package gov.ca.emsa.pulse.broker.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201310UV02;
import org.opensaml.common.SAMLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import gov.ca.emsa.pulse.broker.adapter.service.EHealthQueryProducerService;
import gov.ca.emsa.pulse.broker.dto.DomainToDtoConverter;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientOrganizationMapDTO;
import gov.ca.emsa.pulse.broker.dto.PatientRecordDTO;
import gov.ca.emsa.pulse.broker.dto.SearchResultConverter;
import gov.ca.emsa.pulse.common.domain.Document;
import gov.ca.emsa.pulse.common.domain.Patient;
import gov.ca.emsa.pulse.common.domain.PatientRecord;
import gov.ca.emsa.pulse.common.domain.PatientSearch;
import gov.ca.emsa.pulse.common.soap.JSONToSOAPService;
import gov.ca.emsa.pulse.common.soap.SOAPToJSONService;

@Component
public class EHealthAdapter implements Adapter {
	private static final Logger logger = LogManager.getLogger(EHealthAdapter.class);
	
	@Autowired JSONToSOAPService jsonConverterService;
	@Autowired SOAPToJSONService soapConverterService;
	@Autowired EHealthQueryProducerService queryProducer;
	
	@Override
	public List<PatientRecordDTO> queryPatients(OrganizationDTO org, PatientSearch toSearch, String samlMessage) {
		PRPAIN201305UV02 requestBody = jsonConverterService.convertFromPatientSearch(toSearch);
		String postUrl = org.getEndpointUrl() + "/patientDiscovery";
//		MultiValueMap<String,String> parameters = new LinkedMultiValueMap<String,String>();
//		parameters.add("givenName", toSearch.getGivenName());
//		parameters.add("familyName", toSearch.getFamilyName());
//		parameters.add("dob", toSearch.getDob());
//		parameters.add("gender", toSearch.getGender());
//		parameters.add("ssn", toSearch.getSsn());
//		parameters.add("zipcode", toSearch.getZip());
//		parameters.add("samlMessage", samlMessage);
		RestTemplate restTemplate = new RestTemplate();
		String searchResults = null;
		try {
			searchResults = restTemplate.postForObject(postUrl, requestBody, String.class);
		} catch(Exception ex) {
			logger.error("Exception when querying " + postUrl, ex);
			throw ex;
		}
		
		List<PatientRecordDTO> records = new ArrayList<PatientRecordDTO>();
		if(!StringUtils.isEmpty(searchResults)) {
			try {
				PRPAIN201310UV02 resultObj = queryProducer.unMarshallPatientDiscoveryResponseObject(searchResults);
				List<PatientRecord> patientRecords = soapConverterService.convertToPatientRecords(resultObj);
				for(int i = 0; i < patientRecords.size(); i++) {
					PatientRecordDTO record = DomainToDtoConverter.convertToPatientRecord(patientRecords.get(0));
					records.add(record);
				}
			} catch(SAMLException | SOAPException ex) {
				logger.error("Exception unmarshalling patient discovery response", ex);
			}
		}
		
		return records;
	}

	@Override
	public Document[] queryDocuments(OrganizationDTO org, PatientOrganizationMapDTO toSearch, String samlMessage) {
		String postUrl = org.getEndpointUrl() + "/documents";
		MultiValueMap<String,String> parameters = new LinkedMultiValueMap<String,String>();
		parameters.add("patientId", toSearch.getOrgPatientId());
		parameters.add("samlMessage", samlMessage);
		RestTemplate restTemplate = new RestTemplate();
		Document[] searchResults = null;
		try {
			searchResults = restTemplate.postForObject(postUrl, parameters, Document[].class);
		} catch(Exception ex) {
			logger.error("Exception when querying " + postUrl, ex);
			throw ex;
		}
		return searchResults;
	}
}
