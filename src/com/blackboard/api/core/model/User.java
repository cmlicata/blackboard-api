package com.blackboard.api.core.model;

/**
 * This is the User Model Class that maps to the users table in the database.
 * <p/>
 * Created by ChristopherLicata on 11/14/15.
 */
public class User
{
	private String firstName;

	private String lastName;

	private String email;

	private String password;

	private String schoolName;


	public User()
	{
	}


	public String getFirstName()
	{
		return firstName;
	}


	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}


	public String getLastName()
	{
		return lastName;
	}


	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}


	public String getEmail()
	{
		return email;
	}


	public void setEmail(String email)
	{
		this.email = email;
	}


	public String getPassword()
	{
		return password;
	}


	public void setPassword(String password)
	{
		this.password = password;
	}


	public String getSchoolName()
	{
		return schoolName;
	}


	public void setSchoolName(String schoolName)
	{
		this.schoolName = schoolName;
	}

}
