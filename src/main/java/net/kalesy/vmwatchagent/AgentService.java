package net.kalesy.vmwatchagent;

import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSFileStore;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.util.*;

import static oshi.software.os.OperatingSystem.ProcessSorting.CPU_DESC;
import static oshi.software.os.OperatingSystem.ProcessSorting.RSS_DESC;

@Service
public class AgentService {
    SystemInfo si = new SystemInfo();
    OperatingSystem os = si.getOperatingSystem();
    HardwareAbstractionLayer hal = si.getHardware();
    public double getMemory(){
        GlobalMemory memory = hal.getMemory();
        long total = memory.getTotal();
        long available = memory.getAvailable();
        return (double)(total - available) / total * 100;
    }
    public HashMap<String,Double> getDisk(){
        HashMap<String,Double> diskList = new HashMap<String, Double>();

        List<OSFileStore> disks = os.getFileSystem().getFileStores();
        for(OSFileStore disk : disks){
            double totalSpace = disk.getTotalSpace();
            double totalAvailable = disk.getFreeSpace();
            if(totalSpace > 0){
                diskList.put(disk.getName(),(double)(totalSpace - totalAvailable) / totalSpace * 100);
            }else{
                diskList.put(disk.getName(),(double)0.0);
            }
        }
        return diskList;

    }
    public double getCpu() throws InterruptedException {
        CentralProcessor cpu = hal.getProcessor();
        long[] prevTicks = cpu.getSystemCpuLoadTicks();
        Thread.sleep(1000); // wait 1 second
        return cpu.getSystemCpuLoadBetweenTicks(prevTicks) * 100;
    }
    public HashMap<String, Double> getServiceCpuUsage() {
        HashMap<String, Double> service = new HashMap<>();
        List<OSProcess> processes = os.getProcesses(null, CPU_DESC,10);
        for(OSProcess process : processes){
            service.put(process.getName(), process.getProcessCpuLoadCumulative() * 100);
        }
        return service;
    }

    public HashMap<String, Double> getServiceMemoryUsage() {
        HashMap<String, Double> service = new HashMap<>();
        List<OSProcess> processes = os.getProcesses(null, RSS_DESC,10);
        for(OSProcess process : processes){
            if(hal.getMemory().getTotal() > 0){
                service.put(process.getName(), (double) process.getResidentSetSize() / hal.getMemory().getTotal() * 100);
            }else{
                service.put(process.getName(), 0.0);
            }
        }
        return service;
    }
    public Vm getMetrics(){
        Vm vm = new Vm();
        vm.setDiskUsage(getDisk()) ;
        vm.setMemoryUsage(getMemory());
        vm.setServiceCpuUsage(getServiceCpuUsage());
        vm.setServiceMemoryUsage(getServiceMemoryUsage());
        try {
            vm.setCpuUsage(getCpu());
        } catch (InterruptedException e) {
            System.err.println("Error getting CPU usage");
            vm.setCpuUsage(-1);
        }
        return vm;
    }

}
