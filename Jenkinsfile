pipeline {
    agent any

    stages {
        stage('Commit Stage') {
            steps {
                sh './gradlew check'
            }
        }
        stage('Acceptance Stage') {
            steps {
                sh './gradlew :news-monolith-acceptance-test:test'
            }
        }
    }

//    post {
//        always {
//             junit 'build/reports/**/*.xml'
//        }
//    }
}
