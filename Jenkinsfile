pipeline {
    agent any

    tools {
        // Đổi 'Maven' thành tên Maven tool đã cấu trong Jenkins (Manage Jenkins → Tools)
        // Nếu chưa cấu, xóa block tools và dùng: sh 'mvn ...' (cần Maven có sẵn trên agent)
        maven 'Maven'
    }

    environment {
        // Jenkins và SonarQube cùng Docker network → dùng hostname 'sonarqube'
        // Nếu Jenkins chạy trên host, SonarQube trong Docker: dùng 'http://localhost:9000'
        SONAR_HOST_URL = 'http://sonarqube:9000'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile -B'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test -B'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                // 'SonarQube' = tên SonarQube server đã cấu trong Jenkins (Manage Jenkins → System)
                // Token cấu tại Credentials, gắn với server đó
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar -Dsonar.projectKey=sonarqube-demo -B'
                }
            }
        }
    }

    post {
        always {
            sh 'rm -rf target'
        }
        failure {
            echo 'Pipeline failed. Check logs.'
        }
    }
}
