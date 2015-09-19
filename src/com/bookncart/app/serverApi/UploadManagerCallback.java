package com.bookncart.app.serverApi;

public interface UploadManagerCallback {

	public void uploadFinished(int requestType, int objectId, Object data, int uploadId, boolean status, int parserId);
	
	public void uploadStarted(int requestType, int objectId, int parserId, Object object);
	
}
