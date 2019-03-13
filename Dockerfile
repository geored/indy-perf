FROM georgy/maven:3.5.4_8u191-jdk as builder
ENV INDY_WORKDIR /opt/indy
ENV INDY_VERSION 1.7.4-SNAPSHOT
ENV INDY_SKIP_TESTS true
ENV INDY_MAVEN_PROFILE ci
ENV INDY_TEST_BUILD_DIR ${INDY_WORKDIR}/deployments/launcher/target
ADD . /opt/indy
RUN cd $INDY_WORKDIR && \
    mvn -DskipTests=${INDY_SKIP_TESTS} package && \
	cd $INDY_TEST_BUILD_DIR && \
	tar -xvf indy-launcher-${INDY_VERSION}-complete.tar.gz
#FROM georgy/openjdk:8u191-jdk
FROM openjdk:8-jre-alpine
WORKDIR /opt/indy
COPY --from=builder /opt/indy /opt/indy
ENV INDY_HOME /opt/indy
ENV ETC_BASE ${INDY_HOME}/etc
ENV REPO_BASE ${INDY_TARGET_FOLDER}/var/lib/indy/data/indy
RUN chgrp -R 0 /opt/indy && \
    chmod -R g=u /opt/indy && \
    cd $INDY_HOME && \
    ls -la
ENV JAVA_MAJOR_VERSION 8
ENV JAVA_APP_DIR $INDY_HOME
ENV JAVA_LIB_DIR ${INDY_HOME}/lib
ENV JAVA_OPTIONS "-Dindy.home=. -Dorg.jboss.logging.provider=sl4j -Dhttps.protocols=TLSv1,TLSv1.1,TLSv1.2"
ENV JAVA_CLASSPATH ${JAVA_LIB_DIR}/indy-embedder-${INDY_VERSION}.jar
ENV JAVA_CLASSPATH $JAVA_CLASSPATH:$JAVA_LIB_DIR/thirdparty/*
ENV JAVA_MAIN_CLASS org.commonjava.indy.boot.jaxrs.JaxRsBooter
EXPOSE 8080
CMD java -cp $JAVA_CLASSPATH $JAVA_OPTIONS $JAVA_MAIN_CLASS
