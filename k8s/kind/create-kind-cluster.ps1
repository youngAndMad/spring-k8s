Write-Output "===Starting Kind Cluster==="
kind create cluster --name spring-k8s --config kind-config.yaml
Write-Output "Loading Docker Images into Kind Cluster"
./kind-load.ps1
Write-Output "===Kind Cluster Started==="