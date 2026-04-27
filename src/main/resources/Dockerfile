# -------- 基础运行环境 --------
FROM eclipse-temurin:17-jre-jammy

# 创建非 root 用户（安全）
RUN useradd -r -u 1001 appuser

WORKDIR /app

# 拷贝 jar
COPY target/*.jar app.jar

# 修改权限
RUN chown -R appuser:appuser /app

# 切换用户
USER appuser

# 暴露端口
EXPOSE 8080

# JVM 参数
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+ExitOnOutOfMemoryError", \
  "-Dfile.encoding=UTF-8", \
  "-jar", "/app/app.jar"]
