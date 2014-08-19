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
/**
 * Project: Metalyzer 
 * Author: Juri Seewald
 * Last Change: 
 * 		by: 	$Author: $
 * 		date: 	$Date: $ 
 * Copyright (c): Hochschule Hannover
 */

package de.hshannover.f4.trust.metalyzer.statistic;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

public class REvaluator {
    private static REvaluator rInstance = null;
    private final Rengine rEngineObject;

    private REvaluator(){
            rEngineObject = new Rengine(new String[] { "--vanilla" }, false, null);
    }

    public static REvaluator getInstance(){
            if(rInstance == null){
                    rInstance = new REvaluator();			
            }
            return rInstance;
    }

    public void exportIntArrayToR(String variableName, int array[]){        
        rEngineObject.assign(variableName, array);
    }
    
    public void exportStringArrayToR(String variableName, String array[]){
    	rEngineObject.assign(variableName, array);
    }

    public void exportDoubleArrayToR(String variableName, double array[]){
    	rEngineObject.assign(variableName, array);
    }
    
    public REXP evalInR(String code){
            REXP res = rEngineObject.eval(code);
            return res;		
    }
}
