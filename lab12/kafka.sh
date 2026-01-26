#!/bin/bash

kafka-topics --bootstrap-server kafka:9092 \
  --create --topic numbers \
  --partitions 1 --replication-factor 1

kafka-console-producer --bootstrap-server kafka:9092 --topic numbers
