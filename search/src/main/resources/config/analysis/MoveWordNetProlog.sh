#!/bin/bash

FILE="/etc/elasticsearch/analysis/wn_s.pl";

if [ -f $FILE ];
then
  echo "File $FILE exists"
else
  DIRECTORY='/etc/elasticsearch/analysis';
  mkdir $DIRECTORY;
  echo "Created Directory for copy prolog file";
  cp -r wn_s.pl /etc/elasticsearch/analysis
  echo "Prolog file copied to $DIRECTORY/";
fi