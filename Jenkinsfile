pipeline {
    agent any

    stages {
       
        stage('maven build ') {
            steps {
               sh 'mvn clean package'
            }
        }
        
        stage('docker image build ') {
            steps {
                sh 'docker build -t java-spring-19:v${BUILD_NUMBER} .'
            }
        }
        stage('docker login ') {
            steps {
                sh 'docker login '
            }
        }
        stage('docker tagging ') {
            steps {
                sh 'docker tag java-spring-19:v${BUILD_NUMBER} malleshdevops/devops19:spring-19.${BUILD_NUMBER}'
            }
        }
      stage('image push dockerhub ') {
            steps {
                sh 'docker push malleshdevops/devops19:spring-19.${BUILD_NUMBER}'
            }
        }
      stage('push ECR'){
            steps {
                sh '''aws ecr get-login-password --region us-west-2 | docker login --username AWS --password-stdin 897276212041.dkr.ecr.us-west-2.amazonaws.com
           		    
			docker tag java-spring-19:v${BUILD_NUMBER} 897276212041.dkr.ecr.us-west-2.amazonaws.com/devops19-java:v${BUILD_NUMBER}
                    	 docker push 897276212041.dkr.ecr.us-west-2.amazonaws.com/devops19-java:v${BUILD_NUMBER}
		   
       '''
            }
          }

    }
    post{
        always{
            emailext body: '''Hi,

     The jenkins has been failed . please check it.

     Thanks
     Devops Team''', subject: 'testing jenkins pipeline: $JOB_URL', to: 'malleshdevops2021@outlook.com'
    }
    }

}
