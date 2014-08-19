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
 * Copyright (C) 2012 - 2014 Trust@HsH
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
package de.hshannover.f4.trust.metalyzer.statistic;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * @author juriseewald
 * @author hassannahle
 */
public class Counter {
    /**
     * @param typeName Typename of the Identifier, wich should be count
     * @param list 
     * @return The sum of the Identifiers in the List
    */
    public static int countIdentifier(String typeName, List<Identifier> list){
        int result= 0;
        for(int i=0; i<list.size(); i++){
            if(list.get(i).getTypeName().equals(typeName)){
                result++;
            }
        }
        return result;        
    }
    
    /**
     * @param typeName Typename of the Metadata, wich should be count
     * @param list 
     * @return The sum of the Metadata in the List
    */
    public static int countMetada(String typeName, List<Metadata> list){
        int result = 0;
        for(int i = 0; i<list.size();i++){
            if(list.get(i).getTypeName().equals(typeName)){
                result++;
            }
        }
        return result;        
    }
    
    /**
     * @param ident List of identifiers, whose links should be counted
     * @return Returns a list of links 
     */
    public static ArrayList<Integer> countLinksFromIdentifiers(Collection<Identifier> ident){
		int value = 0;
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(Identifier id : ident){
			value= id.getLinks().size();
			list.add(value);
		}
		return list;
	}
}
