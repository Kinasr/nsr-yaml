package nsr_yaml;

class AsObject<T> {
    private final T obj;
    private final Boolean isCustomObj;


    protected AsObject(T obj, Boolean isCustomObj) {
        this.obj = obj;
        this.isCustomObj = isCustomObj;
    }

    public T obj() {
        return obj;
    }

    public Boolean isCustomObj() {
        return isCustomObj;
    }
}
