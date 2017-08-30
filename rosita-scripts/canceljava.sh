#!/bin/bash

ROSITAJAVAPROCID=$(ps -ef | grep [r]osita\-lib | awk -F" " '{print $2}')
echo Sending SIGKILL to $ROSITAJAVAPROCID
sudo kill -9 $ROSITAJAVAPROCID

