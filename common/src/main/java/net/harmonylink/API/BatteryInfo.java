package net.harmonylink.API;


public class BatteryInfo {
    public boolean hasBattery;
    public int batteryPercent;
    public ChargingStatus chargingStatus;

    @Override
    public String toString() {
        return "BatteryInfo{" +
                "hasBattery=" + hasBattery +
                ", batteryPercent=" + batteryPercent +
                ", chargingStatus='" + chargingStatus + '\'' +
                '}';
    }
}
