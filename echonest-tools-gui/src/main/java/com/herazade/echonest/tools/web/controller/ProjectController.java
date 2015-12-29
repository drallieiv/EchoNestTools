package com.herazade.echonest.tools.web.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.echonest.api.v4.EchoNestException;
import com.herazade.echonest.tools.core.project.EntProject;
import com.herazade.echonest.tools.storage.StorageApi;
import com.herazade.echonest.tools.storage.StorageException;

@RestController
@RequestMapping(value = "/api/project")
public class ProjectController {

	@Inject
	private StorageApi storageApi;

	@RequestMapping(value = "/")
	public List<String> index() throws EchoNestException, StorageException {

		return storageApi.getProjectNames();
	}

	@RequestMapping(value = "{projectName}")
	public ResponseEntity<EntProject> getProject(@PathVariable String projectName) throws EchoNestException {

		try {
			EntProject project = storageApi.getProject(projectName);
			return new ResponseEntity<EntProject>(project,HttpStatus.OK);

		} catch (StorageException e) {
			return new ResponseEntity<EntProject>(HttpStatus.NOT_FOUND);
		}
	}

}
