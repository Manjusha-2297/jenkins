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
  def j = component[i]
  pipelineJob("CI-Pipelines/${j}") {
    configure { flowdefinition ->
      flowdefinition << delegate.'definition'(class: 'org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition', plugin: 'workflow-cps') {
        'scm'(class: 'hudson.plugins.git.GitSCM', plugin: 'git') {
          'userRemoteConfigs' {
            'hudson.plugins.git.UserRemoteConfig' {
              'url'("https://manjusha9722@dev.azure.com/manjusha9722/DevOps/_git/${j}")
              'refspec'('\'+refs/tags/*\':\'refs/remotes/origin/tags/*\'')
            }
          }
          'branches' {
            'hudson.plugins.git.BranchSpec' {
              'name'('**/tags/**')
            }
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

folder('Mutable') {
  displayName('Mutable')
  description('Mutable')
}

pipelineJob("Mutable/vpc") {
  configure { flowdefinition ->
    flowdefinition << delegate.'definition'(class: 'org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition', plugin: 'workflow-cps') {
      'scm'(class: 'hudson.plugins.git.GitSCM', plugin: 'git') {
        'userRemoteConfigs' {
          'hudson.plugins.git.UserRemoteConfig' {
            'url'("https://github.com/Manjusha-2297/terraform-mutable.git")
          }
        }
        'branches' {
          'hudson.plugins.git.BranchSpec' {
            'name'('*/main')
          }
        }
      }
      'scriptPath'('vpc/jenkinsfile')
      'lightweight'(true)
    }
  }
}

pipelineJob("Mutable/DB") {
  configure { flowdefinition ->
    flowdefinition << delegate.'definition'(class: 'org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition', plugin: 'workflow-cps') {
      'scm'(class: 'hudson.plugins.git.GitSCM', plugin: 'git') {
        'userRemoteConfigs' {
          'hudson.plugins.git.UserRemoteConfig' {
            'url'("https://github.com/Manjusha-2297/terraform-mutable.git")
          }
        }
        'branches' {
          'hudson.plugins.git.BranchSpec' {
            'name'('*/main')
          }
        }
      }
      'scriptPath'('databases/jenkinsfile')
      'lightweight'(true)
    }
  }
}

for(int i in 0..count) {
  def j = component[i]
  pipelineJob("Mutable/${j}") {
    configure { flowdefinition ->
      flowdefinition << delegate.'definition'(class: 'org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition', plugin: 'workflow-cps') {
        'scm'(class: 'hudson.plugins.git.GitSCM', plugin: 'git') {
          'userRemoteConfigs' {
            'hudson.plugins.git.UserRemoteConfig' {
              'url'("https://manjusha9722@dev.azure.com/manjusha9722/DevOps/_git/${j}")
              'refspec'('\'+refs/tags/*\':\'refs/remotes/origin/tags/*\'')
            }
          }
          'branches' {
            'hudson.plugins.git.BranchSpec' {
              'name'('**/tags/**')
            }
            'hudson.plugins.git.BranchSpec' {
              'name'('*/main')
            }
          }
        }
        'scriptPath'('jenkinsfile-infra')
        'lightweight'(true)
      }
    }
  }
}

pipelineJob("Mutable/Destroy") {
  configure { flowdefinition ->
    flowdefinition << delegate.'definition'(class: 'org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition', plugin: 'workflow-cps') {
      'scm'(class: 'hudson.plugins.git.GitSCM', plugin: 'git') {
        'userRemoteConfigs' {
          'hudson.plugins.git.UserRemoteConfig' {
            'url'("https://github.com/Manjusha-2297/terraform-mutable.git")
          }
        }
        'branches' {
          'hudson.plugins.git.BranchSpec' {
            'name'('*/main')
          }
        }
      }
      'scriptPath'('jenkinsfile-destroy')
      'lightweight'(true)
    }
  }
}

pipelineJob("Mutable/ALB") {
  configure { flowdefinition ->
    flowdefinition << delegate.'definition'(class: 'org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition', plugin: 'workflow-cps') {
      'scm'(class: 'hudson.plugins.git.GitSCM', plugin: 'git') {
        'userRemoteConfigs' {
          'hudson.plugins.git.UserRemoteConfig' {
            'url'("https://github.com/Manjusha-2297/terraform-mutable.git")
          }
        }
        'branches' {
          'hudson.plugins.git.BranchSpec' {
            'name'('*/main')
          }
        }
      }
      'scriptPath'('alb/jenkinsfile')
      'lightweight'(true)
    }
  }
}

pipelineJob('Mutable/Create') {
  configure { flowdefinition ->
    flowdefinition << delegate.'definition'(class:'org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition',plugin:'workflow-cps') {
      'scm'(class:'hudson.plugins.git.GitSCM',plugin:'git') {
        'userRemoteConfigs' {
          'hudson.plugins.git.UserRemoteConfig' {
            'url'('https://github.com/Manjusha-2297/terraform-mutable.git')
          }
        }
        'branches' {
          'hudson.plugins.git.BranchSpec' {
            'name'('*/main')
          }
        }
      }
      'scriptPath'('Jenkinsfile-create')
      'lightweight'(true)
    }
  }
}



