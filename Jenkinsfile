pipeline {
    agent any

    stages {
        stage('Commit Test') {
            steps {
                sh './gradlew check'
            }
        }
    }

    post {
        always {
            junit 'build/reports/**/*.xml'
        }
    }
}
