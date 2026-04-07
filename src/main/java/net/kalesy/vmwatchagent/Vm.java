package net.kalesy.vmwatchagent;

import java.util.HashMap;

public class Vm {

    private double cpuUsage;
    private double memoryUsage;
    private HashMap<String, Double> diskUsage;

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public HashMap<String, Double> getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(HashMap<String,Double> diskUsage) {
        this.diskUsage = diskUsage;
    }
}
