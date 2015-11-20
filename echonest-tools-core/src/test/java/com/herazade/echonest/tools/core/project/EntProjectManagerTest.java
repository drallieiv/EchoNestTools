package com.herazade.echonest.tools.core.project;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.echonest.api.v4.EchoNestException;
import com.herazade.echonest.tools.core.EntCoreConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = EntCoreConfiguration.class)
public class EntProjectManagerTest {

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

		//project.getAnalysis().getBeats();
	}
}
