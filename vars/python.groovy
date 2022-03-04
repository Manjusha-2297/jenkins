def call(String COMPONENT){

    pipeline{

        agent {
            node {
                label "PYTHON"
            }
        }
        environment {
            sonar_token = credentials('sonar_token')
        }
        stages {


            stage('Submit code quality') {
                steps {
                    sh """
                    #sonar-scanner  -Dsonar.java.binaries=target/. -Dsonar.projectKey=${COMPONENT} -Dsonar.sources=. -Dsonar.host.url=http://172.31.19.162:9000 -Dsonar.login=${sonar_token}
                 echo submit
                  """
                }
            }

            stage('check code quality') {
                steps {
                    //sh "sonar-quality-gate.sh admin admin123 172.31.19.162 ${COMPONENT}"
                    echo "check code quality"
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
                 VERSION=`echo ${GIT_BRANCH}|awk -F / '{print \$NF}'`
                 zip -r ${COMPONENT}-\${VERSION}-.zip * *.ini *.py requirements.txt
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
