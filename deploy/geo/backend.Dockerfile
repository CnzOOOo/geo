FROM eclipse-temurin:17-jre

ENV LANG=C.UTF-8 \
    LC_ALL=C.UTF-8 \
    TZ=Asia/Shanghai \
    JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8"

WORKDIR /jeecg-boot

COPY jeecg-system-start-3.9.3.jar /jeecg-boot/jeecg-system-start-3.9.3.jar

EXPOSE 8080

CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "jeecg-system-start-3.9.3.jar"]
