package net.harmonylink.API;

public class DockInfo {
    public String dockModel;
    public Boolean isDocked;
    public Boolean fallbackDetection;

    @Override
    public String toString() {
        return "DockInfo{" +
                "dockModel='" + dockModel + '\'' +
                ", isDocked=" + isDocked +
                ", fallbackDetection=" + fallbackDetection +
                '}';
    }
}
