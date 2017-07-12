package com.nuance.mobility.dependencywatcher.data.maven;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MavenRepositoryConfigurer {
	
	//TODO: I'm not sure it's a good idea to have this logger putting the logs of the locator
	private Logger logger = LoggerFactory.getLogger(MavenRepositoryConfigurer.class);
	@Bean
	public RepositorySystem newRepositorySystem()
    {
		
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService( RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class );
        locator.addService( TransporterFactory.class, FileTransporterFactory.class );
        locator.addService( TransporterFactory.class, HttpTransporterFactory.class );

        locator.setErrorHandler( new DefaultServiceLocator.ErrorHandler()
        {
            @Override
            public void serviceCreationFailed( Class<?> type, Class<?> impl, Throwable exception )
            {
            	logger.error("Error with maven repo",exception);
            }
        } );
        return locator.getService( RepositorySystem.class );
    }
	
	@Bean
	public static DefaultRepositorySystemSession newRepositorySystemSession( RepositorySystem system )
    {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
        LocalRepository localRepo = new LocalRepository( System.getProperty("user.home")+"/.m2/repository/" );
        session.setLocalRepositoryManager( system.newLocalRepositoryManager( session, localRepo ) );
        session.setTransferListener( new ConsoleTransferListener() );
        session.setRepositoryListener( new ConsoleRepositoryListener() );
        return session;
    }
	
	
}
