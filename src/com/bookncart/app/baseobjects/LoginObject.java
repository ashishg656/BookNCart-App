package com.bookncart.app.baseobjects;

public class LoginObject {

	boolean status;
	int user_profile_id;
	int user_id;

	public LoginObject(boolean status, int user_profile_id, int user_id) {
		super();
		this.status = status;
		this.user_profile_id = user_profile_id;
		this.user_id = user_id;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getUser_profile_id() {
		return user_profile_id;
	}

	public void setUser_profile_id(int user_profile_id) {
		this.user_profile_id = user_profile_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

}
