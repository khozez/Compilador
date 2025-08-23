@echo off
for %%F in (*.txt) do (
    echo %%~nF:
    type "%%F"
    echo.
)