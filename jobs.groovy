freeStyleJob('ypapkou-main-build-job'){
  parameters {
    description 'Main job'
    choiceParam('BRANCH_NAME', ['ypapkou', 'master'], 'Branch name')
    activeChoiceParam('Jobs_to_execute') {
      description('Allows user choose jobs to execute')   	  
      filterable(false)
      choiceType('CHECKBOX')
      groovyScript {
        script('return ["ypapkou-child1-build-job","ypapkou-child2-build-job","ypapkou-child3-build-job","ypapkou-child4-build-job"]')
     	fallbackScript('"fallback choice"')
      }
    }
  }
  
  steps {
    downstreamParameterized {
      trigger('$Jobs_to_execute') {
        block {
          buildStepFailure('FAILURE')
          failure('FAILURE')
          unstable('UNSTABLE')
        }
        parameters {
          predefinedProp('BRANCH_NAME', '$BRANCH_NAME')
        }
      }
    }
  }
}

for (i in 1..4) {
  freeStyleJob("ypapkou-child${i}-build-job") {
    description "Child${i} job"
    parameters {      
      activeChoiceParam('BRANCH_NAME') {
        description('Allows user choose branches')
        filterable(false)
        choiceType('SINGLE_SELECT')
        groovyScript {
          script('("git ls-remote -h https://github.com/repo_url").execute().text.readLines().collect { it.split()[1].replaceAll(\'refs/heads/\', \'\')}')
     	  fallbackScript('"fallback choice"')
        }    
      }
    }
  	scm {
      git {
        remote {
          github('/d323dsl', 'https')
        }
        branch('${BRANCH_NAME}')
      }
    }
    steps {
      shell('bash script.sh > output.txt; tar -zcvf $BRANCH_NAME\\_dsl_script.tar.gz jobs.groovy')
    }
    publishers {
      archiveArtifacts('output.txt, ${BRANCH_NAME}_dsl_script.tar.gz')
    }
  }
}
