/*
*   Copyright 2012-2013 The Regents of the University of Colorado
*
*   Licensed under the Apache License, Version 2.0 (the "License")
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/


/**
 * Running externalized configuration
 * Assuming the following configuration files
 * - in c:\recombinant\Config.groovy
 * - config location set path by system environment variable '<APP_NAME>_CONFIG_LOCATION'
 */
grails.config.locations = []
def defaultConfigFiles = ["/recombinant/${appName}/Config.groovy", "/usr/share/rosita/Config.groovy"]

defaultConfigFiles.each { filePath ->
  def f = new File(filePath)
  if (f.exists()) {
    println "[INFO] Config.groovy found ${filePath}"
    grails.config.locations << "file:${filePath}"
  } else {
    println "[INFO] Config.groovy did not find ${filePath}"
  }
}
String bashSafeEnvAppName = appName.toUpperCase(Locale.ENGLISH).replaceAll(/-/, '_')

def externalConfig = System.getenv("${bashSafeEnvAppName}_CONFIG_LOCATION")
if (externalConfig) {
  println "[INFO] Config.groovy found ${externalConfig}"
  grails.config.locations << "file:" + externalConfig
}
grails.config.locations.each {
  println "[INFO] Including configuration file [${it}] in configuration building."
}
