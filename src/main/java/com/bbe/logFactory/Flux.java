package com.bbe.logFactory;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.apache.log4j.Logger;

public class Flux extends Thread {

	private List<String> idList;

	private Properties prop;
	private final String fluxName;
	private long tempo = 0;
	private FileWriter outputFileWriter;
	private File outputFile;
	private ClientRest client;
	protected static Logger logger = Logger.getLogger(Flux.class);

	public Flux(Properties prop_, ArrayList<String> idList_,String fluxName_) {
		super();
		idList = idList_;
		prop = prop_;
		fluxName = fluxName_;
		if (prop.getProperty(fluxName + ".httpReq")!=null) {
			client = new ClientRest(prop.getProperty(fluxName + ".httpReq"),fluxName);
		}
	}

	public void run() {

		tempo = priseEnComptePeriodeDuFlux();
		idList = priseEnComptePoids();
		boolean doItOnce = false;

		if (prop.getProperty(fluxName+".doItOnce")!=null) {
			if (prop.getProperty(fluxName+".doItOnce").toUpperCase().equals("TRUE")) {
				doItOnce=true;
				executeScenario();
			}
		}

		while ( ! doItOnce ) {
			executeScenario();
		}

	}

	private void executeScenario() {
		for (String id : idList) {
			executeEvenementScenario(id);
			tempo();
		}		
	}

	private void executeEvenementScenario(String id) {
		String input = gestionEntree(id);
		if (input != null) {
			logger.info(input);
			gestionSortie(input);
		}
	}

	private String gestionEntree(String id) {
		logger.debug(fluxName+">"+prop.getProperty(id+"."+fluxName));
		String input = prop.getProperty(id+"."+fluxName);

		if (input.length()>8) {

			if (input.toUpperCase().substring(0, 8).equals("FICHIER:")) {
				String contenuFichier = "";

				if (input.contains("/") && input.trim().length()==input.lastIndexOf("/")+1) {

					File folder = new File(input.trim().substring(8));
					File[] listOfFiles = folder.listFiles();

					for (File file : listOfFiles) {
						if (file.isFile()) {
							try {
								String input_ = readFile(file.getCanonicalPath());
								logger.info(input_);
								gestionSortie(input_);
								tempo();
							} catch (IOException e) {
								logger.error(fluxName+">Erreur lors de la lecture du fichier : " + file);
								logger.error(fluxName+">"+e.getMessage());

							}
						}
					}


					return null;
				}
				else {
					contenuFichier = readFile(input.substring(8));
				}

				return contenuFichier;
			}

		}	
		return input;
	}

	private String readFile(String fichier) {
		try {
			return new String(Files.readAllBytes(Paths.get(fichier))).trim();
		} catch (IOException e) {
			logger.error(fluxName+">Erreur lors de la lecture du fichier : " + fichier);
			logger.error(fluxName+">"+e.getMessage());
		}
		return "";
	}

	private void gestionSortie(String input) {
		try {
			if (outputFile==null) {

				if (prop.getProperty(fluxName+".log.path")==null) {
					outputFile = new File(fluxName+".output.log");
				}
				else {

					String fileName = prop.getProperty(fluxName+".log.path").trim();
					if (fileName.contains("/")) {
						Files.createDirectories(Paths.get(fileName.substring(0,fileName.lastIndexOf("/"))));
					}

					outputFile = new File(fileName);
				}

			}
			outputFileWriter = new FileWriter(outputFile, true);
			outputFileWriter.write(input+"\n");
			outputFileWriter.close();
		} catch (IOException e) {
			logger.error(fluxName+">"+e.getMessage());
		}
		if (client!=null) {
			client.doHttpReq();
		}
	}

	private void tempo() {
		if (tempo>0) {
			try {
				Thread.sleep(tempo);
			} catch (InterruptedException e) {
				logger.error(fluxName+">Erreur lors de la pause de : " + tempo);
				logger.error(fluxName+">"+e.getMessage());
			}			
		}
	}

	private List<String> priseEnComptePoids() {
		List<String> idList_ = new ArrayList<>(idList);

		for (String id : idList_) {
			if (prop.getProperty(id+".poids")!=null) {
				for (int i = 1; i < Integer.parseInt(prop.getProperty(id+".poids")); i++) {
					idList.add(id);
				}
			}
		}
		return idList;
	}

	private long priseEnComptePeriodeDuFlux() {
		if (prop.getProperty(fluxName+".periode")==null) {
			return 1000;
		}
		else {
			return Integer.parseInt(prop.getProperty(fluxName+".periode"));
		}
	}

}
