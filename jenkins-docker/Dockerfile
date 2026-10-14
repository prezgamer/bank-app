# Jenkins image that also has Maven, so `sh 'mvn ...'` works in the Jenkinsfile.
# JDK 17 and Git already come with the base image.
FROM jenkins/jenkins:lts-jdk17

USER root
RUN apt-get update \
    && apt-get install -y --no-install-recommends maven \
    && rm -rf /var/lib/apt/lists/*
USER jenkins
