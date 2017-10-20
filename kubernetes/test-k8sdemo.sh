#! /bin/bash

export POD_NAME=$(kubectl get pods -o go-template --template '{{range .items}}{{.metadata.name}}{{"\n"}}{{end}}')

curl http://localhost:8001/api/v1/proxy/namespaces/default/pods/$POD_NAME/
