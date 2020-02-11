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
                            -Dsonar.host.url=http://localhost:9000"
                        } else {
                             bat "${mvn_home}/bin/mvn sonar:sonar \
                            -Dsonar.projectKey=helloworld \
                            -Dsonar.host.url=http://localhost:9000"
                        }
                    }
                }
            }
        }
        stage('Quality Gate') {
            agent any
            steps {
                script {
                        def qg = waitForQualityGate() 
                        if (qg.status != 'OK') {
                            error "Pipeline aborted due to quality gate failure: ${qg.status}"
                        }
                }
            }
        }
        stage('Artifactory Upload') {
            agent any
            steps {
                script {
                    def server = Artifactory.server('default')
                    def buildInfo = Artifactory.newBuildInfo()
                    buildInfo.env.capture = true
                    buildInfo.env.collect()
                    def rtMaven = Artifactory.newMavenBuild()
                    rtMaven.tool = 'M3'
                    rtMaven.deployer server: server, releaseRepo: 'helloworld', snapshotRepo: 'helloworld'
                    rtMaven.deployer.artifactDeploymentPatterns.addInclude("*.war")
                    rtMaven.run pom: 'pom.xml', goals: 'clean package', buildInfo: buildInfo
                    server.publishBuildInfo buildInfo
              }
          }
       }
       stage('Docker Build') {
            agent any
            steps {
                script{
                    if(isUnix()) {
                        sh '''
                            COUNT=$(docker ps -a | grep "udeep_helloworld_container" | wc -l)
                            if (($COUNT > 0)); then
                                docker stop udeep_helloworld_container && docker rm udeep_helloworld_container
                            fi
                        '''
                        sh 'docker build -t udeep_helloworld_image:latest .' 
                        sh 'docker run --name udeep_helloworld_container -d -p 9050:8080 udeep_helloworld_image'
                    } else {
                        bat '''
                            for /F "tokens=*" %%x in (\'docker ps -a -q --filter "name=udeep_helloworld_container"\') do (set mycontainer=%%x)
                            if defined mycontainer (cmd /c docker stop %mycontainer% && docker rm -f %mycontainer%)
                        ''' 
                        bat 'docker build -t udeep_helloworld_image:latest .' 
                        bat 'docker run --name udeep_helloworld_container -d -p 9050:8080 udeep_helloworld_image'
                    }
                }
            }
        }
    }
}