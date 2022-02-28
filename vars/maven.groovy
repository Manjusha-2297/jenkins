def call(String COMPONENT){

    pipeline{

        agent {
            node {
                label "JAVA"
            }
        }
        environment {
            sonar_token = credentials('sonar_token')
        }
        stages {

            stage('code compile') {
                steps {
                    sh 'mvn compile'
                }
            }

            stage('Submit code quality') {
                steps {
                    sh """
                    sonar-scanner  -Dsonar.java.binaries=target/. -Dsonar.projectKey=${COMPONENT} -Dsonar.sources=. -Dsonar.host.url=http://172.31.19.162:9000 -Dsonar.login=${sonar_token}
                  """
                }
            }

            stage('check code quality') {
                steps {
                    sh "sonar-quality-gate.sh admin admin123 172.31.29.53 ${COMPONENT}"
                }
            }

            stage('link checks'){
                steps {
                    echo 'Link Checks'
                }
            }

            stage('unit tests'){
                steps {
                    echo 'unit tests'
                }
            }

            stage('Prepare artifact') { // artifact is a piece getting ready for a file to get downloaded
                steps {
                    sh """
                 cd static
                 zip -r ${COMPONENT}.zip *
               """
                }
            }

            stage('Publish artifacts'){
                steps {
                    echo 'Publish artifacts'
                }
            }
        }

        post {
            // Clean after build
            always {
                cleanWs()
            }
        }
    }
}
