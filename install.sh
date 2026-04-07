#!/bin/bash
apt-get update                                                                                
apt-get install -y openjdk-17-jre 
mkdir -p /opt/vmwatch
curl -L https://github.com/Akobke/VMWatchAgent/releases/latest/download/vmwatchagent.jar -o /opt/vmwatch/vmwatchagent.jar
cat > /etc/systemd/system/vmwatch-agent.service << 'EOF'
[Unit]                                                                                        
Description=VMWatch Agent                                                                     
                                                                                               
[Service]                                                                                     
ExecStart=java -jar /opt/vmwatch/vmwatchagent.jar                                             
Restart=always                                                                                
                 
[Install]
WantedBy=multi-user.target
EOF
systemctl daemon-reload
systemctl enable --now vmwatch-agent
