#curl -i -X GET -H "Content-Type: application/json" --data '{ "pageIds": [ "33ed38a1-6231-46b7-a6c1-ee61518efb85", "c9fc8537-7386-4adb-80b6-f6917a85b543"]}' localhost:8080/page/getPageByIds
#curl -i -X PUT -H "Content-Type: application/json" --data '{ "wordList": ["word"]}' localhost:8080/material/add

curl -i -X POST -H "Content-Type: application/json" http://localhost:8080/memoryRecord/movePage?recordId=88023deb-1734-40b6-87af-2b3ba60227df\&pageId=44959da0-871d-47a8-a38b-0ee41a5c7855
