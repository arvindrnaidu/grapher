/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grapher.processor;

import com.core.RealFinalDemographicLanguage;

public class FileProcessor {
	
	public RealFinalDemographicLanguage process(String filename) {
		try {

			RealFinalDemographicLanguage model = new RealFinalDemographicLanguage(
					filename);
			model.checkConstraints();
			model.buildMigrationMatrices();
			model.narrate();				
			return model;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static void main (String[] args){
		FileProcessor fp = new FileProcessor();
		RealFinalDemographicLanguage rf = fp.process("/Users/arvindnaidu/jws/realfinaldemographiclanguage/src/admixture.txt");
		System.out.println("asdfasdf");
	}
}
