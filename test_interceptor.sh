#!/bin/bash
./mvnw spring-boot:run &
PID=$!
sleep 15
curl -s -X POST http://localhost:8080/api/demo/send -H "Content-Type: application/json" -d '{"senderVpa":"a","receiverVpa":"b","amount":10,"pin":"1234"}'
curl -s http://localhost:8080/api/dashboard/overview | jq .kpis
kill $PID
