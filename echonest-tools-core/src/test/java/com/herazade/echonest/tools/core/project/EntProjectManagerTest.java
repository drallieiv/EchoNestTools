package com.herazade.echonest.tools.core.project;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.inject.Inject;
import javax.xml.transform.TransformerConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.XmlMappingException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.echonest.api.v4.EchoNestException;
import com.herazade.echonest.tools.core.EchoNestAPIConfig;
import com.herazade.echonest.tools.core.EntCoreConfiguration;
import com.herazade.echonest.tools.core.remix.strategy.ManualRemix;
import com.herazade.echonest.tools.core.remix.strategy.RemixStrategy;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { EntCoreConfiguration.class, EchoNestAPIConfig.class })
public class EntProjectManagerTest {

	@Before
	public void setup() {
		if (System.getProperty("ECHO_NEST_API_KEY") == null && System.getenv("ECHO_NEST_API_KEY") == null)
			System.setProperty("ECHO_NEST_API_KEY", "FAKE");
	}

	@Inject
	private EntProjectManager projectManager;

	private Resource testMusic = new ClassPathResource("files/test-music.mp3");

	@Test
	public void runAnalysisTest() throws IOException, EchoNestException {

		EntProject project = new EntProject();
		project.setMp3FilePath(testMusic.getFile().getAbsolutePath());

		projectManager.runAnalysis(project);

		assertThat(project.getTrackId()).isEqualTo("TRWVXTG13D50B9CC01");
		assertThat(project.getAnalysis()).isNotNull();
	}

	@Test
	public void saveProjectTest() throws IOException {
		// Given
		EntProject project = new EntProject();
		project.setProjectName("Test Project");
		project.setMp3FilePath(testMusic.getFile().getAbsolutePath());

		RemixStrategy remixStrategy = ManualRemix.buildNew().addPart(30, 60);
		project.setRemixStrategy(remixStrategy);

		Path outPath = Paths.get("target/project", "testProject.xml");
		Files.createDirectories(outPath.getParent());

		// When
		projectManager.saveAsFile(project, outPath.toFile());
	}

	@Test
	public void loadProjectTest() throws XmlMappingException, IOException, URISyntaxException {

		// Given
		Path inPath = Paths.get(getClass().getClassLoader().getResource("testProject.xml").toURI());

		// When
		EntProject project = projectManager.loadProject(inPath.toFile());

		// Then
		assertThat(project.getRemixStrategy()).isInstanceOf(ManualRemix.class);
	}
}
