#!/bin/bash
#
# Copyright 2012 The Regents of the University of Colorado
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
export PGDATABASE=rosita
export PGUSER=rosita
export PGPASSWORD=`grep "^password=" /usr/share/rosita/rosita.properties | cut -d "=" -f 2`
DATE=`date +%Y%m%d%H%M%S`
pg_dump -a -f /home/rosita/rosita_backup_cz_${DATE}.dmp -n cz
chmod g+w /home/rosita/rosita_backup_cz_${DATE}.dmp
chgrp rosita /home/rosita/rosita_backup_cz_${DATE}.dmp
pg_dump -a -f /home/rosita/rosita_backup_raw_${DATE}.dmp -n raw
chmod g+w /home/rosita/rosita_backup_raw_${DATE}.dmp
chgrp rosita /home/rosita/rosita_backup_raw_${DATE}.dmp