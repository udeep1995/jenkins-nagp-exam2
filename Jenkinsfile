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
    }
}