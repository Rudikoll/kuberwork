# kubectl kubeplugin

Simple kubectl plugin to get resource usage stats.

## Requirements
- kubectl
- metrics-server installed

## Installation
chmod +x scripts/kubectl-kubeplugin
export PATH=$PATH:$(pwd)/scripts

## Usage
kubectl kubeplugin <namespace> <resource>

Example:
kubectl kubeplugin kube-system pods

## Output
Resource, Namespace, Name, CPU, Memory
