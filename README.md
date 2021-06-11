# SGRouter Routing Service
GAE service to find the shortest path using Singapore's public transit system. Graph created by graph_builder_service and uploaded to Google Cloud Storage in the form of an SQLite Database. Routing algorithm uses a modified Dijkstra algorithm.

## Technical Overview
Language: Java11
Framework: Spring Boot Web
GCP Products:
- App Engine
- Datastore
- Cloud Storage
- Directions API

## Prerequisites
- Java11
- GCP account setup for App Engine, Datastore, Cloud Storage and Directions API

## Installing
First clone this subdirectory of the repository:
```
git clone --depth 1 --filter=blob:none --sparse https://github.com/jloh02/SGRouter
cd SGRouter
git sparse-checkout set routing_service
```

Alternatively, clone the entire repository:
```
git clone https://github.com/jloh02/SGRouter
```

## Wiki
https://github.com/jloh02/SGRouter/wiki/Routing-Service-Overview