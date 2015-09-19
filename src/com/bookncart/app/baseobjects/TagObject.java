package com.bookncart.app.baseobjects;

public class TagObject {

	String tag_name;
	int id;

	public TagObject(String tag_name, int id) {
		super();
		this.tag_name = tag_name;
		this.id = id;
	}

	public String getTag_name() {
		return tag_name;
	}

	public void setTag_name(String tag_name) {
		this.tag_name = tag_name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
