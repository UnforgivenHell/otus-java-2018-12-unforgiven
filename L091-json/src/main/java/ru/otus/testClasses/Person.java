package ru.otus.testClasses;

import java.util.Objects;

public class Person
{
    private String lastName;
    private String firstName;
    private String middleName;
    private int age;

    public Person(String lastName, String firstName, String middleName, int age)
    {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.age = age;
    }

    public Person() {}

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getMidleName()
    {
        return middleName;
    }

    public void setMidleName(String middleName)
    {
        this.middleName = middleName;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(final int age)
    {
        this.age = age;
    }
}
