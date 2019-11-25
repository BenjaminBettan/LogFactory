package com.bbe.logFactory;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import org.apache.log4j.PropertyConfigurator;

public class LogFactory {
	
	private static Set<String> listeFlux = new HashSet<>();
	private static Properties prop = new Properties();
	private static Map<String, List<String>> m_flux = new HashMap<>();

	public static void main(String[] args) throws IOException {
		PropertyConfigurator.configure("log4j.properties");
		lireFluxDepuisFichierProp();

		for (String nomFlux : listeFlux) {
			new Flux((Properties) prop.clone(),new ArrayList<String>(m_flux.get(nomFlux)),nomFlux).start();
		}

	}

	/**Identifier les differents flux du fichier properties et les identifiants des actions elementaires de ce flux
	 * @throws IOException
	 */
	private static void lireFluxDepuisFichierProp() throws IOException {
		prop.load(new FileInputStream("global.properties"));
		Set<Entry<Object, Object>> entries = prop.entrySet();

		for (Entry<Object, Object> entry : entries) {//on parcours les cles
			
			if (entry.getKey().toString().trim().contains(".") 
					&& ! entry.getKey().toString().trim().toUpperCase().contains("POIDS")) {// on cherche les cles "identifiantNumerique.<nomFlux>"
				
				String nomFlux = entry.getKey().toString().trim().split("\\.")[1];
				String id = entry.getKey().toString().trim().split("\\.")[0];
				
				if (id.matches("-?\\d+")) {//id est un nombre
					
					if (m_flux.get(nomFlux)==null) {
						m_flux.put(nomFlux, new ArrayList<String>());
					}
					
					List<String> l = m_flux.get(nomFlux);
					l.add(id);
					listeFlux.add(nomFlux);	
				}
			}
		}
		
	}
}
