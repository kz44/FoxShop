FROM openjdk:21

VOLUME /tmp
ARG JAR_FILE
ARG FOXSHOP_DB_URL
ARG FOXSHOP_DB_USER
ARG FOXSHOP_DB_PASSWORD
ARG FOXSHOP_WEB_PORT
ARG DB_REBUILD
ARG FOXSHOP_JWT_SECRET
ARG FOXSHOP_TOKEN_SECRET

ENV FOXSHOP_DB_URL $FOXSHOP_DB_URL
ENV FOXSHOP_DB_USER $FOXSHOP_DB_USER
ENV FOXSHOP_DB_PASSWORD $FOXSHOP_DB_PASSWORD
ENV FOXSHOP_WEB_PORT $FOXSHOP_WEB_PORT
ENV DB_REBUILD $DB_REBUILD
ENV FOXSHOP_JWT_SECRET $FOXSHOP_JWT_SECRET
ENV FOXSHOP_TOKEN_SECRET $FOXSHOP_TOKEN_SECRET

RUN echo $JAR_FILE
RUN mkdir -p /opt/FoxShopNullPointerNinjasOtocyon
COPY $JAR_FILE "/opt/FoxShopNullPointerNinjasOtocyon/FoxShopNullPointerNinjasOtocyon.jar"
ENTRYPOINT ["java", "-jar", "/opt/FoxShopNullPointerNinjasOtocyon/FoxShopNullPointerNinjasOtocyon.jar"]