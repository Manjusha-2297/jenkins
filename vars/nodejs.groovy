def call(String COMPONENT){

    pipeline{

        agent {
            node {
                label "NODEJS"
            }
        }

        stages {

            stage('Check code quality') {
                steps {
                    echo 'code quality'
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
