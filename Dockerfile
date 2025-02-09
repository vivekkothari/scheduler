FROM amazoncorretto:21-al2023-headless

WORKDIR /app

# Change gnupg2-minimal to gnupg2 for AL2023 because the Doppler CLI requires gpgv command from the full package
RUN dnf swap gnupg2-minimal gnupg2 -y

RUN yum -y update && \
  yum -y remove vi && \
  rpm -e --nodeps curl || true && \
  rpm -e --nodeps libcurl || true

COPY build/libs/scheduler-0.0.1-SNAPSHOT.jar /app/server.jar

ENTRYPOINT ["java", "-jar", "/app/server.jar"]
