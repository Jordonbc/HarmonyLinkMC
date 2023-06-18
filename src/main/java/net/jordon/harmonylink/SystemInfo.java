package net.jordon.harmonylink;

public class SystemInfo {
    OSInfo os_info;
    BatteryInfo battery_info;
    DockInfo dock_info;

    @Override
    public String toString() {
        return "SystemInfo{" +
                "os_info=" + os_info +
                ", battery_info=" + battery_info +
                ", dock_info=" + dock_info +
                '}';
    }
}
