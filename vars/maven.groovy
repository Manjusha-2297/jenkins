def call(String COMPONENT){

    pipeline{

        agent {
            node {
                label "JAVA"
            }
        }
        environment {
            sonar_token = credentials('sonar_token')
            NEXUS = credentials('NEXUS')
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
                    #sonar-scanner  -Dsonar.java.binaries=target/. -Dsonar.projectKey=${COMPONENT} -Dsonar.sources=. -Dsonar.host.url=http://172.31.19.162:9000 -Dsonar.login=${sonar_token}
                   echo submit
                     """
                }
            }

            stage('check code quality') {
                steps {
                    //sh "sonar-quality-gate.sh admin admin123 172.31.19.162 ${COMPONENT}"
                    echo 'check code quality'
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
                when { expression { sh([returnStdout: true, script: 'echo ${GIT_BRANCH} | grep tags || true' ]) } }
                steps {
                    sh """
                 mvn clean package
                 mv target/${COMPONENT}-1.0.jar ${COMPONENT}.jar
                 VERSION=`echo ${GIT_BRANCH}|awk -F / '{print \$NF}'`
                 zip -r ${COMPONENT}-\${VERSION}.zip *
               """
                }
            }

            stage('Publish artifacts'){
                when { expression { sh([returnStdout: true, script: 'echo ${GIT_BRANCH} | grep tags || true' ]) } }
                steps {
                    sh """
                        VERSION=`echo ${GIT_BRANCH}|awk -F / '{print \$NF}'`
                        curl -f -v -u ${NEXUS} --upload-file ${COMPONENT}-\${VERSION}.zip http://172.31.15.180:8081/repository/${COMPONENT}/${COMPONENT}-\${VERSION}.zip
                      """

                }
            }

            stage('Dev Deployment'){
                when { expression { sh([returnStdout: true, script: 'echo ${GIT_BRANCH} | grep tags || true' ]) } }
                steps{
                    sh """
                        VERSION=`echo ${GIT_BRANCH}|awk -F / '{print \$NF}'`
                      """
                    build job: 'APPDeploy', parameters: [string(name: 'COMPONENT', value: "${COMPONENT}"), string(name: 'ENV', value: 'dev'), string(name: 'APP_VERSION', value:"${VERSION}")]
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
