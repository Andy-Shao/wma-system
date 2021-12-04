#curl -i -H 'Content-Type: appplication/json' -X POST -d '{"originRecordId":"88023deb-1734-40b6-87af-2b3ba60227df", "targetRecordId":"88023deb-1734-40b6-87af-2b3ba60227df", "pageId":"44959da0-871d-47a8-a38b-0ee41a5c7855", "moveType":"head"}' http://localhost:8080/memoryRecord/moveStudyPage

curl -H 'Content-Type: application/json' -X GET http://localhost:8080/application
