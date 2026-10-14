pipeline {
    agent any

    options {
        timeout(time: 10, unit: 'MINUTES')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -B clean compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn -B test'
            }
            post {
                always {
                    // shows pass/fail counts and trends in the Jenkins UI
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn -B -DskipTests package'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Deploy') {
            steps {
                // Placeholder. Later: copy the jar to a server, or build and push a Docker image.
                echo 'Deploying bank-app... (nothing to deploy to yet)'
            }
        }
    }

    post {
        success { echo 'Pipeline finished: build and tests passed.' }
        failure { echo 'Pipeline failed. Check the stage that turned red.' }
    }
}
