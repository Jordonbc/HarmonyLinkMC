package net.jordon.harmonylink;

public class OSInfo {
    String osType;
    String osVersion;
    String osEdition;
    String osBits;

    @Override
    public String toString() {
        return "OSInfo{" +
                "osType='" + osType + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", osEdition='" + osEdition + '\'' +
                ", osBits='" + osBits + '\'' +
                '}';
    }
}
