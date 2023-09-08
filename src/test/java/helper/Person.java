package helper;

import kinasr.nsr_yaml.annotation.Alias;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Person {
    private String name;
    @Alias("nick-name")
    private String nickName;
    private Integer age;
    private LocalDate dateOfBirth;
    private Gender gender;
    private List<Person> children;
    private Map<String, Pet> pets;

    public Person() {
    }

    public String getName() {
        return name;
    }

    public Person setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public Person setAge(Integer age) {
        this.age = age;
        return this;
    }

    public List<Person> getChildren() {
        return children;
    }

    public Person setChildren(List<Person> children) {
        this.children = children;
        return this;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Person setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public Person setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public Map<String, Pet> getPets() {
        return pets;
    }

    public Person setPets(Map<String, Pet> pets) {
        this.pets = pets;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public Person setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", nickName='" + nickName + '\'' +
                ", age=" + age +
                ", dateOfBirth=" + dateOfBirth +
                ", gender=" + gender +
                ", children=" + children +
                ", pets=" + pets +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (!Objects.equals(name, person.name)) return false;
        if (!Objects.equals(nickName, person.nickName)) return false;
        if (!Objects.equals(age, person.age)) return false;
        if (!Objects.equals(dateOfBirth, person.dateOfBirth)) return false;
        if (gender != person.gender) return false;
        if (!Objects.equals(children, person.children)) return false;
        return Objects.equals(pets, person.pets);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (nickName != null ? nickName.hashCode() : 0);
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (children != null ? children.hashCode() : 0);
        result = 31 * result + (pets != null ? pets.hashCode() : 0);
        return result;
    }
}
