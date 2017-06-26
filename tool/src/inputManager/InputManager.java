package inputManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import entities.ConstraintDefinition;
import entities.MicroserviceDefinition;
import entities.RulesRegex;
import enums.Constraint;

public class InputManager {

	RulesRegex regex = new RulesRegex();
	HashMap<MicroserviceDefinition, Set<ConstraintDefinition>> map = new HashMap<>();

	public InputManager() {

	}

	public Set<MicroserviceDefinition> getMicroservice() {
		System.out.println(map.keySet());
		return map.keySet();

	}

	public void readFile(File f) throws IOException {

		FileReader file = new FileReader(f);
		BufferedReader buffer = new BufferedReader(file);
		String line = "";
		while (buffer.ready()) {
			line = buffer.readLine();
			// System.out.println(line);
			if (line.matches(regex.getMicroserviceDefinition())) {

				String languageDefinition[] = line.replaceAll(": h", " h").split("(\\s)+");
				languageDefinition[1] = languageDefinition[1].replaceAll(";", "");
				languageDefinition[2] = languageDefinition[2].replaceAll(";", "");
				map.put(new MicroserviceDefinition(languageDefinition[0], languageDefinition[1], languageDefinition[2],
						languageDefinition[3]), new HashSet<ConstraintDefinition>());

			}
			if (line.contains("	")) {
				// System.out.println("verify DCL");
			}
			if (line.matches(regex.getConstraints())) {

				String constraints[] = line.replaceAll(",", " ").split("(\\s+)");
				String left[] = null;
				String right[] = null;
				Constraint c = null;
				for (int i = 0; i < constraints.length; i++) {

					if (constraints[i].contains("-")) {
						int inicial = 0;

						if (constraints[0].equals("only")) {

							constraints[i] = "only-" + constraints[i];
							inicial++;

						}
						left = Arrays.copyOfRange(constraints, inicial, i);
						right = Arrays.copyOfRange(constraints, i + 1, constraints.length);
						c = findConstraint(constraints[i]);
						this.addHash(left, right, c);

					}

				}

			}
		}

		buffer.close();
		file.close();

	}

	public void printHash() {

		for (Entry<MicroserviceDefinition, Set<ConstraintDefinition>> e : map.entrySet()) {
			System.out.println(e.getKey().getName() + "" + e.getValue());

		}
	}

	public Constraint findConstraint(String constraint) {

		for (Constraint c : Constraint.values()) {
			if (c.getValue().equals(constraint)) {
				return c;
			}
		}
		return null;

	}

	public HashMap<MicroserviceDefinition, Set<ConstraintDefinition>> createMap(String[] languageDefinition) {

		map.put(new MicroserviceDefinition(languageDefinition[0], languageDefinition[1], languageDefinition[2],
				languageDefinition[3]), new HashSet<ConstraintDefinition>());
		return map;

	}

	public HashMap<MicroserviceDefinition, Set<ConstraintDefinition>> addHash(String[] left, String[] right,
			Constraint c) {

		for (MicroserviceDefinition m : map.keySet()) {

			for (int j = 0; j < left.length; j++) {
				for (int k = 0; k < right.length; k++) {
					if (m.getName().equals(left[j])) { //
						Set<ConstraintDefinition> access = map.get(m);
						access.add(new ConstraintDefinition(left[j], c, right[k]));
						map.put(m, access);

					}
				}
			}

		}
		return map;
	}

	public HashMap<MicroserviceDefinition, Set<ConstraintDefinition>> getMap() {
		return map;
	}

	public void setMap(HashMap<MicroserviceDefinition, Set<ConstraintDefinition>> map) {
		this.map = map;
	}

}
