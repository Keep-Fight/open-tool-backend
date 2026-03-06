# 使用轻量 JRE 镜像
FROM eclipse-temurin:17-jre

# 设置工作目录
WORKDIR /app

# 拷贝 jar
COPY target/*.jar app.jar

# 暴露端口（Spring Boot 默认 8080）
EXPOSE 8080

# 启动命令
ENTRYPOINT ["java",
  "-Xms256m",
  "-Xmx512m",
  "-XX:+UseG1GC",
  "-jar","/app/app.jar"]