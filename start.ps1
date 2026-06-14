# Запуск всего проекта: mock-1c + student-api (demo) + student-cabinet
$root = Split-Path -Parent $MyInvocation.MyCommand.Path

Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$root\mock-1c'; node server.js" -WindowStyle Normal
Start-Sleep -Seconds 2

Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$root\student-api'; mvn '-Dspring.profiles.active=demo' spring-boot:run" -WindowStyle Normal
Start-Sleep -Seconds 2

Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$root\student-cabinet'; npm install; npm run dev" -WindowStyle Normal

Write-Host ""
Write-Host "Запущено три окна:"
Write-Host "  mock-1c     -> http://localhost:8091"
Write-Host "  student-api -> http://localhost:8080"
Write-Host "  frontend    -> http://localhost:5173"
