package com.contactmanager.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Contact_info")
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int contactId;
	private String contactName;
	private String contactSecondname;
	private String contactWork;
	private String contactEmail;
	private String contactImageUrl;
	@Column(length = 2000)
	private String contactDesc;
	private String contactPhoneNo;

	@ManyToOne
	@JsonIgnore
	private User user;

	public int getContactId() {
		return contactId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactSecondname() {
		return contactSecondname;
	}

	public void setContactSecondname(String contactSecondname) {
		this.contactSecondname = contactSecondname;
	}

	public String getContactWork() {
		return contactWork;
	}

	public void setContactWork(String contactWork) {
		this.contactWork = contactWork;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getContactImageUrl() {
		return contactImageUrl;
	}

	public void setContactImageUrl(String contactImageUrl) {
		this.contactImageUrl = contactImageUrl;
	}

	public String getContactDesc() {
		return contactDesc;
	}

	public void setContactDesc(String contactDesc) {
		this.contactDesc = contactDesc;
	}

	public String getContactPhoneNo() {
		return contactPhoneNo;
	}

	public void setContactPhoneNo(String contactPhoneNo) {
		this.contactPhoneNo = contactPhoneNo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public boolean equals(Object obj) {
	
		return this.contactId==((Contact)obj).contactId;
	}

}
