-- 
-- The main build script. Use this script to build a fresh database.
-- If you need to rebuild the DB, dropping tables and views
-- will be required, and you should run rebuild.sh
--
\set ROOT_DIR ''/Users/sperry/l/MarchMadness/eclipse_workspace/NcaaMarchMadness/src/main''
\set SQL_ROOT_DIR :ROOT_DIR/sql
\set DATA_ROOT_DIR :ROOT_DIR/data
\set LOAD_SCRIPT_ROOT_DIR ''/Users/sperry/l/MarchMadness/data''

-- Tee output to log file
\o :LOAD_SCRIPT_ROOT_DIR/DB_BUILD_LOG.txt

\echo 'BUILDING DB...'
\qecho 'BUILDING DB...'-- Goes to the log file

\echo 'Script variables:'
\echo 'ROOT_DIR ==> ' :ROOT_DIR
\echo 'SQL_ROOT_DIR ==> ' :SQL_ROOT_DIR
\echo 'DATA_ROOT_DIR ==> ' :DATA_ROOT_DIR
\echo 'LOAD_SCRIPT_ROOT_DIR ==> ' :LOAD_SCRIPT_ROOT_DIR

\qecho 'Script variables:'-- Goes to the log file
\qecho 'ROOT_DIR ==> ' :ROOT_DIR-- Goes to the log file
\qecho 'SQL_ROOT_DIR ==> ' :SQL_ROOT_DIR-- Goes to the log file
\qecho 'DATA_ROOT_DIR ==> ' :DATA_ROOT_DIR-- Goes to the log file
\qecho 'LOAD_SCRIPT_ROOT_DIR ==> ' :LOAD_SCRIPT_ROOT_DIR-- Goes to the log file

\echo 'CREATING ALL TABLES...'
\qecho 'CREATING ALL TABLES...'-- Goes to the log file
\i :SQL_ROOT_DIR/create_tables.sql

\echo 'CREATING ALL VIEWS...'
\qecho 'CREATING ALL VIEWS...'-- Goes to the log file
\i :SQL_ROOT_DIR/create_views.sql

\echo 'LOADING ALL TABLES:'
\qecho 'LOADING ALL TABLES:'-- Goes to the log file

\echo 'YEAR: 2009...'
\qecho 'YEAR: 2009...'-- Goes to the log file
\set LOAD_SCRIPT_DIR :LOAD_SCRIPT_ROOT_DIR/2009
\i :SQL_ROOT_DIR/load_tables.sql
\i :DATA_ROOT_DIR/Load_TournamentParticipants-2009.sql

\echo 'YEAR: 2010...'
\qecho 'YEAR: 2010...'-- Goes to the log file
\set LOAD_SCRIPT_DIR :LOAD_SCRIPT_ROOT_DIR/2010
\i :SQL_ROOT_DIR/load_tables.sql
\i :DATA_ROOT_DIR/Load_TournamentParticipants-2010.sql

\echo 'YEAR: 2011...'
\qecho 'YEAR: 2011...'-- Goes to the log file
\set LOAD_SCRIPT_DIR :LOAD_SCRIPT_ROOT_DIR/2011
\i :SQL_ROOT_DIR/load_tables.sql
\i :DATA_ROOT_DIR/Load_TournamentParticipants-2011.sql

\echo 'YEAR: 2012...'
\qecho 'YEAR: 2012...'-- Goes to the log file
\set LOAD_SCRIPT_DIR :LOAD_SCRIPT_ROOT_DIR/2012
\i :SQL_ROOT_DIR/load_tables.sql
\i :DATA_ROOT_DIR/Load_TournamentParticipants-2012.sql

\echo 'YEAR: 2013...'
\qecho 'YEAR: 2013...'-- Goes to the log file
\set LOAD_SCRIPT_DIR :LOAD_SCRIPT_ROOT_DIR/2013
\i :SQL_ROOT_DIR/load_tables.sql
\i :DATA_ROOT_DIR/Load_TournamentParticipants-2013.sql

\echo 'YEAR: 2014...'
\qecho 'YEAR: 2014...'-- Goes to the log file
\set LOAD_SCRIPT_DIR :LOAD_SCRIPT_ROOT_DIR/2014
\i :SQL_ROOT_DIR/load_tables.sql
\i :DATA_ROOT_DIR/Load_TournamentParticipants-2014.sql

\echo 'YEAR: 2015...'
\qecho 'YEAR: 2015...'-- Goes to the log file
\set LOAD_SCRIPT_DIR :LOAD_SCRIPT_ROOT_DIR/2015
\i :SQL_ROOT_DIR/load_tables.sql
\i :DATA_ROOT_DIR/Load_TournamentParticipants-2015.sql

\echo 'YEAR: 2016...'
\qecho 'YEAR: 2016...'-- Goes to the log file
\set LOAD_SCRIPT_DIR :LOAD_SCRIPT_ROOT_DIR/2016
\i :SQL_ROOT_DIR/load_tables.sql
\i :DATA_ROOT_DIR/Load_TournamentParticipants-2016.sql

\echo 'YEAR: 2017...'
\qecho 'YEAR: 2017...'-- Goes to the log file
\set LOAD_SCRIPT_DIR :LOAD_SCRIPT_ROOT_DIR/2017
\i :SQL_ROOT_DIR/load_tables.sql
\i :DATA_ROOT_DIR/Load_TournamentParticipants-2017.sql

\echo 'LOADING TOURNAMENT RESULT DATA FOR ALL YEARS...'
\qecho 'LOADING TOURNAMENT RESULT DATA FOR ALL YEARS...'-- Goes to the log file
\i :SQL_ROOT_DIR/load_tournament_result.sql

--
\echo 'DATABASE BUILD COMPLETE.'
\qecho 'DATABASE BUILD COMPLETE.'-- Goes to the log file
-- Turn off output to log file
\o 
