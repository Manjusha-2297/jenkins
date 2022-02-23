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

pipelineJob('CI-Pipelines/frontend') {
  configure { flowdefinition ->
    flowdefinition << delegate.'definition'(class:'org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition',plugin:'workflow-cps') {
      'scm'(class:'hudson.plugins.git.GitSCM',plugin:'git') {
        'userRemoteConfigs' {
          'hudson.plugins.git.UserRemoteConfig' {
            'url'('https://DevOps-Batches@dev.azure.com/DevOps-Batches/DevOps57/_git/frontend')
          }
        }
        'branches' {
          'hudson.plugins.git.BranchSpec' {
            'name'('*/main')
          }
        }
      }
      'scriptPath'('Jenkinsfile')
      'lightweight'(true)
    }
  }
}