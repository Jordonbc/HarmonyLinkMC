package net.harmonylink;

public class HLSimpleOption<T> {
    private T optionValue;

    public HLSimpleOption(T optionValue) {
        this.optionValue = optionValue;
    }

    // Getter method
    public T get() {
        return optionValue;
    }

    // Setter method
    public void set(T value) {
        optionValue = value;
    }
}
