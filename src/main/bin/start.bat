@echo off
rem ======================================================================
rem windows startup script
rem
rem author: hangdh
rem date: 2021-2-15
rem ======================================================================

rem startup jar
java -Djava.ext.dirs="..\lib\" com.ch.sms.simulator.Simulator

pause