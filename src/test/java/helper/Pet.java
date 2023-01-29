package helper;

import java.util.Objects;

public class Pet {
    private String kind;
    private Integer age;
    private Person owner;

    public String getKind() {
        return kind;
    }

    public Pet setKind(String kind) {
        this.kind = kind;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public Pet setAge(Integer age) {
        this.age = age;
        return this;
    }

    public Person getOwner() {
        return owner;
    }

    public Pet setOwner(Person owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "kind='" + kind + '\'' +
                ", age=" + age +
                ", owner=" + owner +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return Objects.equals(kind, pet.kind) && Objects.equals(age, pet.age) && Objects.equals(owner, pet.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, age, owner);
    }
}
