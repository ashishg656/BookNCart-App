package com.bookncart.app.serverApi;

public interface GetRequestListener {

	public void onRequestStarted(String requestTag);

	public void onRequestCompleted(String requestTag, Object obj);

	public void onRequestFailed(String requestTag, Object obj);
}
