pipeline {
        agent {
                docker {
                        image 'maven:3-apline'
                        args '-v /root/.m2:/root/.m2'
                }
        }
        stages{
                state('Build') {
                        steps {
                                sh 'mvn -B -DskipTests clean package'
                        }
                }
        }
}
