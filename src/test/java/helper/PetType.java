package helper;

public enum PetType {
    CAT("cat"), DOG("dog");

    private final String type;

    PetType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
