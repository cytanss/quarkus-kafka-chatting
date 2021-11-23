oc create configmap quarkus-kafka-chatting --from-file=./application.properties
oc set volume deployment/quarkus-kafka-chatting --add -t configmap --configmap-name=quarkus-kafka-chatting -m /deployments/config
