package org.unibl.etf.mdp.libraryserver.repository;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Singleton;

import org.unibl.etf.mdp.libraryserver.logger.FileLogger;
import org.unibl.etf.mdp.libraryserver.properties.AppConfig;
import org.unibl.etf.mdp.model.User;

public class UserRepository {
	private static final AppConfig conf = new AppConfig();
	private static final Logger logger = FileLogger.getLogger(UserRepository.class.getName());
	private static final String USERS_FOLDER = conf.getUsersFolder();
	private static final String USERS_FILE = conf.getUsersFile();

	private static UserRepository instance = null;

	private UserRepository() {
	}

	public static synchronized UserRepository getInstance() {
		if (instance == null) {
			instance = new UserRepository();
		}
		return instance;
	}

	public void saveAll(List<User> users) {
		try (XMLEncoder encoder = new XMLEncoder(new FileOutputStream(getFilePath()))) {
			encoder.writeObject(users);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error while saving users: " + e.getMessage(), e);
		}
	}

	public List<User> findAll() {
		try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(getFilePath())));) {
			ArrayList<User> users = (ArrayList<User>) decoder.readObject();

			return users;
		} catch (Exception e) {
			logger.log(Level.WARNING, "Error while reading users: " + e.getMessage(), e);
			return new ArrayList<>();
		}
	}

	public Optional<User> findByUsername(String username) {
		return findAll().stream().filter(u -> u.getUsername().equals(username)).findFirst();
	}

	private String getFilePath() {
		File folder = new File(USERS_FOLDER);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File file = new File(Paths.get(USERS_FOLDER, USERS_FILE).toString());
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error while creating file: " + file.getPath(), e);
			}
		}
		return file.getPath();
	}
}