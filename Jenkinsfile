pipeline {
    // Run on any Jenkins machine. The build tools must be installed on that machine.
    agent any

    options {
        // Stop a build that runs longer than this, so a stuck build cannot run forever.
        timeout(time: 10, unit: 'MINUTES')
        // Delete older build records so the Jenkins disk does not fill up.
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    // Jenkins checks out your code automatically before the first stage, so there is no Checkout stage here.
    stages {
        stage('Build') {
            steps {
                // Compile or install dependencies. If a command fails, Jenkins stops here and the stage turns red.
                sh 'mvn -B clean compile'
            }
        }
        stage('Test') {
            steps {
                // Run the unit tests. One failing test makes the command exit with an error and stops the pipeline.
                sh 'mvn -B test'
            }
            post {
                always {
                    // Read the test report so results show on the build page, even when tests fail.
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Package') {
            steps {
                sh 'mvn -B -DskipTests package'
                // Keep the result on the build page so you can download it.
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    // Runs after all stages. Replace the echo lines with an email or chat message later.
    post {
        success { echo 'Pipeline finished: every stage passed.' }
        failure { echo 'Pipeline failed. Open the stage that turned red and read its log.' }
    }
}
