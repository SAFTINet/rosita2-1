@echo off
for /l %%x in (1, 1, 50) do (
	echo STATUS^|^|^|%%x^|^|^|50
	PING 1.1.1.1 -n 1 -w 50 > NUL
)
exit /B 0