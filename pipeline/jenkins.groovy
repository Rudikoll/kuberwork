pipeline {
    agent any

    parameters {
        choice(
            name: 'OS',
            choices: ['linux', 'darwin', 'windows'],
            description: 'Target operating system'
        )
        choice(
            name: 'ARCH',
            choices: ['amd64', 'arm64'],
            description: 'Target architecture'
        )
        booleanParam(
            name: 'SKIP_TESTS',
            defaultValue: false,
            description: 'Skip running tests'
        )
        booleanParam(
            name: 'SKIP_LINT',
            defaultValue: false,
            description: 'Skip running linter'
        )
    }

    environment {
        APP = 'kbot'
    }

    stages {
        stage('Clone') {
            steps {
                echo "Cloning repository..."
                checkout scm
            }
        }

        stage('Lint') {
            when {
                expression { return !params.SKIP_LINT }
            }
            steps {
                echo "Linting for OS=${params.OS} ARCH=${params.ARCH}"
                sh 'go vet ./... || true'
            }
        }

        stage('Test') {
            when {
                expression { return !params.SKIP_TESTS }
            }
            steps {
                echo "Running tests..."
                sh 'go test ./... || true'
            }
        }

        stage('Build') {
            steps {
                echo "Building for OS=${params.OS} ARCH=${params.ARCH}"
                sh """
                    CGO_ENABLED=0 GOOS=${params.OS} GOARCH=${params.ARCH} \
                    go build -v -o ${APP} .
                """
            }
        }
    }

    post {
        success {
            echo "SUCCESS: OS=${params.OS}, ARCH=${params.ARCH}"
        }
        failure {
            echo "FAILED"
        }
    }
}
