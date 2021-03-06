package ru.otus.DataSet;

import ru.otus.Annotation.TableName;

@TableName("otus_users")
public class UserDataSet extends DataSet {
    private String name;
    private Integer age;

    public UserDataSet() {
    }

    public UserDataSet(String name, Integer age) {
        super(-1);
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserDataSet{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}