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

package edu.ucdenver.rosita.utils;

import java.util.Random;

public class NameGenerator {
	
	static String consonants = "tnsrdlcmfgp";
	static String vowels = "aeiouy";
	Random rnd = new Random();
	
	public NameGenerator() {
		
	}
	
	public String getName(int maxLength) {
		int length = rnd.nextInt(maxLength) + 1;
		
		StringBuilder sb = new StringBuilder();
		sb.append(getSyllable(true));
		for (int i = 1; i < length; i++) {
			sb.append(getSyllable(false));
		}
		
		return sb.toString();
	}
	
	public String getSyllable(boolean capitalize) {
		int random = rnd.nextInt(consonants.length());
		String x = consonants.substring(random, random+1);
		random = rnd.nextInt(vowels.length());
		String y = vowels.substring(random, random+1);
		
		if (capitalize) {
			x = x.toUpperCase();
		}
		
		return x + y;
	}

}
