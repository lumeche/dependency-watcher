package com.nuance.mobility.dependencywatcher.data.maven;

import org.eclipse.aether.AbstractRepositoryListener;
import org.eclipse.aether.RepositoryEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simplistic repository listener that logs events to the console.
 */
public class ConsoleRepositoryListener
    extends AbstractRepositoryListener
{

	private Logger logger = LoggerFactory.getLogger(ConsoleRepositoryListener.class);


    public void artifactDeployed( RepositoryEvent event )
    {
        logger.debug( "Deployed " + event.getArtifact() + " to " + event.getRepository() );
    }

    public void artifactDeploying( RepositoryEvent event )
    {
        logger.debug( "Deploying " + event.getArtifact() + " to " + event.getRepository() );
    }

    public void artifactDescriptorInvalid( RepositoryEvent event )
    {
        logger.error( "Invalid artifact descriptor for " + event.getArtifact() + ": "
            + event.getException().getMessage() );
    }

    public void artifactDescriptorMissing( RepositoryEvent event )
    {
        logger.error( "Missing artifact descriptor for " + event.getArtifact() );
    }

    public void artifactInstalled( RepositoryEvent event )
    {
        logger.debug( "Installed " + event.getArtifact() + " to " + event.getFile() );
    }

    public void artifactInstalling( RepositoryEvent event )
    {
        logger.debug( "Installing " + event.getArtifact() + " to " + event.getFile() );
    }

    public void artifactResolved( RepositoryEvent event )
    {
        logger.debug( "Resolved artifact " + event.getArtifact() + " from " + event.getRepository() );
    }

    public void artifactDownloading( RepositoryEvent event )
    {
        logger.debug( "Downloading artifact " + event.getArtifact() + " from " + event.getRepository() );
    }

    public void artifactDownloaded( RepositoryEvent event )
    {
        logger.debug( "Downloaded artifact " + event.getArtifact() + " from " + event.getRepository() );
    }

    public void artifactResolving( RepositoryEvent event )
    {
        logger.debug( "Resolving artifact " + event.getArtifact() );
    }

    public void metadataDeployed( RepositoryEvent event )
    {
        logger.debug( "Deployed " + event.getMetadata() + " to " + event.getRepository() );
    }

    public void metadataDeploying( RepositoryEvent event )
    {
        logger.debug( "Deploying " + event.getMetadata() + " to " + event.getRepository() );
    }

    public void metadataInstalled( RepositoryEvent event )
    {
        logger.debug( "Installed " + event.getMetadata() + " to " + event.getFile() );
    }

    public void metadataInstalling( RepositoryEvent event )
    {
        logger.debug( "Installing " + event.getMetadata() + " to " + event.getFile() );
    }

    public void metadataInvalid( RepositoryEvent event )
    {
        logger.error( "Invalid metadata " + event.getMetadata() );
    }

    public void metadataResolved( RepositoryEvent event )
    {
        logger.debug( "Resolved metadata " + event.getMetadata() + " from " + event.getRepository() );
    }

    public void metadataResolving( RepositoryEvent event )
    {
        logger.debug( "Resolving metadata " + event.getMetadata() + " from " + event.getRepository() );
    }

}
