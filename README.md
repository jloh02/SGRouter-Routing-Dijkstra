# SGRouter Dijkstra Routing Service
GAE service to find the shortest path using Singapore's public transit system. Graph created by graph_builder and uploaded to Google Cloud Storage in the form of an SQLite Database. Routing algorithm uses a modified priority queue Dijkstra algorithm.

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

## Wiki
https://github.com/jloh02/SGRouter/wiki/Routing-Service-Overview

## Installing
Clone the entire repository including other services:
```bash
git clone https://github.com/jloh02/SGRouter
```

Alternatively, only clone this submodule:
```bash
git clone https://github.com/jloh02/SGRouter-Routing-Dijkstra
```

## Setup
### Graph.db
Ensure that a local copy of `graph.db` is created using [graph_builder](https://github.com/jloh02/SGRouter-Graph-Builder). Store it under `<project working directory>/archive` and rename it to `12_sun_graph_short.db`


### GCP Datastore
Ensure you are authenticated to access the Google Cloud Datastore which contains the `walkSpeed` variable used for calculations.

For server-side testing, ensure your App Engine default IAM account has access to Cloud Datastore.

For client-side testing, please remember to authenticate using your owner IAM account json: Refer to <https://cloud.google.com/docs/authentication/production>