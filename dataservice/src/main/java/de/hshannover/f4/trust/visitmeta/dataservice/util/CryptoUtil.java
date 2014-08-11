/*
 * #%L
 * =====================================================
 *   _____                _     ____  _   _       _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \| | | | ___ | | | |
 *    | | | '__| | | / __| __|/ / _` | |_| |/ __|| |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _  |\__ \|  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_| |_||___/|_| |_|
 *                             \____/
 * 
 * =====================================================
 * 
 * Hochschule Hannover
 * (University of Applied Sciences and Arts, Hannover)
 * Faculty IV, Dept. of Computer Science
 * Ricklinger Stadtweg 118, 30459 Hannover, Germany
 * 
 * Email: trust@f4-i.fh-hannover.de
 * Website: http://trust.f4.hs-hannover.de/
 * 
 * This file is part of visitmeta dataservice, version 0.1.2,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@HsH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package de.hshannover.f4.trust.visitmeta.dataservice.util;




import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class CryptoUtil {

	public static String generateMD5(String s){
		MessageDigest md = null;
		byte[] hash = null;
		StringBuffer buffer = null;
		
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		if(md != null){
			buffer = new StringBuffer();
			hash = md.digest(s.getBytes());
			for(Byte b : hash){
				String hex = Integer.toHexString(0xFF & b);
				if(hex.length() == 1)
					buffer.append(0);
				buffer.append(hex);
			}
		}
		return buffer.toString();
	}
	
	public static String generateMD5BySize(String s, int length){
		return generateMD5(s).substring(0, length-1);
	}
	
	public static String randomUUID(int length){
		if(length > 0){
			String uuid = UUID.randomUUID().toString();
			if(uuid.length() <= length)
				return uuid;
			else return uuid.substring(0, length-1);
		}
		else return null;
	}
	
	public static String randomUUID(){
		return UUID.randomUUID().toString();
	}
}
