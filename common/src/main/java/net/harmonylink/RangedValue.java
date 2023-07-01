package net.harmonylink;

public class RangedValue {
    private double min;
    private double max;
    private double value;

    public RangedValue(double min, double max, double value) {
        this.min = min;
        this.max = max;
        this.value = value;
    }

    public double getNormalizedValue() {
        return calculateNormalizedValue(this.value);
    }

    public double getAbsoluteValue() {
        return this.value;
    }

    public void setValue(double absoluteValue) {
        this.value = absoluteValue;
    }

    private double calculateNormalizedValue(double absoluteValue) {
        return (absoluteValue - min) / (max - min);
    }

    public void setNormalizedValue(double normalizedValue) {
        this.value = calculateAbsoluteValue(normalizedValue);
    }

    private double calculateAbsoluteValue(double normalizedValue) {
        return min + (max - min) * normalizedValue;
    }
}
