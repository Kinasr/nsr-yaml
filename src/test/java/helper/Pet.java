package helper;

import java.util.Objects;

public class Pet {
    private String kind;
    private Integer age;
    private Person owner;
    private int numOfLegs;

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

    public int getNumOfLegs() {
        return numOfLegs;
    }

    public Pet setNumOfLegs(int numOfLegs) {
        this.numOfLegs = numOfLegs;
        return this;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "kind='" + kind + '\'' +
                ", age=" + age +
                ", owner=" + owner +
                ", numOfLegs=" + numOfLegs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pet pet = (Pet) o;

        if (numOfLegs != pet.numOfLegs) return false;
        if (!Objects.equals(kind, pet.kind)) return false;
        if (!Objects.equals(age, pet.age)) return false;
        return Objects.equals(owner, pet.owner);
    }

    @Override
    public int hashCode() {
        int result = kind != null ? kind.hashCode() : 0;
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + numOfLegs;
        return result;
    }
}
