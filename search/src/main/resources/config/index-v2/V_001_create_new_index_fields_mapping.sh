# Please change the indexName and Domain name. 
# Create resourceFormat,instructional fields for resource type in resource mapping
curl -XPUT 'http://localhost:9200/goorulocalresource_v2/resource/_mapping' -d '{
  "resource": {
    "properties": {
      "resourceFormat": {
        "type": "string",
        "index": "analyzed",
        "analyzer": "keyword_lowercase",
        "include_in_all": true
      },
      "instructional": {
        "type": "string",
        "index": "analyzed",
        "analyzer": "keyword_lowercase",
        "include_in_all": true
      }
    }
  }
}'

# Create resourceFormat,instructional fields for collection type in resource mapping
curl -XPUT 'http://localhost:9200/goorulocalresource_v2/collection/_mapping' -d '{
  "collection": {
    "properties": {
      "resourceFormat": {
        "type": "string",
        "index": "analyzed",
        "analyzer": "keyword_lowercase",
        "include_in_all": true
      },
      "instructional": {
        "type": "string",
        "index": "analyzed",
        "analyzer": "keyword_lowercase",
        "include_in_all": true
      }
    }
  }
}'

# Create resourceFormat,instructional fields for quiz type in resource mapping
curl -XPUT 'http://localhost:9200/goorulocalresource_v2/quiz/_mapping' -d '{
  "quiz": {
    "properties": {
      "resourceFormat": {
        "type": "string",
        "index": "analyzed",
        "analyzer": "keyword_lowercase",
        "include_in_all": true
      },
      "instructional": {
        "type": "string",
        "index": "analyzed",
        "analyzer": "keyword_lowercase",
        "include_in_all": true
      }
    }
  }
}'

# Create resourceFormat,instructional fields for scollection type in scollection mapping
curl -XPUT 'http://localhost:9200/goorulocalscollection_v2/scollection/_mapping' -d '{
  "scollection": {
    "properties": {
      "resourceFormat": {
        "type": "string",
        "index": "analyzed",
        "analyzer": "keyword_lowercase",
        "include_in_all": true
      },
      "instructional": {
        "type": "string",
        "index": "analyzed",
        "analyzer": "keyword_lowercase",
        "include_in_all": true
      }
    }
  }
}'


