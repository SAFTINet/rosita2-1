#!/bin/bash
#
# Copyright 2012-2013 The Regents of the University of Colorado
#
# Licensed under the Apache License, Version 2.0 (the "License")
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
#export PGUSER=postgres
#export PGPASSWORD=bistate@2013
#mkdir /var/lib/pgsql/9.1/rosita_data
#mkdir /var/lib/pgsql/9.1/rosita_indx
#psql --file=setup1_linux.sql postgres
#export PGDATABASE=rosita
export PGUSER=rosita
export PGPASSWORD=bistate@2013
#JASPER
psql --file=jasper/remove_jasperserver.sql
createdb -T template0 jasperserver
psql jasperserver< jasper/jaserserver.out
