def call(String COMPONENT){

    pipeline{

        agent {
            node {
                label "NODEJS"
            }
        }
        environment {
            sonar_token = credential{'sonar_token'}
        }
        stages {

            stage('Check code quality') {
                steps {
                    sh """
                    sonar-scanner -Dsonar.projectKey=${COMPONENT} -Dsonar.sources=. -Dsonar.host.url=http://172.31.19.162:9000 -Dsonar.login=${sonar_token}
                  """
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
