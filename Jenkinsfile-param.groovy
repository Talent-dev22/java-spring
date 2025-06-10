pipeline {
    agent any
     parameters { choice(name: 'DOCKER_REPO', choices: ['dockerhub', 'awsecr'], description: 'Select the docker image repo') }

    stages {
       
        stage('docker image build ') {
            steps {
                sh 'docker build -t java-spring-22:v${BUILD_NUMBER} -f Dockerfile_old .'
            }
        }
        stage('docker login ') {
            when {
                    expression { params.DOCKER_REPO == 'dockerhub' }
                }

            steps {
                sh 'docker login '
            }
        }
        stage('docker tagging ') {
           when {
                    expression { params.DOCKER_REPO == 'dockerhub' }
                }

            steps {
                sh 'docker tag java-spring-22:v${BUILD_NUMBER} malleshdevops/dev22:spring-22.${BUILD_NUMBER}'
            }
        }
      stage('image push dockerhub ') {
           when {
                    expression { params.DOCKER_REPO == 'dockerhub' }
                }

            steps {
                sh 'docker push malleshdevops/dev22:spring-22.${BUILD_NUMBER}'
            }
        }
      stage('authenticate ECR'){
         when {
                    expression { params.DOCKER_REPO == 'awsecr' }
                }

           steps{
           sh 'aws ecr get-login-password --region us-west-2 | docker login --username AWS --password-stdin 759449706669.dkr.ecr.us-west-2.amazonaws.com'
          }
      }
      stage('push ECR'){
           when {
                    expression { params.DOCKER_REPO == 'awsecr' }
                }
            steps {
                sh '''
		 docker tag java-spring-22:v${BUILD_NUMBER} 759449706669.dkr.ecr.us-west-2.amazonaws.com/dev22/spring:v${BUILD_NUMBER}
                 docker push 759449706669.dkr.ecr.us-west-2.amazonaws.com/dev22/spring:v${BUILD_NUMBER}
		   
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
