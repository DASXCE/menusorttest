package com.example.sortingtest;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * Entity implementation class for Entity: Test
 *
 */
@Entity

public class MenuItem implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@OneToOne
	private MenuItem previous;
	
	@OneToOne
	private MenuItem next;
	
	@ManyToOne
	private MenuItem parent;
	
	public MenuItem() {
	}

	public MenuItem(Long id, String name, MenuItem previous, MenuItem next,
			MenuItem parent) {
		super();
		this.id = id;
		this.name = name;
		this.previous = previous;
		this.next = next;
		this.parent = parent;
	}
	

	public MenuItem(String name, MenuItem previous, MenuItem next,
			MenuItem parent) {
		super();
		this.name = name;
		this.previous = previous;
		this.next = next;
		this.parent = parent;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MenuItem getPrevious() {
		return previous;
	}

	public void setPrevious(MenuItem previous) {
		this.previous = previous;
	}

	public MenuItem getNext() {
		return next;
	}

	public void setNext(MenuItem next) {
		this.next = next;
	}

	public MenuItem getParent() {
		return parent;
	}

	public void setParent(MenuItem parent) {
		this.parent = parent;
	}
	
	@Override
	public String toString() {
		return "~~~~~ MenuItem [id=" + id + ", name=" + name + "] ~~~~~";
	}
	
	
   
}
