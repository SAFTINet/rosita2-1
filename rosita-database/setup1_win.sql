/*
 *   Copyright 2012-2013 The Regents of the University of Colorado
 *
 *   Licensed under the Apache License, Version 2.0 (the "License")
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
create user  with  superuser inherit createrole replication;
create tablespace rosita_data owner rosita location 'C:\Program Files\PostgreSQL\9.1\rosita_data';
create tablespace rosita_indx owner rosita location 'C:\Program Files\PostgreSQL\9.1\rosita_indx';
create database  with owner  tablespace rosita_data;
