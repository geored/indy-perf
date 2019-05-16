timeout(time: 20, unit: 'MINUTES') {
                def appName="test-pipe-app"
                def project=""
                node {
                  stage("Initialize") {
                    project = "nos-perf"
                  }
                }
                node {
                  stage("Build Image") {
                    sh "oc start-build indy-perf -n nos-perf"
                    timeout(time: 20, unit: 'MINUTES') {
                      openshift.withCluster() {
                        openshift.withProject() {
                          def bc = openshift.selector('bc', "indy-perf")
                          echo "Found 1 ${bc.count()} buildconfig"
                          def blds = bc.related('builds')
                          blds.untilEach {
                            return it.object().status.phase == "Complete"
                          }
                        }
                      }
                    }
                  }
                  stage("Deploy") {
                    openshift.withCluster() {
                      openshift.withProject() {
                        def dc = openshift.selector('dc', "indy-perf")
                        dc.rollout().status()
                      }
                    }
                  }
                }
             }