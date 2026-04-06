package net.kalesy.vmwatchagent;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgentController {
    private final AgentService agentService;
    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping("/metrics/disk")
    public Double getDiskUsage() {
        return agentService.getDisk();
    }
    @GetMapping("/metrics/cpu")
    public double getCpuUsage() throws InterruptedException {
        try{
            return agentService.getCpu();
        } catch (InterruptedException e) {
            System.err.println("Agent interrupted");
            return -1;
        }
    }
    @GetMapping("/metrics/memory")
    public double getMemoryUsage() {
        return agentService.getMemory();
    }
    @GetMapping("/metrics")
    public Vm getMetrics() {
        return agentService.getMetrics();
    }

}
