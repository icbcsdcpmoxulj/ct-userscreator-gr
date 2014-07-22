/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//      Contributors:      Xu Lijia

package ci.xlj.tools.userscreator

import ci.xlj.libs.utils.StringUtils

/**
 * A convenient tool creates a couple of users' info based on built-in database.
 * 
 * @author kfzx-xulj
 *
 */
class UsersCreator {

	static main(args) {
		if(args&&args.length<3){
			outputPath=StringUtils.isValid(args[0])?args[0]:outputPath
			createOutputFolder()

			loadFile(args[1]).each {
				def result=generateUserFolder(it)
				if(result){
					generateConfigXml(it)
				}else{
					println 'Creation of config.xml is ignored.'
				}
			}
		}else{
			println 'Invalid parameters. Please check and retry.'
		}
	}

	private static outputPath='./out'
	private static usernames=[]

	private static loadFile(fileName){
		def f=new File(fileName)

		if(f.exists()&&f.canRead()){
			f.eachLine { usernames<<it }
		}else{
			println "Failed to read file '$fileName'"
			System.exit(-2)
		}
	}

	private static void createOutputFolder(){
		def d=new File(outputPath)
		if(!d.exists()){
			if(!d.mkdir()){
				println "Failed to create directory $outputPath"
				System.exit(-1)
			}
		}
	}

	private static generateUserFolder(username){
		def uf=new File("${outputPath}/$username")

		if(!uf.exists()){
			if(!uf.mkdir()){
				println "Failed to create user directory $uf"
				return false
			}
		}

		return true
	}

	private static generateConfigXml(username,email){
		def c=new File("$outputPath/$username/config.xml")
		if(c.canWrite()){
			def w=new OutputStreamWriter(new FileOutputStream(c),'utf-8')
			w.write(config.replaceFirst('${username}',username).replaceFirst('${email}',email))
			w.close()
		}
	}

	/**
	 * default password is 123
	 */
	private static config='''<?xml version='1.0' encoding='UTF-8'?>
<user>
  <fullName>${username}</fullName>
  <properties>
    <jenkins.security.ApiTokenProperty>
      <apiToken>4yDkLDJhm3G/Si5yObTvevc8gmR2hvDIkLjBYcf6Hb6dXAGuNTLCeBPmWqLRHR87</apiToken>
    </jenkins.security.ApiTokenProperty>
    <hudson.model.MyViewsProperty>
      <views>
        <hudson.model.AllView>
          <owner class="hudson.model.MyViewsProperty" reference="../../.."/>
          <name>All</name>
          <filterExecutors>false</filterExecutors>
          <filterQueue>false</filterQueue>
          <properties class="hudson.model.View$PropertyList"/>
        </hudson.model.AllView>
      </views>
    </hudson.model.MyViewsProperty>
    <hudson.search.UserSearchProperty>
      <insensitiveSearch>false</insensitiveSearch>
    </hudson.search.UserSearchProperty>
    <hudson.security.HudsonPrivateSecurityRealm_-Details>
      <passwordHash>#jbcrypt:$2a$10$zEqQISAiX8VoUm9RrgkOPOhAiBOQQ5A.j9Y33lS3/NBhRysi7dAba</passwordHash>
    </hudson.security.HudsonPrivateSecurityRealm_-Details>
    <hudson.tasks.Mailer_-UserProperty>
      <emailAddress>${email}</emailAddress>
    </hudson.tasks.Mailer_-UserProperty>
  </properties>
</user>'''

}
