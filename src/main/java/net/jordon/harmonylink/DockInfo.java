package net.jordon.harmonylink;

public class DockInfo {
    String dockModel;
    boolean isDocked;
    boolean fallbackDetection;

    @Override
    public String toString() {
        return "DockInfo{" +
                "dockModel='" + dockModel + '\'' +
                ", isDocked=" + isDocked +
                ", fallbackDetection=" + fallbackDetection +
                '}';
    }
}
