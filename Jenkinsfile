pipeline {
    agent none
    stages {
            stage('GitCheckout') {
            agent any
            steps {
                script {
                 git branch: 'dev', credentialsId: 'ac07d748-41ef-4fa8-ab77-5e6e672ef4b7', url: 'https://github.com/udeep1995/jenkins-nagp-exam2.git'
                }                
            }    
        }
        stage('Build') {
            agent any
            steps {
                script{
                    mvn_version = 'M3'
                    mvn_home = tool mvn_version
                    if(isUnix()) {
                        sh "'${mvn_home}/bin/mvn' clean install"    
                    } else {
                        bat "${mvn_home}/bin/mvn clean install"    
                    }        
                }
            }   
        }
        stage('SonarQubeAnalysis') {
            agent any 
            steps {
                script {
                    withSonarQubeEnv('sonarqube') {
                        mvn_version = 'M3'
                        mvn_home = tool mvn_version
                        if(isUnix()) {
                            sh "'${mvn_home}/bin/mvn' sonar:sonar \
                            -Dsonar.projectKey=helloworld \
                            -Dsonar.host.url=http://localhost:9000 \
                            -Dsonar.login=8902157eb0c2546ac9ea29bb1902df745e026b6"
                        } else {
                             bat "${mvn_home}/bin/mvn sonar:sonar \
                            -Dsonar.projectKey=helloworld \
                            -Dsonar.host.url=http://localhost:9000 \
                            -Dsonar.login=18902157eb0c2546ac9ea29bb1902df745e026b6"
                        }      
                    }
                }
            }
        }
        stage('Quality Gate') {
            agent any
            steps {
                script {
                    timeout(time: 10, unit: 'SECONDS') { 
                        def qg = waitForQualityGate() 
                        if (qg.status != 'OK') {
                            error "Pipeline aborted due to quality gate failure: ${qg.status}"
                        }
                    }
                }
            }
        }
    }
}