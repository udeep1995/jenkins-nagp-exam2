FROM tomcat:9-jre8

RUN wget -O a.txt http://admin:password123@127.0.0.1:8081/artifactory/api/storage/helloworld-prod/com/nagp/helloworld-prod/0.0.1-SNAPSHOT
RUN res=$(grep war a.txt | cut -d ":" -f2 | grep war | tail -1 | grep -o '"[^"]\+"'| sed 's/"//g')
RUN wget -O /usr/local/tomcat/webapps/helloworld.war http://admin:password123@127.0.0.1:8081/artifactory/helloworld-prod/com/nagp/helloworld-prod/0.0.1-SNAPSHOT/$res

EXPOSE 8080
CMD /usr/local/tomcat/bin/catalina.sh run