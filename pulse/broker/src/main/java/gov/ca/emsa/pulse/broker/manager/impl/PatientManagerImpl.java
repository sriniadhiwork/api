package gov.ca.emsa.pulse.broker.manager.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import gov.ca.emsa.pulse.broker.dao.PatientDAO;
import gov.ca.emsa.pulse.broker.domain.Patient;
import gov.ca.emsa.pulse.broker.dto.OrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.PatientDTO;
import gov.ca.emsa.pulse.broker.dto.PatientQueryResultDTO;
import gov.ca.emsa.pulse.broker.dto.QueryDTO;
import gov.ca.emsa.pulse.broker.dto.QueryOrganizationDTO;
import gov.ca.emsa.pulse.broker.dto.QueryStatus;
import gov.ca.emsa.pulse.broker.manager.OrganizationManager;
import gov.ca.emsa.pulse.broker.manager.PatientManager;
import gov.ca.emsa.pulse.broker.manager.QueryManager;

@Service
public class PatientManagerImpl implements PatientManager, ApplicationContextAware {
	@Autowired private Environment env;
	@Autowired private OrganizationManager orgManager;
	@Autowired private PatientDAO patientDao;
	@Autowired private QueryManager queryManager;
	private ApplicationContext context;
	private final ExecutorService pool;
	
	public PatientManagerImpl() {
		pool = Executors.newFixedThreadPool(100);
	}
	
	@Override
	public PatientDTO getPatientById(Long patientId) {
		return patientDao.getById(patientId);
	}
	
	@Override
	public List<PatientDTO> searchPatients(PatientDTO toSearch) {
		return patientDao.getByPatientIdAndOrg(toSearch);
	}

    @Lookup
    public PatientQueryService getPatientQueryService(){
        //spring will override this method
        return null;
    }
    
	@Override
	public List<PatientDTO> getPatientsByQuery(Long queryId) {
		List<PatientDTO> results = patientDao.getPatientResultsForQuery(queryId);
		return results;
	}
	
	@Override	
	@Transactional
	public QueryDTO queryPatients(String samlMessage, PatientDTO searchParams) throws JsonProcessingException {
		Patient queryTerms = new Patient();
		queryTerms.setFirstName(searchParams.getFirstName());
		queryTerms.setLastName(searchParams.getLastName());
		queryTerms.setDateOfBirth(searchParams.getDateOfBirth());
		queryTerms.setSsn(searchParams.getSsn());
		queryTerms.setGender(searchParams.getGender());
		if(searchParams.getAddress() != null) {
			queryTerms.setZipcode(searchParams.getAddress().getZipcode());
		}
		String queryTermsJson = JSONUtils.toJSON(queryTerms);
		
		QueryDTO query = new QueryDTO();
		query.setUserToken("TBD");
		query.setTerms(queryTermsJson);
		query.setStatus(QueryStatus.ACTIVE.name());
		query = queryManager.createQuery(query);
		
		//get the list of organizations
		List<OrganizationDTO> orgsToQuery = orgManager.getAll();
		for(OrganizationDTO org : orgsToQuery) {
			QueryOrganizationDTO queryOrg = new QueryOrganizationDTO();
			queryOrg.setOrgId(org.getId());
			queryOrg.setQueryId(query.getId());
			queryOrg.setStatus(QueryStatus.ACTIVE.name());
			queryOrg = queryManager.createOrUpdateQueryOrganization(queryOrg);
			query.getOrgStatuses().add(queryOrg);
			
			PatientQueryService service = getPatientQueryService();
			service.setSamlMessage(samlMessage);
			service.setToSearch(searchParams);
			service.setQuery(queryOrg);
			service.setOrg(org);
			pool.execute(service);
		}
		return query;
	}
	
	@Override
	@Transactional
	public PatientDTO create(PatientDTO toCreate) {
		return patientDao.create(toCreate);
	}
	
	@Override
	@Transactional
	public PatientDTO update(PatientDTO toUpdate) {
		return patientDao.update(toUpdate);
	}
	
	@Override
	@Transactional
	public PatientQueryResultDTO mapPatientToQuery(PatientQueryResultDTO toCreate) {
		return patientDao.addPatientResultForQuery(toCreate);
	}
	
	@Override
	@Transactional
	public void cleanupPatientCache(Date oldestAllowedPatient) {
		patientDao.deleteItemsOlderThan(oldestAllowedPatient);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}
}
