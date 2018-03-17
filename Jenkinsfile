pipeline {
    agent { node { label 'docker' } }

    stages {
        stage('Commit Stage') {
            steps {
                sh './gradlew clean build'
            }
        }
        stage('Acceptance Stage') {
            steps {
                sh './gradlew clean build'
            }
        }
    }

//    post {
//        always {
//             junit 'build/reports/**/*.xml'
//        }
//    }
}
