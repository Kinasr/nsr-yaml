package helper;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Person {
    private String name;
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

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
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
        return Objects.equals(name, person.name) &&
                Objects.equals(age, person.age) &&
                Objects.equals(dateOfBirth, person.dateOfBirth) &&
                gender == person.gender &&
                Objects.equals(children, person.children) &&
                Objects.equals(pets, person.pets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, dateOfBirth, gender, children, pets);
    }
}
