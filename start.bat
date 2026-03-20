@echo off
echo ========================================
echo AI 小说创作助手 - 启动脚本
echo ========================================
echo.

echo [1/2] 启动后端服务...
cd novel_serve
start "Novel Backend" cmd /k "mvn spring-boot:run"
echo 后端服务正在启动，请等待...
timeout /t 10 /nobreak > nul

echo.
echo [2/2] 启动前端服务...
cd ..\novel_web
start "Novel Frontend" cmd /k "npm run dev"
echo 前端服务正在启动...

echo.
echo ========================================
echo 启动完成！
echo ========================================
echo.
echo 前端地址: http://localhost:5173
echo 后端地址: http://localhost:8080
echo.
echo 请在浏览器中访问前端地址开始使用
echo.
pause