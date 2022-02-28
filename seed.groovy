//freeStyleJob('example') {
//    logRotator(-1, 10)
//    jdk('Java 8')
//    scm {
//        github('jenkinsci/job-dsl-plugin', 'master')
//    }
//    triggers {
//        githubPush()
//    }
//    steps {
//        gradle('clean build')
//    }
//    publishers {
//        archiveArtifacts('job-dsl-plugin/build/libs/job-dsl.hpi')
//    }
//}

folder('CI-Pipelines') {
    displayName('CI-Pipelines')
    description('CI-Pipelines')
}

def component = ["cart", "catalogue", "user", "frontend", "shipping", "payment"]

def count = (component.size() -1 )

for(int i in 0..count) {
  pipelineJob('CI-Pipelines/${component[i]}') {
    configure { flowdefinition ->
      flowdefinition << delegate.'definition'(class: 'org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition', plugin: 'workflow-cps') {
        'scm'(class: 'hudson.plugins.git.GitSCM', plugin: 'git') {
          'userRemoteConfigs' {
            'hudson.plugins.git.UserRemoteConfig' {
              'url'('https://manjusha9722@dev.azure.com/manjusha9722/DevOps/_git/frontend')
            }
          }
          'branches' {
            'hudson.plugins.git.BranchSpec' {
              'name'('*/main')
            }
          }
        }
        'scriptPath'('jenkinsfile')
        'lightweight'(true)
      }
    }
  }
}