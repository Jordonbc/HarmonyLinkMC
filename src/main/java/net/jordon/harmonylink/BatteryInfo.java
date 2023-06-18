package net.jordon.harmonylink;


public class BatteryInfo {
    boolean hasBattery;
    int batteryPercent;
    ChargingStatus chargingStatus;

    @Override
    public String toString() {
        return "BatteryInfo{" +
                "hasBattery=" + hasBattery +
                ", batteryPercent=" + batteryPercent +
                ", chargingStatus='" + chargingStatus + '\'' +
                '}';
    }
}
