package com.herazade.echonest.tools.storage;

import java.util.List;

import org.springframework.expression.spel.ast.Projection;

import com.herazade.echonest.tools.core.project.EntProject;

public interface StorageApi {
	
	/**
	 * List all existing projects
	 * @return
	 * @throws StorageException 
	 */
	public List<String> getProjectNames() throws StorageException;
	
	/**
	 * Delete existing project
	 * @param projectName
	 * @throws StorageException 
	 */
	public void deleteProject(String projectName) throws StorageException;
	
	/**
	 * Load Project
	 * @param projectName
	 * @return Project
	 * @throws StorageException 
	 */
	public EntProject getProject(String projectName) throws StorageException;
	
	/**
	 * Save or Update Project
	 * @param project
	 * @throws StorageException 
	 */
	public void saveProject(EntProject project) throws StorageException;
}
