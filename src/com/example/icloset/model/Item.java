package com.example.icloset.model;

import java.io.Serializable;

public class Item implements Serializable {

	public long id;
	public String path;
	public String description;
	public String category;

	public String toString() {
		return path;
	}

}
