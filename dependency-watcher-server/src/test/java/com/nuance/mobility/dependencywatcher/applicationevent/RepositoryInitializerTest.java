package com.nuance.mobility.dependencywatcher.applicationevent;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.core.io.ClassPathResource;

import com.nuance.mobility.dependencywatcher.exceptions.MavenRepositoryException;
import com.nuance.mobility.dependencywatcher.exceptions.UpdatingDependenciesException;
import com.nuance.mobility.dependencywatcher.interfaces.IPomRetriever;
import com.nuance.mobility.dependencywatcher.service.DependencyService;

public class RepositoryInitializerTest {

	private RepositoryInitializer testee;
	private IPomRetriever pomRetriever;
	private DependencyService dependencyService;

	private String a;
	private String b;
	private String c;
	private Logger logger;

	@Before
	public void setup() throws IOException, MavenRepositoryException {
		testee = new RepositoryInitializer();

		a = FileUtils.readFileToString(new ClassPathResource("/a.xml").getFile());
		b = FileUtils.readFileToString(new ClassPathResource("/b.xml").getFile());
		c = FileUtils.readFileToString(new ClassPathResource("/c.xml").getFile());

		pomRetriever = mock(IPomRetriever.class);
		dependencyService = mock(DependencyService.class);
		logger = mock(Logger.class);

		testee.setPomRetriever(pomRetriever);
		testee.setDependencyService(dependencyService);
		testee.setLogger(logger);

		when(pomRetriever.retrievePomUrls(anyString(), anyString()))
				.thenReturn(new ArrayList<String>(Arrays.asList("a", "b", "c")));
		when(pomRetriever.retrievePomFromUrl(anyString())).thenReturn(a, b, c);
	}

	@Test
	public void notStopUpdateWhenException() throws UpdatingDependenciesException {
		// WHEN
		when(dependencyService.updateDependencies(b)).thenThrow(new UpdatingDependenciesException());
		// DO
		testee.onApplicationEvent(mock(ApplicationReadyEvent.class));
		// THEN
		verify(dependencyService, times(3)).updateDependencies(anyString());
		// verify(logger,atMost(1000)).debug(anyString(),any(Exception.class));
		// verify(logger,atMost(1000)).info(anyString(),any(Exception.class));
		verify(logger).error(anyString(), any(Exception.class));
	}

}
