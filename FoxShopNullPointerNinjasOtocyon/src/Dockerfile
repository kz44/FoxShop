FROM openjdk:21

VOLUME /tmp
ARG JAR_FILE
ARG FOXSHOP_DB_URL
ARG FOXSHOP_DB_USER
ARG FOXSHOP_DB_PASSWORD

ENV FOXSHOP_DB_URL $FOXSHOP_DB_URL
ENV FOXSHOP_DB_USER $FOXSHOP_DB_USER
ENV FOXSHOP_DB_PASSWORD $FOXSHOP_DB_PASSWORD

RUN echo $JAR_FILE
RUN mkdir -p /opt/FoxShopNullPointerNinjasOtocyon
COPY $JAR_FILE "/opt/FoxShopNullPointerNinjasOtocyon/FoxShopNullPointerNinjasOtocyon.jar"
ENTRYPOINT ["java", "-jar", "/opt/FoxShopNullPointerNinjasOtocyon/FoxShopNullPointerNinjasOtocyon.jar"]