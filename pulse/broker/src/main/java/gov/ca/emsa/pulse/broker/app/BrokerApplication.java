package gov.ca.emsa.pulse.broker.app;

import java.util.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import gov.ca.emsa.pulse.broker.cache.DirectoryRefreshManager;
import gov.ca.emsa.pulse.broker.cache.QueryCacheManager;
import gov.ca.emsa.pulse.broker.manager.DocumentManager;
import gov.ca.emsa.pulse.broker.manager.OrganizationManager;
import gov.ca.emsa.pulse.broker.manager.PatientManager;
import gov.ca.emsa.pulse.broker.manager.impl.PatientQueryService;

@PropertySource("classpath:/application.properties")
@EnableTransactionManagement(proxyTargetClass=true)
@SpringBootApplication(scanBasePackages= {"gov.ca.emsa.pulse.broker.**",
		"gov.ca.emsa.pulse.service.**"})
public class BrokerApplication implements EnvironmentAware {
	
	public static void main(String[] args) {
		SpringApplication.run(BrokerApplication.class, args);
	}
	
	@Autowired private OrganizationManager organizationManager;
	@Autowired private PatientManager patientManager;
	@Autowired private DocumentManager docManager;
	@Autowired private Environment env;
	
	@Override
	public void setEnvironment(final Environment e) {
		this.env = e;
	}
	
	//TODO: the env is still null when the entity manager bean gets called and 
	//i really have no idea why. This is a short-term fix. The longer answer is that
	//spring boot really does not need the persistence.xml and the db stuff should be
	//configured in application.properties
	@Bean
	public org.springframework.orm.jpa.LocalEntityManagerFactoryBean entityManagerFactory(){
		org.springframework.orm.jpa.LocalEntityManagerFactoryBean bean = new org.springframework.orm.jpa.LocalEntityManagerFactoryBean();
		//bean.setPersistenceUnitName(env.getProperty("persistenceUnitName"));
		bean.setPersistenceUnitName("pulse");
		return bean;
	}

	@Bean
	public org.springframework.orm.jpa.JpaTransactionManager transactionManager(){
		org.springframework.orm.jpa.JpaTransactionManager bean = new org.springframework.orm.jpa.JpaTransactionManager();
		bean.setEntityManagerFactory(entityManagerFactory().getObject());
		return bean;
	}
	
	@Bean
	public QueryCacheManager queryCacheManager() {
		int queryCacheExpirationMinutes = new Integer(env.getProperty("queryCacheExpireMinutes").trim());
		long queryCacheExpirationMillis = queryCacheExpirationMinutes * 60 * 1000;
		int queryCacheCleanupMinutes = new Integer(env.getProperty("queryCacheCleanupMinutes").trim());
		long queryCacheCleanupMillis = queryCacheCleanupMinutes * 60 * 1000;
		
		QueryCacheManager qcTask = null;

		if(queryCacheExpirationMinutes > 0) {
			qcTask = new QueryCacheManager();
			qcTask.setExpirationMillis(queryCacheExpirationMillis);
			qcTask.setPatientManager(patientManager);
			qcTask.setDocManager(docManager);
			
			Timer timer = new Timer();
			//timer.scheduleAtFixedRate(qcTask, queryCacheCleanupMinutes, queryCacheCleanupMinutes);
			timer.scheduleAtFixedRate(qcTask, 0, queryCacheCleanupMillis);

		} //TODO: 0 and -1 mean different things?
		
		return qcTask;
	}
	
	@Bean
	public DirectoryRefreshManager directoryRefreshManager() {
		int directoryRefresh = new Integer(env.getProperty("directoryRefreshSeconds").trim());
		long directoryRefreshExpirationMillis = directoryRefresh * 1000;
		
		DirectoryRefreshManager qcTask = null;

		if(directoryRefresh > 0) {
			qcTask = new DirectoryRefreshManager();
			qcTask.setManager(organizationManager);
			qcTask.setExpirationMillis(directoryRefreshExpirationMillis);
			qcTask.setDirectoryServicesUrl(env.getProperty("directoryServicesUrl"));
			
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(qcTask, 0, directoryRefreshExpirationMillis);

		}
		
		return qcTask;
	}
	
	@Bean
    @Scope(scopeName=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public PatientQueryService patientQueryService() {
        return new PatientQueryService();
    }
}
