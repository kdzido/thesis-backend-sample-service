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
                echo 'TODO Acceptance Stage'
            }
        }
    }

//    post {
//        always {
//             junit 'build/reports/**/*.xml'
//        }
//    }
}
