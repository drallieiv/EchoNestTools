package com.herazade.echonest.tools.storage.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.XmlMappingException;
import org.springframework.util.StringUtils;

import com.herazade.echonest.tools.core.project.EntProject;
import com.herazade.echonest.tools.core.project.EntProjectManager;
import com.herazade.echonest.tools.storage.StorageApi;
import com.herazade.echonest.tools.storage.StorageError;
import com.herazade.echonest.tools.storage.StorageException;

public class FileSystemStorage implements StorageApi {

	// Logger
	private Logger logger = LoggerFactory.getLogger(getClass());

	private static final String PROJECT_XML = "project.xml";

	// Folder Storage Path
	private Path storageFolderPath;

	@Inject
	private EntProjectManager entProjectManager;

	public FileSystemStorage(String path) throws StorageException {
		storageFolderPath = Paths.get(path);

		if (Files.exists(storageFolderPath)) {
			if (!Files.isDirectory(storageFolderPath)) {
				throw new StorageException(StorageError.INVALID_FOLDER);
			}
		} else {
			try {
				logger.info("Initialize storage Folder : {}", storageFolderPath.toAbsolutePath());
				Files.createDirectories(storageFolderPath);
			} catch (IOException e) {
				throw new StorageException(StorageError.CANNOT_CREATE_FOLDER, e);
			}
		}
	}

	@Override
	public List<String> getProjectNames() throws StorageException {

		List<String> projects = new ArrayList<String>();

		try {
			DirectoryStream<Path> content = Files.newDirectoryStream(storageFolderPath);
			for (Path path : content) {
				if (Files.isDirectory(path) && Files.exists(path.resolve(PROJECT_XML))) {
					projects.add(path.getFileName().toString());
				}
			}
			return projects;

		} catch (IOException e) {
			throw new StorageException(StorageError.IO_ERROR, e);
		}
	}

	@Override
	public void deleteProject(String projectName) throws StorageException {
		try {
			Files.deleteIfExists(storageFolderPath.resolve(projectName));
		} catch (IOException e) {
			throw new StorageException(StorageError.IO_ERROR, e);
		}

	}

	@Override
	public EntProject getProject(String projectName) throws StorageException {
		try {
			return entProjectManager.loadProject(storageFolderPath.resolve(projectName).resolve(PROJECT_XML).toFile());
		} catch (XmlMappingException | IOException e) {
			throw new StorageException(StorageError.IO_ERROR, e);
		}
	}

	@Override
	public void saveProject(EntProject project) throws StorageException {

		if (project == null || StringUtils.isEmpty(project.getProjectName())) {
			throw new IllegalArgumentException("Invalid Project");
		}

		File outFile = storageFolderPath.resolve(project.getProjectName()).resolve(PROJECT_XML).toFile();
		try {
			entProjectManager.saveAsFile(project, outFile);
		} catch (IOException e) {
			throw new StorageException(StorageError.IO_ERROR, e);
		}

	}

	/**
	 * @return the storageFolderPath
	 */
	public Path getStorageFolderPath() {
		return storageFolderPath;
	}

	/**
	 * @param storageFolderPath
	 *            the storageFolderPath to set
	 */
	public void setStorageFolderPath(Path storageFolderPath) {
		this.storageFolderPath = storageFolderPath;
	}

}
