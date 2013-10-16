package com.example.icloset.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Event implements Serializable {

	public long id;
	public String startTimeDate;
	public String description;
	public String endTimeDate;
	public String name;
	ArrayList<Item> items;

}
