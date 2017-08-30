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



import groovy.time.TimeDuration;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

class UtilsTagLib {
	
	DecimalFormat df = new DecimalFormat("0.0")
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	
	def plainField = { attrs, body ->
		
		def bean = attrs.bean
		def field = attrs.field
		out << bean."${field}"
		return	
	}
	
	def fileSize = { attrs, body ->
		
		def bytes = attrs.bytes
		
		if (bytes < 1024) {
			out << bytes + " B" //Don't format a decimal on!
			return
		}
		
		bytes /= 1024
		
		if (bytes < 1024) {
			out << df.format(bytes) + " KB"
			return
		}
		
		bytes /= 1024
		
		if (bytes < 1024) {
			out << df.format(bytes) + " MB"
			return
		}
		
	}
	
	def dateFromLong = { attrs, body ->
		
		Date date = new Date(attrs.date)
		String fullDate = sdf.format(date)
		
		//See if the day of year matches today or yesterday
		Calendar now = Calendar.getInstance();
		Calendar dateCal = Calendar.getInstance();
		dateCal.setTime(date);
		
		if (dateCal.get(Calendar.DAY_OF_YEAR).equals(now.get(Calendar.DAY_OF_YEAR))) {
			out << "Today at " + fullDate.substring(10)
		}
		else if ((dateCal.get(Calendar.DAY_OF_YEAR)+1).equals(now.get(Calendar.DAY_OF_YEAR))) {
			out << "Yesterday at " + fullDate.substring(10)
		}
		else {
			out << sdf.format(date)
		}
	}
	
	def timeSpanFromMs = { attrs, body ->
		
		def span = attrs.ms
		if (!(span instanceof Long)) {
			span = Long.parseLong(span.toString())
		}
		span = (Long) (span / 1000)
		Long seconds = span % 60
		Long minutes = Math.floor(span / 60) % 60
		Long hours = Math.floor(span / 60 / 60)
		
		out << hours + ":" + minutes + ":" + seconds
					
	}
	
	def timeEstimateFromMs = { attrs, body ->

		def currentSpan = attrs.ms
		if (!(currentSpan instanceof Long)) {
			currentSpan = Long.parseLong(currentSpan.toString())
		}
		
		if (currentSpan < 1000) {
			out << " Waiting..."
			return
		}

		currentSpan /= 1000

		if (currentSpan < 60) {
			currentSpan = (int) currentSpan
			out << currentSpan + " second"
			if (currentSpan > 1) { out << "s" }
			return
		}

		currentSpan /= 60

		if (currentSpan < 60) {
			currentSpan = (int) currentSpan
			out << currentSpan + " minute"
			if (currentSpan > 1) { out << "s" }
			return
		}

		currentSpan /= 60

		if (currentSpan < 24) {
			currentSpan = (int) currentSpan
			out << currentSpan + " hour"
			if (currentSpan > 1) { out << "s" }
			return
		}

		currentSpan /= 24

		currentSpan = (int) currentSpan
		out << currentSpan + " day"
		if (currentSpan > 1) { out << "s" }
		return
					
	}
	
	def pluralize = { attrs, body ->
		def showNumber = attrs.showCount;
		def number = attrs.count;
		def singular = attrs.singular;
		def plural = attrs.plural;
		
		if (showNumber) {
			out << number + " "
		}
		if (number == 1) {
			out << singular
		}
		else {
			out << plural
		}
	}
}
