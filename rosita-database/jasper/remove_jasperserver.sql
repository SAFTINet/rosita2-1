SELECT pg_terminate_backend(pg_stat_activity.procpid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'jasperserver'
AND procpid <> pg_backend_pid();

DROP DATABASE jasperserver;

