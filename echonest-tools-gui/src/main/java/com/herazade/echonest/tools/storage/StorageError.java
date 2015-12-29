package com.herazade.echonest.tools.storage;

/**
 * Enumarion of possible storage Errors
 * @author drallieiv
 *
 */
public enum StorageError {

	// Not a folder
	INVALID_FOLDER("INVALID_FOLDER", "Storage Folder is not Valid"), 
	
	// Cannot create folder
	CANNOT_CREATE_FOLDER("CREATE_FOLDER_FAILED", "Storage Folder cannot be created"),
	
	// Other Technical Error
	IO_ERROR("IO_ERROR", "IO ERROR");

	private StorageError(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	private String code;
	private String msg;
	private String context;

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the context
	 */
	public String getContext() {
		return context;
	}

	/**
	 * @param context
	 *            the context to set
	 */
	public void setContext(String context) {
		this.context = context;
	}

}
